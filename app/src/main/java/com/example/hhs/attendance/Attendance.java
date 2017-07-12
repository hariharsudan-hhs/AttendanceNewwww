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
import android.support.v4.app.Fragment;
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


public class Attendance extends Fragment {

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Attendance";
    public SeekBar seekBar;
    int atthour=1;
    String attdate;
    EditText editText2;

    public static String select="";
    public static boolean init=false;
    ArrayList<String> clscontent=new ArrayList<>();
    ArrayList<String> stucontent=new ArrayList<>();
    ArrayList<String> subcontent=new ArrayList<>();
    ArrayList<DataObject> addlist=new ArrayList<>();
    ArrayList<String>attlist;
    int stusize;
    String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.attendance, container, false);
        TextView save = (TextView) view.findViewById(R.id.save);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter2(new ArrayList<DataObject>(),0);
//        mRecyclerView.setAdapter(mAdapter);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");

        save.setTypeface(face);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Save Attendance");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to save your changes ?");

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        attlist=new ArrayList<String>(MyRecyclerViewAdapter2.ret_list());
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


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        LayoutInflater li = LayoutInflater.from(getContext());
        System.out.println("LI is "+li);
        View promptsView = li.inflate(R.layout.att_select,null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner) promptsView.findViewById(R.id.spinner2);
        TextView textView4 = (TextView) promptsView.findViewById(R.id.textView4);
        TextView textView5 = (TextView) promptsView.findViewById(R.id.textView5);
        TextView textView6 = (TextView) promptsView.findViewById(R.id.textView6);
        ArrayAdapter<String>obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,clscontent);

        ArrayAdapter<String>obj2=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,subcontent);

        attdate=new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        editText2 = (EditText) promptsView.findViewById(R.id.editText2);
        editText2.setTypeface(face);
        final TextView textView7 = (TextView) promptsView.findViewById(R.id.textView7);
        textView7.setTypeface(face);
        textView4.setTypeface(face);
        textView5.setTypeface(face);
        textView6.setTypeface(face);
        editText2.setText(attdate);
        FloatingActionButton fab2 = (FloatingActionButton) promptsView.findViewById(R.id.fab2);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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


        seekBar=(SeekBar)promptsView.findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(9);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView7.setText("Hour : "+(progress+1));
                atthour=progress+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View promptsView2 = li.inflate(R.layout.max_select, null);

                                final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                                        getActivity());

                                // set prompts.xml to alertdialog builder
                                alertDialogBuilder2.setView(promptsView2);

                                RadioGroup radioGroup2 = (RadioGroup)  promptsView2.findViewById(R.id.radiogroup2);
                                RadioButton rb1 = (RadioButton) promptsView2.findViewById(R.id.rb1);
                                RadioButton rb2 = (RadioButton) promptsView2.findViewById(R.id.rb2);
                                RadioButton rb3 = (RadioButton) promptsView2.findViewById(R.id.rb3);
                                RadioButton rb4 = (RadioButton) promptsView2.findViewById(R.id.rb4);

                                rb1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        select = "Present";

                                    }
                                });

                                rb2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        select = "Absent";
                                    }
                                });

                                rb3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        select = "On Duty";
                                    }
                                });

                                rb4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        select = "Leave";
                                    }
                                });

                                alertDialogBuilder2
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        mAdapter = new MyRecyclerViewAdapter2(addlist,stusize);
                                                        mRecyclerView.setAdapter(mAdapter);
                                                        dialog.dismiss();
                                                    }
                                                    });


                                // create alert dialog
                                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                                // show it
                                alertDialog2.show();


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
        getActivity().setTitle("Attendance");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter2) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter2
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

            attdate=String.valueOf(mFormat.format(Double.valueOf(dayOfMonth))) + "-" + String.valueOf(mFormat.format(Double.valueOf(month+1)))
                    + "-" + String.valueOf(mFormat.format(Double.valueOf(year)));
        }
    };
}