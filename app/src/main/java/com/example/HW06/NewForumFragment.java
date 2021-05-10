package com.example.HW06;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.inclass08.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewForumFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    EditText forumTitle;
    EditText forumDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        getActivity().setTitle(R.string.NewForum);

        forumTitle = view.findViewById(R.id.newTitle);
        forumDesc = view.findViewById(R.id.newDesc);
        view.findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = String.valueOf(forumTitle.getText());
                String description = String.valueOf(forumDesc.getText());
                String creatorName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                HashMap<String,Object> likedBy=new HashMap<>();
                ArrayList<Comment> comments = new ArrayList<>();
                Forum forum = new Forum(title, creatorName, description, likedBy,new Date(),comments);
                createForum(forum);
            }
        });

        view.findViewById(R.id.tvNewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.returnToForumList();
            }
        });
        return view;
    }

    private void createForum(Forum forum) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Forum")
                .document(forum.forumID)
                .set(forum).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                itemClickListener.returnToForumList();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error")
                                .setMessage(e.getMessage())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.create().show();

                    }
                });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof NewForumFragment.ItemClickListener){
            itemClickListener=(NewForumFragment.ItemClickListener)context;
        }else{
            throw new RuntimeException(context.toString()+R.string.iListenerError);
        }

    }

    NewForumFragment.ItemClickListener itemClickListener;
    public interface ItemClickListener{
        void returnToForumList();
    }
}