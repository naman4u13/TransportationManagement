package com.example.divyanshsingh.transportationmanagement.acitivity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.divyanshsingh.transportationmanagement.R;
import com.example.divyanshsingh.transportationmanagement.adapters.ViewPagerAdapter;
import com.example.divyanshsingh.transportationmanagement.fragments.CurrentFragment;
import com.example.divyanshsingh.transportationmanagement.fragments.SearchResultFragment;

public class DashboardActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;
    private SearchResultFragment resultFragment;
    private boolean profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_dashboard);

        tabLayout = findViewById(R.id.tab_layout1);

        viewPager = findViewById(R.id.container);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, 1);
        resultFragment = new SearchResultFragment();
        adapter.AddFragment(resultFragment, "Result");
        adapter.AddFragment(new CurrentFragment(), "Current");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void setCurrentItem(int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(RESULT_OK);
    }
}
