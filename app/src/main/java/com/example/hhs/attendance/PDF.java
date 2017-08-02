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
    Firebase fb_db;
    String Base_Url;
    String CID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.generate_pdf, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        CID = pref.getString("CID","");
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
//                Document document = new Document();
//                String fname = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
//                String path = Environment.getExternalStorageDirectory()+"/Attendance/fname.xlsx";
//                try {
//
//
//                    PdfWriter.getInstance(document,new FileOutputStream(path));
//                    document.open();
//                    document.add(new Paragraph("subash"));
//                    document.close();
//                    System.out.println("BOWW 2");
//
//                } catch (Exception e)
//                {
//                    System.out.println("EXCEPTION is "+e);
//                }


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
                    System.out.println("LIST IS "+Classlist);
                    System.out.println("LIST IS "+Subjlist);
                    System.out.println("LIST IS "+Datelist);
                    System.out.println("LIST IS "+Hourlist);


                    CreatePDF();


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
    public void CreatePDF()
    {
        Document document = new Document();
        String fname = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        String path = Environment.getExternalStorageDirectory()+"/Attendance/"+fname+".pdf";
        try {

            System.out.println("LIST IS "+Subjlist.toString());

            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            document.add(new Paragraph(Subjlist.toString()));
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell = new PdfPCell(new Phrase(fname));
            cell.setColspan(3);
            table.addCell(cell);

            // we add the four remaining cells with addCell()
//                        table.addCell("row 1; cell 1");
//                        table.addCell("row 1; cell 2");
//                        table.addCell("row 2; cell 1");
//                        table.addCell("row 2; cell 2");
            for(int i=0;i<Classlist.size();i++)
            {
                table.addCell(Classlist.get(i));
                table.addCell(Subjlist.get(i));
                System.out.println("Adding "+Classlist.get(i));
            }
            document.add(table);
            document.close();
            System.out.println("BOWW 2");

        } catch (Exception e)
        {
            System.out.println("EXCEPTION is "+e);
        }

    }
}