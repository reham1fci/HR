package com.androidmax.max.hr.ChatPkg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidmax.max.hr.Admin.employeesList;
import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.Employee.employee_class;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.dialog_interface;
import com.androidmax.max.hr.keys;
import com.androidmax.max.hr.normalWindow;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class chat extends Fragment implements View.OnClickListener {
    public static final String USER_EXTRA = "USER";
    public static final String TAG = "ChatActivity";
    static FirebaseDatabase firebase_instance = FirebaseDatabase.getInstance();
    public static DatabaseReference sRef = firebase_instance.getReference("chat");
    ListView list;
    ImageButton save;
    EditText message;
    boolean flag = false;
    ProgressBar progress;
    Query q;
    String chat_type = "";
    ArrayList<employee_class> receivers;
    Bundle b;
    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String reciver_name = "", sender_name, sender_empCode, reciver_empCode, chat_key;
    private Date mLastMessageDate = new Date();
    private String mConvoId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat, null);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        list = (ListView) v.findViewById(R.id.list);
        save = (ImageButton) v.findViewById(R.id.save);
        message = (EditText) v.findViewById(R.id.messsage);
        save.setOnClickListener(this);
        b = getArguments();
        sender_empCode = b.getString("sender_empCode");
        sender_name = b.getString("sender_empName");
        receivers = (ArrayList<employee_class>) b.getSerializable("receivers");
        chat_key = b.getString("chat_key");
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages, getActivity());
        list.setAdapter(mAdapter);
        setHasOptionsMenu(true);
        getCallerFragment();
//      String name=  getCallerFragment();
//
//        Log.d("fragment chat ",  name);
// if(name.equals("employeesList")){
//     Fragment fragment = new employeesList();
//
//     getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//
//     //onBackPressed();
// }
//        String name2=  getCallerFragment();
 //Log.d("fragment after delete  ",  name);
        if (chat_key.equals("null")) {
            check_user();
            chat_type = "chat";
            getActivity().setTitle(b.getString("chat_title"));

        } else if (chat_key.equals("group")) {
            chat_key = sRef.push().getKey();
            chat_type = "group";
            Log.d("create new chat ", " call from if firstcreateNewChat function");

            createNewChat(chat_key);
        } else
            { // come from all chat click chat item list
            getActivity().setTitle(b.getString("chat_title"));
            String type = b.getString("type");
            if (type.equals("group")) {
                chat_type = "group";
            }
            else
                {
                chat_type = "chat";
                setHasOptionsMenu(true);
            }
            getAllMessages(chat_key);
         }
