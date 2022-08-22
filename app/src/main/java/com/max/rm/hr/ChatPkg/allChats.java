 package com.max.rm.hr.ChatPkg;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import  androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.rm.hr.Admin.employeesList;
import com.max.rm.hr.R;
import com.max.rm.hr.keys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

 public class allChats extends Fragment implements AdapterView.OnItemClickListener{
    ListView list;
    ProgressBar progress;
    ArrayList<String>chatNames;
    ArrayList<chatItemClass>chats;
    ArrayList<String>chats_ids;
    ArrayList<String>emp_ids;
    FloatingActionButton addChat;
    FragmentManager manger;
    FragmentTransaction transaction;
    chatsAdapter chatsAdapter;
    LinearLayout fabLayout,oneChat, groupChat;
    private boolean fabExpanded = false;
String name= "null";
    static FirebaseDatabase firebase_instance= FirebaseDatabase.getInstance();
    public  static DatabaseReference sRef=firebase_instance.getReference("chat");
   public static int flag=0;
    SharedPreferences shared;
    String myEmpCode, chat_key;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.all_chats,null);
        getActivity().setTitle(getString(R.string.messages));
        list=( ListView)v.findViewById(R.id.list) ;
        addChat=(FloatingActionButton)v.findViewById(R.id.addBtn);
        progress=(ProgressBar)v.findViewById(R.id.progress);
        fabLayout=(LinearLayout) v.findViewById(R.id.fabLayout);
        oneChat=(LinearLayout) v.findViewById(R.id.oneChat);
        groupChat=(LinearLayout) v.findViewById(R.id.groupChat);
        flag=0;
        oneChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new employeesList();
                Bundle b= new Bundle();
                b.putString(keys.CLASS_NAME,"chat");
                f.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"employeesList").addToBackStack("employeesList").commit();
               // transaction.addToBackStack("employeesList") ;
              //  transaction.commit();
            }
        });
        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f=new employeesList();
                Bundle b= new Bundle();
                b.putString(keys.CLASS_NAME,"group");
                f.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"employeesList").addToBackStack("employeesList").commit();
            }
        });
        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddChat(view);
            }
        });
        chatNames= new ArrayList<>();
        chats_ids= new ArrayList<>();
        emp_ids= new ArrayList<>();
        chats= new ArrayList<>();
        manger= getActivity().getSupportFragmentManager();
        transaction= manger.beginTransaction();
        chatsAdapter= new chatsAdapter(getActivity(),chats);
     //   adapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, chatNames);
        list.setAdapter(chatsAdapter);
        closeSubMenusFab();
        shared= getActivity().getSharedPreferences("user",0);
        if (shared.getBoolean("admin", false)){
            myEmpCode= shared.getString("admin_emp_code","");
        }
        else {
        myEmpCode= shared.getString("emp_code","");
        }

        getChatData();
        list.setOnItemClickListener(this);
        return v;
    }
  /*  public  void   check_user(){
        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren().iterator().next().getValue();
                 progress.setVisibility(View.GONE);
                for ( DataSnapshot dsp : dataSnapshot.getChildren()) {
                    ArrayList<String>keys= new ArrayList<>();
                    ArrayList<String>names= new ArrayList<>();
                    for (DataSnapshot dsp2 : dsp.child("member_child").getChildren()) {
                        String key=   dsp2.getKey();
                        Employees emp= dsp2.getValue(Employees.class);
                        names.add(emp.getEmp_name());
                        keys.add(key);
                    }
                   Log.d("keys", keys.toString());
                    Log.d("sender", myEmpCode);
                  //  Log.d("reciver", reciver_empCode);

                    if(keys.contains(myEmpCode)) {

                        int indx = keys.indexOf(myEmpCode);
                        keys.remove(myEmpCode);
                        names.remove(indx);
                        chatNames.addAll(names);
                        adapter.notifyDataSetChanged();
                        chat_key = dsp.getKey();
                        chats_ids.add(chat_key);
                        emp_ids.addAll(keys);
                        getLastMessage(chat_key, chatNames.size() - 1);
                        Log.d("chats", chats_ids.toString());

                    }
                }
               }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Fragment f=new chat();
        Bundle bundle= new Bundle();
        SharedPreferences shared=  getActivity().getSharedPreferences("user",0);
        bundle.putString("sender_empCode",myEmpCode);// function name
        bundle.putString("receiver_empCode",chats.get(i).getEmp_id());// function name
        bundle.putString("chat_title",chats.get(i).getName());// function name
        bundle.putString("sender_empName",shared.getString("name",""));// function name
        bundle.putString("chat_key",chats.get(i).getChat_id());// function name
        bundle.putString("type",chats.get(i).getType());// function name
        f.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"chat").addToBackStack("chat").commit();
    }
// add chat
    public void onClickAddChat(View view) {
        if(shared.getBoolean("admin", false)){
            if (fabExpanded){
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        }
        else{
        Fragment f=new employeesList();
        Bundle b= new Bundle();
        b.putString(keys.CLASS_NAME,"chat");
        f.setArguments(b);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f,"employeesList").commit();
       // transaction.addToBackStack("employeesList") ;
    }
    }
    public void getLastMessage(final String  chat_key, final int position){
        sRef.child(chat_key).child("messages").orderByChild("date").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String time="" , messagetTxt="";
                Log.d("data",dataSnapshot.toString());
               if(dataSnapshot.getValue()!=null){
                for ( DataSnapshot dsp : dataSnapshot.getChildren()) {

                          // error her
                    Message message = dsp.getValue(Message.class);
                    String message_id = message.getMessage_id();
                     time = message.getTime();
                     messagetTxt = message.getmTxt();


                }
                chats.add( new chatItemClass(chatNames.get(position),time,messagetTxt,"",chat_key, emp_ids.get(position)));
                chatsAdapter.notifyDataSetChanged();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void closeSubMenusFab(){
        fabLayout.setVisibility(View.INVISIBLE);
        list.setAlpha(1);

        addChat.setImageResource(R.drawable.chat);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        fabLayout.setVisibility(View.VISIBLE);
        fabLayout.bringToFront();
        list.setAlpha(0.5f);
        //Change settings icon to 'X' icon
        addChat.setImageResource(R.drawable.ic_close_black_24dp);

        fabExpanded = true;
    }
    public  void getChatData(){
        sRef.orderByChild("lastTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getChildren().iterator().next().getValue();
                progress.setVisibility(View.GONE);
                for ( DataSnapshot dsp : dataSnapshot.getChildren()) {
                    ArrayList<String>names= new ArrayList<>();
                    chatItemClass chat= new chatItemClass();
                    int count=0;

                    for (DataSnapshot dsp2 : dsp.child("member_child").getChildren()) {
                        String key=   dsp2.getKey();
                        Employees emp= dsp2.getValue(Employees.class);
                        names.add(emp.getEmp_name());
                        if(key.equals(myEmpCode)){
                            Log.d("key",key);
                            count=emp.getCountNotSeenMessages();
                            chatInfo info =dsp.child("info").getValue(chatInfo.class);
                           String chat_id=dsp.getKey();
                           String chat_type=info.getType();
                             name="";
                            String time="";
                            String message_txt="";
                            String description;
                            String created_by;
                            String created_at;
                           if(chat_type.equals("group")){
                               name=info.getName();
                               description=info.getDescription();
                               created_by=info.createdBy;
                               created_at=info.createdAt;
                           }
                           else {
                               if(names.size()==2){
                                   name=names.get(0);
                               }
                                description="";
                                created_at="";
                                created_by="";
                           }
                          //  Message message = new Message();

                            for (DataSnapshot messagedsp : dsp.child("messages").getChildren()) {
                                Message messageDetails = messagedsp.getValue(Message.class);
                               message_txt=messageDetails.getmTxt();
                                time=messageDetails.getTime();
                            }
                            chat= new chatItemClass(chat_type,name,description,created_by,created_at,time,
                                    message_txt,"",chat_id,"emp_id");
                            chat.setCountItem(String.valueOf(count));


                        }

                    }
                    if(!name.equals("null")){
                            if(!chat.getLastMessage().isEmpty()||chat.getType().equals("group")){
                    if (name.equals("")){
                        chat.setName(names.get(1));
                    }
                    chats.add(chat);
                    Collections.reverse(chats);
                    Log.d("chat",chat.toString());
                    chatsAdapter.notifyDataSetChanged();
                    }}
                    name="null";
// after every chat
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}

