/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.name.Name

fun IrBuilderWithScope.irString(builderAction: StringBuilder.() -> Unit) =
  irString(buildString { builderAction() })

fun IrBuilderWithScope.irLambda(
  returnType: IrType,
  lambdaType: IrType,
  startOffset: Int = this.startOffset,
  endOffset: Int = this.endOffset,
  block: IrBlockBodyBuilder.() -> Unit,
): IrFunctionExpression {
  val scope = this
  val lambda = context.irFactory.buildFun {
    this.startOffset = startOffset
    this.endOffset = endOffset
    name = Name.special("<anonymous>")
    this.returnType = returnType
    visibility = DescriptorVisibilities.LOCAL
    origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
  }.apply {
    val bodyBuilder = DeclarationIrBuilder(context, symbol, startOffset, endOffset)
    body = bodyBuilder.irBlockBody {
      block()
    }
    parent = scope.parent
  }
  return IrFunctionExpressionImpl(startOffset, endOffset, lambdaType, lambda, IrStatementOrigin.LAMBDA)
}
