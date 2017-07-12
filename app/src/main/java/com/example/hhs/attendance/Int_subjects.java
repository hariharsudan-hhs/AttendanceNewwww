package com.example.hhs.attendance;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hhs on 28/2/17.
 */
public class Int_subjects implements Serializable{
    public ArrayList<String> subjects;
    public Int_subjects(ArrayList<String> subjects){
        this.subjects=subjects;
    }
}
