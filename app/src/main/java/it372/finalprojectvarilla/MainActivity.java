package it372.finalprojectvarilla;
/*
Name: Joseph Varilla
        Date: 6/10/2019
        Final Project
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Define All Form Widgets
    public Spinner owedSpinner;
    public EditText borrowedAmountText;
    public EditText otherFirstNameText;
    public EditText otherLastNameText;
    public EditText otherPhoneText;
    public DatePicker deadlineDatePicker;
    public ToggleButton alertBtn;
    public CheckBox affirmationBox;
    public Button submitBtn;
    public String startDateLoadedOnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeForm();

        // Store the current date which the phone defaults to onload
        startDateLoadedOnCreate = parseDateFromDatePicker();
        if (savedInstanceState != null) {
            // Unbundle values
            String owedSave = savedInstanceState.getString("owed");
            int owedSpinnerPos = savedInstanceState.getInt("owedSpinnerPos");
            String amountSave = String.valueOf(savedInstanceState.getFloat("amount"));
            String fnameSave = savedInstanceState.getString("fname");
            String lnameSave = savedInstanceState.getString("lname");
            String phoneSave = savedInstanceState.getString("phone");
            int dueYearSave = savedInstanceState.getInt("dueYear");
            int dueMonthSave = savedInstanceState.getInt("dueMonth");
            int dueDaySave = savedInstanceState.getInt("dueDay");
            boolean alertSave = savedInstanceState.getBoolean("alert");
            boolean affirmSave = savedInstanceState.getBoolean("affirm");

            // Restore Values
            owedSpinner.setSelection(owedSpinnerPos);
            borrowedAmountText.setText(amountSave);
            otherFirstNameText.setText(fnameSave);
            otherLastNameText.setText(lnameSave);
            otherPhoneText.setText(phoneSave);
            deadlineDatePicker.updateDate(dueYearSave, dueMonthSave, dueDaySave);
            alertBtn.setChecked(alertSave);
            affirmationBox.setChecked(affirmSave);


        }
    }

    // Save All To Bundle
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);


        // Save Everything
        String owedSave = owedSpinner.getSelectedItem().toString();
        int owedSpinnerPos = owedSpinner.getSelectedItemPosition();
        Float amountSave = Float.parseFloat(borrowedAmountText.getText().toString());
        String fnameSave = otherFirstNameText.getText().toString();
        String lnameSave = otherLastNameText.getText().toString();
        String phoneSave = otherPhoneText.getText().toString();
        int dueYearSave = deadlineDatePicker.getYear();
        int dueMonthSave = deadlineDatePicker.getMonth();
        int dueDaySave = deadlineDatePicker.getDayOfMonth();
        boolean alertSave = alertBtn.isChecked();
        boolean affirmSave = affirmationBox.isChecked();

        bundle.putString("owed", owedSave);
        bundle.putInt("owedSpinnerPos", owedSpinnerPos);
        bundle.putFloat("amount", amountSave);
        bundle.putString("fname", fnameSave);
        bundle.putString("lname", lnameSave);
        bundle.putString("phone", phoneSave);
        bundle.putInt("dueYear", dueYearSave);
        bundle.putInt("dueMonth", dueMonthSave);
        bundle.putInt("dueDay", dueDaySave);
        bundle.putBoolean("alert", alertSave);
        bundle.putBoolean("affirm", affirmSave);

    }

    protected void initializeForm() {
        // Initialize all of the form elements
        owedSpinner = findViewById(R.id.spnnr_owe1);
        borrowedAmountText = findViewById(R.id.amount_txt1);
        otherFirstNameText = findViewById(R.id.ofname_txt1);
        otherLastNameText = findViewById(R.id.olname_txt1);
        otherPhoneText = findViewById(R.id.ophone_txt1);
        deadlineDatePicker = findViewById(R.id.due_date_picker1);
        alertBtn = findViewById(R.id.alert_toggle1);
        affirmationBox = findViewById(R.id.affirm_checkbox1);
        submitBtn = findViewById(R.id.submitBtn1);
    }

    // Submit form
    public void submitForm(View view) {
        // Make sure that the loan is certified
        if (borrowedAmountText.getText().toString().trim().length() < 1) {
            String warning = "Please Enter In A Borrowd Amount";
            int duration = Toast.LENGTH_LONG;
            Toast needAmountToast = Toast.makeText(this, warning, duration);
            needAmountToast.show( );
        } else if (!affirmationBox.isChecked()) { // if not certified make a toast to warn a user
            String warning = "Please Check That You Affirm The Terms";
            int duration = Toast.LENGTH_LONG;
            Toast certifyRequiredToast = Toast.makeText(this, warning, duration);
            certifyRequiredToast.show( );
        } else {
            // Prepare data for insertion

            String startDate = startDateLoadedOnCreate;
            String dueDate = parseDateFromDatePicker();

            boolean isOwed = owedSpinner.getSelectedItem().toString().equals("I Owe");
            float amount = Float.parseFloat(borrowedAmountText.getText().toString());

            // Make a new loan object
            Loan newLoan = new Loan(isOwed, amount, otherFirstNameText.getText().toString(),
                    otherLastNameText.getText().toString(), otherPhoneText.getText().toString(),
                    startDate, dueDate, alertBtn.isChecked(), affirmationBox.isChecked());

            // Put it in the database
            insertNewLoan(newLoan);
            goToDisplay();

        }
    }

    public String parseDateFromDatePicker() {
        // The month date and year are all in hex.
        // The year is in hexadecimal so convert
        String yearStr = String.valueOf(deadlineDatePicker.getYear());

        // January is 0 so add 1
        String monthStr = String.valueOf(deadlineDatePicker.getMonth() + 1);

        String dayStr = String.valueOf(deadlineDatePicker.getDayOfMonth());
        // Should look like 1999-01-01 (%02d makes sure single digit numbers get a leading 0
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }

        if (dayStr.length() < 2) {
            dayStr = "0" + dayStr;
        }
        String outputDate =  yearStr + "-" + monthStr + "-" + dayStr;

        return outputDate;

    }

    // Inserts a river point into memory
    public void insertNewLoan(Loan loan) {
        LoansDBHelper loanhelper = new LoansDBHelper(this);
        SQLiteDatabase db = loanhelper.getWritableDatabase();

        ContentValues loanValues = new ContentValues();
        // Null is put in for the id since it relies on SQLITE to make an autoint
        loanValues.put("owedTo", loan.isOwed());
        loanValues.put("amount", loan.getAmount());
        loanValues.put("fname", loan.getFname());
        loanValues.put("lname", loan.getLname());
        loanValues.put("phone", loan.getPhone());
        loanValues.put("startDate", loan.getBorrowedDate());
        loanValues.put("dueDate", loan.getDueDate());
        loanValues.put("receiveAlerts", loan.isReceiveAlerts());
        loanValues.put("affirmed", loan.isAffirmed());
        loanValues.put("isPaid", false);


        db.insert("loans", null, loanValues);

    }

    public void goToDisplay() {
        //Intent loadDisplayScreen that lists all rows
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }
}
