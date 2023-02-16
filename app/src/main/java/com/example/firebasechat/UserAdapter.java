package com.example.firebasechat;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> users;
    Context context;
    FirebaseUser fuser;
    boolean s;
    String lastMessage;
    public UserAdapter(Context context,ArrayList<User> users,boolean s) {
        this.users= users;
        this.context= context;
        this.s=s;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        User user= users.get(position);
        Log.d("user",user.getName());
        Log.d("user",user.getImageurl());
        holder.txt.setText(user.getName());
        Glide.with(context)
                .load(user.getImageurl())
                .override(40, 40)
                .into(holder.img);
        holder.cv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    Intent intent= new Intent(context,MessageActivity.class);
                    intent.putExtra("name",user.getName());
                    intent.putExtra("imageurl",user.getImageurl());
                    intent.putExtra("ouid",user.getId());
                    context.startActivity(intent);
             }
         }
        );
        if(s)
        {
            lastMessage(user.getId(),holder.lastm);
        }
        else
        {
            holder.lastm.setVisibility(View.GONE);
        }
        if(s)
        {
            holder.s.setVisibility(View.VISIBLE);
            if(user.getStatus().equals("online"))
            {
                Glide.with(context).load("https://i.ytimg.com/vi/-QCoV4U1H1w/maxresdefault.jpg").override(9,9).into(holder.s);
            }
            else
            {
                Glide.with(context).load("https://www.macmillandictionary.com/us/external/slideshow/full/Grey_full.png").override(9,9).into(holder.s);
            }
        }
        else
        {
            holder.s.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView txt;
        public ConstraintLayout cv;
        public ImageView s;
        public TextView lastm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv= (ConstraintLayout) itemView.findViewById(R.id.constraint);
            txt =(TextView)itemView.findViewById(R.id.textView2);
            lastm =(TextView)itemView.findViewById(R.id.lastmessage);
            img=itemView.findViewById(R.id.imagec);
            s= itemView.findViewById(R.id.status);
        }
    }

    private void lastMessage(String uid,TextView txt)
    {
        lastMessage="";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d: snapshot.getChildren())
                {
                    Chat chat= d.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid)
                    || chat.getReceiver().equals(uid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        lastMessage=chat.getMessage();
                    }
                }
                txt.setText(lastMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
