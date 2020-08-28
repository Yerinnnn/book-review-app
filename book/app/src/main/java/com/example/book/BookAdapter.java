package com.example.book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    //item click 시

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener bListener = null;
    private OnItemLongClickListener bLongListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.bListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.bLongListener = listener;
    }

    private ArrayList<BookCoverList> bList;

    public class BookViewHolder extends RecyclerView.ViewHolder {
        protected TextView bookScore;
        protected ImageView bookCover;
        protected CheckBox checkbox;

        public BookViewHolder(final View view) {
            super(view);

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.bookCover = view.findViewById(R.id.bookCover);
            this.bookScore = view.findViewById(R.id.bookScore);
            this.checkbox = view.findViewById(R.id.checkbox);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        bListener.onItemClick(v, position);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        bLongListener.onItemLongClick(v, position);

//                        checkbox.setVisibility();
                    }

                    return true;
                }
            });
        }
    }

    public BookAdapter(ArrayList<BookCoverList> list) {
        bList = list;
    }


    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);

        BookViewHolder viewHolder = new BookViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookScore.setText(bList.get(position).getbScore());
        bookViewHolder.bookCover.setImageURI(Uri.parse(bList.get(position).getbCover()));
}

    @Override
    public int getItemCount() {
        return bList  == null ? 0 : bList.size();
    }

}
