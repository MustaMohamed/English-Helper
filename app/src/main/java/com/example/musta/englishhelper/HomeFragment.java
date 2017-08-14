package com.example.musta.englishhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.example.musta.englishhelper.helpers.WordsListAdapter;

import java.util.ArrayList;

import static com.example.musta.englishhelper.MainActivity.AppWordsList;

public class HomeFragment extends Fragment {

    View rootView;
    AutoCompleteTextView atocmpWordsSearch;
    private WordsListAdapter mListVadapter;
    private ArrayList<Pair<String, String>> mCustomWordsList;
    public HomeFragment() {
        // Required empty public constructor
        mCustomWordsList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        atocmpWordsSearch = (AutoCompleteTextView)rootView.findViewById(R.id.atocmptxtV_searchForWord);
        initWordsList();
        atocmpWordsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initWordsList();
                editListSearchable(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ((ListView)rootView.findViewById(R.id.lstV_homeWordsList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowWordDetails.class);
                intent.putExtra("wordDetailsTitle", mCustomWordsList.get(i).first);
                intent.putStringArrayListExtra("wordDetailsArray",
                        AppWordsList.getmWordsList().get(mCustomWordsList.get(i).first).getmTraslatedWords());
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void initWordsList() {
        mCustomWordsList = new ArrayList<>(AppWordsList.getmCustomWordList());
        mListVadapter = new WordsListAdapter(getContext(), mCustomWordsList);
        ((ListView)rootView.findViewById(R.id.lstV_homeWordsList)).setAdapter(mListVadapter);
    }

    public void editListSearchable(String searchWord) {
        ArrayList<Pair<String, String>> searchList = new ArrayList<>(mCustomWordsList);
        for(Pair<String, String> word : searchList)
            if(!word.first.contains(searchWord)) mCustomWordsList.remove(word);
        mListVadapter.notifyDataSetChanged();
    }

}
