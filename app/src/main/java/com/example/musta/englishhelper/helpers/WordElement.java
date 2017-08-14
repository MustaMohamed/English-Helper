package com.example.musta.englishhelper.helpers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by musta on 12/08/17.
 */

public class WordElement implements Serializable {

    private String mSourceEnglishWord;
    private ArrayList<String> mTraslatedWords;

    WordElement(String srcWord, ArrayList<String> words) {
        this.mSourceEnglishWord = srcWord;
        this.mTraslatedWords = new ArrayList<>(words);
    }

    public String getmSourceEnglishWord() {
        return mSourceEnglishWord;
    }

    public void setmSourceEnglishWord(String mSourceEnglishWord) {
        this.mSourceEnglishWord = mSourceEnglishWord;
    }

    public ArrayList<String> getmTraslatedWords() {
        return mTraslatedWords;
    }

    public void setmTraslatedWords(ArrayList<String> mTraslatedWords) {
        this.mTraslatedWords = mTraslatedWords;
    }
}
