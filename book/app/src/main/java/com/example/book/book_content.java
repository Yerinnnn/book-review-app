package com.example.book;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class book_content extends Fragment {

    private ImageView bookCover;
    private RatingBar bookRatingBar;
    private TextView bookScore;
    private TextView bookTitle;
    private TextView bookWriter;
    private TextView bookPublisher;
    private TextView bookStartDate;
    private TextView bookFinishDate;
    private TextView bookComment;

    private byte[] bCover;
    private String bScore;
    private String bTitle;
    private String bWriter;
    private String bPublisher;
    private String bStartDate;
    private String bFinishDate;
    private String bComment;

    private Bitmap bitmap;

    public book_content() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_content, container, false);

        bookCover = view.findViewById(R.id.bookCover);
        bookRatingBar = view.findViewById(R.id.ratingBar);
        bookScore = view.findViewById(R.id.bookScore);
        bookTitle = view.findViewById(R.id.bookTitle);
        bookWriter = view.findViewById(R.id.bookWriter);
        bookPublisher = view.findViewById(R.id.bookPublisher);
        bookStartDate = view.findViewById(R.id.bookStartDate);
        bookFinishDate = view.findViewById(R.id.bookFinishDate);
        bookComment = view.findViewById(R.id.bookComment);

        bCover = getArguments().getByteArray("bCover");
        bScore = getArguments().getString("bScore");
        bTitle = getArguments().getString("bTitle");
        bWriter = getArguments().getString("bWriter");
        bPublisher = getArguments().getString("bPublisher");
        bStartDate = getArguments().getString("bStartDate");
        bFinishDate = getArguments().getString("bFinishDate");
        bComment = getArguments().getString("bComment");

        Log.d(TAG, "book_content 79 getArguments() bCover: " + bCover + bTitle + "============================================================================");

//        bitmap = BitmapFactory.decodeByteArray(bCover, 0, bCover.length);
        bookCover.setImageBitmap(BitmapFactory.decodeByteArray(bCover, 0, bCover.length));
        bookRatingBar.setRating(Float.parseFloat(bScore));
        bookScore.setText(bScore);
        bookTitle.setText(bTitle);
        bookWriter.setText(bWriter);
        bookPublisher.setText(bPublisher);
        bookStartDate.setText(bStartDate);
        bookFinishDate.setText(bFinishDate);
        bookComment.setText(bComment);
        Log.d(TAG, "book_content 91 bCover: " + bCover + "**********************************************************************************");

        Button btnEditBook = (Button) view.findViewById(R.id.btnEditBook);
        btnEditBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditBookActivity.class);

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

        Button btnDelBook = (Button) view.findViewById(R.id.btnDelBook);
        btnDelBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("정말로 삭제하시겠습니까?");
                dialog.setMessage("책 정보와 등록된 메모들이 모두 삭제됩니다.");
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookDBHelper bookDBHelper = new BookDBHelper(getContext(), "book", null, 1);
                        MemoDBHelper memoDBHelper = new MemoDBHelper(getContext(), "memo", null, 1);

                        bookDBHelper.bDelete(bTitle);
                        memoDBHelper.mDeleteAll(bTitle);

                        // 삭제 후 메인 페이지로 이동
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(getContext(), "모두 삭제되었습니다.", Toast.LENGTH_LONG).show();
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

        return view;
    }
}
