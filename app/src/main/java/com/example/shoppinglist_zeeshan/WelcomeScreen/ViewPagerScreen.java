package com.example.shoppinglist_zeeshan.WelcomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.shoppinglist_zeeshan.LogIn;
import com.example.shoppinglist_zeeshan.R;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ViewPagerScreen extends AppCompatActivity implements FirstFragment.FirstFragInterface, SecFragment.SecondFragInterface {

    ViewPager mViewPager;
    MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_screen);

        mViewPager = findViewById(R.id.viewPagerID);

        mAdapter = new MyAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void implementFirstFragInterface() {
        mViewPager.setCurrentItem(1);
    }


    @Override
    public void implementSecondFragInterfaceNext() {
        startActivity(new Intent(ViewPagerScreen.this, LogIn.class));

        SharedPreferences sharedPreferences = getSharedPreferences("checkHolder", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("value", true).apply();

        finish();
    }

    @Override
    public void implementSecondFragInterfaceBack() {
        mViewPager.setCurrentItem(0);
    }
}