package com.example.book;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class memo_list extends Fragment {
    private ArrayList<Memo> mArrayList;
    private MemoAdapter mAdapter;

    private static final String TAG = "memo_list";

    public String bCover;
    public String bScore;
    public String bTitle;
    public String bWriter;
    public String bPublisher;
    public String bStartDate;
    public String bFinishDate;
    public String bComment;

    private String mDate;
    private String mContent;


    public memo_list() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_list, container, false);

        MemoDBHelper memoDbHelper = new MemoDBHelper(getContext(), "memo", null, 1);

        // 책 정보 가져오기
        bCover = getArguments().getString("bCover");
        bScore = getArguments().getString("bScore");
        bTitle = getArguments().getString("bTitle");
        bWriter = getArguments().getString("bWriter");
        bPublisher = getArguments().getString("bPublisher");
        bStartDate = getArguments().getString("bStartDate");
        bFinishDate = getArguments().getString("bFinishDate");
        bComment = getArguments().getString("bComment");

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        mArrayList = new ArrayList<>();
        mArrayList = memoDbHelper.mGetMemo(bTitle);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.memoList);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        //수직 정렬
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 MemoAdapter 객체 지정.
        mAdapter = new MemoAdapter(mArrayList);

        //memo_list.xml의 memoList에 adapter를 설정해줌
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), MemoContent.class);

                mContent = mArrayList.get(position).getmContent();
                mDate = mArrayList.get(position).getmDate();

                intent.putExtra("mContent", mContent);
                intent.putExtra("mDate", mDate);
                intent.putExtra("bTitle", bTitle);

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

        FloatingActionButton btnAddMemo = view.findViewById(R.id.btnAddMemo);
        btnAddMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMemoActivity.class);

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


        // Inflate the layout for this fragment
        return view;
    }


}
