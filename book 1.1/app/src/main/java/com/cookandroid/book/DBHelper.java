package com.cookandroid.book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public DBHelper(View.OnClickListener context) {
        super((Context) context, "bookDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String bookSQL = "CREATE TABLE bookData" + "(bookID INTEGER(10) PRIMARY KEY," + "bookDate,"
                +"bookCover BLOB," +"bookTitle," + "bookScore," + "writer," + "publisher,"
                + "readingStartDate," + "readingFinishDate," + "comment)";
//        String memoSQL = "CREATE TABLE memoData" + "(memoID INTEGER(10) PRIMARY KEY," + "memoDate,"
//                +"bookTitle," + "bookCover," + "bookTitle," + "bookScore," + "writer," + "publisher," + "memoContent)";
        db.execSQL(bookSQL);
//        db.execSQL(memoSQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            db.execSQL("drop table bookData");
            onCreate(db);
        }
    }
}
