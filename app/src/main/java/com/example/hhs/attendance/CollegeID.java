package com.example.hhs.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CollegeID extends AppCompatActivity
{
    Firebase fb_db;
    String BASE_URL = "https://attendance-79ba4.firebaseio.com/CollegeID/";
    EditText colgid;
    Button submit;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String stats = pref.getString("islogin","");
        System.out.println("Stats is "+stats);
        if(stats.equals("yes"))
        {
            Intent i = new Intent(CollegeID.this,Home.class);
            startActivity(i);
        }
        else
        {

            setContentView(R.layout.activity_college_id);
            Firebase.setAndroidContext(this);
            colgid = (EditText)findViewById(R.id.colgid);
            submit = (Button)findViewById(R.id.colidsubmit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ID = colgid.getText().toString();
                    new MyTask().execute();


                }
            });
        }


    }
    public class MyTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            fb_db=new Firebase(BASE_URL);
            fb_db.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        System.out.println("fuck is "+postSnapshot.getKey());
                        ColgIDAdapter colgIDAdapter = postSnapshot.getValue(ColgIDAdapter.class);
                        String S =colgIDAdapter.getID();
                        String name = colgIDAdapter.getName();
                        System.out.println("LOL 1 IS "+S+"  "+name);
                        System.out.println("retrieved ID is "+S);
                        if(ID.equals(S))
                        {
                            System.out.println("LOL 2 IS "+S+"  "+name);
                            editor.putString("CID",S);
                            editor.putString("Cname",name);
                            editor.commit();
                            Intent i = new Intent(CollegeID.this,StartingActivity.class);
                            startActivity(i);
                        }

                    }



                }

                @Override
                public void onCancelled(FirebaseError firebaseError)
                {
                    System.out.println("FIREBASE ERROR OCCURED");

                }

            });
            return null;
        }

    }
}
