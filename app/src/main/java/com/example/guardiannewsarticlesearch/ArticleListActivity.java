package com.example.guardiannewsarticlesearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays a list of articles based on a search query.
 * Users can tap on any article to view its detailed information in another activity.
 */
public class ArticleListActivity extends AppCompatActivity {

    private ListView listView;
    private List<Article> articleList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        listView = findViewById(R.id.listViewArticles);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        // Set up item click listener to open ArticleDetailActivity with details of the clicked article
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Article selectedArticle = articleList.get(position);
            Intent detailIntent = new Intent(ArticleListActivity.this, ArticleDetailActivity.class);
            detailIntent.putExtra("ARTICLE_TITLE", selectedArticle.getTitle());
            detailIntent.putExtra("ARTICLE_URL", selectedArticle.getUrl());
            detailIntent.putExtra("ARTICLE_SECTION", selectedArticle.getSectionName());
            startActivity(detailIntent);
        });

        // Fetch articles based on the search query provided in the intent
        String query = getIntent().getStringExtra("SEARCH_QUERY");
        new FetchArticlesTask().execute(query);
    }

    /**
     * AsyncTask to fetch articles from The Guardian API.
     */
    private class FetchArticlesTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(String... strings) {
            String query = strings[0];
            String apiKey = "4f732a4a-b27e-4ac7-9350-e9d0b11dd949";
            String urlString = "https://content.guardianapis.com/search?api-key=" + apiKey + "&q=" + query;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // No data to process, return null
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Buffer was empty, no data read
                    return null;
                }
                jsonResponse = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray results = jsonObject.getJSONObject("response").getJSONArray("results");

                List<Article> articles = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject articleJson = results.getJSONObject(i);
                    Article article = new Article(
                            articleJson.getString("webTitle"),
                            articleJson.getString("webUrl"),
                            articleJson.getString("sectionName")
                    );
                    articles.add(article);
                }
                return articles;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
            if (articles != null && !articles.isEmpty()) {
                articleList.clear();
                articleList.addAll(articles);

                List<String> titles = new ArrayList<>();
                for (Article article : articleList) {
                    titles.add(article.getTitle());
                }

                adapter.clear();
                adapter.addAll(titles);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ArticleListActivity.this, "No articles found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
