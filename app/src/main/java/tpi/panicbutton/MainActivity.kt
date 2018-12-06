package tpi.panicbutton

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_panic -> {

                createFragment(R.id.navigation_panic)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_information -> {

                createFragment(R.id.navigation_information)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                createFragment(R.id.navigation_settings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFragment(R.id.navigation_panic)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun createFragment(type: Int) {
        val transaction = manager.beginTransaction()
        var fragment : Fragment
        if (type == R.id.navigation_panic) {
            fragment = Panic()
        } else if (type == R.id.navigation_information) {
            fragment = Information()
        } else {
            fragment = SettingsFragment()
        }
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
