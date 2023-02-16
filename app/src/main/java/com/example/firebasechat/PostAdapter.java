package com.example.firebasechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> posts;
    FirebaseUser fu;
    FirebaseFirestore db;
    ArrayList<String> ids;
    DatabaseReference mDatabase;
    boolean liked=false;
    ArrayList<String> a;
    public PostAdapter(Context context, ArrayList<Post> posts,ArrayList<String> ids)
    {
        this.context=context;
        this.posts=posts;
        this.ids=ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post p= posts.get(position);
        fu= FirebaseAuth.getInstance().getCurrentUser();
        db= FirebaseFirestore.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(p.getUser()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    User a=task.getResult().getValue(User.class);
                    holder.user.setText(a.getName());
                }
            }
        });
        Glide.with(context).load(p.getProfileurl()).override(40,40).into(holder.profile);
        Glide.with(context).load(p.getImageurl()).into(holder.image);
        holder.desc.setText(p.getText());

//        holder.like_count.setText(p.likecount()+"");
        holder.like.setVisibility(View.GONE);
        holder.like_count.setVisibility(View.GONE);
        Log.d("isro",p.getImageurl()+" "+p.getTime()+p.getText()+" "+p.getUser());
        Utils utils= new Utils(System.currentTimeMillis(),Long.parseLong(p.getTime()));
        holder.time.setText(utils.getString());

        // getLikedBy
//        DocumentReference docRef = db.collection("Posts").document(ids.get(holder.getAdapterPosition()));
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists())
//                    {
//                        Map<String,Object> po=document.getData();
//                        Log.d("so",po.get("likedBy").toString());
//                        a=(ArrayList<String>) po.get("likedBy");
//                        Log.d("so",a.get(0));
//
//                        for (int i=0;i<a.size();i++)
//                        {
//                            String m=a.get(i);
//                            if(m.equals(fu.getUid()))
//                            {
//                                liked= true;
//                            }
//                        }
//                        Log.d("so",liked+"");
//                        if(liked)
//                        {
//                            holder.like.setImageResource(R.drawable.ic_baseline_favorite_24);
//                        }
//                        else
//                        {
//                            holder.like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                        }
//
//                    } else
//                    {
//                        Log.d("so", "No such document");
//                    }
//                } else {
//                    Log.d("so", "get failed with ", task.getException());
//                }
//            }
//        });

//
        // like button onClick
//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(liked)
//                {
//                    holder.like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
////                    ArrayList<String> a= p.getLikedBy();
//                    a.remove(fu.getUid());
//                    Log.d("so",a.toString());
//                    HashMap<String,Object> m= new HashMap<>();
//                    m.put("likedBy",a);
//                    db.collection("Posts").document(ids.get(holder.getAdapterPosition())).update(m).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(@NonNull Void unused) {
//                            Log.d("nasa","Updated Successfully");
//                        }
//                    });
//                }
//                else
//                {
//                    holder.like.setImageResource(R.drawable.ic_baseline_favorite_24);
////                    ArrayList<String> a= p.getLikedBy();
//                    Log.d("nasa",a.size()+"");
//                    a.add(fu.getUid());
//                    HashMap<String,Object> m= new HashMap<>();
//                    m.put("likedBy",a);
//                    db.collection("Posts").document(ids.get(holder.getAdapterPosition())).update(m).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(@NonNull Void unused) {
//                            Log.d("nasa","Updated Successfully");
//                        }
//                    });
//                }
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView user;
        public TextView desc;
        public TextView like_count;
        public ImageView profile;
        public ImageView image;
        public ImageView like;
        public CardView card;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user= itemView.findViewById(R.id.textView5);
            desc= itemView.findViewById(R.id.textView6);
            like_count= itemView.findViewById(R.id.textView7);
            image= itemView.findViewById(R.id.imageView2);
            like= itemView.findViewById(R.id.imageView3);
            profile= itemView.findViewById(R.id.circleImageView);
            card= itemView.findViewById(R.id.card);
            time=itemView.findViewById(R.id.textView8);
        }
    }
}
