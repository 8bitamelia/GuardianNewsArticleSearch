package com.example.guardiannewsarticlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

/**
 * The main activity of the app, serving as the entry point and the main interface.
 * It provides a search functionality, access to favorites, and navigation through a drawer.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private EditText searchText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize search text field and progress bar
        searchText = findViewById(R.id.editTextSearch);
        progressBar = findViewById(R.id.progressBar);

        // Setup search button and its click listener
        Button searchButton = findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(v -> performSearch());

        // Setup drawer and navigation view
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Handle home navigation or refresh
            } else if (id == R.id.nav_favorites) {
                // Navigate to FavoritesActivity to view saved articles
                startActivity(new Intent(this, FavoritesActivity.class));
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Initiates a search operation by fetching the text from the search field and starting the ArticleListActivity.
     */
    private void performSearch() {
        progressBar.setVisibility(View.VISIBLE);
        String query = searchText.getText().toString();
        Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            // Show help information in a dialog
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.help_content))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
