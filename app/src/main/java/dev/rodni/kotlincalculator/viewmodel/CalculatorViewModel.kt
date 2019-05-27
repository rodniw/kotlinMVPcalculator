package dev.rodni.kotlincalculator.viewmodel

import android.arch.lifecycle.ViewModel
import dev.rodni.kotlincalculator.domain.domainmodel.Expression
import dev.rodni.kotlincalculator.view.IViewContract
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject

/**
 * This thing contains the state of the View, and makes it easy for the Presenter to sort out calls to the View.
 */
class CalculatorViewModel(private val data: Expression
                          = Expression.createSuccessModel(""),
                          private val displayFlowable: BehaviorSubject<String>
                          = BehaviorSubject.create()) : ViewModel(),
        IViewContract.ViewModel {
    override fun getDisplayState(): String {
        return data.result
    }

    override fun getDisplayStatePublisher(): Flowable<String> {
        return displayFlowable.toFlowable(BackpressureStrategy.LATEST)
    }

    override fun setDisplayState(result: String) {
        this.data.result = result
        displayFlowable.onNext(getDisplayState())
    }
}

