package com.example.musta.englishhelper.helpers;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by musta on 12/08/17.
 */

public class WordsList implements Comparable{
    private TreeMap<String, WordElement> mWordsList;
    private ArrayList<Pair<String, String>> mCustomWordList;
    private ArrayList<String> mEglishWordsList;
    private String mListName;
    public WordsList(TreeMap<String, WordElement> wordsList) {
        this.mWordsList = new TreeMap<>(wordsList);
        finish();
    }

    public WordsList() {
        this.mWordsList = new TreeMap<>();
    }

    public void finish() {
        initCustomList();
    }

    public String getmListName() {
        return mListName;
    }

    public void setmListName(String mListName) {
        this.mListName = mListName;
    }

    public ArrayList<Pair<String, String>> getmCustomWordList() {
        return mCustomWordList;
    }

    public ArrayList<String> getmEglishWordsList() {
        return mEglishWordsList;
    }

    private void initCustomList() {
        mCustomWordList = new ArrayList<>();
        mEglishWordsList = new ArrayList<>();
        for(Map.Entry<String, WordElement > entry : mWordsList.entrySet()) {
            mCustomWordList.add(Pair.create(entry.getKey(),
                    entry.getValue().getmTraslatedWords().get(0)));
            mEglishWordsList.add(entry.getKey());
        }
    }

    public TreeMap<String, WordElement> getmWordsList() {
        return mWordsList;
    }

    public void setmWordsList(TreeMap<String, WordElement> mWordsList) {
        this.mWordsList = mWordsList;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!WordsList.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final WordsList other = (WordsList) obj;
        if ((this.mListName == null) ? (other.getmListName() != null) : !this.mListName.equals(other.getmListName())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        if (obj == null) {
            return -1;
        }
        if (!WordsList.class.isAssignableFrom(obj.getClass())) {
            return -1;
        }
        final WordsList other = (WordsList) obj;
        return this.mListName.compareTo(((WordsList) obj).getmListName());
    }
}
