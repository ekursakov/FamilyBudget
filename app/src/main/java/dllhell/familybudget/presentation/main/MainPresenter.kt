package dllhell.familybudget.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth

import javax.inject.Inject

import dllhell.familybudget.presentation.BasePresenter
import dllhell.familybudget.ui.navigation.Screens
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter @Inject
constructor(private val router: Router) : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            router.newRootScreen(Screens.SCANNER)
        } else {
            router.newRootScreen(Screens.AUTH)
        }
    }

    fun navigateToScanner() {
        router.newRootScreen(Screens.SCANNER)
    }

    fun navigateToAddExpense() {
        router.newRootScreen(Screens.ADD_EXPENSE)
    }

    fun navigateToHistory() {
        router.newRootScreen(Screens.HISTORY)
    }

    fun navigateToStats() {
        router.newRootScreen(Screens.HISTORY)
    }
}

