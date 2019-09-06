package it372.finalprojectvarilla;
/*
Name: Joseph Varilla
        Date: 6/10/2019
        Final Project
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        // Reload the display
        loadDisplay();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Reload the display (to refresh screen after database is updated
        loadDisplay();
    }

    protected void loadDisplay() {
        LoansDBHelper loansDBHelper = new LoansDBHelper(this);
        SQLiteDatabase db = loansDBHelper.getReadableDatabase();

        // Get all records
        Cursor c = db.rawQuery("SELECT * FROM loans", new String[] {});
        c.moveToFirst( );
        String results = "";

        // Make the three lists to add loans to
        LinearLayout owedList = (LinearLayout) findViewById(R.id.owed_loan_list);
        LinearLayout loanedList = (LinearLayout) findViewById(R.id.loaned_loan_list);
        LinearLayout paiddList = (LinearLayout) findViewById(R.id.paid_loan_list);

        // Clear out all loans to not make duplicates
        owedList.removeAllViews();
        loanedList.removeAllViews();
        paiddList.removeAllViews();
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        // These variable are used for alternating the background color of each row loan
        int owedBackgroundColorAlternator = 0;
        int loanedBackgroundColorAlternator = 0;
        int paidBackgroundColorAlternator = 0;
        int alternatingBackgroundColors[] = {R.color.lightCyan, R.color.medCyan, R.color.lightGray, R.color.medGray};


        do { // For every loan

            TextView newRow = new TextView(this);
            newRow.setLayoutParams(params);
            newRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            // Get the fields for row view
            int loanId = c.getInt(0);
            String owedBorrowed = c.getInt(1) > 0 ? "I Owe" : "Lent To";
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String amountOwed = formatter.format(c.getFloat(2));
            String fname = c.getString(3);
            String lname = c.getString(4);
            String dueDate = c.getString(7);
            boolean isPaid = c.getInt(10) > 0 ? true : false;

            // Separate the loans into the three lists
            if (!isPaid) {
                if (owedBorrowed.equals("I Owe")) {// Place in I Owe List
                    owedList.addView(newRow);
                    newRow.setBackgroundColor(getResources().getColor(alternatingBackgroundColors[owedBackgroundColorAlternator % 2]));
                    owedBackgroundColorAlternator++;
                } else { // Place in I Lent List
                    loanedList.addView(newRow);
                    newRow.setBackgroundColor(getResources().getColor(alternatingBackgroundColors[loanedBackgroundColorAlternator % 2]));
                    loanedBackgroundColorAlternator++;
                }
            } else { // Place in Paid Loan List
                paiddList.addView(newRow);
                newRow.setBackgroundColor(getResources().getColor(alternatingBackgroundColors[(paidBackgroundColorAlternator % 2) + 2 ]));
                paidBackgroundColorAlternator++;
            }

            // Set Text to show a fiew fields
            newRow.setText(String.format("%s \t %s. %s \t %s", amountOwed, fname.charAt(0), lname,dueDate));

            // Add loanId data tag so that the LoanActivity class can query for the right loan
            newRow.setTag(loanId);

            // Set click handler for loan
            newRow.setOnClickListener(new HandleLoanClicked(this));
        } while(c.moveToNext());
    }

    // When a loan row is clicked it should boot up a loan activity
    public class HandleLoanClicked implements View.OnClickListener {
        Context savedContext;
        public HandleLoanClicked(Context context) {
            savedContext = context;
        }
        public void onClick(View view) {
            int loanId = (int) view.getTag();

            // Start new activity and send the loan id
            Intent intent = new Intent(savedContext, LoanActivity.class);
            intent.putExtra("loanId", loanId);
            startActivity(intent);
        }
    }
}



