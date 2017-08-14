package com.example.musta.englishhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowWordDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_word_details);
        ((TextView)findViewById(R.id.txtV_wordDetailsTitle)).setText(getIntent().getStringExtra("wordDetailsTitle"));
        ((ListView)findViewById(R.id.lstV_wordDetailsList)).setAdapter(new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, getIntent().getStringArrayListExtra("wordDetailsArray")));
        this.setTitle(getIntent().getStringExtra("wordDetailsTitle"));
    }
}
