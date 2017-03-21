package com.example.zsoltvarga.hydrate;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "daily_entries.db";
    public static final String TABLE_DAILY = "entries";

   //columns of table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "dateofentry";
    public static final String COLUMN_PERCENTAGE = "percentageofentry";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String doThis = "CREATE TABLE " + TABLE_DAILY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_PERCENTAGE + " TEXT NOT NULL "+
                ");";

        db.execSQL(doThis);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY);
        onCreate(db);
    }

    /**
     * add new row to database
     */
    public void addEntry(DailyEntry entry){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, entry.get_date());
        values.put(COLUMN_PERCENTAGE, entry.get_percentage());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DAILY, null, values);
        db.close();
    }

    public void deleteEntry(String dateOfEntry){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DAILY + " WHERE " + COLUMN_DATE + "=\"" + dateOfEntry + "\";");

    }

    //generate a String that contains the database
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String doThis = "SELECT * FROM " + TABLE_DAILY + " WHERE 1";

        Cursor c = db.rawQuery(doThis, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_DATE))!= null ){
                dbString += "id: " + c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += " date: " + c.getString(c.getColumnIndex(COLUMN_DATE));
                if(c.getString(c.getColumnIndex(COLUMN_PERCENTAGE))!= null){
                    dbString += "  percentage: " + c.getString(c.getColumnIndex(COLUMN_PERCENTAGE));
                }
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    //deletes database table  - for development purposes (to be removed)
    public void deleteTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY);
        onCreate(db);

    }

    //creates database for all days in this month up to now
    public void makeUpDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY);
        onCreate(db);

        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String currentDay = sdf.format(new Date());

        SimpleDateFormat smf = new SimpleDateFormat("MM");
        String currentMonth = smf.format(new Date());


        int today = Integer.parseInt(currentDay);

        for(int i =(today-1); i>-1; i--){
            Random rnd = new Random();
            String addZero = "";
            if((today-i)<10){
                addZero = "0";
            }
            values.put(COLUMN_DATE, addZero+ "" + (today-i)+":"+currentMonth+ ":17");
            values.put(COLUMN_PERCENTAGE, rnd.nextInt(140)+70);
            db.insert(TABLE_DAILY, null, values);
        }

        db.close();
    }

    /**
     * returns data from one day in format: dd:MM:yy:percent
     * @param noDays [int] number of days to go back to past (rows to go up from the last one )
     * @return String data from the day in format: dd:MM:yy:percent example: 03:04:17:99
     */
    public String getPastDay(int noDays){
        String dbString = "0";
        SQLiteDatabase db = getWritableDatabase();
        String doThis = "SELECT * FROM " + TABLE_DAILY + " WHERE 1";

        Cursor c = db.rawQuery(doThis, null);

        boolean emptyDatabase = false;

        //movetofirst returns true if theres something at the first line
        emptyDatabase = !c.moveToFirst();

        c.moveToLast();
        //this pushes the cursor back noDays number of lines
        boolean theend= false;
        for(int i =0; i<noDays; i++){
            if(c.getPosition()>=2) {
                c.moveToPrevious();
            } else if(c.getPosition()==1){
                theend = true;
            }
        }

        if(emptyDatabase == false) {
            if (c.getString(c.getColumnIndex(COLUMN_DATE)) != null && theend == false) {
                dbString = "";
                dbString += c.getString(c.getColumnIndex(COLUMN_DATE));
                if (c.getString(c.getColumnIndex(COLUMN_PERCENTAGE)) != null) {
                    dbString += ":" + c.getString(c.getColumnIndex(COLUMN_PERCENTAGE));
                }
            }
        }
        db.close();
        return dbString;
    }

    /**
     * returns percentage data from one day
     * @param noDays [int] number of days to go back to past(rows to go up from the last one )
     * @return int
     */
    public int getPastPercentage(int noDays){
        String dbString;
        SQLiteDatabase db = getWritableDatabase();
        String doThis = "SELECT * FROM " + TABLE_DAILY + " WHERE 1";

        Cursor c = db.rawQuery(doThis, null);
        boolean emptyDatabase = false;

        //movetofirst returns true if theres something at the first line
        emptyDatabase = !c.moveToFirst();


        c.moveToLast();

        //this pushes the cursor back noDays number of lines
        boolean theend = false;
        for(int i =0; i<noDays; i++){
            if(c.getPosition()>=2) {
                c.moveToPrevious();
            } else if(c.getPosition()==1){
                theend = true;
            }
        }

        if(emptyDatabase == false) {
            if (c.getString(c.getColumnIndex(COLUMN_PERCENTAGE)) != null && theend == false) {
                dbString = "";
                dbString += c.getString(c.getColumnIndex(COLUMN_PERCENTAGE));
            } else {
                dbString = "0";
            }
        } else {
            dbString = "0";
        }
        db.close();
        return Integer.parseInt(dbString);
    }


}

















