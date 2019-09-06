package it372.finalprojectvarilla;
/*
Name: Joseph Varilla
        Date: 6/10/2019
        Final Project
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.time.LocalDate;

public class LoanActivity extends AppCompatActivity {
    protected  SQLiteDatabase db;
    protected int loanId;
    protected String loanInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        TextView loanInfoDsp = findViewById(R.id.dspLoanData);
        try {
            // Unpack Intent
            Intent intent = getIntent();
            loanId = intent.getIntExtra("loanId", 0);

            LoansDBHelper loansDBHelper = new LoansDBHelper(this);
            db = loansDBHelper.getWritableDatabase();

            String query = String.format("SELECT * FROM loans WHERE id=%d", loanId);
            Cursor c = db.rawQuery(query, new String[] {});
            c.moveToFirst();


            String name = c.getString(4);
            loanInfoDsp.setText(name);

            String owedBorrowed = c.getInt(1) > 0 ? "I Owe" : "Lent To";
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String amountOwed = formatter.format(c.getFloat(2));
            String fname = c.getString(3);
            String lname = c.getString(4);
            String startDate = c.getString(6);
            String dueDate = c.getString(7);
            String receiveAlerts = c.getInt(8) > 0 ? "Yes" : "No";
            String toBePaid = c.getInt(10) > 0 ? "Not Paid" : "Paid";

            loanInfo = String.format("" +
                    " Loan #%d " +
                    "\n %s to %s %s" +
                    "\n Start Date: %s" +
                    "\n Due Date: %s" +
                    "\n Receive Alerts: %s" +
                    "\n Status: %s", loanId, owedBorrowed, amountOwed, fname, lname, startDate, dueDate, receiveAlerts, toBePaid);

            loanInfoDsp.setText(loanInfo);
            db.close();
        } catch (Error error) {
            loanInfoDsp.setText("Loan Not Found");
        }

        if (savedInstanceState != null) { // Restore from bundle
            loanId = savedInstanceState.getInt("loanId");
            loanInfo = savedInstanceState.getString("loanInfo");
        }
    }

    // Save the loan data
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("loanId", loanId);
        bundle.putString("loanInfo", loanInfo);
    }
    // Mark as loan as paid
    public void markLoanAsPaid(View view) {
         String id = String.valueOf(loanId);
         LoansDBHelper loansDBHelper = new LoansDBHelper(this);
         SQLiteDatabase db = loansDBHelper.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put("isPaid", true);
         db.update("loans", cv, "id = ?", new String[]{id});
         String confirmation = "Loan Was Marked As Paid";
         int duration = Toast.LENGTH_LONG;
         Toast markedAsPaidToast = Toast.makeText(this, confirmation, duration);
         markedAsPaidToast.show( );
         finish();
    }
}
