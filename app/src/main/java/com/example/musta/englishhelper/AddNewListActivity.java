package com.example.musta.englishhelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musta.englishhelper.helpers.WordDataBaseContract;
import com.example.musta.englishhelper.helpers.WordDataBaseHelper;
import com.example.musta.englishhelper.helpers.WordsList;
import com.example.musta.englishhelper.helpers.WordsListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.musta.englishhelper.MainActivity.AppWordsList;

public class AddNewListActivity extends AppCompatActivity {

    private WordsList mNewWordsList;
    private AutoCompleteTextView atocmpWordsSearch;
    private ArrayList<String> mEnglishWords, mTempEnglishWords;
    private WordsListAdapter mListVadapter;
    private ArrayList<Pair<String, String>> mCustomWordsList, mTempCustomWordsList;
    private ListView mWordsListView;
    private String parentActivityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        copyTemps();
        parentActivityName = getIntent().getStringExtra("parentActivity");
        if(parentActivityName.equals("showMyListActivity")) {
            initExistList();
        }
        initWordsList();
        atocmpWordsSearch.addTextChangedListener(new TextWatcher() {
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
        mWordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String tempWordName = mTempEnglishWords.get(pos);
                mNewWordsList.getmWordsList().put(tempWordName,
                        AppWordsList.getmWordsList().get(tempWordName));
                mEnglishWords.remove(tempWordName);
                mCustomWordsList.remove(Pair.create(mTempCustomWordsList.get(pos).first,
                        mTempCustomWordsList.get(pos).second));
                mListVadapter.remove(Pair.create(mTempCustomWordsList.get(pos).first,
                        mTempCustomWordsList.get(pos).second));
                mListVadapter.notifyDataSetChanged();
                initWordsList();
            }
        });
    }

    private void initExistList() {
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("listElement");
        mNewWordsList = gson.fromJson(json, WordsList.class);
        ((EditText)(findViewById(R.id.edtxt_listName))).setText(getIntent().getStringExtra("listName"));
        ((EditText)(findViewById(R.id.edtxt_listName))).setEnabled(false);
        for(String tempWordName : mNewWordsList.getmEglishWordsList()) {
            mEnglishWords.remove(tempWordName);
            mCustomWordsList.remove(Pair.create(tempWordName, mNewWordsList.getmWordsList().
            get(tempWordName).getmTraslatedWords().get(0)));
            mListVadapter.remove(Pair.create(tempWordName, mNewWordsList.getmWordsList().
                    get(tempWordName).getmTraslatedWords().get(0)));
            mListVadapter.notifyDataSetChanged();
        }
        initWordsList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addlist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save) {
            String tempListName = ((EditText)findViewById(R.id.edtxt_listName))
                    .getText().toString();
            if(!tempListName.equals("")) {
                Toast.makeText(getBaseContext(), "Save Selected ",
                        Toast.LENGTH_SHORT).show();
                mNewWordsList.setmListName(tempListName);
                mNewWordsList.finish();
                if(checkListName(tempListName) && !parentActivityName.equals("showMyListActivity")) {
                    Toast.makeText(getBaseContext(), "This name is already exist !",
                            Toast.LENGTH_SHORT).show();
                } else if(mNewWordsList.getmEglishWordsList().size() == 0) {
                    Toast.makeText(getBaseContext(), "This list is empty !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    saveToDataBase(); finish();
                }
            } else {
                Toast.makeText(getBaseContext(), "Enter list Name !", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkListName(String tempListName) {

        int cnt = 0;
          try {
            WordDataBaseHelper mDbHelper = new WordDataBaseHelper(this);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String [] returnedColumns = {WordDataBaseContract.WordDataBaseEntry._ID};
            String selection = WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME + " = ?";
            String[] selectionArgs = { tempListName };
            Cursor cursor = db.query(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME,
                    returnedColumns,
                    selection,
                    selectionArgs, null, null, null);
            cnt = cursor.getCount();
            cursor.close();
            return cnt > 0;

        } catch (Exception e) {
        }
        return true;
    }

    private void copyTemps() {
        mNewWordsList = new WordsList();
        mCustomWordsList = new ArrayList<>(AppWordsList.getmCustomWordList());
        mEnglishWords = new ArrayList<>(AppWordsList.getmEglishWordsList());
        mTempCustomWordsList = new ArrayList<>(mCustomWordsList);
        mTempEnglishWords = new ArrayList<>(AppWordsList.getmEglishWordsList());
        mWordsListView = (ListView)findViewById(R.id.lstV_newListWordsList);
        atocmpWordsSearch = (AutoCompleteTextView)findViewById(R.id.atocmptxtV_newListSearchForWord);
        mListVadapter = new WordsListAdapter(this, mTempCustomWordsList);
        mWordsListView.setAdapter(mListVadapter);
    }

    private void initWordsList() {
        mTempCustomWordsList = new ArrayList<>(mCustomWordsList);
        mTempEnglishWords = new ArrayList<>(mEnglishWords);
        /*mListVadapter = new WordsListAdapter(this, mTempCustomWordsList);
        mWordsListView.setAdapter(mListVadapter);*/
    }

    public void editListSearchable(String searchWord) {
        initWordsList();
        ArrayList<Pair<String, String>> searchList = new ArrayList<>(mCustomWordsList);
        for(Pair<String, String> word : searchList)
            if(!word.first.contains(searchWord)) mTempCustomWordsList.remove(word);
        mListVadapter = new WordsListAdapter(this, mTempCustomWordsList);
        mWordsListView.setAdapter(mListVadapter);
    }

    private void saveToDataBase() {
        WordDataBaseHelper mDBHelper = new WordDataBaseHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            long newRowID = -1;
            if(parentActivityName.equals("showMyListActivity")) {
                Gson gson = new Gson();
                String wordList_json = gson.toJson(mNewWordsList);

                values.put(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT, wordList_json);
                String selection = WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME+ " = ?";
                String[] selectionArgs = { mNewWordsList.getmListName() };
                newRowID = db.update(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else {
                values.put(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME,
                        mNewWordsList.getmListName());

                Gson gson = new Gson();
                String wordList_json = gson.toJson(mNewWordsList);
                values.put(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT, wordList_json);

                newRowID = db.insert(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME, null, values);
            }
            if(newRowID != -1) {
                Toast.makeText(getBaseContext(), "Saved To Data Base !", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }
}
