package com.sliit.budgetcalculator.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import com.sliit.budgetcalculator.model.IncomeExpense;

/**
 * Created by Sk  on 9/16/2017.
 */

public class IEDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ie.db";
    private static final int DATABASE_VERSION = 3 ;
    public static final String TABLE_NAME = "budget";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IE_AMOUNT = "amount";
    public static final String COLUMN_IE_DESP = "description";
    public static final String COLUMN_IE_DATE = "date";
    public static final String COLUMN_IE_TYPE = "type";
    private static final String VALUE_INCOME ="income" ;
    private static final String VALUE_EXPENSE = "expenses";


    public IEDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IE_AMOUNT + " NUMBER NOT NULL, " +
                COLUMN_IE_DESP + " TEXT NOT NULL, " +
                COLUMN_IE_TYPE + " TEXT NOT NULL, " +
                COLUMN_IE_DATE + " TEXT NOT NULL); "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    public void saveNewIE(IncomeExpense incomeExpense) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IE_AMOUNT, incomeExpense.getAmount());
        values.put(COLUMN_IE_DESP, incomeExpense.getDescription());
        values.put(COLUMN_IE_TYPE, incomeExpense.getIe_type());
        values.put(COLUMN_IE_DATE, incomeExpense.getDate());

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<IncomeExpense> IElist(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<IncomeExpense> incomeExpenseLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        IncomeExpense incomeExpense;

        if (cursor.moveToFirst()) {
            do {
                incomeExpense = new IncomeExpense();
                incomeExpense.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                incomeExpense.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_IE_DESP)));
                incomeExpense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_IE_DATE)));
                incomeExpense.setAmount(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IE_AMOUNT))));
                incomeExpenseLinkedList.add(incomeExpense);
            } while (cursor.moveToNext());
        }
        return incomeExpenseLinkedList;
    }

    /**Query only 1 record**/
    public IncomeExpense getIE(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        IncomeExpense receivedIncomeExpense = new IncomeExpense();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedIncomeExpense.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_IE_DESP)));
            receivedIncomeExpense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_IE_DATE)));
            receivedIncomeExpense.setAmount(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IE_AMOUNT))));
        }
        return receivedIncomeExpense;
    }


    /**delete record**/
    public void deleteIERecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updateIERecord(long personId, Context context, IncomeExpense updatedIE) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET description ='"+ updatedIE.getDescription() + "', date ='" + updatedIE.getDate()+ "', amount ='"+ updatedIE.getAmount()  + "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }

    /**for chart - get income**/
    public Float getTotalIncome(){
        int amount;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT sum(amount) FROM " + TABLE_NAME + "  WHERE type='"+ VALUE_INCOME+"'";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        return Float.valueOf(amount);
    }

    /**for chart - get income**/
    public Float getTotalExpense(){
        int amount;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT sum(amount) as total FROM " + TABLE_NAME + "  WHERE type='"+ VALUE_EXPENSE+"'";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        return Float.valueOf(amount);
    }



}
