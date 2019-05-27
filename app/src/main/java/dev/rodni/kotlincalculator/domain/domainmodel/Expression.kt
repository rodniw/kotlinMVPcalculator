package dev.rodni.kotlincalculator.domain.domainmodel

/**
 * This class models the data of a mathematical expression.
 */
class Expression private constructor(var result: String,
                                     var successful: Boolean) {
    companion object Factory{
        fun createSuccessModel(result: String): Expression {
            return Expression(result,
                    true)
        }

        fun createFailureModel(result: String): Expression {
            return Expression(result,
                    false)
        }
    }
}