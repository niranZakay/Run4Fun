package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUpActivity extends AppCompatActivity {
    private static final String TAG="signUpActivity";
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d(TAG,"onCreate: starting.");



        EditText email= findViewById(R.id.editTextEmail);
        EditText password=findViewById(R.id.passwordeitTextText);
        EditText confirmPassword=findViewById(R.id.confirmPasswordeitTextText);
        Button register=findViewById(R.id.RegisterButton);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull  Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(signUpActivity.this,"register successfully please check your email for verification"
                                                        ,Toast.LENGTH_LONG).show();
                                                email.setText("");
                                                password.setText("");
                                            }else {Toast.makeText(signUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();}

                                        }
                                    });

                                }
                                else { Toast.makeText(signUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();}
                            }
                        });
            }
        });


        Button button= (Button) findViewById(R.id.signinButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: clicked button.");
                Intent intent=new Intent(signUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
