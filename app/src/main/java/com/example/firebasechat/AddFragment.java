package com.example.firebasechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class AddFragment extends BottomSheetDialogFragment {

    FirebaseUser user;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseFirestore db;
    String txt;
    EditText text;
    Button b,post;
    Uri uri=null;
    UploadTask uploadTask;
    ProgressBar pb;
    TextView t4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        storage=FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();
        pb=view.findViewById(R.id.progressBar3);
        t4=view.findViewById(R.id.textView4);
        t4.setText("Create a post");
        pb.setVisibility(View.GONE);
        b= view.findViewById(R.id.chooseimage);
        text=view.findViewById(R.id.text);
        post= view.findViewById(R.id.post);
        getPost();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt=text.getText().toString();
                Log.d("nasa",txt);
                if(TextUtils.isEmpty(txt))
                {
                    Toast.makeText(getContext(), "Description is Empty", Toast.LENGTH_SHORT).show();
                    if(uri==null)
                    {
                        Toast.makeText(getContext(), "Fill fields", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    if(uri != null)
                    {
                        pb.setVisibility(View.VISIBLE);
                        post(uri);
                        pb.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please Select an image", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return view;
    }


    private void chooseImage() {
        Intent in=new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(in,"Select an image"),200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == getActivity().RESULT_OK);
        {
            Toast.makeText(getContext(), "Image Selected", Toast.LENGTH_SHORT).show();
            uri= data.getData();
           Log.d("sohel",uri.getLastPathSegment().toString());
        }
    }

    private void post(Uri uri) {

        StorageReference r = storage.getReference();
        StorageReference ref=r.child("Images");
        storageRef=ref.child(uri.getLastPathSegment());
        uploadTask = storageRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                save(storageRef);
            }
        });
    }

    private void save(StorageReference s)
    {
        s.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(@NonNull Uri uri) {
                String url=uri.toString();
                Long t=System.currentTimeMillis();
                uploadPost(user.getUid(),user.getPhotoUrl().toString(),url,txt,t.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void uploadPost(String user,String profileurl,String imageurl,String text,String time)
    {
        ArrayList<String> arr=new ArrayList<>();
        HashMap<String,Object> h= new HashMap<>();
        h.put("user",user);
        h.put("profileurl",profileurl);
        h.put("imageurl",imageurl);
        h.put("text",text);
        h.put("time",time);
        h.put("likedBy",arr);

        db.collection("Posts").add(h).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(@NonNull DocumentReference documentReference) {
                Log.d("nasa","Post Created");
                Toast.makeText(getContext(), "Post Created", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("nasa","Something went wrong!");
            }
        });
    }
    private void getPost()
    {
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("nasa", document.getId() + " => " + document.getData());
                                Post p= document.toObject(Post.class);
                                Log.d("nasa",p.getText());
                                String t=p.getTime();
                                Long s=System.currentTimeMillis();
                                Long s1=Long.parseLong(t);
                                Long r=s-s1;
                                Long minutes = (r/ 1000) / 60;
                                Log.d("nasa",minutes.toString()+" minutes ago");
                            }
                        } else {
                            Log.d("nasa", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("nasa", "Error getting documents: ");
            }
        });
    }

//    db.collection("cities")
//        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//        @Override
//        public void onEvent(@Nullable QuerySnapshot value,
//                @Nullable FirebaseFirestoreException e) {
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e);
//                return;
//            }
//
//            List<String> cities = new ArrayList<>();
//            for (QueryDocumentSnapshot doc : value) {
//                if (doc.get("name") != null) {
//                    cities.add(doc.getString("name"));
//                }
//            }
//            Log.d(TAG, "Current cites in CA: " + cities);
//        }
//    });

}