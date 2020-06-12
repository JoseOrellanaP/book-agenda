package com.example.booksagenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button save, cleanup, datePicker;
    EditText title, author;
    Spinner spinnerGender;
    ArrayAdapter spinnerGenderAdapter;
    DataBaseHelper myDB;
    TextView dateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DataBaseHelper(this);
        title = findViewById(R.id.editTextBN);
        author = findViewById(R.id.editTextAN);
        spinnerGender = findViewById(R.id.spinnerGN);



        // To open and pick a date

        dateTextView = findViewById(R.id.textViewDate);
        datePicker = findViewById(R.id.buttonDateP);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });




        // Populate spinner with the defined genders --------------------------------------

        spinnerGenderAdapter = ArrayAdapter.createFromResource(this, R.array.genders,android.R.layout.simple_spinner_item);
        spinnerGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerGenderAdapter);




        //Button add---------------------------------------------------------------------

        save = findViewById(R.id.buttonAdd);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String titleN = title.getText().toString();
                final String authorN = author.getText().toString();
                final String genderN = spinnerGender.getSelectedItem().toString();
                final String dateP = dateTextView.getText().toString();

               if (titleN.isEmpty() || authorN.isEmpty() || genderN.isEmpty() || dateP.isEmpty()){
                    Toast.makeText(MainActivity.this, "You need to fill all the required data", Toast.LENGTH_SHORT).show();
                }else {

                   DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           switch (i){
                               case DialogInterface.BUTTON_POSITIVE:
                                   saveData(titleN, authorN, genderN, dateP);
                                   break;
                               case DialogInterface.BUTTON_NEGATIVE:
                                   break;
                           }
                       }
                   };

                   AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                   builder.setMessage("Do you want to save the data").setPositiveButton("Yes",dialogClickListener).setNegativeButton("No",dialogClickListener).show();
                }
            }
        });



        //Button cleanup----------------------------------------------------------------------

        cleanup = findViewById(R.id.buttonClean);
        cleanup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title.setText("");
                author.setText("");
                dateTextView.setText("");

            }
        });

    }


    //Saving up the data-------------------------------------------------

    public void saveData(String titleN, String authorN, String genderN, String dateN){
        boolean saved = myDB.saveData(titleN, authorN, genderN, dateN);
        if (saved == true){
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        dateTextView.setText(currentDateString);
    }
}
