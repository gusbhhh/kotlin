/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.diagram

import org.jetbrains.kotlin.ir.builders.IrStatementsBuilder
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

data class IrTemporaryVariable(
  val temporary: IrVariable,
  val original: IrExpression,
)

class IrTemporaryExtractionTransformer(
  private val builder: IrStatementsBuilder<*>,
  private val transform: Set<IrExpression>,
) : IrElementTransformerVoid() {
  private val _variables = mutableListOf<IrTemporaryVariable>()
  val variables: List<IrTemporaryVariable> = _variables

  override fun visitExpression(expression: IrExpression): IrExpression {
    return if (expression in transform) {
      val copy = expression.deepCopyWithSymbols(builder.scope.getLocalDeclarationParent())
      val variable = builder.irTemporary(super.visitExpression(expression))
      _variables.add(IrTemporaryVariable(variable, copy))
      builder.irGet(variable)
    } else {
      super.visitExpression(expression)
    }
  }
}
