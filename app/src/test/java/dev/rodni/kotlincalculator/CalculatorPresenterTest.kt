package dev.rodni.kotlincalculator

import dev.rodni.kotlincalculator.domain.usecase.EvaluateExpression
import dev.rodni.kotlincalculator.presenter.CalculatorPresenter
import dev.rodni.kotlincalculator.view.IViewContract
import dev.rodni.kotlincalculator.domain.domainmodel.Expression
import dev.rodni.kotlincalculator.viewmodel.CalculatorViewModel
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Test behaviour of Presenter.
 *
 */
class CalculatorPresenterTest {

    private lateinit var scheduler: TestScheduler

    private lateinit var presenter: CalculatorPresenter

    @Mock
    private lateinit var view: IViewContract.View

    @Mock
    private lateinit var viewModel: CalculatorViewModel

    @Mock
    private lateinit var eval: EvaluateExpression


    val EXPRESSION = "2+2"
    val ANSWER = "4"

    val INVALID_EXPRESSION = "2+Q"
    val INVALID_ANSWER = "Error: Invalid ExpressionDataModel"


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        scheduler = TestScheduler()

        presenter = CalculatorPresenter(view, viewModel, scheduler, eval)
    }

    @Test
    fun onEvaluateValidSimpleExpression() {
        val result = Expression.createSuccessModel(ANSWER)

        Mockito.`when`(eval.execute(EXPRESSION))
                .thenReturn(
                        Flowable.just(
                                result
                        )
                )

        Mockito.`when`(eval.execute(EXPRESSION))
                .thenReturn(
                        Flowable.just(
                                result
                        )
                )

       //this is the "Unit" what we are testing
        presenter.onEvaluateClick(EXPRESSION)

        //These are the assertions which must be satisfied in order to pass the test
        Mockito.verify(eval).execute(EXPRESSION)
        Mockito.verify(viewModel).setDisplayState(ANSWER)
    }

    @Test
    fun onEvaluateInvalidExpression() {
        Mockito.`when`(eval.execute(INVALID_EXPRESSION))
                //...do this
                .thenReturn(
                        Flowable.just(
                                Expression.createFailureModel(INVALID_ANSWER)
                        )
                )

        presenter.onEvaluateClick(INVALID_EXPRESSION)

        Mockito.verify(eval).execute(INVALID_EXPRESSION)
        Mockito.verify(view).showError(INVALID_ANSWER)
    }

    @Test
    fun onEvaluateFatalError() {
        Mockito.`when`(eval.execute(INVALID_EXPRESSION))
                //...do this
                .thenReturn(
                        Flowable.error(Exception(INVALID_ANSWER))
                )


        presenter.onEvaluateClick(INVALID_EXPRESSION)

        Mockito.verify(eval).execute(INVALID_EXPRESSION)
        Mockito.verify(view).restartFeature()
    }


}
