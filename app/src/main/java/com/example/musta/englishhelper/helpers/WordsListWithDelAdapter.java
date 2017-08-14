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
import android.widget.Toast;

import com.example.musta.englishhelper.R;

import java.util.ArrayList;

/**
 * Created by musta on 12/08/17.
 */

public class WordsListWithDelAdapter extends ArrayAdapter<Pair<String, String> > {

    public WordsListWithDelAdapter(Context context, ArrayList<Pair<String, String> > Words) {
        super(context, 0, Words);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemsView = convertView;
        if(listItemsView == null) {
            listItemsView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_listview_with_addnewword, parent, false);
        }

        Pair<String, String> wordItem = getItem(position);
        ((TextView)listItemsView.findViewById(R.id.txtV_wordTitle)).setText(wordItem.first);
        ((TextView)listItemsView.findViewById(R.id.txtV_wordMean)).setText(wordItem.second);
        (listItemsView.findViewById(R.id.icn_deleteWord)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Delete Selectes", Toast.LENGTH_SHORT).show();
            }
        });
        return listItemsView;

    }

}
