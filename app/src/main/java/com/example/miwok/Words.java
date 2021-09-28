package com.example.miwok;

public class Words {

    private String mDefaultTranslations;
    private String mMiwokTranslations;
    private int mImageRerourceID = NO_IMAGE_PROVIDED;
    private int mSoundResourceID;

    private static final int NO_IMAGE_PROVIDED = -1;


    public Words(String defaultTranslations, String miwokTranslations, int imageResourceID, int soundResourceID){
        mMiwokTranslations = miwokTranslations;
        mDefaultTranslations = defaultTranslations;
        mImageRerourceID = imageResourceID;
        mSoundResourceID = soundResourceID;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslations;
    }

    public String  getEnglishTranslation() {
        return mDefaultTranslations;
    }

    public int getImageResourceID() { return mImageRerourceID;}

    public boolean hasImage() {
        if(mImageRerourceID != NO_IMAGE_PROVIDED)
            return true;
        else
            return false;
    }

    public int getmSoundResourceID() { return mSoundResourceID; }
}
