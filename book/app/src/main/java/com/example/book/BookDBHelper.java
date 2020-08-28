package com.example.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class BookDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    // 생성자
    public BookDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 최초 DB를 만들때 한번만 호출됨
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
//
        /* 이름: book, 자동으로 값 증가하는 _id 정수형 기본키, bScore 책 평점 문자열 컬럼, bTitle 책 제목 문자열 컬럼, bWriter 저자 문자열 컬럼,
        bPublisher 출판사 문자열 컬럼, bSatrtDate 읽기 시작한 날 문자열 컬럼, bFinishDate 다 읽은 날 문자열 컬럼, bComment 한줄평 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE book (_id INTEGER PRIMARY KEY AUTOINCREMENT, bScore TEXT, bTitle TEXT, bWriter TEXT, " +
            "bPublisher TEXT, bStartDate TEXT, bFinishDate TEXT, bComment TEXT, bCover TEXT)");
//        , bDate Text not null
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DB_VERSION) {
            db.execSQL("DROP TABLE book");
            onCreate(db);
        }
//        db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
//        onCreate(db);
    }

    public void bInsert(String bScore, String bTitle, String bWriter, String bPublisher, String bStartDate, String bFinishDate, String bComment, String bCover) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO book VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement sqLiteStatement = db.compileStatement(query);
        // DB에 입력한 값으로 행 추가

        sqLiteStatement.bindString(2, bScore);
        sqLiteStatement.bindString(3, bTitle);
        sqLiteStatement.bindString(4, bWriter);
        sqLiteStatement.bindString(5, bPublisher);
        sqLiteStatement.bindString(6, bStartDate);
        sqLiteStatement.bindString(7, bFinishDate);
        sqLiteStatement.bindString(8, bComment);
        sqLiteStatement.bindString(9, bCover);

        sqLiteStatement.execute();

        db.close();

    }

    public void bUpdate(String bScore, String bTitle, String bWriter, String bPublisher, String bStartDate, String bFinishDate, String bComment, String bCover) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE book SET bScore='" + bScore + "', bTitle='" + bTitle + "', bWriter='" + bWriter + "', bPublisher='" + bPublisher + "'," +
                " bStartDate='" + bStartDate + "', bFinishDate='" + bFinishDate + "', bComment='" + bComment + "', bCover='" + bCover + "';");
        db.close();
    }

    public void bDelete(String bTitle) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM book WHERE bTitle= '" + bTitle + "' ;");
        db.close();
    }

    public ArrayList<BookCoverList> bGetBookCoverScore() {
        SQLiteDatabase db = getReadableDatabase();
        String bScore = null;
        String bCover = null;
        ArrayList<BookCoverList> list = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT bScore, bCover  FROM book", null);

        while (cursor.moveToNext()) {
            bScore = cursor.getString(0);
            bCover = cursor.getString(1);

            BookCoverList results = new BookCoverList(bScore, bCover);
            list.add(results);
        }
        return list;
    }

    public ArrayList<Book> bGetBook() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String bScore = null;
        String bTitle = null;
        String bWriter = null;
        String bPublisher = null;
        String bStartDate = null;
        String bFinishDate = null;
        String bComment = null;
        String bCover = null;
        ArrayList<Book> list = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT *  FROM book", null);

        while (cursor.moveToNext()) {
            bScore = cursor.getString(1);
            bTitle = cursor.getString(2);
            bWriter = cursor.getString(3);
            bPublisher = cursor.getString(4);
            bStartDate = cursor.getString(5);
            bFinishDate = cursor.getString(6);
            bComment = cursor.getString(7);
            bCover = cursor.getString(8);

            Book results = new Book(bScore, bTitle, bWriter, bPublisher, bStartDate, bFinishDate, bComment, bCover);
            list.add(results);
        }
        return list;
    }
}