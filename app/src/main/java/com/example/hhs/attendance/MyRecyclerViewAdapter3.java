package com.example.hhs.attendance;

/**
 * Created by hhs on 27/2/17.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter3 extends RecyclerView.Adapter<MyRecyclerViewAdapter3
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static String[] checklist;
    public  static int stusize;
    public  static ArrayList<String>retlist;
    private static MyClickListener myClickListener;
    public ArrayList<String>attlist;
    public ArrayList<String>stulist;


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;
        RadioGroup radioGroup;
        RadioButton radioButton,radioButton2,radioButton3,radioButton4;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.rg);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            radioButton2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
            radioButton3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
            radioButton4 = (RadioButton) itemView.findViewById(R.id.radioButton4);


            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public MyRecyclerViewAdapter3(ArrayList<String> stulist, ArrayList<String> attlist) {
       // mDataset = attlist;
        this.attlist=new ArrayList<>(attlist);
        this.stulist=new ArrayList<>(stulist);
        System.out.println("INSIDE ADAPTER "+this.attlist+" and "+this.stulist);
        stusize=stulist.size();
        checklist=new String[stusize];
        for(int i=0;i<stusize;i++)
        {
            checklist[i]=attlist.get(i);
        }


    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row_stu, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        Typeface face= Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Ubuntu-R.ttf");
        holder.label.setText(stulist.get(position));
        holder.label.setTypeface(face);
        holder.itemView.setLongClickable(true);

        switch (attlist.get(position))
        {
            case "Present":
                holder.radioButton.toggle();
                break;
            case "Absent":
                holder.radioButton2.toggle();
                break;
            case "On Duty":
                holder.radioButton3.toggle();
                break;
            case "Leave":
                holder.radioButton4.toggle();
                break;
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklist[position]="Present";
                System.out.println("Present"+position);

            }
        });

        holder.radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklist[position]="Absent";
                System.out.println("Absent"+position);

            }
        });

        holder.radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklist[position]="On Duty";
                System.out.println("On Duty"+position);

            }
        });

        holder.radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklist[position]="Leave";
                System.out.println("Leave"+position);

            }
        });

    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return stusize;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
    public static ArrayList<String> ret_list()
    {
        retlist=new ArrayList<>();
        for(int i=0;i<stusize;i++)
        {
            retlist.add(checklist[i]);
        }

        return retlist;
    }
}