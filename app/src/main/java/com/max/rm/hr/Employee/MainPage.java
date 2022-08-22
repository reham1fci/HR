package com.max.rm.hr.Employee;

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
import androidx.core.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.max.rm.hr.Admin.adMainPage;
import com.max.rm.hr.Admin.reminderList;
import com.max.rm.hr.ChatPkg.allChats;
import com.max.rm.hr.ChatPkg.chat;
import com.max.rm.hr.Employee.attendancePkg.MyAttendance;
import com.max.rm.hr.Employee.infoPkg.MyInformation;
import com.max.rm.hr.Employee.payrollPkg.MyPayroll;
import com.max.rm.hr.Employee.requestpkg.requestsTypes;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;

import java.util.Locale;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager manger;
    TextView emp_name, jobDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(getSharedPreferences("user",0).getBoolean("admin",true)){
        navigationView.getMenu().findItem(R.id.admin).setVisible(true);
          }
        View headerView = navigationView.getHeaderView(0);
        emp_name=(TextView)headerView. findViewById(R.id.user_name);
        jobDegree=(TextView)headerView. findViewById(R.id.jobDegree);
        //jobDegree.setText(getIntent().getExtras().getString("jobDegree"));
        String empName= getSharedPreferences("user",0).getString("name","");
        emp_name.setText(empName);
        manger = getSupportFragmentManager();
        FragmentTransaction transaction=manger.beginTransaction();
        Fragment f=new NotificationStatsics();
        transaction.replace(R.id.frame,f);
        transaction.commit();
        Bundle b= getIntent().getExtras();
        if(b!=null&&b.containsKey(keys.CLASS_NAME)){
         String  class_name=   b.getString(keys.CLASS_NAME);
         if(class_name.equals("notification")){
             Fragment fchat=new chat();
       Bundle bundle= new Bundle();
       bundle.putString("sender_empCode",b.getString("sender_empCode"));// function name
       bundle.putString("chat_title",b.getString("chat_title"));// function name
       bundle.putString("sender_empName",b.getString("sender_empName"));// function name
       bundle.putString("chat_key",b.getString("chat_key"));// function name
       bundle.putString("type",b.getString("type"));// function name
       fchat.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().replace(R.id.frame,fchat,"chat").addToBackStack("chat").commit();


         }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction transaction =manger.beginTransaction();
        int id = item.getItemId();


        if (id == R.id.notification) {
            Fragment f=new NotificationStatsics();
            transaction.replace(R.id.frame,f,"notification");
            transaction.addToBackStack("notification") ;

            transaction.commit();
            // Handle the camera action
        } else if (id == R.id.requests) {
            Fragment f=new requestsTypes();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,false);
            bundle.putBoolean(keys.addButton,true);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"requestsTypes").addToBackStack("requestsTypes").commit();

        } else if (id == R.id.myInfo) {
            Fragment f=new MyInformation();
            transaction.replace(R.id.frame,f,"MyInformation");
            transaction.addToBackStack("MyInformation") ;
            transaction.commit();

        } else if (id == R.id.myPayroll) {
            Fragment f=new MyPayroll();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,false);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"MyPayroll");
            transaction.addToBackStack("MyPayroll") ;
            transaction.commit();

        } else if (id == R.id.attendance) {
            Fragment f=new MyAttendance();
            Bundle bundle= new Bundle();
            bundle.putString("fun_name","Get_Emp_MonthlyAttend");
            bundle.putString("fun_name_daily","Get_Emp_DailyAttend");

            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"MyAttendance");
            transaction.addToBackStack("MyAttendance") ;
            transaction.commit();

        } else if (id == R.id.message) {
            Fragment f=new allChats();
            transaction.replace(R.id.frame,f,"allChats");
            transaction.addToBackStack("allChats") ;

            transaction.commit();

        }
        else if (id == R.id.complain) {
            Fragment f=new Complaints_Suggestions();
            transaction.replace(R.id.frame,f,"complain");
            transaction.addToBackStack("complain") ;

            transaction.commit();

        }
        else if (id == R.id.logout) {
            SharedPreferences file= getSharedPreferences("user",0);
            SharedPreferences.Editor shared= file.edit();
            String register_key=file.getString("register_key", "null");
            shared.clear().commit();
            shared.putString("register_key",register_key).commit();
            Intent i= new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            finish();

        }
        else if (id==R.id.tasks){
            Fragment f=new reminderList();
            Bundle bundle= new Bundle();
            bundle.putBoolean(keys.allEmployee,false);
            bundle.putString(keys.CLASS_NAME,"Get_Avilable_Reminders");// function name
            bundle.putBoolean(keys.addButton,false);
            f.setArguments(bundle);
            transaction.replace(R.id.frame,f,"reminderList").addToBackStack("reminderList").commit();

        }
        else if (id == R.id.admin) {
            Intent i= new Intent(getApplicationContext(),adMainPage.class);
            SharedPreferences.Editor shared=getSharedPreferences("user",0).edit();
            shared.putString("account", "admin");
            shared.commit();
            startActivity(i);
            finish();}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
