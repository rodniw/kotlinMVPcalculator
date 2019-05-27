package dev.rodni.kotlincalculator

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dev.rodni.kotlincalculator.R
import dev.rodni.kotlincalculator.dependencyinjection.Injector
import dev.rodni.kotlincalculator.view.CalculatorFragment

class CalculatorActivity : AppCompatActivity() {

    companion object {
        val VIEW: String = "VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val manager: FragmentManager = this.supportFragmentManager

        //if (object/function) returns/is null :? do this
        val view = manager.findFragmentByTag(VIEW) as CalculatorFragment?
                ?: CalculatorFragment.newInstance(Injector(this))

        manager.beginTransaction().replace(R.id.root_activity_calculator, view).commit()

    }
}
