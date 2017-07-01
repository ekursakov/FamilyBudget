package dllhell.familybudget.data.models;

import java.util.Date;

public class Expense {
    private final Date date;
    private final long sum;

    public Expense(Date date, long sum) {
        this.date = date;
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public long getSum() {
        return sum;
    }
}
