package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

import static android.content.Context.MODE_PRIVATE;
import static com.example.hhs.attendance.Classes.cur_class;


public class PastAttendance extends Fragment {

    String node;
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


    AlertDialog.Builder alertDialogBuilder;
    String CID,uname;
    Firebase fb_db1,fb_db2;
    String BASE_URL = "https://attendance-79ba4.firebaseio.com/";

    ArrayList<String> hrlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");
        uname = pref.getString("uname","");

        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.past_attendance, container, false);
        tclass = (TextView) view.findViewById(R.id.textView10);
        thour = (TextView) view.findViewById(R.id.textView11);
        tdate = (TextView) view.findViewById(R.id.textView12);
        tsubject = (TextView) view.findViewById(R.id.textView13);

        Firebase.setAndroidContext(getActivity());
        //new  MyTask().execute();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new MyRecyclerViewAdapter3(new ArrayList<DataObject>(),attcontent);
//        stats = (FloatingActionButton) view.findViewById(R.id.stats);
//        stats.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment= new Stats();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.content_frame, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
//                transaction.addToBackStack(null);  // this will manage backstack
//                transaction.commit();
//            }
//        });
//        mRecyclerView.setAdapter(mAdapter);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");
        tclass.setTypeface(face);
        thour.setTypeface(face);
        tdate.setTypeface(face);
        tsubject.setTypeface(face);

        //date=new SimpleDateFormat("dd-MM-yyyy").format(new Date());

         alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getContext());
        System.out.println("LI is "+li);
        View promptsView = li.inflate(R.layout.past_att_select,null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

      spinner = (Spinner) promptsView.findViewById(R.id.spinner3);


//        ArrayAdapter<String> obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,hourcontent);
//
//        spinner.setAdapter(obj);
        editText2 = (EditText) promptsView.findViewById(R.id.editText2);
        final TextView textView7 = (TextView) promptsView.findViewById(R.id.textView7);
        //editText2.setText(date);
        FloatingActionButton fab2 = (FloatingActionButton) promptsView.findViewById(R.id.fab2);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour=spinner.getSelectedItem().toString();
                //new MyTask2().execute();

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
                            public void onClick(DialogInterface dialog,int id)
                            {
                                System.out.println("BOWW BOWW IS"+hour);
                                System.out.println("BOWWW IS "+spinner.getSelectedItem());
                                new MyTask2().execute();

                                tclass.setText("Class : "+attclass);
                                tsubject.setText("Subject : "+attsubject);
                                thour.setText("Hour : "+hour);
                                tdate.setText("Date : "+date);
//                                mAdapter = new MyRecyclerViewAdapter3(addlist,attcontent);
//                                mRecyclerView.setAdapter(mAdapter);
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
//        ((MyRecyclerViewAdapter3) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter3
//                .MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.i(LOG_TAG, " Clicked on Item " + position);
//
//            }
//        });

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





            new MyTask().execute();
            ArrayAdapter<String> obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,hourcontent);
            spinner.setAdapter(obj);

        }
    };


    private class MyTask extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = BASE_URL+"Attendance/"+CID+"/";
            System.out.println("BASE URL IS "+URL);
            fb_db2=new Firebase(URL);

            fb_db2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    hrlist.clear();
                    System.out.println("INVOKER ");
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {

                        System.out.println("Date and user is "+uname+" and "+date);
                        System.out.println("ATTENDANCE IS "+postSnapshot.getKey());
                        String S = postSnapshot.getKey();
                        if(S.contains(uname)&&S.contains(date))
                        {
                            String [] lol = S.split("_");
                            System.out.println("CLASS IS "+lol[4]);
                            hrlist.add(lol[4]);
                        }

                    }
                    ArrayAdapter<String> obj=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,hrlist);

                    spinner.setAdapter(obj);

                    System.out.println("%%%%"+hour);
                    System.out.println("%%%%"+date);




                }

                @Override
                public void onCancelled(FirebaseError firebaseError)
                {
                    System.out.println("FIREBASE ERROR OCCURED");

                }

            });


            return "SUCCESS";
        }

    }


    private class MyTask2 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = BASE_URL+"Attendance/"+CID+"/";
            System.out.println("BASE URL IS "+URL);
            fb_db2=new Firebase(URL);

            fb_db2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    System.out.println("INVOKER 2");
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        String S1 = postSnapshot.getKey();
                        if(S1.contains(hour)&&S1.contains(uname)&&S1.contains(date))
                        {
                            String[] f=S1.split("_");
                            attclass = f[1];
                            attsubject = f[2];
                            node = uname+"_"+attclass+"_"+attsubject+"_"+date+"_"+hour+"/";

                            System.out.println("node is "+node);
                        }
                        tclass.setText("Class : "+attclass);
                        tsubject.setText("Subject : "+attsubject);
                        thour.setText("Hour : "+hour);
                        tdate.setText("Date : "+date);

                    }
                    new MyTask3().execute();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError)
                {
                    System.out.println("FIREBASE ERROR OCCURED");

                }

            });


            return "SUCCESS";
        }

    }


    private class MyTask3 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = BASE_URL+"Attendance/"+CID+"/";
            System.out.println("BASE URL IS "+URL);
            fb_db2=new Firebase(URL);

            fb_db2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    System.out.println("INVOKER &%&%");
                    addlist.clear();
                    //System.out.println("DATA SNAP"+dataSnapshot.getChildren());
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        System.out.println("DATA SNAP"+dataSnapshot.getChildren());
                        System.out.println("jhgkhjgjh");
                        AttendanceAdapter adapter = postSnapshot.getValue(AttendanceAdapter.class);
                        System.out.println("NOOBIE "+adapter.StudAttendance+"   "+adapter.StudList);
                        attcontent = adapter.StudAttendance;
                        stucontent = adapter.StudList;
                        //addlist.add(new DataObject());
                        System.out.println("NOWW PRITNIGN "+stucontent+" and "+ attcontent);


                    }

                    mAdapter = new MyRecyclerViewAdapter3(stucontent,attcontent);
                    mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError)
                {
                    System.out.println("FIREBASE ERROR OCCURED");

                }

            });


            return "SUCCESS";
        }

    }




}