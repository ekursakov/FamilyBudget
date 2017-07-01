package dllhell.familybudget.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.presentation.main.MainPresenter
import dllhell.familybudget.presentation.main.MainView
import dllhell.familybudget.ui.fragment.AuthFragment
import dllhell.familybudget.ui.fragment.AddExpenseFragment
import dllhell.familybudget.ui.fragment.ScannerFragment
import dllhell.familybudget.ui.fragment.history.HistoryFragment
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
            return when (screenKey) {
                Screens.AUTH -> AuthFragment() as Fragment
                Screens.SCANNER -> ScannerFragment()
                Screens.HISTORY -> HistoryFragment()
                Screens.ADD_EXPENSE -> AddExpenseFragment()
                else -> throw IllegalStateException("Navigating to unknown screen: " + screenKey)
            }.apply {
                if (data is Bundle) {
                    arguments = data
                }
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

        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { _ -> onBackPressed() }

        updateToolbarBackButton()

        DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Add"),
                        PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName("History"),
                        PrimaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Stats")
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                    when (drawerItem.identifier) {
                        1L -> presenter.navigateToScanner()
                        2L -> presenter.navigateToHistory()
                        3L -> presenter.navigateToStats()
                    }
                    false
                }
                .build()
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
