package dev.rodni.kotlincalculator.util.error

class ValidationException: Exception(){
    companion object {
        const val message = "Invalid ExpressionDataModel"
    }
}