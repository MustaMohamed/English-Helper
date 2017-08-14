package com.example.musta.englishhelper.helpers;

import android.provider.BaseColumns;

/**
 * Created by musta on 12/08/17.
 */

public final class WordDataBaseContract {
    public WordDataBaseContract() {}

    public static abstract class WordDataBaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "listofwords";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_LIST_NAME = "listname";
        public static final String COLUMN_LIST_ELEMENT = "listelement";
    }
}
