package dllhell.familybudget.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ResultCodes
import com.google.firebase.auth.FirebaseAuth

import javax.inject.Inject

import dllhell.familybudget.presentation.BasePresenter
import dllhell.familybudget.ui.navigation.Screens
import ru.terrakok.cicerone.Router

@InjectViewState
class AuthPresenter @Inject
constructor(private val router: Router) : BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener { router.newRootScreen(Screens.SCANNER) }
                .addOnFailureListener { router.exitWithMessage("Auth error") }
    }

    fun onSignInResponse(resultCode: Int, response: IdpResponse?) {
        if (resultCode == ResultCodes.OK) {
            router.newRootScreen(Screens.SCANNER)
        } else {
            router.exit()
        }
    }
}

