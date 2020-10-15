package com.example.tips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogUpActivity extends AppCompatActivity {

    EditText emailID, password, reEnterPassword;
    Button btnCreatrAcount;
    TextView tVSignUp;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        reEnterPassword = findViewById(R.id.reEnterPasswordText);
        btnCreatrAcount = findViewById(R.id.createAcountButton);

        btnCreatrAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String userPassword = password.getText().toString();
                String reEnterPasswordString = reEnterPassword.getText().toString();
                if (email.isEmpty() && userPassword.isEmpty() && reEnterPasswordString.isEmpty())
                {
                    Toast.makeText(LogUpActivity.this,"Fields are empty!",Toast.LENGTH_SHORT).show();

                }
                else if (email.isEmpty())
                {
                    emailID.setError("Please enter an email");
                    emailID.requestFocus();
                }
                else if(userPassword.isEmpty())
                {
                    password.setError("Please enter a password");
                    password.requestFocus();
                }
                else if(reEnterPasswordString.isEmpty())
                {
                    reEnterPassword.setError("Please re-enter a password");
                    reEnterPassword.requestFocus();
                }
                else if(!reEnterPasswordString.equals(userPassword))
                {
                    reEnterPassword.setError("The passwords are not the same, please try again");
                    reEnterPassword.requestFocus();
                }
                else
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, userPassword)
                            .addOnCompleteListener(LogUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful())
                                    {
                                        Toast.makeText(LogUpActivity.this,"Unsuccessful sign up, please try again",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        startActivity(new Intent(LogUpActivity.this, HomeActivity.class));
                                    }
                            }
                    });
                }
            }
        });
    }
}