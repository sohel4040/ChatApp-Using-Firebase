package com.example.firebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Thread t= new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(2000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
//                    Log.d("exp","Hiiiiiiiiiiiiiii");

                }
                finally
                {
                    if(firebaseUser != null)
                    {
                        Intent intent= new Intent(SplashActivity.this,MainActivity2.class);
                        intent.putExtra("Name",firebaseUser.getDisplayName());
                        intent.putExtra("emailid",firebaseUser.getEmail());
                        intent.putExtra("imageurl",firebaseUser.getPhotoUrl());
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent= new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
        t.start();

    }
}