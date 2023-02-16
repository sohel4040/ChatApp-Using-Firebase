package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    EditText message;
    Token userToken;
    String uToken;
//    ="dKmdEOXZTd6cFMa24Hea7o:APA91bFJW9tONna2akLEJCYN1q75_wqR59nGkqroV80VxcDspl70061VcFDxwtlqXYnFMmFT_OEmz_IHIWzJB4BdNb3RsMECGPM-Sosnv4vR799oX8HMlIBWA34ewV1B71OatLZwtYVv";
    RecyclerView r;
    TextView name;
    EditText ed;
    ImageView img,send;
    String n,url,ouid;
    ChatAdapter chatAdapter;
    ArrayList<Chat> chats;
    FirebaseUser u;
    ValueEventListener val;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();
        ed= findViewById(R.id.message);
        send= findViewById(R.id.send);
        r= findViewById(R.id.recyclerview);
        name=findViewById(R.id.name);
        img=findViewById(R.id.mi);
        Intent i= getIntent();
        n=i.getStringExtra("name");
        url=i.getStringExtra("imageurl");
        ouid=i.getStringExtra("ouid");
        r.setHasFixedSize(true);
        LinearLayoutManager l= new LinearLayoutManager(this);
        l.setStackFromEnd(true);
        r.setLayoutManager(l);
        chats= new ArrayList<>();

        u= FirebaseAuth.getInstance().getCurrentUser();
        name.setText(n);
        Glide.with(this)
                .load(url)
                .override(40,40)
                .into(img);

        readMessages();
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseDatabase.getInstance().getReference("Tokens").child(ouid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userToken= task.getResult().getValue(Token.class);
                uToken =userToken.getToken();
                Log.d("token",uToken);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(ed.getText()))
                {
                    sendmessage(u.getUid(),ouid,ed.getText().toString());
                    ed.setText("");
                    FcmNotificationSender sender= new FcmNotificationSender(uToken,n,"New message from "+u.getDisplayName(),getApplicationContext(),MessageActivity.this);
                    sender.SendNotifications();

//                    FcmNotificationSender sender1= new FcmNotificationSender("/topics/all","Hii","Welcome to our App",getApplicationContext(),MessageActivity.this);
//                    sender1.SendNotifications();
                }
            }
        });
    seenMessage(ouid);

    }


    private void seenMessage(String uid)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        val=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d: snapshot.getChildren())
                {
                    Chat chat= d.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid))
                    {
                        HashMap<String,Object> h= new HashMap<String,Object>();
                        h.put("seen",true);
                        d.getRef().updateChildren(h);
                    }

                }
                chatAdapter= new ChatAdapter(MessageActivity.this,chats);
                r.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmessage(String sender,String receiver,String message)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        Chat chat = new Chat(sender,receiver,message,false);
        databaseReference.push().setValue(chat);

    }
    private void readMessages() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot d: snapshot.getChildren())
                {
                    Chat chat= d.getValue(Chat.class);
                    if(chat.getSender().equals(ouid) && chat.getReceiver().equals(firebaseUser.getUid())
                    || chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(ouid))
                    {
                        chats.add(chat);
                    }

                }
                chatAdapter= new ChatAdapter(getApplicationContext(),chats);
                r.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void status(String message)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(u.getUid());
        HashMap<String, Object> result = new HashMap<>();
        result.put("status",message);
        databaseReference.updateChildren(result);
    }
    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        databaseReference.removeEventListener(val);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}