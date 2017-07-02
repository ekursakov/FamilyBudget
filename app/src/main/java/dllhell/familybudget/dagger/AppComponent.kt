package dllhell.familybudget.dagger

import javax.inject.Provider
import javax.inject.Singleton

import dagger.Component
import dllhell.familybudget.presentation.addexpense.AddExpensePresenter
import dllhell.familybudget.presentation.auth.AuthPresenter
import dllhell.familybudget.presentation.history.HistoryPresenter
import dllhell.familybudget.presentation.main.MainPresenter
import dllhell.familybudget.presentation.scanner.ScannerPresenter
import dllhell.familybudget.ui.MainActivity

@Singleton
@Component(modules = arrayOf(AppModule::class, NavigationModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun mainPresenterProvider(): Provider<MainPresenter>

    fun authPresenterProvider(): Provider<AuthPresenter>

    fun scannerPresenterProvider(): Provider<ScannerPresenter>

    fun historyPresenterProvider(): Provider<HistoryPresenter>

    fun addExpensePresenterProvider(): Provider<AddExpensePresenter>
}
