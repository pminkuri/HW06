package com.example.HW06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inclass08.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
                                        LoginFragment.ILoginListener,
                                        RegisterFragment.IRegListener,
                                        ForumsFragment.ItemClickListener,
                                        NewForumFragment.ItemClickListener{

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ContainerView,new LoginFragment(),"login")
                .commit();
    }

    @Override
    public void setAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerView, new ForumsFragment(),"forums")
                .commit();
    }

    @Override
    public void navigateOnCancel() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerView,new LoginFragment())
                .commit();
    }


    @Override
    public void navigateToRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerView,new RegisterFragment(),"registration")
                .commit();

    }

    @Override
    public void logout() {
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerView,new LoginFragment())
                .commit();

    }

    @Override
    public void gotoForumDetails(Forum forum) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ContainerView, ForumDetailsFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void newForum() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.ContainerView,new NewForumFragment(),"registration")
                .commit();

    }

    @Override
    public void returnToForumList() {
        getSupportFragmentManager().popBackStack();
    }
}