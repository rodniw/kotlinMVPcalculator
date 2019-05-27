package dev.rodni.kotlincalculator.dependencyinjection

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import dev.rodni.kotlincalculator.data.CalculatorImpl
import dev.rodni.kotlincalculator.data.ValidatorImpl
import dev.rodni.kotlincalculator.domain.usecase.EvaluateExpression
import dev.rodni.kotlincalculator.presenter.CalculatorPresenter
import dev.rodni.kotlincalculator.util.scheduler.SchedulerProviderImpl
import dev.rodni.kotlincalculator.view.CalculatorFragment
import dev.rodni.kotlincalculator.view.IViewContract
import dev.rodni.kotlincalculator.viewmodel.CalculatorViewModel

/**
 * Basic DI implementation.
 */
class Injector(private var activity:AppCompatActivity) {

    private var validator: ValidatorImpl = ValidatorImpl
    private var calculator: CalculatorImpl = CalculatorImpl
    private var schedulerProvider: SchedulerProviderImpl = SchedulerProviderImpl


    fun providePresenter(view: CalculatorFragment): IViewContract.Presenter {
        return CalculatorPresenter(
                view,
                ViewModelProviders.of(activity).get(CalculatorViewModel::class.java),
                schedulerProvider,
                EvaluateExpression(calculator, validator, schedulerProvider)
        )
    }
}