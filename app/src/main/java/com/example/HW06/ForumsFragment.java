package com.example.HW06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;


public class ForumsFragment extends Fragment {
    private FirebaseAuth mAuth;
    ArrayList<Forum> forumArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    RecyclerView recyclerViewForum;
    LinearLayoutManager mLayoutManager;
    ForumsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle(R.string.Forums);
        recyclerViewForum = view.findViewById(R.id.rvForum);
        getForums();

        view.findViewById(R.id.buttonLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.logout();
            }
        });


        view.findViewById(R.id.btnNewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.newForum();
            }
        });

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ForumsFragment.ItemClickListener) {
            itemClickListener = (ForumsFragment.ItemClickListener) context;
        } else {

            throw new RuntimeException(context.toString() + "Implements Listner");
        }

    }

    ForumsFragment.ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void logout();
        void gotoForumDetails(Forum forum);
        void newForum();
    }


    void getForums() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Forum")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forumArrayList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Forum forum = document.toObject(Forum.class);
                            forumArrayList.add(forum);
                        }

                        if (forumArrayList.size() > 0) {
                            adapter = new ForumsAdapter();
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerViewForum.setLayoutManager(mLayoutManager);
                            recyclerViewForum.setHasFixedSize(true);
                            recyclerViewForum.setAdapter(adapter);
                        }


                    }
                });

    }

    void deleteForum(Forum forum) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Forum").document(forum.forumID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Demo", "Forum successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Demo", "Error deleting FOrum", e);
                    }
                });

        forumArrayList.remove(forum);
        adapter.notifyDataSetChanged();

    }


    void toggleLikeButton(Forum forum,String name,Boolean like) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> liked=forum.getLikedBy();

        if(like){
            liked.put(name,true);
        }
        else{
            liked.remove(name);
        }
        db.collection("Forum").document(forum.forumID)
               .update("likedBy",liked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Demo", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Demo", "Error writing document", e);
                    }
                });
    }


    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {

        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.forum_row_item, parent, false);
            return new ForumViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return forumArrayList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            Forum forum = forumArrayList.get(position);
            holder.setupForumItem(forum);
        }

        class ForumViewHolder extends RecyclerView.ViewHolder {
            TextView textViewTitle, textViewDesc, textViewOwner, textViewCreatedDate,textViewlikesCount;
            ImageView imageViewLike, imageViewDeleteForum;
            Forum mForum;


            public ForumViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.tvForumTitle);
                textViewDesc = itemView.findViewById(R.id.tvForumDesc);
                textViewOwner = itemView.findViewById(R.id.tvCreaterName);
                textViewCreatedDate = itemView.findViewById(R.id.tvForumDate);
                textViewlikesCount=itemView.findViewById(R.id.tvLikes);

                imageViewLike = itemView.findViewById(R.id.imageViewLikes);
                imageViewDeleteForum = itemView.findViewById(R.id.imageViewDelete);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.gotoForumDetails(mForum);
                    }
                });
            }

            public void setupForumItem(Forum forum) {
                this.mForum = forum;
                textViewTitle.setText(forum.getTitle());

                String desc200 = forum.getDescription().substring(0, Math.min(200, forum.getDescription().length()));
                textViewDesc.setText(desc200);
                textViewOwner.setText(forum.getCreatorName());
                int likeCount = forum.getLikedBy().size();
                String likeString = "No Likes";
                if(likeCount == 1){
                    likeString = "1 Like";
                } else if(likeCount > 1) {
                    likeString = likeCount + " Likes";
                }
                else{
                    likeString = "No Likes";
                }
                textViewlikesCount.setText(likeString);
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewCreatedDate.setText(formatter.format(forum.createdOn));
                if (forum.creatorName.equals(mAuth.getCurrentUser().getDisplayName())) {
                    imageViewDeleteForum.setVisibility(View.VISIBLE);
                    imageViewDeleteForum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteForum(forum);
                        }
                    });
                } else {
                    imageViewDeleteForum.setVisibility(View.INVISIBLE);
                }

                if(forum.getLikedBy().containsKey(mAuth.getUid())){
                    imageViewLike.setImageResource(R.drawable.like_favorite);
                } else {
                    imageViewLike.setImageResource(R.drawable.like_not_favorite);
                }

                imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(forum.getLikedBy().containsKey(mAuth.getUid())){
                            toggleLikeButton(forum,mAuth.getUid(),false);
                        } else {
                            toggleLikeButton(forum,mAuth.getUid(),true);
                        }
                    }
                });

            }
        }
    }


}