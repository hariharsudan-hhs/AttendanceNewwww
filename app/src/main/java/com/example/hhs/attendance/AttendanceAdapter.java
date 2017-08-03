package com.example.hhs.attendance;

import java.util.ArrayList;

/**
 * Created by msuba on 16-07-2017.
 */

public class AttendanceAdapter
{
    public AttendanceAdapter(){}

    public AttendanceAdapter(ArrayList Studlist,ArrayList StudAttendance)
    {
        this.StudList=Studlist;
        this.StudAttendance =StudAttendance;
    }


    public ArrayList<String> StudList;
    public ArrayList<String> StudAttendance;


}
