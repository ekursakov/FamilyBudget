package dllhell.familybudget.dagger

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AppModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return applicationContext
    }
}
