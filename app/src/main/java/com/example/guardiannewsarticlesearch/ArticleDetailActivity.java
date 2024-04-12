package com.example.guardiannewsarticlesearch;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity to display the details of a specific article.
 * This activity hosts a fragment that shows the article's title, URL, and section name.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout defined in activity_article_detail.xml
        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState == null) {
            // Create a new instance of ArticleDetailFragment
            ArticleDetailFragment fragment = new ArticleDetailFragment();
            // Pass any extras included in the intent to the fragment
            fragment.setArguments(getIntent().getExtras());

            // Begin a fragment transaction to add the fragment to the container
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, fragment);
            ft.commit(); // Commit the transaction to display the fragment
        }
    }
}
