package com.example.musta.englishhelper.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by musta on 12/08/17.
 */

public class WordDataBaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "wordDataBase.db";
    private Context mContext;
    public WordDataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + WordDataBaseContract.WordDataBaseEntry.TABLE_NAME + " ("
                + WordDataBaseContract.WordDataBaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_NAME + " TEXT NOT NULL, "
                + WordDataBaseContract.WordDataBaseEntry.COLUMN_LIST_ELEMENT+ " TEXT);";
        sqLiteDB.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
