package com.example.musta.englishhelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musta.englishhelper.helpers.WordDataBaseContract;
import com.example.musta.englishhelper.helpers.WordDataBaseHelper;
import com.example.musta.englishhelper.helpers.WordElement;
import com.example.musta.englishhelper.helpers.WordsList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class QuickTestActivity extends AppCompatActivity {

    private String mListName;
    private WordsList mWordListForTest;
    private ArrayList<Pair<String, ArrayList<String>>> mWordListShuffled;
    private int pos = 0, listSize, correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queck_test);
        mListName = getIntent().getStringExtra("listName");
        setTitle(mListName + " Test");
        getElementFromDB();
        initWordMap();
        startTest();
        ((Button)findViewById(R.id.btn_nextWord)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++pos; pos %= listSize;
                startTest();
            }
        });

        ((Button)findViewById(R.id.btn_previousWord)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --pos; pos %= listSize;
                startTest();
            }
        });

        ((Button)findViewById(R.id.btn_resetWordsList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = 0;
                startTest();
            }
        });

        ((Button)findViewById(R.id.btn_showWord)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Correct Word : " + mWordListShuffled.get(pos).first
                + " !", Toast.LENGTH_SHORT).show();
                pos = 0;
                startTest();
            }
        });

        ((Button)findViewById(R.id.btn_submitWords)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWordListShuffled.get(pos).first.equals(((EditText)findViewById(R.id.edtxtV_userWordTest))
                .getText().toString())) {
                    ((TextView)findViewById(R.id.txtV_wordStat)).setText("Coreect !");
                    ((TextView)findViewById(R.id.txtV_wordStat)).setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    ((EditText)findViewById(R.id.edtxtV_userWordTest)).setText("");
                    correctAnswers++;
                    ++pos; pos %= listSize;
                } else if(((EditText)findViewById(R.id.edtxtV_userWordTest))
                        .getText().toString().equals("") ) {
                    ((TextView)findViewById(R.id.txtV_wordStat)).setText("Please Enter Text !");
                } else {
                    ((TextView)findViewById(R.id.txtV_wordStat)).setText("Wrong !");
                    ((TextView)findViewById(R.id.txtV_wordStat)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                startTest();
            }
        });

    }

    private void startTest() {
        if(pos == 0) ((Button) findViewById(R.id.btn_previousWord)).setEnabled(false);
        else ((Button)findViewById(R.id.btn_previousWord)).setEnabled(true);
        if(pos == mWordListShuffled.size()) ((Button)findViewById(R.id.btn_nextWord)).setEnabled(false);
        else ((Button)findViewById(R.id.btn_nextWord)).setEnabled(true);

        ((EditText)findViewById(R.id.edtxtV_userWordTest)).setText("");
        String tempWordMean = "* ";
        int translationSize = mWordListShuffled.get(pos).second.size();
        for(int j = 0; j < translationSize; j++) {
            tempWordMean += mWordListShuffled.get(pos).second.get(j);
            if(j != translationSize - 1) tempWordMean += " / ";
        }
        tempWordMean += " *";
        ((TextView)findViewById(R.id.txtV_wordMean)).setText(tempWordMean);

    }

    private void initWordMap() {
        mWordListShuffled = new ArrayList<>();
        for(WordElement word : mWordListForTest.getmWordsList().values()) {
            mWordListShuffled.add(Pair.create(word.getmSourceEnglishWord(), word.getmTraslatedWords()));
        }
        Collections.shuffle(mWordListShuffled);
        listSize = mWordListShuffled.size();
    }


    private void getElementFromDB() {
        WordDataBaseHelper mDbHelper = new WordDataBaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String [] returnedColumns = {WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT};
        String selection = WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME + " = ?";
        String[] selectionArgs = { mListName };
        Cursor cursor = db.query(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME,
                returnedColumns,
                selection,
                selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        String json = cursor.getString(cursor.getColumnIndexOrThrow(
                WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT));
        cursor.close();
        Gson gson = new Gson();
        mWordListForTest = gson.fromJson(json, WordsList.class);

    }
}
