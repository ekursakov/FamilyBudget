package dllhell.familybudget.presentation.history

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import dllhell.familybudget.data.models.Expense
import dllhell.familybudget.util.AddToEndSingleByTagStateStrategy

@StateStrategyType(AddToEndSingleStrategy::class)
interface HistoryView : MvpView
