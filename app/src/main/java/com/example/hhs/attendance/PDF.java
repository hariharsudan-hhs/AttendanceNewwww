package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Belal on 18/09/16.
 */


public class PDF extends Fragment
{
    Button gen;
    Spinner classspinner,subjspinner,datespinner,hourspinner;
    ArrayList<String> Classlist;
    ArrayList<String> Subjlist ;
    ArrayList<String> Datelist;
    ArrayList<String> Hourlist;
    Firebase fb_db,fb_db2;
    String Base_Url;
    String CID,uname;
    String c,s,d,h;
    ArrayList<String> class_content=new ArrayList<>();
    ArrayList<String> sub_content=new ArrayList<>();
    ArrayList<String> date_content=new ArrayList<>();
    ArrayList<String> hour_content=new ArrayList<>();
    ArrayList<String> attlist = new ArrayList<>();
    ArrayList<String> stulist = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.generate_pdf, container, false);

        classspinner = (Spinner) v.findViewById(R.id.classspinner);
        subjspinner = (Spinner) v.findViewById(R.id.subjspinner);
        datespinner = (Spinner) v.findViewById(R.id.datespinner);
        hourspinner = (Spinner) v.findViewById(R.id.hourspinner);


        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");
        uname = pref.getString("uname","");
        Firebase.setAndroidContext(getActivity());
        new MyTask().execute();
        getActivity().setTitle("Generate PDF");
        File dir = new File(Environment.getExternalStorageDirectory()+"/Attendance");
        try{
            if(dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            System.out.println("BOWW 1");
            e.printStackTrace();
        }


        gen = (Button)view.findViewById(R.id.genbut);

        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                c = classspinner.getSelectedItem().toString();
                s = subjspinner.getSelectedItem().toString();
                d = datespinner.getSelectedItem().toString();
                h = hourspinner.getSelectedItem().toString();

                System.out.println("LOLOLOL "+c+"_"+s+"_"+d+"_"+h);
                new MyTask2().execute();

            }
        });



    }
    public class MyTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
             Classlist = new ArrayList<>();
             Subjlist = new ArrayList<>();
             Datelist = new ArrayList<>();
             Hourlist = new ArrayList<>();


            Base_Url = "https://attendance-79ba4.firebaseio.com/Attendance/"+CID+"/";
            fb_db=new Firebase(Base_Url);
            System.out.println("BASE URL IS "+Base_Url);
            fb_db.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        System.out.println("KEYS ARE "+postSnapshot.getKey());
                        String S = postSnapshot.getKey();
                        String S1[]=S.split("_");
                        Classlist.add(S1[1]);
                        Subjlist.add(S1[2]);
                        Datelist.add(S1[3]);
                        Hourlist.add(S1[4]);
                    }

                    Set<String> hs = new HashSet<String>();
                    hs.addAll(Classlist);
                    Classlist.clear();
                    Classlist.addAll(hs);

                    Set<String> hs1 = new HashSet<String>();
                    hs1.addAll(Subjlist);
                    Subjlist.clear();
                    Subjlist.addAll(hs1);

                    Set<String> hs2 = new HashSet<String>();
                    hs2.addAll(Datelist);
                    Datelist.clear();
                    Datelist.addAll(hs2);

                    Set<String> hs3 = new HashSet<String>();
                    hs3.addAll(Hourlist);
                    Hourlist.clear();
                    Hourlist.addAll(hs3);

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Classlist);
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Subjlist);
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Datelist);
                    ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Hourlist);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    classspinner.setAdapter(dataAdapter);
                    subjspinner.setAdapter(dataAdapter2);
                    datespinner.setAdapter(dataAdapter3);
                    hourspinner.setAdapter(dataAdapter4);

                    System.out.println("LIST IS "+Classlist);
                    System.out.println("LIST IS "+Subjlist);
                    System.out.println("LIST IS "+Datelist);
                    System.out.println("LIST IS "+Hourlist);







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
//    public void CreatePDF()
//    {
//
//    }

    private class MyTask2 extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params) {



            String URL = "https://attendance-79ba4.firebaseio.com/Attendance/"+CID+"/"+uname+"_"+c+"_"+s+"_"+d+"_"+h+"/";
            System.out.println("BASE 2 URL IS $"+URL);
            fb_db2=new Firebase(URL);
            attlist = new ArrayList<>();
            stulist = new ArrayList<>();

//            uname+"_"+c+"_"+s+"_"+d+"_"+h+"/"

            fb_db2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                        System.out.println("keys are 1:" + postSnapshot.getKey());

                        String S = postSnapshot.getKey();
                        AttendanceAdapter attendanceAdapter = dataSnapshot.getValue(AttendanceAdapter.class);

                            System.out.println("LOL PRINT 2 " + attendanceAdapter.StudAttendance);
                            attlist = attendanceAdapter.StudAttendance;
                            stulist = attendanceAdapter.StudList;

                            System.out.println("LOL PRINT AAGUTHU");

                            System.out.println("STU LIST IS " + stulist);
                            System.out.println("STU LIST IS " + attlist);




                    }
                    System.out.println("LOL POST EXEC");
//            CreatePDF();
                    System.out.println("IN PDF CREATOR");
                    Document document = new Document();
                    String fname = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
                    String path = Environment.getExternalStorageDirectory()+"/Attendance/"+fname+"_"+s+"_"+h+".pdf";
                    try {

                        System.out.println("LIST IS "+Subjlist.toString());

                        PdfWriter.getInstance(document,new FileOutputStream(path));
                        document.open();
                        document.add(new Paragraph("Class   :   "+c));
                        document.add(new Paragraph("Subject   :   "+s));
                        document.add(new Paragraph("Date   :   "+d));
                        document.add(new Paragraph("Hour   :   "+h));
                        document.add(new Paragraph("\n"));

                        PdfPTable table = new PdfPTable(2);
                        PdfPCell cell = new PdfPCell(new Phrase(fname));
                        cell.setColspan(3);
                        table.addCell(cell);

                        // we add the four remaining cells with addCell()
//                        table.addCell("row 1; cell 1");
//                        table.addCell("row 1; cell 2");
//                        table.addCell("row 2; cell 1");
//                        table.addCell("row 2; cell 2");
                        System.out.println("PDF * "+stulist+"  "+attlist);
                        for(int i=0;i<stulist.size();i++)
                        {
                            table.addCell(stulist.get(i));
                            table.addCell(attlist.get(i));
                            System.out.println("Adding "+stulist.get(i));
                        }
                        document.add(table);
                        document.close();
                        System.out.println("BOWW 2");

                    } catch (Exception e)
                    {
                        System.out.println("EXCEPTION is "+e);
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
        @Override
        protected void onPostExecute(String result)
        {
            System.out.println("IN POST EXEC");

        }


            //    progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }






}