package dllhell.familybudget.presentation.addexpense;

import com.arellomobile.mvp.InjectViewState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.data.models.Expense;
import dllhell.familybudget.presentation.BasePresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AddExpensePresenter extends BasePresenter<AddExpenseView> {

    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public AddExpensePresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }


    public void onSaveClick(Date date, double sum, String category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Expense post = new Expense("", date, sum, category, "");
        database.getReference().child("expenses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .push()
                .setValue(post)
                .addOnSuccessListener(command -> {
                    router.exit();
                })
                .addOnFailureListener(command -> {
                    router.showSystemMessage("Error");
                });
    }
}
