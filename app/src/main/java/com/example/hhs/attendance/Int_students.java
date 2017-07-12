package com.example.hhs.attendance;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hhs on 28/2/17.
 */
public class Int_students implements Serializable{
    public ArrayList<String> classes;
    public Int_students(ArrayList<String> classes){
        this.classes=classes;
    }
}
