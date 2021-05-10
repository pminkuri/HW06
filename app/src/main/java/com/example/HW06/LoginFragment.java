package com.example.HW06;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inclass08.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    final String TAG = "Demo";
    EditText email;
    EditText password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle(R.string.login);
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = view.findViewById(R.id.email);
                email.setText("a@a.com");
                String emailValue = email.getText().toString();

                password = view.findViewById(R.id.password);
                password.setText("test123");
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
                }
                else if (passwordValue.isEmpty()) {

                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Enter email id")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                }

                else {

                    mAuth.signInWithEmailAndPassword(emailValue, passwordValue)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mloginListener.setAccount();

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

        TextView create = view.findViewById(R.id.tvCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mloginListener.navigateToRegistration();
            }
        });
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ILoginListener) {
            mloginListener = (ILoginListener) context;
        } else {

            throw new RuntimeException(context.toString() + R.string.iListenerError);
        }

    }

    ILoginListener mloginListener;
    public interface ILoginListener {

        void setAccount();

        void navigateToRegistration();
    }
}