package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

/**
 * Created by Belal on 18/09/16.
 */


public class Stats extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ArrayList<DataObject>present;
    public static ArrayList<DataObject>absent;
    public static ArrayList<DataObject>onduty;
    public static ArrayList<DataObject>leave;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.stats, container, false);
        setcontent();
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Present(), "Present");
        adapter.addFragment(new Absent(), "Absent");
        adapter.addFragment(new OnDuty(), "OnDuty");
        adapter.addFragment(new Leave(), "Leave");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Stats");
    }
    public void setcontent()
    {
        present=new ArrayList<>();
        absent=new ArrayList<>();
        onduty=new ArrayList<>();
        leave=new ArrayList<>();
        for(int i=0;i<PastAttendance.allatt.size();i++)
        {
            switch(PastAttendance.allatt.get(i))
            {
                case "Present":
                    present.add(new DataObject(PastAttendance.allstu.get(i)));
                    break;
                case "Absent":
                    absent.add(new DataObject(PastAttendance.allstu.get(i)));
                    break;
                case "On Duty":
                    onduty.add(new DataObject(PastAttendance.allstu.get(i)));
                    break;
                case "Leave":
                    leave.add(new DataObject(PastAttendance.allstu.get(i)));
                    break;

            }
        }
    }

}