package com.example.hpprobook.photogram;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Uri mainImageURI = null;
    private TextView accountName;
    private CircleImageView accountImage;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.example.hpprobook.blogapp.R.layout.fragment_account, container, false);

        accountImage = view.findViewById(com.example.hpprobook.blogapp.R.id.account_image);
        accountName = view.findViewById(com.example.hpprobook.blogapp.R.id.account_name);

        getUser();

        return view;
    }

    protected void getUser() {

        firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    if(currentUser != null){
        String Uid = currentUser.getUid();

        firebaseFirestore.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String name = task.getResult().getString("name");
                String image = task.getResult().getString("image");

                mainImageURI = Uri.parse(image);

                accountName.setText(name);

                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(com.example.hpprobook.blogapp.R.drawable.person);

                Glide.with(getActivity()).setDefaultRequestOptions(placeholderRequest).load(image).into(accountImage);



            }
        });
    }
    }
}

