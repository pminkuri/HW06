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
import android.widget.TextView;

import com.example.inclass08.R;
import com.google.firebase.auth.UserProfileChangeRequest;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    EditText email;
    EditText password;
    EditText name;
    final String TAG = "Demo";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle(R.string.Registration);
        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = view.findViewById(R.id.editName);
                String nameValue = name.getText().toString();
                email = view.findViewById(R.id.editEmail);
                String emailValue = email.getText().toString();
                password = view.findViewById(R.id.editPassword);
                String passwordValue = password.getText().toString();

                if (emailValue.isEmpty()) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Enter email id")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else if (passwordValue.isEmpty()) {

                    // Toast.makeText(getContext(), "Enter password id", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Enter password")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else if (nameValue.isEmpty()) {

                    // Toast.makeText(getContext(), "Enter password id", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Enter name")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else {

                    mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nameValue)
                                                .build();
                                        mAuth.getCurrentUser().updateProfile(profileChangeRequest)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });


                                        mListener.setAccount();
                                    } else {

                                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Error")
                                                .setMessage(task.getException().getMessage())
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                        builder.create().show();
                                    }

                                }
                            });


                }


            }

        });
        TextView cancel = view.findViewById(R.id.tvCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.navigateOnCancel();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RegisterFragment.IRegListener) {
            mListener = (RegisterFragment.IRegListener) context;
        } else {

            throw new RuntimeException(context.toString() + R.string.iListenerError);
        }

    }

    RegisterFragment.IRegListener mListener;

    public interface IRegListener {

        void setAccount();

        void navigateOnCancel();
    }
}