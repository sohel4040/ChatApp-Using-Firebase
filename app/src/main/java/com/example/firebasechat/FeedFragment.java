package com.example.firebasechat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class FeedFragment extends Fragment {

    RecyclerView p;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    ArrayList<String> ids;
    FirebaseUser u;
    FirebaseFirestore db;
    SwipeRefreshLayout sf;
    FloatingActionButton fb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feed, container, false);
        u= FirebaseAuth.getInstance().getCurrentUser();
        db= FirebaseFirestore.getInstance();
        fb= view.findViewById(R.id.floatingActionButton2);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragment ad= new AddFragment();
                ad.show(getParentFragmentManager(),ad.getTag());
            }
        });
        p=view.findViewById(R.id.postpage);
        posts= new ArrayList<>();
        ids= new ArrayList<>();
        getPosts();
        sf= view.findViewById(R.id.swipe);
        sf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
                sf.setRefreshing(false);
            }
        });
        return view;
    }

    private void getPosts() {
        db.collection("Posts")
                .orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        posts.clear();
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Post p= doc.toObject(Post.class);
//                            Log.d("nasa",p.getText());
//                            String t=p.getTime();
//                            Long s=System.currentTimeMillis();
//                            Long s1=Long.parseLong(t);
//                            Long r=s-s1;
//                            Long minutes = (r/ 1000) / 60;
//                            Log.d("nasa",minutes.toString()+" minutes ago");
                            if(p != null)
                            {
                                posts.add(p);
                                Log.d("nasa",p.getText());
                                ids.add(doc.getId());
                            }
                        }
                        Collections.reverse(posts);
                        postAdapter= new PostAdapter(getContext(),posts,ids);

                        p.setLayoutManager(new MyLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        p.setAdapter(postAdapter);
                    }


                });

//        db.collection("Posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Log.d("nasa", document.getId() + " => " + document.getData());
//                                Post p= document.toObject(Post.class);
//                                String t=p.getTime();
//                                Long s=System.currentTimeMillis();
//                                Long s1=Long.parseLong(t);
//                                Long r=s-s1;
//                                Long minutes = (r/ 1000) / 60;
////                                Log.d("nasa",minutes.toString()+" minutes ago");
//
//                                posts.add(p);
//                                Log.d("nasa",p.getText());
//
//                            }
//                            Log.d("nasa",posts.toString());
//                            Log.d("isro",posts.size()+"");
//                            postAdapter= new PostAdapter(getContext(),posts);
//                            p.setLayoutManager(new LinearLayoutManager(getContext()));
//                            p.setAdapter(postAdapter);
//                        } else {
//                            Log.d("nasa", "Error getting documents: ", task.getException());
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("nasa", "Error getting documents: ");
//            }
//        });


    }
}