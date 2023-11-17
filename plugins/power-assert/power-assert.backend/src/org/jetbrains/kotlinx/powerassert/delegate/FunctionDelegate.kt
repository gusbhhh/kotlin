/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.delegate

import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.parent
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols

interface FunctionDelegate {
  val function: IrFunction
  val messageParameter: IrValueParameter

  fun buildCall(
    builder: IrBuilderWithScope,
    original: IrCall,
    dispatchReceiver: IrExpression?,
    extensionReceiver: IrExpression?,
    valueArguments: List<IrExpression?>,
    messageArgument: IrExpression,
  ): IrExpression

  fun IrBuilderWithScope.irCallCopy(
    overload: IrSimpleFunctionSymbol,
    original: IrCall,
    dispatchReceiver: IrExpression?,
    extensionReceiver: IrExpression?,
    valueArguments: List<IrExpression?>,
    messageArgument: IrExpression,
  ): IrExpression {
    return irCall(overload, type = original.type).apply {
      this.dispatchReceiver = original.dispatchReceiver?.deepCopyWithSymbols(parent)
      this.extensionReceiver = (extensionReceiver ?: original.extensionReceiver)?.deepCopyWithSymbols(parent)
      for (i in 0 until original.typeArgumentsCount) {
        putTypeArgument(i, original.getTypeArgument(i))
      }
      for ((i, argument) in valueArguments.withIndex()) {
        putValueArgument(i, argument?.deepCopyWithSymbols(parent))
      }
      putValueArgument(valueArguments.size, messageArgument.deepCopyWithSymbols(parent))
    }
  }
}
