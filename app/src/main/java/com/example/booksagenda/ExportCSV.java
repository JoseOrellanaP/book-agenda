package com.example.booksagenda;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class ExportCSV {

    DataBaseHelper myDB;

    public void exportData(Context context){

        myDB = new DataBaseHelper(context);
        File folder = new File(Environment.getExternalStorageDirectory() + "/ExportSQLiteCSV");
        String file = folder.toString() + "/" + "books.csv";

        boolean isCreated = false;
        if (!folder.exists()){
            isCreated = folder.mkdir();

        }


        try {
            FileWriter fileWriter = new FileWriter(file);
            Cursor data = myDB.getData();


            if (data.getCount()==0){

                Toast.makeText(context, "There is no entries", Toast.LENGTH_SHORT).show();

            }else {

                while (data.moveToNext()){

                    fileWriter.append(data.getString(1));
                    fileWriter.append(",");
                    fileWriter.append(data.getString(2));
                    fileWriter.append(",");
                    fileWriter.append(data.getString(3));
                    fileWriter.append(",");
                    fileWriter.append(data.getString(4));
                    fileWriter.append("\n");

                }

                Toast.makeText(context, "Data exported to internal memory", Toast.LENGTH_SHORT).show();
            }

            myDB.close();
            fileWriter.close();

        }catch (Exception e){

        }
    }
}
