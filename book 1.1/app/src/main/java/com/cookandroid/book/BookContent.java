package com.cookandroid.book;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class BookContent extends TabActivity {

    private TextView textView;
    private ListView listview;
    private ListView memoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_content);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecBook = tabHost.newTabSpec("Book").setIndicator("책 정보");
        tabSpecBook.setContent(R.id.tabBookContent);
        tabHost.addTab(tabSpecBook);

        TabHost.TabSpec tabSpecMemo = tabHost.newTabSpec("Book").setIndicator("책 메모");
        tabSpecMemo.setContent(R.id.tabBookMemo);
        tabHost.addTab(tabSpecMemo);

        tabHost.setCurrentTab(0);


        Button btnEditBook = (Button) findViewById(R.id.btnEditBook);
        btnEditBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBook.class);
                startActivity(intent);
            }
        });

        Button btnDelBook = (Button) findViewById(R.id.btnDelBook);
        btnDelBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final String[] memoTitle = {"안녕하세요", "감사합니다", "안녕하세요", "감사합니다", "안녕하세요", "감사합니다", "안녕하세요", "감사합니다", "안녕하세요", "감사합니다"
                , "안녕하세요", "감사합니다", "안녕하세요", "감사합니다"};

        ListView memoList = (ListView) findViewById(R.id.memoList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, memoTitle);
        memoList.setAdapter(adapter);

        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), memoTitle[arg2], Toast.LENGTH_SHORT).show();

            }
        });

        FloatingActionButton btnAddMemo = (FloatingActionButton) findViewById(R.id.btnAddMemo);
        btnAddMemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMemo.class);
                startActivity(intent);
            }
        });

        textView = findViewById(R.id.memoContent);
        }




}
