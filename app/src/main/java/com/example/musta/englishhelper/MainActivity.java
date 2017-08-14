package com.example.musta.englishhelper;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musta.englishhelper.helpers.TabsAdapter;
import com.example.musta.englishhelper.helpers.WordDataBaseContract;
import com.example.musta.englishhelper.helpers.WordDataBaseHelper;
import com.example.musta.englishhelper.helpers.WordsList;
import com.example.musta.englishhelper.helpers.XMLParser;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public static WordsList AppWordsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("English Helper");
        setContentView(R.layout.activity_main);
        ((ActionBar)this.getSupportActionBar()).setDisplayUseLogoEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.pgrV_tabs);
        setupViewPager(mViewPager);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_myLists:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_quickTest:
                        mViewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                    navigation.setSelectedItemId(R.id.navigation_home);
                else if(position == 1)
                    navigation.setSelectedItemId(R.id.navigation_myLists);
                else navigation.setSelectedItemId(R.id.navigation_quickTest);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        testFirstTime();
    }

    private void testFirstTime() {
        SharedPreferences prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            saveToDataBaseFirstTime();
            prefs.edit().putBoolean("firstrun", false).commit();
        } else {
            initWordsList();
        }
    }

    private void saveToDataBaseFirstTime() {
        XMLParser xmlParser = new XMLParser(getResources().openRawResource(R.raw.words));
        AppWordsList = new WordsList(xmlParser.getmAllWordsElements());
        AppWordsList.setmListName("AllAppWords");

        WordDataBaseHelper mDBHelper = new WordDataBaseHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME,
                    AppWordsList.getmListName());

            Gson gson = new Gson();
            String wordList_json = gson.toJson(AppWordsList);
            values.put(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT, wordList_json);

            long newRowID = db.insert(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME, null, values);
            if(newRowID != -1) {
                Toast.makeText(getBaseContext(), "Setup DataBase!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MyListsFragment());
        adapter.addFragment(new QuickTestFragment());
        viewPager.setAdapter(adapter);
    }

    private void initWordsList() {
        WordDataBaseHelper mDbHelper = new WordDataBaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT
                + " FROM " + WordDataBaseContract.WordDataBaseEntry.TABLE_NAME + " WHERE _ID = 1", null);
        try {
            cursor.moveToFirst();
            String json = cursor.getString(0);
            Gson gson = new Gson();
            AppWordsList = gson.fromJson(json, WordsList.class);
        }  catch (Exception e) {
        } finally {
            cursor.close();
        }
    }



}
