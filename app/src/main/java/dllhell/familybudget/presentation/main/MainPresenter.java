package dllhell.familybudget.presentation.main;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import dllhell.familybudget.presentation.BasePresenter;
import dllhell.familybudget.ui.navigation.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    private final Router router;

    @Inject
    public MainPresenter(Router router) {
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        router.newRootScreen(Screens.AUTH);
    }

    public void navigateToScanner() {
        router.newRootScreen(Screens.SCANNER);
    }

    public void navigateToAddExpense() {
        router.newRootScreen(Screens.ADD_EXPENSE);
    }

    public void navigateToHistory() {
        router.newRootScreen(Screens.HISTORY);
    }

    public void navigateToStats() {
        router.newRootScreen(Screens.HISTORY);
    }
}

