package com.example.hhs.attendance;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shyam on 2/3/17.
 */

public class Int_attend implements Serializable {
    public String class_name;
    public String subject;
    public ArrayList<String>stulist;
    public ArrayList<String>attlist;
    public Int_attend(String class_name,String subject,ArrayList<String>stulist,ArrayList<String>attlist)
    {
        this.class_name=class_name;
        this.subject=subject;
        this.stulist=stulist;
        this.attlist=attlist;
    }
}
