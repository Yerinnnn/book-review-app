package com.example.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private ArrayList<Memo> mList;

    public interface OnItemListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private MemoAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(MemoAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class MemoViewHolder extends RecyclerView.ViewHolder {
        protected TextView memoTitle;
        protected TextView memoContent;
        protected TextView memoDate;

        public MemoViewHolder(final View view) {
            super(view);

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.memoTitle = view.findViewById(R.id.memoTitle);
            this.memoContent = view.findViewById(R.id.memoContent);
            this.memoDate = view.findViewById(R.id.memoDate);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MemoAdapter(ArrayList<Memo> list) {
        mList = list;
    }

    @Override
    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list_item, parent, false);

        MemoViewHolder viewHolder = new MemoViewHolder(view);

        return viewHolder;
    }

    // 실제 각 뷰 홀더에 데이터를 연결해줌
    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int position) {
//        memoViewHolder.memoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
//        memoViewHolder.memoDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//        memoViewHolder.memoContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        memoViewHolder.memoTitle.setText(mList.get(position).getmContent());
        memoViewHolder.memoDate.setText(mList.get(position).getmDate());
        memoViewHolder.memoContent.setText(mList.get(position).getmContent());
    }

    private void removeItemView(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size()); // 지워진 만큼 다시 채워넣기.
    }

    @Override
    public int getItemCount() {
        return mList.size();
//        return mList == null ? 0 : mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
