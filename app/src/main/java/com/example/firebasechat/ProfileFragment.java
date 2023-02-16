package com.example.firebasechat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends BottomSheetDialogFragment {

    ImageView i;
    TextView t,e;
    Button b;
    FirebaseAuth mAuth;
    FirebaseUser us;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth= FirebaseAuth.getInstance();
        us= FirebaseAuth.getInstance().getCurrentUser();
        i= view.findViewById(R.id.circleImageView2);
        t=view.findViewById(R.id.n);
        e= view.findViewById(R.id.e);
        b= view.findViewById(R.id.button2);

        Glide.with(getContext())
                .load(us.getPhotoUrl().toString())
                .override(50,50)
                .into(i);
        t.setText(us.getDisplayName());
        e.setText(us.getEmail());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                GoogleSignInClient googleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("24291275337-14p3mdr3d2782j3tj11egr0dld4205o0.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                googleSignInClient= GoogleSignIn.getClient(getContext(),gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent i=new Intent(getContext(),SplashActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
            }
        });
        return view;
    }
}