package dllhell.familybudget.presentation.main;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.presentation.BasePresenter;
import dllhell.familybudget.ui.navigation.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public MainPresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        router.newRootScreen(Screens.AUTH);
    }
}

