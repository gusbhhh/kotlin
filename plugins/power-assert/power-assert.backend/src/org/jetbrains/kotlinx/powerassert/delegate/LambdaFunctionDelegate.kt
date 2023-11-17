/*
 * Copyright (C) 2020-2023 Brian Norman
 * Copyright 2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert.delegate

import org.jetbrains.kotlinx.powerassert.irLambda
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

class LambdaFunctionDelegate(
  private val overload: IrSimpleFunctionSymbol,
  override val messageParameter: IrValueParameter,
) : FunctionDelegate {
  override val function = overload.owner

  override fun buildCall(
    builder: IrBuilderWithScope,
    original: IrCall,
    dispatchReceiver: IrExpression?,
    extensionReceiver: IrExpression?,
    valueArguments: List<IrExpression?>,
    messageArgument: IrExpression,
  ): IrExpression = with(builder) {
    val expression = irLambda(context.irBuiltIns.stringType, messageParameter.type) {
      +irReturn(messageArgument)
    }
    irCallCopy(
      overload = overload,
      original = original,
      dispatchReceiver = dispatchReceiver,
      extensionReceiver = extensionReceiver,
      valueArguments = valueArguments,
      messageArgument = expression,
    )
  }
}
