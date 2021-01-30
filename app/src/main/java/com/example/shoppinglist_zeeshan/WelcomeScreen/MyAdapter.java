package com.example.shoppinglist_zeeshan.WelcomeScreen;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0 : fragment = new FirstFragment();
            break;
            case 1 : fragment = new SecFragment();
            break;
            default : fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
