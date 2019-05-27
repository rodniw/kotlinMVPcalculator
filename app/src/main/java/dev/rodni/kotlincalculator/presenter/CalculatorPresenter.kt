package dev.rodni.kotlincalculator.presenter

import dev.rodni.kotlincalculator.domain.usecase.EvaluateExpression
import dev.rodni.kotlincalculator.util.scheduler.BaseSchedulerProvider
import dev.rodni.kotlincalculator.view.IViewContract
import dev.rodni.kotlincalculator.domain.domainmodel.Expression
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subscribers.DisposableSubscriber

class CalculatorPresenter(private var view: IViewContract.View,
                          private var viewModel: IViewContract.ViewModel,
                          private val scheduler: BaseSchedulerProvider,
                          private val eval: EvaluateExpression) : IViewContract.Presenter {

    private val eventStream = CompositeDisposable()

    private val EMPTY = ""

    //Update the state, then the view.
    override fun onLongDeleteClick() {
        viewModel.setDisplayState(EMPTY)
    }

    override fun onInputButtonClick(value: String) {
        viewModel.setDisplayState(
                viewModel.getDisplayState() + value
        )
    }

    override fun onDeleteClick() {
        viewModel.setDisplayState(
                viewModel.getDisplayState().dropLast(1)
        )
    }

    override fun onEvaluateClick(expression: String) {
        //Presenter is the Observer
        eventStream.add(
                eval.execute(expression)
                        .observeOn(scheduler.getUiScheduler())
                        .subscribeWith(object : DisposableSubscriber<Expression>() {
                            override fun onNext(data: Expression?) {
                                if (data!!.successful) {
                                    viewModel.setDisplayState(data.result)
                                } else {
                                    view.showError(data.result)
                                }
                            }

                            //Reserved for fatal errors
                            override fun onError(t: Throwable?) {
                                restartFeature()
                            }

                            override fun onComplete() {

                            }
                        })
        )
    }

    private fun restartFeature() {
        eventStream.clear()
        view.restartFeature()
    }

    override fun bind() {
        eventStream.add(
                viewModel.getDisplayStatePublisher()
                        .subscribeWith(
                                object : DisposableSubscriber<String>() {
                                    override fun onNext(displayState: String) {
                                        view.setDisplay(displayState)
                                    }

                                    override fun onError(t: Throwable?) {
                                        restartFeature()
                                    }

                                    override fun onComplete() {}
                                }
                        )
        )
        //  view.setDisplay(viewModel.getDisplayStateFlowable())
    }

    override fun clear() {
        eventStream.clear()
    }

}

