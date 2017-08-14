package com.example.musta.englishhelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musta.englishhelper.helpers.WordDataBaseContract;
import com.example.musta.englishhelper.helpers.WordDataBaseHelper;
import com.example.musta.englishhelper.helpers.WordsList;
import com.example.musta.englishhelper.helpers.WordsListWithDelAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowMyList extends AppCompatActivity {

    private String mListName;
    private EditText mSearchForWordView;
    private ListView mSelectedListListView;
    private WordsList mSelectedWordList;
    private WordsListWithDelAdapter mListVadapter;
    private ArrayList<Pair<String, String>> mCustomWordsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mListName = getIntent().getStringExtra("listName");
        this.setTitle(mListName);
        mSearchForWordView = (EditText) findViewById(R.id.edtxtV_searchForWordInList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowMyList.this, AddNewListActivity.class);
                intent.putExtra("parentActivity", "showMyListActivity");
                intent.putExtra("listName", mListName);
                Gson gson = new Gson();
                String wordList_json = gson.toJson(mSelectedWordList);
                intent.putExtra("listElement", wordList_json);
                startActivity(intent);
            }
        });

        if (getWordListFromDB()) initMyList();
        else {
            Toast.makeText(getBaseContext(), "Faild to import List!", Toast.LENGTH_SHORT).show();
            finish();
        }

        mSearchForWordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editListSearchable(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSelectedListListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mSelectedListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ShowMyList.this, ShowWordDetails.class);
                intent.putExtra("wordDetailsTitle", mCustomWordsList.get(i).first);
                intent.putStringArrayListExtra("wordDetailsArray",
                        mSelectedWordList.getmWordsList().get(mCustomWordsList.get(i).first).getmTraslatedWords());
                startActivity(intent);
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mylist_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_deleteList) {
            dropListFromDB();
        } else if (item.getItemId() == R.id.action_startTest) {
            Toast.makeText(getBaseContext(), "Start Test", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, QuickTestActivity.class);
            intent.putExtra("listName", mListName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void dropListFromDB() {
        WordDataBaseHelper mDbHelper = new WordDataBaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME + " = ? ";
        String[] selectionArgs = { mListName };
        db.delete(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME, selection, selectionArgs);
        finish();
    }

    private void editListSearchable(String s) {
        initMyList();
        ArrayList<Pair<String, String>> searchList = new ArrayList<>(mCustomWordsList);
        for(Pair<String, String> word : searchList)
            if(!word.first.contains(s)) mCustomWordsList.remove(word);
        mListVadapter.notifyDataSetChanged();
    }

    private boolean getWordListFromDB() {
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
        boolean getDB = false;
        try {

            cursor.moveToFirst();
            String json = cursor.getString(cursor.getColumnIndexOrThrow(
                    WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT));
            Gson gson = new Gson();
            mSelectedWordList = gson.fromJson(json, WordsList.class);
            getDB = true;
        }  catch (Exception e) {
            getDB =  false;
        }
        finally {
            cursor.close();
        }
        return getDB;
    }


    private void initMyList() {
        mCustomWordsList = new ArrayList<>(mSelectedWordList.getmCustomWordList());
        mListVadapter = new WordsListWithDelAdapter(this, mCustomWordsList);
        mSelectedListListView = (ListView)findViewById(R.id.lstV_selectedWordsList);
        mSelectedListListView.setAdapter(mListVadapter);
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        getWordListFromDB();
        initMyList();
    }
}
