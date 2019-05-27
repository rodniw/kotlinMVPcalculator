package dev.rodni.kotlincalculator.domain.repository

interface IValidator {

    //This part of the program can operate synchronously
    fun validateExpression(expression:String): Boolean
}