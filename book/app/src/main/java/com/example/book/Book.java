package com.example.book;

public class Book {
    private String bScore;
    private String bTitle;
    private String bWriter;
    private String bPublisher;
    private String bStartDate;
    private String bFinishDate;
    private String bComment;
    private byte[] bCover;

    public Book(String bScore, String bTitle, String bWriter, String bPublisher, String bStartDate, String bFinishDate, String bComment, byte[] bCover) {
        this.bScore = bScore;
        this.bTitle = bTitle;
        this.bWriter = bWriter;
        this.bPublisher = bPublisher;
        this.bStartDate = bStartDate;
        this.bFinishDate = bFinishDate;
        this.bComment = bComment;
        this.bCover = bCover;
    }

    public String getbScore() {
        return bScore;
    }

    public void setbScore(String bScore) {
        this.bScore = bScore;
    }

    public String getbTitle() {
        return bTitle;
    }

    public void setbTitle(String bTitle) {
        this.bTitle = bTitle;
    }

    public String getbWriter() {
        return bWriter;
    }

    public void setbWriter(String bWriter) {
        this.bWriter = bWriter;
    }

    public String getbPublisher() {
        return bPublisher;
    }

    public void setbPublisher(String bPublisher) {
        this.bPublisher = bPublisher;
    }

    public String getbStartDate() {
        return bStartDate;
    }

    public void setbStartDate(String bStartDate) {
        this.bStartDate = bStartDate;
    }

    public String getbFinishDate() {
        return bFinishDate;
    }

    public void setbFinishDate(String bFinishDate) {
        this.bFinishDate = bFinishDate;
    }

    public String getbComment() {
        return bComment;
    }

    public void setbComment(String bComment) {
        this.bComment = bComment;
    }

    public byte[] getbCover() {
        return bCover;
    }

    public void setbCover(byte[] bCover) {
        this.bCover = bCover;
    }
}
