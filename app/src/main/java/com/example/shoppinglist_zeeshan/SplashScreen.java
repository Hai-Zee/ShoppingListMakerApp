package com.example.shoppinglist_zeeshan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.shoppinglist_zeeshan.WelcomeScreen.ViewPagerScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    FirebaseAuth myAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myAuth = FirebaseAuth.getInstance();
        mUser = myAuth.getCurrentUser();

        sharedPreferences = getSharedPreferences("checkHolder", MODE_PRIVATE);
        boolean checked = sharedPreferences.getBoolean("value", false);

        if (checked){

            if(mUser!=null){
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
            else if (mUser == null){
                startActivity(new Intent(SplashScreen.this, LogIn.class));
                finish();
            }

        }
        else{
            startActivity(new Intent(SplashScreen.this, ViewPagerScreen.class));
            finish();
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}