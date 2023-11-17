/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.diagram

import org.jetbrains.kotlinx.powerassert.irString
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.SourceRangeInfo
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.expressions.addArgument

fun IrBuilderWithScope.irDiagramString(
    sourceFile: SourceFile,
    prefix: IrExpression? = null,
    call: IrCall,
    variables: List<IrTemporaryVariable>,
): IrExpression {
    val callInfo = sourceFile.getSourceRangeInfo(call)
    val callIndent = callInfo.startColumnNumber

    val stackValues = variables.map { it.toValueDisplay(sourceFile, callIndent, callInfo) }

    val valuesByRow = stackValues.groupBy { it.row }
    val rows = sourceFile.getText(callInfo)
        .replace("\n" + " ".repeat(callIndent), "\n") // Remove additional indentation
        .split("\n")

    return irConcat().apply {
        if (prefix != null) addArgument(prefix)

        for ((row, rowSource) in rows.withIndex()) {
            val rowValues = valuesByRow[row]?.let { values -> values.sortedBy { it.indent } } ?: emptyList()
            val indentations = rowValues.map { it.indent }

            addArgument(
                irString {
                    if (row != 0 || prefix != null) appendLine()
                    append(rowSource)
                    if (indentations.isNotEmpty()) {
                        appendLine()
                        var last = -1
                        for (i in indentations) {
                            if (i > last) indent(i - last - 1).append("|")
                            last = i
                        }
                    }
                },
            )

            for (tmp in rowValues.asReversed()) {
                addArgument(
                    irString {
                        appendLine()
                        var last = -1
                        for (i in indentations) {
                            if (i == tmp.indent) break
                            if (i > last) indent(i - last - 1).append("|")
                            last = i
                        }
                        indent(tmp.indent - last - 1)
                    },
                )
                addArgument(irGet(tmp.value))
            }
        }
    }
}

private data class ValueDisplay(
    val value: IrVariable,
    val indent: Int,
    val row: Int,
    val source: String,
)

private fun IrTemporaryVariable.toValueDisplay(
    fileSource: SourceFile,
    callIndent: Int,
    originalInfo: SourceRangeInfo,
): ValueDisplay {
    val info = fileSource.getSourceRangeInfo(original)
    var indent = info.startColumnNumber - callIndent
    var row = info.startLineNumber - originalInfo.startLineNumber

    val source = fileSource.getText(info)
        .replace("\n" + " ".repeat(callIndent), "\n") // Remove additional indentation
    val columnOffset = findDisplayOffset(fileSource, original, source)

    val prefix = source.substring(0, columnOffset)
    val rowShift = prefix.count { it == '\n' }
    if (rowShift == 0) {
        indent += columnOffset
    } else {
        row += rowShift
        indent = columnOffset - (prefix.lastIndexOf('\n') + 1)
    }

    return ValueDisplay(temporary, indent, row, source)
}

/**
 * Responsible for determining the diagram display offset of the expression
 * beginning from the startOffset of the expression.
 *
 * Equality:
 * ```
 * number == 42
 * | <- startOffset
 *        | <- display offset: 7
 * ```
 *
 * Arithmetic:
 * ```
 * i + 2
 * | <- startOffset
 *   | <- display offset: 2
 * ```
 *
 * Infix:
 * ```
 * 1 shl 2
 * | <- startOffset
 *   | <- display offset: 2
 * ```
 *
 * Standard:
 * ```
 * 1.shl(2)
 *   | <- startOffset
 *   | <- display offset: 0
 * ```
 */
private fun findDisplayOffset(
    sourceFile: SourceFile,
    expression: IrExpression,
    source: String,
): Int {
    return when (expression) {
        is IrMemberAccessExpression<*> -> memberAccessOffset(sourceFile, expression, source)
        is IrTypeOperatorCall -> typeOperatorOffset(expression, source)
        else -> 0
    }
}

private fun memberAccessOffset(
    sourceFile: SourceFile,
    expression: IrMemberAccessExpression<*>,
    source: String,
): Int {
    when (expression.origin) {
        // special case to handle `value != null`
        IrStatementOrigin.EXCLEQ, IrStatementOrigin.EXCLEQEQ -> return source.indexOf("!=")
        // special case to handle `in` operator
        IrStatementOrigin.IN -> return source.indexOf(" in ") + 1
        // special case to handle `in` operator
        IrStatementOrigin.NOT_IN -> return source.indexOf(" !in ") + 1
        else -> Unit
    }

    val owner = expression.symbol.owner
    if (owner !is IrSimpleFunction) return 0

    if (owner.isInfix || owner.isOperator || owner.origin == IrBuiltIns.BUILTIN_OPERATOR) {
        // Ignore single value operators
        val singleReceiver = (expression.dispatchReceiver != null) xor (expression.extensionReceiver != null)
        if (singleReceiver && expression.valueArgumentsCount == 0) return 0

        // Start after the dispatcher or first argument
        val receiver = expression.dispatchReceiver
            ?: expression.extensionReceiver
            ?: expression.getValueArgument(0).takeIf { owner.origin == IrBuiltIns.BUILTIN_OPERATOR }
            ?: return 0
        val expressionInfo = sourceFile.getSourceRangeInfo(expression)
        var offset = receiver.endOffset - expressionInfo.startOffset + 1
        if (receiver is IrConst<*> && receiver.kind == IrConstKind.String) offset++ // String constants don't include the quote
        if (offset < 0 || offset >= source.length) return 0 // infix function called using non-infix syntax

        // Continue until there is a non-whitespace character
        while (source[offset].isWhitespace() || source[offset] == '.') {
            offset++
            if (offset >= source.length) return 0
        }
        return offset
    }

    return 0
}

private fun typeOperatorOffset(
    expression: IrTypeOperatorCall,
    source: String,
): Int {
    return when (expression.operator) {
        IrTypeOperator.INSTANCEOF -> source.indexOf(" is ") + 1
        IrTypeOperator.NOT_INSTANCEOF -> source.indexOf(" !is ") + 1
        else -> 0
    }
}

fun StringBuilder.indent(indentation: Int): StringBuilder {
    repeat(indentation) { append(" ") }
    return this
}
