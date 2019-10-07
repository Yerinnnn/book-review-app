package com.cookandroid.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("book");

        final GridView gv = (GridView) findViewById(R.id.gridView1);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);
    }

    public class MyGridAdapter extends BaseAdapter {
            Context context;
            public MyGridAdapter(Context c) {
                context = c;
            }
            public int getCount() {
                return coverID.length;
            }
            public Object getItem(int arg0) {
                return null;
            }
            public long getItemId(int arg0) {
                return 0;
            }


            //sql
            DBHelper helper = new DBHelper((View.OnClickListener) this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT bookCover FROM bookData + order by bookID desc limit 1", null);





            Integer[] coverID = {
                R.drawable.book01, R.drawable.book02, R.drawable.book01, R.drawable.book02, R.drawable.book01, R.drawable.book02, R.drawable.book01, R.drawable.book02,
                    R.drawable.book01, R.drawable.book02, R.drawable.book01, R.drawable.book02, R.drawable.book01, R.drawable.book02
            };
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageview = new ImageView(context);
                imageview.setLayoutParams(new GridView.LayoutParams(250, 350));
                imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageview.setPadding(25, 25, 25, 25);

                imageview.setImageResource(coverID[position]);

                imageview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), BookContent.class);
                        startActivity(intent);
                    }
                });

                Button btnAddBook = (Button) findViewById(R.id.btnAddBook);
                btnAddBook.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AddBook.class);
                        startActivity(intent);
                    }
                });

                return imageview;
            }
    }
}
