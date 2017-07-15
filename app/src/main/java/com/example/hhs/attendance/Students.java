package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hhs.attendance.Classes.cur_class;


public class Students extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Classes";
    ArrayList<String> clscontent=new ArrayList<>();
    ArrayList<DataObject> addlist=new ArrayList<>();
    ArrayList<DataObject> addlist2=new ArrayList<>();
    Firebase fb_db1,fb_db2;
    String BASE_URL = "https://attendance-79ba4.firebaseio.com/";
    String CID;
    String Sname;
    String SID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.students,container,false);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerViewAdapterStud(addlist);
        mRecyclerView.setAdapter(mAdapter);
        Firebase.setAndroidContext(getContext());
        new MyTask1().execute();
        final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.stud_name_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                TextView textView1 = (TextView) promptsView.findViewById(R.id.textView1);

                final EditText userInput2 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput2);
                TextView textView2 = (TextView) promptsView.findViewById(R.id.textView2);
                textView2.setTypeface(face);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        Sname = userInput.getText().toString();
                                        SID = userInput2.getText().toString();
                                        System.out.println("STUD ID IS "+SID);
                                        System.out.println("STUD NAME IS "+Sname);
                                        addlist.add(new DataObject(Sname));
                                        new MyTask().execute();
                                        mAdapter=new MyRecyclerViewAdapterStud(addlist);
                                        mRecyclerView.setAdapter(mAdapter);


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


            }
        });

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Students");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapterStud) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapterStud
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);

            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Class " + index);
            results.add(index, obj);
        }
        return results;
    }



    private class MyTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            String BASE_URL1 = BASE_URL+CID+"/"+cur_class+"/";
            fb_db1=new Firebase(BASE_URL1);
            System.out.println("CLASS URL IS "+BASE_URL1);

            System.out.println("LUNA ");
            StudAdapter studAdapter = new StudAdapter(SID,Sname);
            fb_db1.child(SID).setValue(studAdapter);
            return "SUCCESS";


        }

    }

    private class MyTask1 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = BASE_URL+CID+"/"+cur_class+"/";
            System.out.println("BASE URL IS "+URL);
            fb_db2=new Firebase(URL);

            fb_db2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    System.out.println("INVOKER ");
                    addlist.clear();
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {

                        StudAdapter studAdapter;
                        System.out.println("fuck is "+postSnapshot.getKey());
                        if(!(postSnapshot.getKey().equals("clas")))
                        {
                            studAdapter  = postSnapshot.getValue(StudAdapter.class);
                            System.out.println("ASDFASDF"+studAdapter.SID);
                            System.out.println("ASDFASDF"+studAdapter.Sname);
                            String studid=studAdapter.SID;
                            String studname = studAdapter.Sname;
                            addlist.add(new DataObject(studid,studname));
                            //addlist2.add(new DataObject(studname));


                        }
                    }

                    mAdapter = new MyRecyclerViewAdapterStud(addlist);
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