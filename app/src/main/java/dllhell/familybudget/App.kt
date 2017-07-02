package dllhell.familybudget

import android.app.Application

import com.google.firebase.database.FirebaseDatabase

import dllhell.familybudget.dagger.AppComponent
import dllhell.familybudget.dagger.AppModule
import dllhell.familybudget.dagger.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
            private set
    }
}
