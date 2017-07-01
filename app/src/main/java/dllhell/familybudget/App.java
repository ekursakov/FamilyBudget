package dllhell.familybudget;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

import dllhell.familybudget.dagger.AppComponent;
import dllhell.familybudget.dagger.AppModule;
import dllhell.familybudget.dagger.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        FirebaseAuth.getInstance().signInAnonymously();
    }
}
