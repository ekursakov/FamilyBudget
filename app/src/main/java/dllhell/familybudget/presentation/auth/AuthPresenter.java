package dllhell.familybudget.presentation.auth;

import com.arellomobile.mvp.InjectViewState;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import dllhell.familybudget.presentation.BasePresenter;
import dllhell.familybudget.ui.navigation.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AuthPresenter extends BasePresenter<AuthView> {
    private final Router router;

    @Inject
    public AuthPresenter(Router router) {
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(result -> router.newRootScreen(Screens.SCANNER))
                .addOnFailureListener(e -> router.exitWithMessage("Auth error"));
    }

    public void onSignInResponse(int resultCode, @Nullable IdpResponse response) {
        if (resultCode == ResultCodes.OK) {
            router.newRootScreen(Screens.SCANNER);
        } else {
            router.exit();
        }
    }
}

