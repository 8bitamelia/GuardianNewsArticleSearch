package com.example.guardiannewsarticlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * FavoritesActivity is an activity that displays a list of favorite articles.
 * Users can view details of a favorite article or remove an article from favorites.
 */
public class FavoritesActivity extends AppCompatActivity {
    private ListView listViewFavorites;
    private ArrayAdapter<String> adapter;
    private List<Article> favoritesList;
    private DatabaseHelper dbHelper;

    /**
     * This method initializes the activity.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        listViewFavorites = findViewById(R.id.listViewFavorites);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewFavorites.setAdapter(adapter);

        dbHelper = new DatabaseHelper(this);
        loadFavorites();

        // Implement item click listener to view article details
        listViewFavorites.setOnItemClickListener((parent, view, position, id) -> {
            Article selectedArticle = favoritesList.get(position);
            Intent detailIntent = new Intent(FavoritesActivity.this, ArticleDetailActivity.class);
            detailIntent.putExtra("ARTICLE_TITLE", selectedArticle.getTitle());
            detailIntent.putExtra("ARTICLE_URL", selectedArticle.getUrl());
            detailIntent.putExtra("ARTICLE_SECTION", selectedArticle.getSectionName());
            startActivity(detailIntent);
        });

        // Implement item long click listener for deletion
        listViewFavorites.setOnItemLongClickListener((parent, view, position, id) -> {
            Article article = favoritesList.get(position);
            dbHelper.removeFavorite(article.getTitle());
            favoritesList.remove(position); // Remove the article from the local list
            adapter.remove(adapter.getItem(position)); // Update the adapter
            adapter.notifyDataSetChanged(); // Refresh the ListView
            Toast.makeText(FavoritesActivity.this, "Article removed from favorites", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    /**
     * This method loads the favorite articles from the database into the ListView.
     */
    private void loadFavorites() {
        favoritesList = dbHelper.getAllFavorites(); // Get the favorites from the database
        List<String> titles = new ArrayList<>();
        for (Article article : favoritesList) {
            titles.add(article.getTitle());
        }
        adapter.clear();
        adapter.addAll(titles);
        adapter.notifyDataSetChanged();
    }
}
