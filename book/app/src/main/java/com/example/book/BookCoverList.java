package com.example.book;


public class BookCoverList {
    private String bScore;
    private byte[] bCover;

    public BookCoverList(String bScore, byte[] bCover) {
        this.bScore = bScore;
        this.bCover = bCover;
    }

    public String getbScore() {
        return bScore;
    }

    public void setbScore(String bScore) {
        this.bScore = bScore;
    }

    public byte[] getbCover() {
        return bCover;
    }

    public void setbCover(byte[] bCover) {
        this.bCover = bCover;
    }
}
