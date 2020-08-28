package com.example.book;


import android.net.Uri;

public class BookCoverList {
    private String bScore;
    private String bCover;

    public BookCoverList(String bScore, String bCover) {
        this.bScore = bScore;
        this.bCover = bCover;
    }

    public String getbScore() {
        return bScore;
    }

    public void setbScore(String bScore) {
        this.bScore = bScore;
    }

    public String getbCover() {
        return bCover;
    }

    public void setbCover(String bCover) {
        this.bCover = bCover;
    }
}
