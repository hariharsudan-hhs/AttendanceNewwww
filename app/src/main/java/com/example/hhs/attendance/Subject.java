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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class Subject extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Subjects";
    ArrayList<String> subcontent=new ArrayList<>();
    ArrayList<DataObject> addlist=new ArrayList<>();
    Firebase fb_db1,fb_db2;
    String BASE_URL = "https://attendance-79ba4.firebaseio.com/";
    String CID,Username,Subname,Scode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.subjects, container, false);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");
        Username = pref.getString("uname","");

        Firebase.setAndroidContext(getContext());
        new MyTask1().execute();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerViewAdapterStud(addlist);
        mRecyclerView.setAdapter(mAdapter);

        final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.sub_name_prompt, null);

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
                alertDialogBuilder.setPositiveButton("OK",null);
                alertDialogBuilder.setNegativeButton("Cancel",null);
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialogInterface) {
                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Subname = userInput.getText().toString();
                                        Scode = userInput2.getText().toString();
                                        if(Subname.equals("")||Scode.equals(""))
                                        {
                                            Toast.makeText(getActivity(),"Please enter the Subject Details",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            addlist.add(new DataObject(Subname));
                                            new MyTask().execute();
                                            mAdapter=new MyRecyclerViewAdapterStud(addlist);
                                            mRecyclerView.setAdapter(mAdapter);
                                            alertDialog.dismiss();
                                        }



                            }
                        });
                    }
                });










//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id)
//                                    {
//
//                                        Subname = userInput.getText().toString();
//                                        Scode = userInput2.getText().toString();
//                                        if(Subname.equals("")||Scode.equals(""))
//                                        {
//                                            Toast.makeText(getActivity(),"Please enter the Subject Details",Toast.LENGTH_SHORT).show();
//                                        }
//                                        else
//                                        {
//                                            addlist.add(new DataObject(Subname));
//                                            new MyTask().execute();
//                                            mAdapter=new MyRecyclerViewAdapterStud(addlist);
//                                            mRecyclerView.setAdapter(mAdapter);
//                                        }
//
//
//
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();

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
        getActivity().setTitle("Subjects");
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

    private class MyTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {

            fb_db1=new Firebase(BASE_URL);
            System.out.println("CLASS URL IS "+BASE_URL);

            System.out.println("LUNA ");
            SubjAdapter subjAdapter = new SubjAdapter(Subname,Scode);
            fb_db1.child("ColgSubj").child(CID).child(Username).child(Scode).setValue(subjAdapter);
            return "SUCCESS";


        }

    }
    private class MyTask1 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = "https://attendance-79ba4.firebaseio.com/ColgSubj/"+CID+"/"+Username+"/";

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

                        SubjAdapter subjAdapter;
                        System.out.println("fuck is "+postSnapshot.getKey());
                        if(!(postSnapshot.getKey().equals("clas")))
                        {
                            subjAdapter  = postSnapshot.getValue(SubjAdapter.class);
                            System.out.println("ASDFASDF"+subjAdapter.Subname);
                            System.out.println("ASDFASDF"+subjAdapter.Subcode);
                            String subcde=subjAdapter.Subcode;
                            String subnme = subjAdapter.Subname;
                            addlist.add(new DataObject(subnme,subcde));
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