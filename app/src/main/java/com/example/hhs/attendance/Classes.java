package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Classes extends Fragment
{

    Firebase fb_db1,fb_db2;
    String BASE_URL = "https://attendance-79ba4.firebaseio.com/";
    private RecyclerView mRecyclerView;
    String cls="";
    public static String cur_class="";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Classes";
    ArrayList<String> clscontent=new ArrayList<>();
    ArrayList<DataObject> addlist=new ArrayList<>();
    String CID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.classes,container,false);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(addlist);
        mRecyclerView.setAdapter(mAdapter);
        Firebase.setAndroidContext(getContext());
        new MyTask1().execute();
        final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.class_name_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                TextView textView1 = (TextView) promptsView.findViewById(R.id.textView1);
                textView1.setTypeface(face);
                userInput.setTypeface(face);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                         cls= userInput.getText().toString();
                                        System.out.println("CLASS IS "+cls);
                                        addlist.add(new DataObject(cls));
                                        System.out.println("LIST OF CLS IS "+addlist);
                                        new MyTask().execute();

                                        mAdapter = new MyRecyclerViewAdapter(addlist);
                                        mRecyclerView.setAdapter(mAdapter);
//                                        DataObject obj = new DataObject(cls);
//                                        ((MyRecyclerViewAdapter) mAdapter).addItem(obj, 0);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        dialog.cancel();

                                    }
                                });



                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });



        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Classes");
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position+addlist.get(position).getmText1());
                cur_class=addlist.get(position).getmText1();
                Fragment fragment= new Students();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();
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

            System.out.println("LUNA ");
            ClassAdapter classAdapter = new ClassAdapter();

            classAdapter.setClas(cls);
            System.out.println("CID "+CID);
            fb_db1.child(CID).child(cls).setValue(classAdapter);
            return "SUCCESS";


        }

    }



    private class MyTask1 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {


            String URL = BASE_URL+CID+"/";
            System.out.println("BASE URL IS "+URL);
            fb_db2=new Firebase(URL);

            fb_db2.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    System.out.println("INVO ");
                    addlist.clear();
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {

                        System.out.println("fuck is "+postSnapshot.getKey());
                        addlist.add(new DataObject(postSnapshot.getKey()));
                        mAdapter = new MyRecyclerViewAdapter(addlist);
                        mRecyclerView.setAdapter(mAdapter);
                    }



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