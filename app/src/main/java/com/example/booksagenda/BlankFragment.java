package com.example.booksagenda;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXTB = "textB";
    private static final String TEXTA = "textA";
    private static final String TEXTG = "textG";
    private static final String TEXTRID = "textRID";
    private static final String TEXTDA = "textDa";


    // TODO: Rename and change types of parameters
    private String mTextB;
    private String mAuthor;
    private String mGender, mRealID, mDate;

    private EditText bookE, authorE;
    private TextView genderE, dateE;
    private Button updateE;
    private Button returnR, updateD;

    DataBaseHelper myDB;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String textB, String  textA, String textG, String textRID, String textD) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(TEXTB, textB);
        args.putString(TEXTA, textA);
        args.putString(TEXTG, textG);
        args.putString(TEXTRID, textRID);
        args.putString(TEXTDA, textD);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTextB = getArguments().getString(TEXTB);
            mAuthor = getArguments().getString(TEXTA);
            mGender = getArguments().getString(TEXTG);
            mRealID = getArguments().getString(TEXTRID);
            mDate = getArguments().getString(TEXTDA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        myDB = new DataBaseHelper(view.getContext());
        bookE = view.findViewById(R.id.editTextBE);
        authorE = view.findViewById(R.id.editTextAE);
        genderE = view.findViewById(R.id.textViewbookGE);
        dateE = view.findViewById(R.id.textViewDateE);

        updateE = view.findViewById(R.id.buttonUpdateE);
        returnR = view.findViewById(R.id.buttonReturnE);
        updateD = view.findViewById(R.id.buttonUpdateDate);

        bookE.setText(mTextB);
        bookE.requestFocus();

        authorE.setText(mAuthor);
        genderE.setText(mGender);
        dateE.setText(mDate);

        returnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSupportFragmentManager().findFragmentByTag("Frag1")==null){
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }else {
                    BlankFragment.super.getActivity().onBackPressed();
                }
            }
        });

        updateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newBookName = bookE.getText().toString();
                String newAuthorName = authorE.getText().toString();
                String newGender = genderE.getText().toString();
                String newDate = dateE.getText().toString();

                boolean updated = myDB.updateData(mRealID, newBookName, newAuthorName, newGender, newDate);

                if (updated == true){
                    Toast.makeText(view.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(view.getContext(), "Not updated", Toast.LENGTH_SHORT).show();
                }

                // Comunicate changes

                String sendBackTest = "Change";
                sendBack(sendBackTest);

                // Close fragment

                if (getActivity().getSupportFragmentManager().findFragmentByTag("Frag1")==null){
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }else {
                    BlankFragment.super.getActivity().onBackPressed();
                }

            }
        });

        updateD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DAY_OF_MONTH, day);
                        String currentDate = DateFormat.getDateInstance().format(calendar1.getTime());
                        dateE.setText(currentDate);

                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack(String sendBackTest) {
        if (mListener != null) {
            mListener.onFragmentInteraction(sendBackTest);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String sendBackText);
    }


}
