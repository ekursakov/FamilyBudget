package dllhell.familybudget.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.firebase.ui.auth.AuthUI
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.presentation.auth.AuthPresenter
import dllhell.familybudget.presentation.auth.AuthView
import com.firebase.ui.auth.IdpResponse
import android.content.Intent



class AuthFragment : MvpAppCompatFragment(), AuthView {
    companion object {
        private const val RC_SIGN_IN = 1
    }

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    internal fun providePresenter(): AuthPresenter {
        return App.appComponent.authPresenterProvider().get()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onStart() {
        super.onStart()

        activity.setTitle(R.string.title_auth)
    }

    override fun startSignIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(listOf(
                                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                        ))
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            presenter.onSignInResponse(resultCode, IdpResponse.fromResultIntent(data))
        }
    }
}