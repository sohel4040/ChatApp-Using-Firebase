package com.example.firebasechat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    ArrayList<User> users;
    RecyclerView rv;

    UserAdapter userAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        rv= view.findViewById(R.id.rv1);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        users= new ArrayList<User>();
        readUsers();
        return view;
    }

    private void readUsers() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for(DataSnapshot d: snapshot.getChildren())
                    {
                        User user= d.getValue(User.class);
                        assert user != null;
                        assert firebaseUser != null;
                        if(!user.getId().equals(firebaseUser.getUid()))
                        {
                            Log.d("database",user.getName());
                            Log.d("database",user.getImageurl());
                            users.add(user);
                        }

                    }
                    userAdapter= new UserAdapter(getContext(),users,false);
                    rv.setAdapter(userAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

}
