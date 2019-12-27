package com.example.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import static com.gun0912.tedpermission.TedPermission.TAG;

//import static com.google.android.gms.vision.L.TAG;

public class MemoDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    // 생성자
    public MemoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 최초 DB를 만들때 한번만 호출됨
    @Override
    public void onCreate(SQLiteDatabase db) {

        // foreign key 허용
//        if (!db.isReadOnly()) {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
//                db.execSQL("PRAGMA foreign_keys = ON;");
//            } else {
//                db.setForeignKeyConstraintsEnabled(true);
//            }
//        }
        if (!db.isReadOnly()) {

            // Enable foreign key constraints

            db.execSQL("PRAGMA foreign_keys=ON;");

        }

        Log.d(TAG, "onCreate: 46 foreign_key 허용 ==================================================================================");


        // 새로운 테이블 생성
        // 이름: memo, 자동으로 값 증가하는 _id 정수형 기본키, item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성
        db.execSQL("CREATE TABLE memo (_id INTEGER PRIMARY KEY AUTOINCREMENT, mContent TEXT, mDate TEXT not null, bTitle TEXT);");
//        , FOREIGN KEY(bTitle) REFERENCES book(bTitle)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DB_VERSION) {
            db.execSQL("DROP TABLE memo");
            onCreate(db);
        }
//        db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
//        onCreate(db);
    }

    public void mInsert(String mContent, String bTitle) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO memo VALUES(null, '" + mContent + "', datetime('now', 'localtime'), '" + bTitle + "');");
        db.close();
    }

    public void mUpdate(String mContent, String bTitle) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE memo SET mContent='" + mContent + "', mDate=datetime('now', 'localtime') WHERE bTitle='" + bTitle + "';");
        db.close();
    }

    public void mDeleteOne(String mContent) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM memo WHERE mContent='" + mContent + "';");
        db.close();
    }

    public void mDeleteAll(String bTitle) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM memo Where bTitle= '" + bTitle + "';");
        db.close();
    }

    public ArrayList<Memo> mGetMemo(String bTitle) {
        Log.d(TAG, "mGetMemo: 88 mGetMemo(String bTitle) ========================================================================" + bTitle);

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String mContent = null;
        String mDate = null;
        ArrayList<Memo> list = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE bTitle = '" + bTitle + "';", null);

        while (cursor.moveToNext()) {
            mContent = cursor.getString(1);
            mDate = cursor.getString(2);

            Memo results = new Memo(mContent, mDate);
            list.add(results);
        }
        return list;
    }
}


