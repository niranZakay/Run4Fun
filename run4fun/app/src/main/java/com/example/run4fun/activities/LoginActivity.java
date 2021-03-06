package com.example.run4fun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.run4fun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText useremail= findViewById(R.id.editTextEmail);
        EditText userpassword=findViewById(R.id.passwordeitTextText);
        Button userLogin=findViewById(R.id.LoginButton);
        Button button= (Button) findViewById(R.id.signUpButton);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInWithEmailAndPassword(useremail.getText().toString().trim(),
                        userpassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this,"success"
                                        ,Toast.LENGTH_LONG).show();
                                useremail.setText("");
                                userpassword.setText("");
                            }else {Toast.makeText(LoginActivity.this,"please verify your email address",Toast.LENGTH_LONG).show();}
                        }
                        else {Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();}
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG,"onClick: clicked button.");
            Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
        });
    }
}