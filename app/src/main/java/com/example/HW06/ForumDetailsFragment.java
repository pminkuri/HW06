package com.example.HW06;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inclass08.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumDetailsFragment extends Fragment {

    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";
    private Forum mForum;
    private FirebaseAuth mAuth;
    public ForumDetailsFragment() {
    }


    public static ForumDetailsFragment newInstance(Forum forum) {
        ForumDetailsFragment fragment = new ForumDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Forum) getArguments().getSerializable(ARG_PARAM_FORUM);
        }
    }

    TextView textViewForumTitle, textViewForumOwnerName, textViewForumDesc, textViewNumComments;
    EditText editTextTextComment;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<Comment> comments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forum_details, container, false);
        getActivity().setTitle(R.string.Forum);
        mAuth = FirebaseAuth.getInstance();
        textViewForumTitle = view.findViewById(R.id.textViewForumTitle);
        textViewForumOwnerName = view.findViewById(R.id.textViewForumOwnerName);
        textViewForumDesc = view.findViewById(R.id.textViewForumDesc);
        textViewNumComments = view.findViewById(R.id.textViewNumComments);
        editTextTextComment = view.findViewById(R.id.editTextTextComment);
        recyclerView = view.findViewById(R.id.recyclerView);

        textViewForumTitle.setText(mForum.getTitle());
        textViewForumOwnerName.setText(mForum.getCreatorName());
        textViewForumDesc.setText(mForum.getDescription());

        if(comments.size()==0){
            textViewNumComments.setText("No Comments");
        }





        view.findViewById(R.id.buttonPostSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = editTextTextComment.getText().toString();
                if(commentText.isEmpty()){
                    Toast.makeText(getActivity(), R.string.EnterComments, Toast.LENGTH_SHORT).show();
                } else {
                    Comment comment=new Comment(commentText,mAuth.getCurrentUser().getDisplayName(),mForum.forumID,new Date());
                    createNewComment(comment);
                    editTextTextComment.setText("");
                }
            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CommentsAdapter();
        getComments();
        return view;
    }

    void getComments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Comment")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        comments.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            if(comment.getForumID().equals(mForum.forumID)){
                                comments.add(comment);
                            }

                        }

                        if (comments.size() > 0) {
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                            textViewNumComments.setText(comments.size() + " Comments");
                        }


                    }
                });

    }

    void createNewComment(Comment comment){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Comment")
                .document(comment.commentID)
                .set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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


    void deleteComment(Comment comment) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Comment").document(comment.getCommentID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Demo", "Comment successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Demo", "Error deleting comment", e);
                    }
                });
        comments.remove(comment);
        if(comments.size()==0){
            textViewNumComments.setText("No Comments");
        }
        adapter.notifyDataSetChanged();

    }
    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder>{
        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comments_row_item, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = comments.get(position);
            holder.setupComment(comment);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class CommentHolder extends RecyclerView.ViewHolder{
            TextView textViewDesc, textViewOwner, textViewDate;
            ImageView imageViewDeleteComment;


            public CommentHolder(@NonNull View itemView) {
                super(itemView);
                textViewDesc = itemView.findViewById(R.id.textViewDesc);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                imageViewDeleteComment = itemView.findViewById(R.id.imageViewDeleteComment);
            }

            public void setupComment(Comment comment){
                textViewDesc.setText(comment.getCommentText());
                textViewOwner.setText(comment.getCommentedBy());

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewDate.setText(formatter.format(comment.getCommentedOn()));
                if(comment.getCommentedBy().equals(mAuth.getCurrentUser().getDisplayName())){
                    imageViewDeleteComment.setVisibility(View.VISIBLE);
                    imageViewDeleteComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          deleteComment(comment);
                        }
                    });
                } else {
                    imageViewDeleteComment.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}