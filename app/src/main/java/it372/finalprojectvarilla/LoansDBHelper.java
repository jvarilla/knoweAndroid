package it372.finalprojectvarilla;
/*
Name: Joseph Varilla
        Date: 6/10/2019
        Final Project
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LoansDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "loans.db";
    private static final int DB_VERSION = 1;
    private static Context referenceToActivity = null;

    public LoansDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        referenceToActivity = context;
    }

    @Override
    // This method is only called if the database
    // does not exist.
    public void onCreate(SQLiteDatabase db) {
        // Create River and Houses Tables and load data into them

        // Creation of table and insertion of values must be done successfully together
        try {
            // Create the loans table
            db.execSQL("create table loans(" +
                    "id integer PRIMARY KEY, " +
                    "owedTo boolean, amount float," +
                    "fname varchar(32), lname varchar(32), phone varchar(32)," +
                    "startDate varchar(32), dueDate varchar(32), " +
                    "receiveAlerts boolean, affirmed boolean, isPaid boolean)");
        } catch (Error e) {
            System.out.println("Failed");
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

        // This method is required, but not used
        // in this example
    }
}