//
        allChats.flag=1;
        return v;
    }

    // send message
    public void onClick(View v) {
        String newMessage = message.getText().toString();
        if (newMessage.isEmpty()) {

        } else {
            message.setText("");
            addNewMessage(chat_key, newMessage);

        }

    }


    public void check_user() {
        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataSnapshot.getChildren().iterator().next().getValue();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    ArrayList<String> keys = new ArrayList<>();

                    for (DataSnapshot dsp2 : dsp.child("member_child").getChildren()) {
                        String key = dsp2.getKey();
                        keys.add(key);
                    }
                    Log.d("receiverkolha", receivers.toString());

                    Log.d("receiver", receivers.get(0).getEMP_CODE());
                    if (keys.size() == 2) {

                        if (keys.contains(sender_empCode) && keys.contains(receivers.get(0).getEMP_CODE())) {
                            chat_key = dsp.getKey();
                            getAllMessages(chat_key);
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {

                    chat_key = sRef.push().getKey();
                    createNewChat(chat_key);
                    Log.d("create new chat ", " call from check user  firstcreateNewChat function");


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllMessages(final String chat_key) {
        resetCount(chat_key);
       // save_register_key();
        sRef.child(chat_key).child("member_child").child(sender_empCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Employees emp = dataSnapshot.getValue(Employees.class);
                String start_at = emp.getStart_at();
                String end_at = emp.getEnd_at();
                Log.d("start", start_at);
                Log.d("end", end_at);
                if (start_at.equals("null") && !end_at.equals("null")) {
                    message.setText("you leave group can't send or Receive messages  ");
                    message.setFocusable(false);
                    message.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    message.setClickable(false);
                    save.setClickable(false);
                } else if (start_at.equals("null")) {

                } else {
                    if (end_at.equals("null")) {
                        q = sRef.child(chat_key).child("messages").orderByChild("time").startAt(start_at);

                    } else {

                        q = sRef.child(chat_key).child("messages").orderByChild("time").startAt(start_at).endAt(end_at);
                        message.setText("you leave group can't send or Receive messages  ");
                        message.setFocusable(false);
                        message.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                        message.setClickable(false);
                        save.setClickable(false);
                    }
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            progress.setVisibility(View.GONE);

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                Message message = dsp.getValue(Message.class);
                                String message_id = message.getMessage_id();
                                if (searchMessage(message_id)) {

                                } else {
                                    mMessages.add(message);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                            Log.d("messages", mMessages.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNewMessage(String chat_key, String messageTxt) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String Date = df.format(new Date());
        String message_id = sRef.push().getKey();
        Long tsLong = System.currentTimeMillis() / 1000;
        String time = tsLong.toString();
        Message m = new Message(messageTxt, sender_empCode, Date, time, message_id, sender_name);
        getAllUserChat(time,messageTxt);
        sRef.child(chat_key).child("messages").child(message_id).setValue(m);
        sRef.child(chat_key).child("lastTime").setValue(time);
        Log.d("chat_key", chat_key.toString());
    }

    public void createNewChat(String chat_key) {
        Log.d("create new chat ", "createNewChat function");
        progress.setVisibility(View.GONE);
        Employees emp2 = new Employees(sender_empCode, sender_name, "null", "null", "null",0);
        HashMap<String, Employees> mapper = new HashMap<String, Employees>();
        for (int i = 0; i < receivers.size(); i++) {
            Employees emp = new Employees(receivers.get(i).getEMP_CODE(), receivers.get(i).getNameAr(), "testkey2", "null", "null",0);
            mapper.put(receivers.get(i).getEMP_CODE(), emp);
            reciver_name = reciver_name + receivers.get(i).getNameAr() + ",";
        }
        reciver_name = reciver_name.substring(0, reciver_name.length() - 2);
        getActivity().setTitle(reciver_name);
        mapper.put(sender_empCode, emp2);
        sRef.child(chat_key).child("member_child").setValue(mapper);
        String gName, gDesc;
        if (chat_type.equals("group")) {
            gName = b.getString("gName");
            gDesc = b.getString("gDesc");
        } else {
            gName = "null";
            gDesc = "null";
        }
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.ENGLISH);
        String currentDate = df.format(new Date());
        chatInfo info = new chatInfo(chat_type, gName, gDesc, sender_empCode, currentDate);
        sRef.child(chat_key).child("info").setValue(info);
        getAllMessages(chat_key);
       // getActivity().getSupportFragmentManager().popBackStack();

    }

    public boolean searchMessage(String message_id) {
        for (int i = 0; i < mMessages.size(); i++) {
            String mID = mMessages.get(i).getMessage_id();
            if (message_id.equals(mID)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.group_details, menu);
        MenuItem item = menu.findItem(R.id.english);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.arabic);
        item2.setVisible(false);
        if (chat_type.equals("chat")) {
            menu.findItem(R.id.groupDetails).setVisible(false);
            menu.findItem(R.id.leave).setVisible(false);
            menu.findItem(R.id.add_member).setVisible(false);
        }
        if (getActivity().getSharedPreferences("user", 0).getBoolean("admin", false) && chat_type.equals("group")) {
            menu.findItem(R.id.add_member).setVisible(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.groupDetails) {
            detailsGroup();
        }
        if (id == R.id.delete) {
            delete_conversation();
        }

        if (id == R.id.add_member) {
            getGroupMembers();
        }
        if (id == R.id.leave) {
            leave_group();
        }
        return super.onOptionsItemSelected(item);

    }

    public void detailsGroup() {
        Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.group_details);
        final TextView group_name = (TextView) d.findViewById(R.id.name);
        final TextView members = (TextView) d.findViewById(R.id.members);
        final TextView description = (TextView) d.findViewById(R.id.description);
        Log.d("chat", chat_key);
        sRef.child(chat_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String membersNames = "";
                chatInfo info = dataSnapshot.child("info").getValue(chatInfo.class);
                Log.d("info", info.toString());

                description.setText(info.getDescription());
                group_name.setText(info.getName());
                for (DataSnapshot member : dataSnapshot.child("member_child").getChildren()) {
                    String member_name = member.getValue(Employees.class).getEmp_name();
                    Log.d("member", dataSnapshot.toString());

                    membersNames = membersNames + member_name + "\n";

                }
                members.setText(membersNames);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        d.show();

    }

    public void delete_conversation() {
        normalWindow.dialogOkCancel(getString(R.string.delete) + "?", new dialog_interface() {
            @Override
            public void onDialogOkClick(AlertDialog alertDialog) {

                sRef.child(chat_key).child("member_child").child(sender_empCode).child("start_at").setValue("null");
                //    sRef.child(chat_key).child("member_child").child(sender_empCode).child("end_at").setValue("null");
                mMessages.clear();
                mAdapter.notifyDataSetChanged();
                getActivity().getSupportFragmentManager().popBackStack();


            }

            @Override
            public void onDialogCancelClick(AlertDialog alertDialog) {

            }
        }, getActivity());
    }

    public void getAllUserChat(final String startMessageTime, final String messageTxt) {
        sRef.child(chat_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String register_keys= "";
                for (DataSnapshot member : dataSnapshot.child("member_child").getChildren()) {
                    String start_at = member.getValue(Employees.class).getStart_at();
                    String end_at = member.getValue(Employees.class).getEnd_at();
                    String emp_register_key=member.getValue(Employees.class).getRegister_key();

                    if(!emp_register_key.equals("null")){
                    register_keys=register_keys+emp_register_key+",";
                    }
                    int count_not_seen = member.getValue(Employees.class)  .getCountNotSeenMessages();

                    if(end_at.equals("null")&&!member.getKey().equals(sender_empCode)) {
                        sRef.child(chat_key).child("member_child").child(member.getKey()).child("countNotSeenMessages").setValue(count_not_seen + 1);
                    }
                    if (start_at.equals("null")) {
                        String key = member.getKey();
                        sRef.child(chat_key).child("member_child").child(key).child("start_at").setValue(startMessageTime);
                    }


                }
                if(register_keys.length()>0){

                sendNotification(chat_key, chat_type, sender_name,register_keys, messageTxt);}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getCallerFragment(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int count = getFragmentManager().getBackStackEntryCount();
        Log.d("count stack", String.valueOf(count));
        for (int i=0; i <count; i++){
            Log.d("f"+i,fm.getBackStackEntryAt(i).getName() );
        }

      //  Fragment f=
      /*    if(fm.getBackStackEntryAt(count - 2).getName().equals("employeesList")){
              Fragment fragment = new employeesList();
              fm.beginTransaction().remove(fragment).commit();

          }*/


    }
    public void leave_group() {
        normalWindow.dialogOkCancel(getString(R.string.leave_group) + "?", new dialog_interface() {
            @Override
            public void onDialogOkClick(AlertDialog alertDialog) {
                String timeLastMessage = "00000";
                if (mMessages.size() > 0) {
                    timeLastMessage = mMessages.get(mMessages.size() - 1).getTime();
                }

                sRef.child(chat_key).child("member_child").child(sender_empCode).child("end_at").setValue(timeLastMessage);
                message.setText("you leave group can't send or Receive messages  ");
                message.setFocusable(false);
                message.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                message.setClickable(false);
                save.setClickable(false);
            }

            @Override
            public void onDialogCancelClick(AlertDialog alertDialog) {

            }
        }, getActivity());
    }

    public void getGroupMembers() {
        sRef.child(chat_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Employees> memberes = new ArrayList<>();
                for (DataSnapshot member : dataSnapshot.child("member_child").getChildren()) {
                    Employees emp = member.getValue(Employees.class);
                    memberes.add(emp);
                }

                addNewMember(memberes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNewMember(ArrayList<Employees> emps) {
        Fragment f = new employeesList();
        Bundle b = new Bundle();
        b.putString(keys.CLASS_NAME, "add_member");
        b.putSerializable("members", emps);
        b.putString("chat_key", chat_key);
        f.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, f, "employeesList").commit();
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
     public void resetCount(String chat_key ){
        sRef.child(chat_key).child("member_child").child(sender_empCode).child("countNotSeenMessages").setValue(0);
     }
     public  void save_register_key(){
         sRef.child(chat_key).child("member_child").child(sender_empCode).child("register_key").addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             String register_key= dataSnapshot.getValue(String.class);
             SharedPreferences shared= getActivity().getSharedPreferences("user",0);
           String shared_register_key=  shared.getString("register_key","");
             if(register_key.equals("null")||!register_key.equals(shared_register_key)){
                 sRef.child(chat_key).child("member_child").child(sender_empCode).child("register_key").setValue(shared_register_key);

             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

     }
     public void sendNotification(String chat_key,String chat_type,String chat_title,String register_key, String message){
         Api api= new Api(getActivity());
         api.sendNotification(chat_key, chat_type, chat_title, sender_empCode, sender_name, message, register_key, new RequestInterface() {
             @Override
             public void onResponse(String response) {
               //  Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onError() {

             }
         });
     }


}



