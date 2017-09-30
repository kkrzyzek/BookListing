package com.example.android.booklisting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-09-25.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, ArrayList<Book>books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.single_book_view, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.book_title);
        titleText.setText(currentBook.getTitle());

        TextView authorText = (TextView) listItemView.findViewById(R.id.book_author);

        ArrayList<String> authorTextArray = currentBook.getAuthors();
        String authors = formatAuthors(authorTextArray);
        authorText.setText(authors);

        TextView publisherNameAndDateText = (TextView) listItemView.
                findViewById(R.id.book_publisher_name_and_date);
        String publisherDataText = currentBook.getPublisher() + ", " +
                currentBook.getPublishedDate();
        publisherNameAndDateText.setText(publisherDataText);

        TextView pageCountText = (TextView) listItemView.findViewById(R.id.book_page_count);
        pageCountText.setText("" + currentBook.getPageCount());

        RatingBar ratingBar = (RatingBar) listItemView.findViewById(R.id.rating_bar_view);
        float ratingData = (float) currentBook.getAverageRating();
        ratingBar.setRating(ratingData);

        ImageView thumbnailImage = (ImageView) listItemView.findViewById(R.id.imageView);
        Drawable image = currentBook.getSmallThumbnailLink();
        thumbnailImage.setImageDrawable(image);

        return listItemView;
    }

    private String formatAuthors (ArrayList<String> authors) {
        String authorTextTemp = authors.get(0);
        if (authors.size()>1){
            for(int i=1; i<authors.size(); i++) {
                authorTextTemp += ", " + authors.get(i);
            }
        }
        return authorTextTemp;
    }

}