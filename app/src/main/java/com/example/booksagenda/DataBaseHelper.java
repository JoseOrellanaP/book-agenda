package com.example.booksagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "BOOKS_DIRECTORY.db";
    public static final String TABLENAME = "BOOKS_TABLE";
    public static final String COL1 = "ID";
    public static final String COL2 = "TITLE";
    public static final String COL3 = "AUTHOR";
    public static final String COL4 = "GENDER";
    public static final String COL5 = "DATE";

    public DataBaseHelper(Context context) {
        super(context, DATABASENAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE "+TABLENAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, AUTHOR TEXT, GENDER TEXT, DATE TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS "+TABLENAME);
    }

    public boolean saveData (String title, String author, String gender, String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, title);
        contentValues.put(COL3, author);
        contentValues.put(COL4, gender);
        contentValues.put(COL5, date);
        Long added = db.insert(TABLENAME, null, contentValues);

        if (added == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getData = db.rawQuery("SELECT * FROM "+TABLENAME, null);
        return getData;
    }

    public Cursor getDataSp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getDataSp = db.rawQuery("SELECT * FROM "+TABLENAME+ " WHERE ID = "+id, null);
        return getDataSp;
    }

    public Integer deleteEntry(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLENAME, "ID = "+id, null);
    }

    public boolean updateData (String idN, String bookN, String authorN, String genderN, String dateN){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, bookN);
        contentValues.put(COL3, authorN);
        contentValues.put(COL4, genderN);
        contentValues.put(COL5, dateN);

        db.update(TABLENAME, contentValues, "id = ?", new String[]{idN});
        return true;

    }

}
