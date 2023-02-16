package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SearchView sr;
    RecyclerView rv;
    UserAdapter us;
    ArrayList<User> filter;
    DatabaseReference db;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        sr= findViewById(R.id.searchview);
        rv= findViewById(R.id.filter);
        filter= new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        sr.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("text",s);
                searchUsers(s);

                return false;
            }
        });
    }
    private void searchUsers(String s)
    {
        db= FirebaseDatabase.getInstance().getReference("Users");
        Query query=db.orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filter.clear();
                for(DataSnapshot s: snapshot.getChildren())
                {
                    User u= s.getValue(User.class);
                    filter.add(u);

                }
                us= new UserAdapter(SearchActivity.this,filter,false);
                rv.setAdapter(us);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}