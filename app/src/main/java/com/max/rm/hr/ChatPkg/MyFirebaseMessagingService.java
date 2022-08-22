package com.max.rm.hr.ChatPkg;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.max.rm.hr.Employee.MainPage;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager manger;
    NotificationCompat.Builder builder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data= remoteMessage.getData();
        builder= new NotificationCompat.Builder(this);
        String message=data.get("message");
        String chat_key=data.get("chat_key");
        String chat_type=data.get("chat_type");
        String chat_title=data.get("chat_title");
        String sender_code=data.get("sender_code");
        String sender_name=data.get("sender_name");
        sendNotification(chat_key,chat_type,chat_title,sender_code,sender_name,message);

    }
   public void sendNotification(final String chat_key, final String chat_type, final String chat_title, final String sender_emp_code, final String sender_emp_name, final String message_txt) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setColor(getResources().getColor(R.color.white));
                    builder.setSmallIcon(R.mipmap.ic_launcher);


                } else {
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                }                 builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setContentText(message_txt);
                builder.setContentTitle(sender_emp_name);
Intent bundle = new Intent(this, MainPage.class);
       bundle.putExtra(keys.CLASS_NAME,"notification");
       bundle.putExtra("sender_empCode",sender_emp_code);// function name
       bundle.putExtra("chat_title",chat_title);// function name
       bundle.putExtra("sender_empName",sender_emp_name);// function name
       bundle.putExtra("chat_key",chat_key);// function name
       bundle.putExtra("type",chat_type);
//       Fragment f=new chat();
//       Bundle bundle= new Bundle();
//       SharedPreferences shared= getSharedPreferences("user",0);
//       bundle.putString("sender_empCode",sender_emp_name);// function name
//       bundle.putString("chat_title",chat_title);// function name
//       bundle.putString("sender_empName",sender_emp_name);// function name
//       bundle.putString("chat_key",chat_key);// function name
//       bundle.putString("type",chat_type);// function name
//       f.setArguments(bundle);
    // getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"chat").addToBackStack("chat").commit();


       int count= getSharedPreferences("user",0).getInt("count",0);
                PendingIntent pendingIntent=PendingIntent.getActivity(MyFirebaseMessagingService.this,count,bundle, PendingIntent.FLAG_UPDATE_CURRENT );
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                manger = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setChannelId("com.androidmax.max.hr");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            "com.androidmax.max.hr",
                            "HRChat",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
                    if (manger != null) {
                        manger.createNotificationChannel(channel);
                    }}
               manger.notify(count,builder.build());
               count=count+1;
                SharedPreferences.Editor sharedEditor= getSharedPreferences("user",MODE_PRIVATE).edit();
                sharedEditor.putInt("count",count).commit();

            }


    }

