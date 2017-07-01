package dllhell.familybudget.data.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Expense {
    public String id;
    public Date date;
    public Double sum;
    public String category;
    public String comment;

    public Expense() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("date", date);
        result.put("sum", sum);
        result.put("category", category);
        result.put("comment", comment);
        return result;
    }
}