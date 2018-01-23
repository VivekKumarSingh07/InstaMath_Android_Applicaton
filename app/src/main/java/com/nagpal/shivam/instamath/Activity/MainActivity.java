package com.nagpal.shivam.instamath.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.nagpal.shivam.instamath.Adapter.ActivityDetailAdapter;
import com.nagpal.shivam.instamath.R;
import com.nagpal.shivam.instamath.Utils.ActivityDetail;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<ActivityDetail> mActivityDetailArrayList;
    private AutoCompleteTextView actvSearch;
    private Toolbar toolbar;
    private Boolean isSearchOpen = false;
    private Float defaultToolbarElevation;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isSearchOpen) {
            getMenuInflater().inflate(R.menu.main_search_open, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_search_close, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.search_open_menu_item:
                openSearch();
                return true;

            case R.id.search_clear_menu_item:
                actvSearch.setText(null);
                return true;

            case R.id.preferences_menu_item:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar_default);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            defaultToolbarElevation = toolbar.getElevation();
        }
//        Toast.makeText(MainActivity.this, "" + defaultToolbarElevation, Toast.LENGTH_LONG).show();
        ListView listView = (ListView) findViewById(R.id.list_view_activity);


        mActivityDetailArrayList = new ArrayList<>();

        mActivityDetailArrayList.add(new ActivityDetail("Basic Calculator", BasicCalculatorActivity.class));
        mActivityDetailArrayList.add(new ActivityDetail("Scientific Calculator", ScientificCalculatorActivity.class));
        mActivityDetailArrayList.add(new ActivityDetail("Interest", InterestActivity.class));
//        mActivityDetailArrayList.add(new ActivityDetail("Compound Interest", CompoundInterestActivity.class));
        mActivityDetailArrayList.add(new ActivityDetail("Interpolation", InterpolationActivity.class));
        mActivityDetailArrayList.add(new ActivityDetail("Number Conversion", NumberConversionActivity.class));

        ActivityDetailAdapter activityDetailAdapter = new ActivityDetailAdapter(this, mActivityDetailArrayList);

        ArrayList<String> activityNamesArrayList = new ArrayList<>();

        for (ActivityDetail activityDetail : mActivityDetailArrayList) {
            activityNamesArrayList.add(activityDetail.getName());
        }

        actvSearch = new AutoCompleteTextView(this);
        actvSearch.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        actvSearch.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actvSearch.setSingleLine();
        actvSearch.setHintTextColor(ContextCompat.getColor(this, android.R.color.white));
        actvSearch.setHint("Search");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, activityNamesArrayList);
        actvSearch.setAdapter(arrayAdapter);
        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getAdapter().getItem(position).toString();

                for (ActivityDetail aI : mActivityDetailArrayList) {
                    if (aI.getName().equals(selectedItem)) {
                        startActivity(new Intent(MainActivity.this, aI.getActivityClass()));
                    }
                }
            }
        });


        listView.setAdapter(activityDetailAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, mActivityDetailArrayList.get(i).getActivityClass());
                startActivity(intent);
            }
        });
    }

    private void openSearch() {
        isSearchOpen = true;
        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.searchOpenedToolbar));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5 * defaultToolbarElevation);
        }
        actvSearch.requestFocus();
        invalidateOptionsMenu();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.addView(actvSearch);
    }

    private void closeSearch() {
        isSearchOpen = false;
        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        actvSearch.setText(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(defaultToolbarElevation);
        }
        toolbar.removeView(actvSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpen) {
            closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
