package dev.rodni.kotlincalculator.domain.usecase

import dev.rodni.kotlincalculator.domain.BaseUseCase
import dev.rodni.kotlincalculator.domain.domainmodel.Expression
import dev.rodni.kotlincalculator.domain.repository.ICalculator
import dev.rodni.kotlincalculator.domain.repository.IValidator
import dev.rodni.kotlincalculator.util.error.ValidationException
import dev.rodni.kotlincalculator.util.scheduler.BaseSchedulerProvider
import io.reactivex.Flowable

class EvaluateExpression(private val calculator: ICalculator,
                         private val validator: IValidator,
                         private val scheduler: BaseSchedulerProvider) : BaseUseCase<Expression> {

    override fun execute(expression: String): Flowable<Expression> {
        //Validator operates synchronously
        if (validator.validateExpression(expression)) {
            return calculator.evaluateExpression(expression)
                    .flatMap { result ->
                        Flowable.just(
                                Expression.createSuccessModel(result.value)
                        )
                    }
                    .subscribeOn(scheduler.getComputationScheduler())
        }

        return Flowable.just(
                Expression.createFailureModel(ValidationException.message)
        )
    }
}
