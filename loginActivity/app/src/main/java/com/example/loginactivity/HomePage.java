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
import android.os.Bundle;

public class HomePage {
    private static final String TAG="HomePageActivity";
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        setContentView(R.layout.home_page);
        Log.d(TAG,"onCreate: starting.");}

    private void setContentView(int home_page) {
    }


}
