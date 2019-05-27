package dev.rodni.kotlincalculator.domain.repository

import dev.rodni.kotlincalculator.data.datamodel.ExpressionDataModel
import io.reactivex.Flowable

interface ICalculator {

    //operates asynchronously via Rxjava
    fun evaluateExpression(expression: String): Flowable<ExpressionDataModel>
}