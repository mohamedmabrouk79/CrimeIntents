package com.example.mohamedmabrouk.crimeintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mohamed Mabrouk on 26/03/2016.
 */
public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSusbect;
    private Date mTime;
    public Crime(){
        this(UUID.randomUUID());
    }
    public Crime(UUID uuid){
        this.mID=uuid;
        mDate=new Date();
        mTime=new Date();
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date mTime) {
        this.mTime = mTime;
    }

    public String getPhotoFilename() {
        return "IMG_" + getID().toString() + ".jpg";
    }
    public String getSusbect() {
        return mSusbect;
    }

    public void setSusbect(String mSusbect) {
        this.mSusbect = mSusbect;
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }
}
