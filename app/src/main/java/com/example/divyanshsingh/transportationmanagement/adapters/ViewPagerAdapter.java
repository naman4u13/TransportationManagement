package com.example.divyanshsingh.transportationmanagement.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();
    private FirebaseAuth mAuth;
    private Context context;
    int type;

    public ViewPagerAdapter(FragmentManager fm, Context context, int type) {
        super(fm);
        this.context = context;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        //mAuth = FirebaseAuth.getInstance();
        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
            case 3:
                return fragmentList.get(3);
            case 4:
                return fragmentList.get(4);
            default:
                return fragmentList.get(1);
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title) {
        if (!fragmentList.contains(fragment)) {
            fragmentList.add(fragment);
            fragmentListTitles.add(title);
        }
    }

    public void ReplaceFragment(int position, Fragment fm, String title) {
        fragmentList.add(position, fm);
        fragmentListTitles.add(position, title);
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged(Fragment fragment, String title, int position) {
        super.notifyDataSetChanged();
        fragmentList.add(position, fragment);
        fragmentListTitles.add(position, title);

    }
}
