package com.example.run4fun.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.run4fun.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // show toast
        Toast.makeText(context, "dont forget to training today!!!", Toast.LENGTH_SHORT).show();
    }
}