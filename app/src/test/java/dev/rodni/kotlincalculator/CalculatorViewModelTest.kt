package dev.rodni.kotlincalculator

import dev.rodni.kotlincalculator.viewmodel.CalculatorViewModel
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import kotlin.test.assertTrue

class CalculatorViewModelTest {


    val STATE = "LOL"

    @Test
    fun onSetDisplayState() {
        val viewModel = CalculatorViewModel()

        val subscriber = TestSubscriber<String>()

        viewModel.getDisplayStatePublisher().subscribeWith(subscriber)
        viewModel.setDisplayState(STATE)

        assertTrue(subscriber.values()[0] == STATE)
    }

}