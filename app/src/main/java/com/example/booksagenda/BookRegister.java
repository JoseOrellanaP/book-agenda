package com.example.booksagenda;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookRegister extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener{

    List<BooksItem> booksItems;
    ListView listView;
    BookAdapter adapter;
    DataBaseHelper myDDBB;
    SearchView searchView;
    RadioButton bookR, authorR, genderR, dateR;
    RadioGroup radioGroup;
    String checkedI;
    Button datePicker;
    TextView dateTextView;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewer);

        frameLayout=findViewById(R.id.fragment_container);

        listView = findViewById(R.id.listviewBooks);
        myDDBB = new DataBaseHelper(this);
        searchView = findViewById(R.id.searchViewBooks);
        innitiate();


        // Using radiobutton to get the filter condition

        bookR = findViewById(R.id.radioButtonBook);
        authorR = findViewById(R.id.radioButtonAuthor);
        genderR = findViewById(R.id.radioButtonGender);
        dateR = findViewById(R.id.radioButtonDate);

        radioGroup = findViewById(R.id.radioGroupB);

        bookR.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (bookR.isChecked()){
                    checkedI = "book";
                }else if(authorR.isChecked()){
                    checkedI = "author";
                }else if(genderR.isChecked()) {
                    checkedI = "gender";
                }else{
                    checkedI = "date";
                }

                adapter = new BookAdapter(BookRegister.this, booksItems, checkedI);
                listView.setAdapter((ListAdapter) adapter);
            }
        });


        adapter = new BookAdapter(this, booksItems, "book");
        listView.setAdapter((ListAdapter) adapter);

        // Searching through the list

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            final CharSequence[] items = {"Edit", "Delate"};

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BookRegister.this);
                builder.setTitle("Options");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {

                        TextView realI = view.findViewById(R.id.textViewRInd);
                        final String ind = realI.getText().toString();

                        // Action when delete option is selected

                        if (items[i2].toString().contains("Delate")){

                            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i3) {
                                    switch (i3){

                                        case DialogInterface.BUTTON_POSITIVE:

                                            int deleted = myDDBB.deleteEntry(ind);
                                            if (deleted>0){
                                                Toast.makeText(BookRegister.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                booksItems.remove(i);
                                                adapter.notifyDataSetChanged();
                                            }else {
                                                Toast.makeText(BookRegister.this, "Not", Toast.LENGTH_SHORT).show();
                                            }

                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;

                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();


                        }

                        // Action when edit option has been selected

                        if (items[i2].toString().contains("Edit")){

                            int indextomod2 = Integer.parseInt(ind);


                            String bookName = getSpecificD(indextomod2 , 1);
                            String author = getSpecificD(indextomod2,2);
                            String gender = getSpecificD(indextomod2, 3);
                            String date = getSpecificD(indextomod2, 4);

                            //Toast.makeText(BookRegister.this, ""+bookName+author+gender, Toast.LENGTH_SHORT).show();

                            openFragment(bookName, author, gender, ind, date);

                        }

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void innitiate(){
        booksItems = new ArrayList<>();

        ArrayList<String> index = getDataFromRegister(0);
        ArrayList<String> bookN = getDataFromRegister(1);
        ArrayList<String> authorN = getDataFromRegister(2);
        ArrayList<String> genderN = getDataFromRegister(3);
        ArrayList<String> dateN = getDataFromRegister(4);

        for (int i=0; i<bookN.size();i++){
            String realI = index.get(i);
            String boo = bookN.get(i);
            String aut = authorN.get(i);
            String gen = genderN.get(i);
            String dat = dateN.get(i);
            BooksItem BI = new BooksItem(boo, aut, gen,realI, dat);
            booksItems.add(BI);

        }
    }

    public ArrayList<String> getDataFromRegister(int index){

        Cursor getData = myDDBB.getData();
        ArrayList<String> dataRetrieved = new ArrayList<>();

        if (getData.getCount()==0){
            return dataRetrieved;
        }else {

            while (getData.moveToNext()){
                dataRetrieved.add(getData.getString(index));
            }
        }
        return dataRetrieved;
    }

    public String getSpecificD(int pos, int index){

        Cursor getSpData = myDDBB.getDataSp(pos);
        String field;

        if (getSpData.getCount()==0){
            field = "";
        }else {
            getSpData.moveToNext();
            field = getSpData.getString(index);

        }

        return field;

    }

    public void openFragment(String book, String author, String gender, String realID, String date){
        BlankFragment fragment = BlankFragment.newInstance(book, author, gender, realID, date);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack("Frag1");
        transaction.commit();

    }

    @Override
    public void onFragmentInteraction(String sendBackText) {
        this.recreate();
    }


}
