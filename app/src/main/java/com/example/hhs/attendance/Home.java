package com.example.hhs.attendance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-R.ttf");

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
//        TextView nav_classes = (TextView)drawer.findViewById(R.id.nav_classes);
//        TextView nav_attendance = (TextView)drawer.findViewById(R.id.nav_attendance);
//        TextView nav_subjects = (TextView)drawer.findViewById(R.id.nav_subject);
//        TextView nav_pastatt = (TextView)drawer.findViewById(R.id.nav_past);
//        TextView nav_send = (TextView)drawer.findViewById(R.id.nav_send);
//        TextView nav_pdf = (TextView)drawer.findViewById(R.id.nav_pdf);


        ImageView nav_image = (ImageView) hView.findViewById(R.id.imageView);
        if(StartingActivity.img=="male")
        {
            nav_user.setText("Male");
            nav_image.setImageResource(R.drawable.man);
        }
        else
        {
            nav_user.setText("Female");
            nav_image.setImageResource(R.drawable.woman);
        }

        nav_user.setText(StartingActivity2.name);
        nav_user.setTypeface(face);
//        nav_classes.setTypeface(face);
//        nav_attendance.setTypeface(face);
//        nav_pastatt.setTypeface(face);
//        nav_send.setTypeface(face);
//        nav_pdf.setTypeface(face);
//        nav_subjects.setTypeface(face);




        //add this line to display menu1 when the activity is loaded
       // displaySelectedScreen(R.id.nav_classes);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_classes) {
//            // Handle the camera action
//        } else if (id == R.id.nav_subject) {
//
//        } else if (id == R.id.nav_attendance) {
//
//        } else if (id == R.id.nav_past) {
//
//        } else if (id == R.id.nav_pdf) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_classes:
                fragment = new Classes();
                break;
            case R.id.nav_subject:
                fragment = new Subject();
                break;
            case R.id.nav_attendance:
                fragment = new Attendance();
                break;
            case R.id.nav_past:
                fragment = new PastAttendance();
                break;
            case R.id.nav_pdf:
                fragment = new PDF();
                break;
            case R.id.nav_send:
                fragment = new SendSMS();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
