package com.example.book;

public class Memo {
    private String mContent;
    private String mDate;


    public Memo(String mContent, String mDate) {
        this.mContent = mContent;
        this.mDate = mDate;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
