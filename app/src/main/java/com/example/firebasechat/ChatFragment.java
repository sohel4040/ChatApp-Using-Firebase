package com.example.firebasechat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class ChatFragment extends Fragment {

    ArrayList<User> users;
    UserAdapter userAdapter;
    ArrayList<String> userList;
    RecyclerView rv;
    ImageView profile;
    FirebaseUser auth;
    DatabaseReference databaseReference;
    ArrayList<String> newl;
    ArrayList<User> newu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        rv= view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        profile= view.findViewById(R.id.civ);
        Glide.with(getContext())
                .load(auth.getPhotoUrl()
                .toString())
                .override(40,40)
                .into(profile);
        userList= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot d:snapshot.getChildren())
                {
                    Chat c= d.getValue(Chat.class);

                    if(c.getSender().equals(auth.getUid()))
                    {
                        userList.add(c.getReceiver());
                    }
                    if(c.getReceiver().equals(auth.getUid()))
                    {
                        userList.add(c.getSender());
                    }
                }
                newl = removeDuplicates(userList);
                Log.d("lpg",newl.toString());
                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<T>();
        for(T element : list) {
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }
        return newList;
    }
    private void readChats() {
        users= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot s:snapshot.getChildren())
                {
                    User user = s.getValue(User.class);
                    for(String id:newl)
                    {
                        if(user.getId().equals(id))
                        {
                            if(users.size() != 0)
                            {
                                for(int i=0;i<users.size();i++)
                                {
                                    User  u=users.get(i);
                                    if(!user.getId().equals(u.getId()))
                                    {
                                        users.add(user);
                                    }
                                }
                            }
                            else
                            {
                                users.add(user);
                            }
                        }
                    }
                }
                newu = removeDuplicates(users);
                Log.d("lpg",users.toString());
                userAdapter= new UserAdapter(getContext(),newu,true);
                rv.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
