package com.example.guardiannewsarticlesearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment to display the details of an article.
 * This fragment shows the article's title, URL, and section, and allows adding it to favorites.
 */
public class ArticleDetailFragment extends Fragment {

    /**
     * Constructor for the fragment. Required for Android's fragment management.
     */
    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, using the fragment_article_detail XML layout file
        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        // Find and store references to TextViews and Button from the inflated layout
        TextView titleView = rootView.findViewById(R.id.title);
        TextView urlView = rootView.findViewById(R.id.url);
        TextView sectionView = rootView.findViewById(R.id.section);
        Button addToFavoritesButton = rootView.findViewById(R.id.buttonAddToFavorites);

        // Retrieve article details passed from activity through fragment arguments
        Bundle args = getArguments();
        String title = args.getString("ARTICLE_TITLE", "");
        String url = args.getString("ARTICLE_URL", "");
        String section = args.getString("ARTICLE_SECTION", "");

        // Set the retrieved details into the corresponding TextViews
        titleView.setText(title);
        urlView.setText(url);
        sectionView.setText(section);

        // Set up a click listener on the URL text view to open the article in a web browser
        urlView.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        // Set up a click listener on the 'Add to Favorites' button to save the article
        addToFavoritesButton.setOnClickListener(view -> addToFavorites(title, url, section, view));

        return rootView;
    }

    /**
     * Adds the current article to the favorites database.
     * @param title Title of the article.
     * @param url URL of the article.
     * @param section Section of the article.
     * @param view The view that was clicked, used for showing the Snackbar.
     */
    private void addToFavorites(String title, String url, String section, View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Article article = new Article(title, url, section);
        dbHelper.addFavorite(article);
        // Show a Snackbar notification that the article was added to favorites
        Snackbar.make(view, "Article added to favorites", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
