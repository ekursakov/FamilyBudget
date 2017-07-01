package dllhell.familybudget.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dllhell.familybudget.App
import dllhell.familybudget.presentation.auth.AuthPresenter
import dllhell.familybudget.presentation.auth.AuthView

class AuthFragment : MvpAppCompatFragment(), AuthView {

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    internal fun providePresenter(): AuthPresenter {
        return App.getAppComponent().authPresenterProvider().get()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }
}