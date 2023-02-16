package com.example.firebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth mAuth;

    private FirebaseUser firebaseUser;

    Intent in;

BottomNavigationView bmv;
    ChatFragment audioFragment;
    UserFragment videoFragment;
//    AddFragment addFragment;
    FeedFragment feedFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bmv= findViewById(R.id.bmv);
        bmv.setOnNavigationItemSelectedListener(this);
        audioFragment= new ChatFragment();
        videoFragment= new UserFragment();
//        addFragment= new AddFragment();
        feedFragment= new FeedFragment();
        bmv.setSelectedItemId(R.id.audios);
        in= getIntent();
        mAuth= FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                Log.d("token",task.getResult());
                if(firebaseUser!= null) {
                    FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).child("token").setValue(task.getResult()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(getApplicationContext(), "Token Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.audios:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,audioFragment).addToBackStack(null).commit();

                return true;
            case R.id.videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,videoFragment).addToBackStack(null).commit();
                return true;
            case R.id.feeds:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,feedFragment).addToBackStack(null).commit();
                return true;
            case R.id.search:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container,addFragment).commit();
//                AddFragment add= new AddFragment();
//                add.show(getSupportFragmentManager(),add.getTag());
                Intent in= new Intent(MainActivity2.this,SearchActivity.class);
                Log.d("Intent","Intent");
                startActivity(in);
                return true;
            case R.id.logout:
                showProfile();
//                mAuth.signOut();
//                GoogleSignInClient googleSignInClient;
//                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken("24291275337-14p3mdr3d2782j3tj11egr0dld4205o0.apps.googleusercontent.com")
//                        .requestEmail()
//                        .build();
//                googleSignInClient= GoogleSignIn.getClient(MainActivity2.this,gso);
//                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        Intent i=new Intent(MainActivity2.this,SplashActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                });
        }
        return false;
    }

    private void showProfile()
    {

        ProfileFragment p= new ProfileFragment();
        p.show(getSupportFragmentManager(),p.getTag());

    }

    private void status(String message)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> result = new HashMap<>();
        result.put("status",message);
        databaseReference.updateChildren(result);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}