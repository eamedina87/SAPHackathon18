package ec.erickmedina.sapchallenge

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import ec.erickmedina.sapchallenge.history.ScanHistoryFragment
import ec.erickmedina.sapchallenge.scan.ScanViewFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val scanViewFragment = ScanViewFragment()
    private val historyFragment = ScanHistoryFragment()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, scanViewFragment)
                        .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history-> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, historyFragment)
                        .commitAllowingStateLoss()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState==null){
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, scanViewFragment)
                    .commitAllowingStateLoss()
        }
    }
}
