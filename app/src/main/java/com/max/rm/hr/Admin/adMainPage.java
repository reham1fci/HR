package com.max.rm.hr.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import androidx.core.view.GravityCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.max.rm.hr.ChatPkg.allChats;
import com.max.rm.hr.Employee.MainPage;
import com.max.rm.hr.Employee.attendancePkg.MyAttendance;
import com.max.rm.hr.Employee.payrollPkg.MyPayroll;
import com.max.rm.hr.Employee.requestpkg.requestsTypes;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;
import com.max.rm.hr.locationLoginMainActivity;

import java.util.Locale;

public class adMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager manger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView  emp_name=(TextView)headerView. findViewById(R.id.user_name);
        //jobDegree.setText(getIntent().getExtras().getString("jobDegree"));
        String empName= getSharedPreferences("user",0).getString("name","");
        emp_name.setText(empName);
         manger = getSupportFragmentManager();
        FragmentTransaction transaction=manger.beginTransaction();
        Fragment f=new notification();
        transaction.replace(R.id.frame,f);
        transaction.commit();
    }

    @Override

        public void onBackPressed() {
            FragmentManager fm = getSupportFragmentManager();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            else if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                super.onBackPressed();
            }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.arabic) {
            changeLanguage("ar");
            return true;
        }
        else if(id == R.id.english){
            changeLanguage("en");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        finishAffinity();

        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        startActivity(i);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction transaction =manger.beginTransaction();

        int id = item.getItemId();

        if (id == R.id.notification) {
            // Handle the camera action
            Fragment f=new notification();
            transaction.replace(R.id.frame,f,"notification");
            transaction.addToBackStack("notification") ;
            transaction.commit();
        }
        else if (id == R.id.message) {
            Fragment f=new allChats();
            transaction.replace(R.id.frame,f,"allChats");
            transaction.addToBackStack("allChats") ;
            transaction.commit();

        }
        else if (id == R.id.requests) {
            Fragment f=new requestsTypes();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,true);
            bundle.putBoolean(keys.addButton,false);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"requestsTypes").addToBackStack("requestsTypes").commit();

        }
        else if (id == R.id.tasks) {

            Fragment f=new reminderList();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,true);
            bundle.putString(keys.CLASS_NAME,"Get_Reminders");// function name
            bundle.putBoolean(keys.addButton,true);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"reminderList").addToBackStack("reminderList").commit();

        }
        else if (id == R.id.attendance) {
            Fragment f=new MyAttendance();
            Bundle bundle= new Bundle();
            bundle.putString("fun_name","Get_All_MonthlyAttend");
            bundle.putString("fun_name_daily","Get_All_DailyAttend");
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"MyAttendance").addToBackStack("MyAttendance").commit();


        } else if (id == R.id.allEmployee) {
             allChats.flag=0;
             employeesList.type="";
            Fragment f=new employeesList();
            transaction.replace(R.id.frame,f,"employeesList");
            transaction.addToBackStack("employeesList") ;

            transaction.commit();
        }
        else if (id == R.id.myAccount) {

           Intent i = new Intent(getApplicationContext(), MainPage.class);
           SharedPreferences shared = getSharedPreferences("user",0);
           SharedPreferences.Editor editor = shared.edit();
           String admin_code= shared.getString("admin_emp_code","");
           int admin_job_degree= shared.getInt("admin_job_degree",0);
           editor.putString("emp_code",admin_code);
            editor.putString("account", "emp");

            editor.putInt("jobDegree",admin_job_degree);
            editor.commit();
            startActivity(i);
            finish();
        }
        else if (id == R.id.payroll) {
            Fragment f=new MyPayroll();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,true);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"MyPayroll");
            transaction.addToBackStack("MyPayroll") ;
            transaction.commit();
        }
         else if ( id == R.id.location){
            Intent i = new Intent(getApplicationContext(), locationLoginMainActivity.class);
              startActivity(i );
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
