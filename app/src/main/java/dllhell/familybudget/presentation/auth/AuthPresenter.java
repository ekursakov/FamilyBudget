package dllhell.familybudget.presentation.auth;

import com.arellomobile.mvp.InjectViewState;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.data.models.Expense;
import dllhell.familybudget.presentation.BasePresenter;
import dllhell.familybudget.ui.navigation.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AuthPresenter extends BasePresenter<AuthView> {
    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public AuthPresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(command -> {
            dataRepository.addExpense(new Expense(new Date(10000000), 50010)).subscribe();
            router.newRootScreen(Screens.ADD_EXPENSE);
        });
    }
}

