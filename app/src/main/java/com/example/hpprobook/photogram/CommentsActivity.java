package com.example.hpprobook.photogram;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private Toolbar commentToolbar;

    private EditText comment_field;
    private ImageView comment_post_btn;

    private RecyclerView comment_list;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;

    private List<Comments> commentsList;
    private List<User> userList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String blog_post_id;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.hpprobook.blogapp.R.layout.activity_comments);

        commentToolbar = findViewById(com.example.hpprobook.blogapp.R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Comments");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        blog_post_id = getIntent().getStringExtra("blog_post_id");

        comment_field = findViewById(com.example.hpprobook.blogapp.R.id.comment_field);
        comment_post_btn = findViewById(com.example.hpprobook.blogapp.R.id.comment_post_btn);
        comment_list = findViewById(com.example.hpprobook.blogapp.R.id.comment_list);

        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        userList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList,userList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentsRecyclerAdapter);


        firebaseFirestore.collection("Posts/" + blog_post_id + "/Comments")
                .addSnapshotListener(CommentsActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            for (final DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String commentUserId= doc.getDocument().getString("user_id");
                                    final Comments comments = doc.getDocument().toObject(Comments.class);

                                    firebaseFirestore.collection("Users").document(commentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){

                                                User user = task.getResult().toObject(User.class);
                                                commentsList.add(comments);
                                                userList.add(user);
                                                
                                                commentsRecyclerAdapter.notifyDataSetChanged();
                                            }

                                        }
                                    });


                                }
                            }

                        }

                    }
                });

        comment_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = comment_field.getText().toString();


                Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", comment_message);
                commentsMap.put("user_id", current_user_id);
                commentsMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Posts/" + blog_post_id + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(CommentsActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            comment_field.setText("");

                        }

                    }
                });

            }
        });


    }
}
