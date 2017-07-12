package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Belal on 18/09/16.
 */


public class PastAttendance extends Fragment {

    private RecyclerView mRecyclerView;
    private static String LOG_TAG = "Attendance";
public String attclass="",attsubject="";
    private RecyclerView.Adapter mAdapter;
    ArrayList<String> hourcontent=new ArrayList<>();
    String hour,date;
    ArrayList<String> stucontent=new ArrayList<>();
    ArrayList<String> attcontent=new ArrayList<>();
    public static ArrayList<String> allstu,allatt;
    ArrayList<DataObject> addlist=new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String>attlist;
    EditText editText2;
    int stusize;
    FloatingActionButton stats;
    TextView tclass,tsubject,thour,tdate;
     Spinner spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.past_attendance, container, false);
        TextView update = (TextView) view.findViewById(R.id.update);
        tclass = (TextView) view.findViewById(R.id.textView10);
        thour = (TextView) view.findViewById(R.id.textView11);
        tdate = (TextView) view.findViewById(R.id.textView12);
        tsubject = (TextView) view.findViewById(R.id.textView13);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter3(new ArrayList<DataObject>(),attcontent);
        stats = (FloatingActionButton) view.findViewById(R.id.stats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new Stats();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();
            }
        });
//        mRecyclerView.setAdapter(mAdapter);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");
        tclass.setTypeface(face);
        thour.setTypeface(face);
        tdate.setTypeface(face);
        tsubject.setTypeface(face);
        update.setTypeface(face);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Update Attendance");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure to save your changes ?");

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        attlist=new ArrayList<String>(MyRecyclerViewAdapter3.ret_list());
                        System.out.println("GOna print"+stusize);
                        for(int i=0;i<stusize;i++)
                        {
                            System.out.println("Now"+stucontent.get(i)+" is"+attlist.get(i));
                        }

                        dialog.dismiss();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });
        date=new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        LayoutInflater li = LayoutInflater.from(getContext());
        System.out.println("LI is "+li);
        View promptsView = li.inflate(R.layout.past_att_select,null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

      spinner = (Spinner) promptsView.findViewById(R.id.spinner3);


        ArrayAdapter<String> obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,hourcontent);

        spinner.setAdapter(obj);
        editText2 = (EditText) promptsView.findViewById(R.id.editText2);
        final TextView textView7 = (TextView) promptsView.findViewById(R.id.textView7);
        editText2.setText(date);
        FloatingActionButton fab2 = (FloatingActionButton) promptsView.findViewById(R.id.fab2);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour=spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();

            }
        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                tclass.setText("Class : "+attclass);
                                tsubject.setText("Subject : "+attsubject);
                                thour.setText("Hour : "+hour);
                                tdate.setText("Date : "+date);
                                mAdapter = new MyRecyclerViewAdapter3(addlist,attcontent);
                                mRecyclerView.setAdapter(mAdapter);
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Past Attendance");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter3) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter3
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);

            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getActivity(), "Click the Navigation Drawer to change menu", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void showDatePicker() {
        SelectDateFragment date = new SelectDateFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            DecimalFormat mFormat= new DecimalFormat("00");

            editText2.setText(String.valueOf(mFormat.format(Double.valueOf(dayOfMonth))) + "-" + String.valueOf(mFormat.format(Double.valueOf(month+1)))
                    + "-" + String.valueOf(mFormat.format(Double.valueOf(year))));

            date=String.valueOf(mFormat.format(Double.valueOf(dayOfMonth))) + "-" + String.valueOf(mFormat.format(Double.valueOf(month+1)))
                    + "-" + String.valueOf(mFormat.format(Double.valueOf(year)));
            ArrayAdapter<String> obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,hourcontent);

            spinner.setAdapter(obj);
        }
    };
}