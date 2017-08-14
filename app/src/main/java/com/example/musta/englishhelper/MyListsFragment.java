package com.example.musta.englishhelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.musta.englishhelper.helpers.WordDataBaseContract;
import com.example.musta.englishhelper.helpers.WordDataBaseHelper;

import java.util.ArrayList;


public class MyListsFragment extends Fragment {

    private View rootView;
    private ListView mMyListsListView;
    private ArrayList<String> mMyListsName;
    private ArrayAdapter<String> mListViewAdapter;
    private EditText mSearchForListNameView;
    public MyListsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_lists, container, false);
        mSearchForListNameView = rootView.findViewById(R.id.edtxtV_searchForList);
        android.support.design.widget.FloatingActionButton fb = rootView.findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewListActivity.class);
                intent.putExtra("parentActivity", "MyListsFragment");
                startActivity(intent);

            }

        });

        mMyListsListView = rootView.findViewById(R.id.lstV_myListsList);
        mMyListsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowMyList.class);
                intent.putExtra("listName", mMyListsName.get(i));
                startActivity(intent);
            }
        });

        mSearchForListNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initMyLists();
                editList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return rootView;
    }

    private void editList(String tempStr) {
        ArrayList<String> tempListsNames = new ArrayList<>(mMyListsName);
        for(String str : tempListsNames)
            if(!str.contains(tempStr)) mMyListsName.remove(str);
        mListViewAdapter.notifyDataSetChanged();
    }

    private void initMyLists() {
        mMyListsName = new ArrayList<>();
        WordDataBaseHelper mDbHelper = new WordDataBaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String [] returnedColumns = {WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME};
        Cursor cursor = db.query(WordDataBaseContract.WordDataBaseEntry.TABLE_NAME,
                returnedColumns,
                null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(
                    cursor.getColumnIndexOrThrow(WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME));
            mMyListsName.add(temp);
        }
        cursor.close();
        mMyListsName.remove(0);
        mListViewAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mMyListsName);
        mMyListsListView.setAdapter(mListViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMyLists();
    }
}
