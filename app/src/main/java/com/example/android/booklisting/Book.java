package com.example.android.booklisting;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by user on 2017-09-25.
 */

public class Book {

    private String mTitle;
    private ArrayList<String> mAuthors;
    private String mPublisher;
    private String mPublishedDate;
    private int mPageCount;
    private String mCanonicalVolumeLink;
    private double mAverageRating;
    private Drawable mSmallThumbnailLink;

    public Book(String title, ArrayList<String> authors, String publisher,
                String publishedDate,int pageCount, String canonicalVolumeLink,
                double averageRating, Drawable smallThumbnailLink) {

        mTitle = title;
        mAuthors = authors;
        mPublisher = publisher;
        mPublishedDate = publishedDate;
        mPageCount = pageCount;
        mCanonicalVolumeLink = canonicalVolumeLink;
        mAverageRating = averageRating;
        mSmallThumbnailLink = smallThumbnailLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public String getCanonicalVolumeLink() {
        return mCanonicalVolumeLink;
    }

    public double getAverageRating() {
        return mAverageRating;
    }

    public Drawable getSmallThumbnailLink() {
        return mSmallThumbnailLink;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthors=" + mAuthors +
                ", mPublisher='" + mPublisher + '\'' +
                ", mPublishedDate=" + mPublishedDate +
                ", mPageCount=" + mPageCount +
                ", mCanonicalVolumeLink='" + mCanonicalVolumeLink + '\'' +
                ", mAverageRating=" + mAverageRating +
                ", mSmallThumbnail='" + mSmallThumbnailLink + '\'' +
                '}';
    }
}
