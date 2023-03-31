package com.varunbansal.chatapp;

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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edit_text_username;
    private EditText edit_text_email;
    private EditText edit_text_password;
    private Button button_signUp;
    private TextView text_view_logging;

    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_text_username = findViewById(R.id.edit_text_username);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_password = findViewById(R.id.edit_text_password);
        button_signUp = findViewById(R.id.button_signUp);
        text_view_logging = findViewById(R.id.text_view_logging);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this,FriendsActivity.class));
        }

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_text_email.getText().toString().isEmpty() || edit_text_password.getText().toString().isEmpty())
                {
                    if(isSigningUp && edit_text_username.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isSigningUp) {
                    handleSignUp();
                } else {
                    handleLogin();
                }
            }
        });

        text_view_logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSigningUp) {
                    isSigningUp = false;
                    edit_text_username.setVisibility(View.GONE);
                    button_signUp.setText("Log In");
                    text_view_logging.setText("Don't have an account? Sign Up");
                } else {
                    isSigningUp = true;
                    edit_text_username.setVisibility(View.VISIBLE);
                    button_signUp.setText("Sign Up");
                    text_view_logging.setText("Already have an account? Log In");

                }
            }
        });
    }
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edit_text_email.getText().toString(),edit_text_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Signed Up Successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edit_text_email.getText().toString(),edit_text_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}