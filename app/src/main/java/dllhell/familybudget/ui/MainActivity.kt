package dllhell.familybudget.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.presentation.main.MainPresenter
import dllhell.familybudget.presentation.main.MainView
import dllhell.familybudget.ui.fragment.BarcodeScannerFragment
import dllhell.familybudget.ui.navigation.Screens
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val navigator = object : SupportFragmentNavigator(
            supportFragmentManager, R.id.container) {
        override fun createFragment(screenKey: String, data: Any?): Fragment {
            when (screenKey) {
                Screens.ADD_EXPENSE -> return BarcodeScannerFragment()
                else -> throw IllegalStateException("Navigating to unknown screen: " + screenKey)
            }
        }

        override fun showSystemMessage(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }

        override fun exit() {
            finish()
        }
    }

    private val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener { this.updateToolbarBackButton() }


    @ProvidePresenter
    internal fun providePresenter(): MainPresenter {
        return App.getAppComponent().mainPresenterProvider().get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { _ -> onBackPressed() }

        updateToolbarBackButton()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigatorHolder.setNavigator(navigator)

        supportFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        supportFragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener)

        super.onPause()
    }

    private fun updateToolbarBackButton() {
        val actionBar = supportActionBar ?: return

        if (supportFragmentManager.backStackEntryCount > 0) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false)
        }
    }
}
