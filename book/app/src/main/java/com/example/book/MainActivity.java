package com.example.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Animation fab_open, fab_close, rotate_clockwise, rotate_anticlockwise;   // floating button animation
    private Boolean isFabOpen = false;
    private FloatingActionButton btnAddBook, btnSearch, btnWrite;

    private static final String TAG = "MainActivity";

    private Boolean isPermission = true;

    private BookAdapter bAdapter;
    private ArrayList<BookCoverList> bBookCoverList;
    private ArrayList<Book> bArrayList;

    private String bCover;
    private String bScore;
    private String bTitle;
    private String bWriter;
    private String bPublisher;
    private String bStartDate;
    private String bFinishDate;
    private String bComment;

    private TextView firstMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setTitle("book");

//        tedPermission();

        final BookDBHelper bookDbHelper = new BookDBHelper(getApplicationContext(), "book", null, 1);

        bBookCoverList = bookDbHelper.bGetBookCoverScore();

        firstMessage = findViewById(R.id.firstMessage);
        if (bBookCoverList != null) {
            firstMessage.setText("");
        } else if (bBookCoverList == null) {
            firstMessage.setText("아직 등록된 리뷰가 없습니다. \n아래 버튼을 눌러 읽은 책 정보와 리뷰를 등록해보세요 :)");
        }

        // 리사이클러뷰에 GridLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.bookList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // 리사이클러뷰에 BookAdapter 객체 지정.
        bAdapter = new BookAdapter(bBookCoverList);

        // memo_list.xml의 memoList에 adapter를 설정해줌
        recyclerView.setAdapter(bAdapter);


        // 어댑터 아이템 클릭 시 뷰페이저 실행하는 BookContent 액티비티로 이동
        bAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), BookContent.class);

                // 책 정보를 받아서 bArrayList에 저장
                bArrayList = bookDbHelper.bGetBook();

                bScore = bArrayList.get(position).getbScore();
                bTitle = bArrayList.get(position).getbTitle();
                bWriter = bArrayList.get(position).getbWriter();
                bPublisher = bArrayList.get(position).getbPublisher();
                bStartDate = bArrayList.get(position).getbStartDate();
                bFinishDate = bArrayList.get(position).getbFinishDate();
                bComment = bArrayList.get(position).getbComment();
                bCover = bArrayList.get(position).getbCover();

                Log.d(TAG, "onItemClick: bGetBook ========================================================================================" + bTitle);

                intent.putExtra("bCover", bCover);
                intent.putExtra("bScore", bScore);
                intent.putExtra("bTitle", bTitle);
                intent.putExtra("bWriter", bWriter);
                intent.putExtra("bPublisher", bPublisher);
                intent.putExtra("bStartDate", bStartDate);
                intent.putExtra("bFinishDate", bFinishDate);
                intent.putExtra("bComment", bComment);

                Log.d(TAG, "onItemClick: putExtra ==========================================================================================" + bTitle);

                startActivity(intent);
            }
        });

        // 어댑터 아이템 롱 클릭시
        bAdapter.setOnItemLongClickListener(new BookAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

        // 플로팅 버튼에 대한 변수
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.book_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.book_fab_close);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        btnAddBook = findViewById(R.id.btnAddBook);
        btnSearch = findViewById(R.id.btnSearch);
        btnWrite = findViewById(R.id.btnWrite);

        // 플로팅 버튼 클릭 시 동작
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim();
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim();
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim();
            }
        });
    }

    public void anim() {
        if (isFabOpen) {
            btnWrite.startAnimation(fab_close);
            btnSearch.startAnimation(fab_close);
            btnAddBook.startAnimation(rotate_anticlockwise);
            btnWrite.setClickable(false);
            btnSearch.setClickable(false);
            isFabOpen = false;

        } else {
            btnWrite.startAnimation(fab_open);
            btnSearch.startAnimation(fab_open);
            btnAddBook.startAnimation(rotate_clockwise);
            btnWrite.setClickable(true);
            btnSearch.setClickable(true);
            isFabOpen = true;
        }
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
//                .setRationaleMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
//                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}
