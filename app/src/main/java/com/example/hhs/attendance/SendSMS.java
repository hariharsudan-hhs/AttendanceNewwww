package com.example.hhs.attendance;

/**
 * Created by hhs on 28/2/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Belal on 18/09/16.
 */


public class SendSMS extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        return inflater.inflate(R.layout.sendsms, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        SharedPreferences pref2 = getActivity().getSharedPreferences("MyPref3", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref2.edit();

        editor.putBoolean("isFirstTime",false);
        editor.commit();
        getActivity().setTitle("Logout");
        getActivity().finish();
        startActivity(new Intent(getActivity(),StartingActivity.class));

    }
}