package dllhell.familybudget.dagger;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Component;
import dllhell.familybudget.presentation.auth.AuthPresenter;
import dllhell.familybudget.presentation.history.HistoryPresenter;
import dllhell.familybudget.presentation.main.MainPresenter;
import dllhell.familybudget.ui.MainActivity;

@Singleton
@Component(modules = {AppModule.class, NavigationModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    Provider<MainPresenter> mainPresenterProvider();

    Provider<AuthPresenter> authPresenterProvider();

    Provider<HistoryPresenter> historyPresenterProvider();
}
