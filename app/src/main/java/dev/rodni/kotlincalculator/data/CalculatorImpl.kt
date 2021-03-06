package dev.rodni.kotlincalculator.data

import android.support.annotation.VisibleForTesting
import dev.rodni.kotlincalculator.data.datamodel.ExpressionDataModel
import dev.rodni.kotlincalculator.data.datamodel.OperandDataModel
import dev.rodni.kotlincalculator.data.datamodel.OperatorDataModel
import dev.rodni.kotlincalculator.domain.repository.ICalculator
import io.reactivex.Flowable
import java.lang.IllegalArgumentException

object CalculatorImpl : ICalculator {
    override fun evaluateExpression(expression: String): Flowable<ExpressionDataModel> {

        return evaluate(expression)
    }

    private fun evaluate(expression: String): Flowable<ExpressionDataModel> {

        //get ops and ops
        val operatorDataModels: MutableList<OperatorDataModel> = getOperators(expression)
        val operands: MutableList<OperandDataModel> = getOperands(expression)

        while (operands.size > 1) {
            val firstOperand = operands[0]
            val secondOperand = operands[1]
            val firstOperator = operatorDataModels[0]


            if (firstOperator.evaluateFirst ||
                    operatorDataModels.elementAtOrNull(1) == null ||
                    !operatorDataModels[1].evaluateFirst) {
                val result = OperandDataModel(evaluatePair(firstOperand, secondOperand, firstOperator))
                operatorDataModels.remove(firstOperator)
                operands.remove(firstOperand)
                operands.remove(secondOperand)

                operands.add(0, result)
            } else {

                val thirdOperand = operands[2]
                val secondOperator = operatorDataModels[1]
                val result = OperandDataModel(evaluatePair(secondOperand, thirdOperand, secondOperator))

                operatorDataModels.remove(secondOperator)
                operands.remove(secondOperand)
                operands.remove(thirdOperand)

                operands.add(1, result)
            }
        }

        //when calculations are finished, emit the result
        return Flowable.just(ExpressionDataModel(operands[0].value, true))
    }

    @VisibleForTesting
    internal fun getOperands(expression: String): MutableList<OperandDataModel> {
        val operands = expression.split("+", "-", "/", "*")
        val outPut: MutableList<OperandDataModel> = arrayListOf()

        operands.indices.mapTo(outPut) {
            OperandDataModel(operands[it])
        }
        return outPut
    }

    @VisibleForTesting
    internal fun getOperators(expression: String): MutableList<OperatorDataModel> {

        val operators = expression.split("\\d+(?:\\.\\d+)?".toRegex())
                .filterNot { it == "" }
                .toMutableList()
        val outPut: MutableList<OperatorDataModel> = arrayListOf()

        operators.indices.mapTo(outPut) {
            OperatorDataModel(operators[it])
        }
        return outPut
    }

    @VisibleForTesting
    internal fun evaluatePair(firstOperand: OperandDataModel, secondOperand: OperandDataModel, operatorDataModel: OperatorDataModel): String {
        when (operatorDataModel.operatorValue) {
            "+" -> return (firstOperand.value.toFloat() + secondOperand.value.toFloat()).toString()
            "-" -> return (firstOperand.value.toFloat() - secondOperand.value.toFloat()).toString()
            "/" -> return (firstOperand.value.toFloat() / secondOperand.value.toFloat()).toString()
            "*" -> return (firstOperand.value.toFloat() * secondOperand.value.toFloat()).toString()
        }
        throw  IllegalArgumentException("Illegal Operator.")
    }

}