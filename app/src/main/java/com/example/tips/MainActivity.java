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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignIn;
    TextView tVSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAutheStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignIn =  findViewById(R.id.buttonSignIn);
        tVSignUp = findViewById(R.id.buttonSignUp);

        mAutheStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null)
                {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        tVSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogUpActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String userPassword = password.getText().toString();
                if (email.isEmpty() && userPassword.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Fields are empty!",
                            Toast.LENGTH_SHORT).show();
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
                else
                {

                    mFirebaseAuth.signInWithEmailAndPassword(email,userPassword)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this,
                                        "Login error, please try again",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                startActivity(new Intent(MainActivity.this,
                                        HomeActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAutheStateListener);
    }
}