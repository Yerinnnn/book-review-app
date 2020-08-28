package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class MemoContent extends Activity {
    private String mDate;
    private String mContent;

    public String bCover;
    public String bScore;
    public String bTitle;
    public String bWriter;
    public String bPublisher;
    public String bStartDate;
    public String bFinishDate;
    public String bComment;

    private TextView bookTitle;
    private TextView memoDate;
    private TextView memoContent;
    private ImageView btnShareMemo;
    private Button btnDelMemo;
    private Button btnEditMemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_content);

        bCover = getIntent().getStringExtra("bCover");
        bScore = getIntent().getStringExtra("bScore");
        bTitle = getIntent().getStringExtra("bTitle");
        bWriter = getIntent().getStringExtra("bWriter");
        bPublisher = getIntent().getStringExtra("bPublisher");
        bStartDate = getIntent().getStringExtra("bStartDate");
        bFinishDate = getIntent().getStringExtra("bFinishDate");
        bComment = getIntent().getStringExtra("bComment");

        mDate = getIntent().getStringExtra("mDate");
        mContent = getIntent().getStringExtra("mContent");

        Log.d(TAG, "MemoContent 57 bCover: " + bCover + "==================================================###########################################3");

        bookTitle = findViewById(R.id.bookTitle);
        memoDate = findViewById(R.id.memoDate);
        memoContent = findViewById(R.id.memoContent);
        btnShareMemo = findViewById(R.id.btnShareMemo);
        btnDelMemo = findViewById(R.id.btnDelMemo);
        btnEditMemo = findViewById(R.id.btnEditMemo);

        bookTitle.setText(bTitle);
        memoDate.setText(mDate);
        memoContent.setText(mContent);

        // 공유하기 버튼 btnShareMemo 클릭 시
        btnShareMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/plain");

                // Set default text message
                // String subject = "문자의 제목";
                String text = mContent;
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);

                // Title of intent
                Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                startActivity(chooser);
            }
        });

        // 삭제하기 버튼 btnDelMemo 클릭 시
        btnDelMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MemoContent.this);
                dialog.setTitle("메모 삭제하기");
                dialog.setMessage("정말로 삭제하시겠습니까?");
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MemoDBHelper memoDBHelper = new MemoDBHelper(getApplicationContext(), "memo", null, 1);
                        memoDBHelper.mDeleteOne(mContent);

                        // 삭제 후 BookContent.class로 이동
                        Intent intent = new Intent(getApplicationContext(), BookContent.class);

                        Log.d(TAG, "MemoContent 106 ==========================================================================####################" + bCover);

                        intent.putExtra("bCover", bCover);
                        intent.putExtra("bScore", bScore);
                        intent.putExtra("bTitle", bTitle);
                        intent.putExtra("bWriter", bWriter);
                        intent.putExtra("bPublisher", bPublisher);
                        intent.putExtra("bStartDate", bStartDate);
                        intent.putExtra("bFinishDate", bFinishDate);
                        intent.putExtra("bComment", bComment);

                        Log.d(TAG, "MemoContent 117 =====================================================================#######################" + bCover);

                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        // 편집 버튼 btnEditMemo 클릭 시
        btnEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMemoActivity.class);

                intent.putExtra("mContent", mContent);

                intent.putExtra("bCover", bCover);
                intent.putExtra("bScore", bScore);
                intent.putExtra("bTitle", bTitle);
                intent.putExtra("bWriter", bWriter);
                intent.putExtra("bPublisher", bPublisher);
                intent.putExtra("bStartDate", bStartDate);
                intent.putExtra("bFinishDate", bFinishDate);
                intent.putExtra("bComment", bComment);

                startActivity(intent);
            }
        });
    }
}
