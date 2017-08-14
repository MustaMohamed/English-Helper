package com.example.musta.englishhelper.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.musta.englishhelper.R;

import java.util.ArrayList;

/**
 * Created by musta on 12/08/17.
 */

public class WordsListAdapter extends ArrayAdapter<Pair<String, String> > {

    public WordsListAdapter(Context context, ArrayList<Pair<String, String> > Words) {
        super(context, 0, Words);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemsView = convertView;
        if(listItemsView == null) {
            listItemsView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_word_listview, parent, false);
        }

        Pair<String, String> wordItem = getItem(position);
        ((TextView)listItemsView.findViewById(R.id.txtV_wordTitle)).setText(wordItem.first);
        ((TextView)listItemsView.findViewById(R.id.txtV_wordMean)).setText(wordItem.second);
        return listItemsView;

    }

}
