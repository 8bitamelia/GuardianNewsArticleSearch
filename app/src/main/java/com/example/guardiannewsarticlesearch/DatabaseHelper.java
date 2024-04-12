package com.example.guardiannewsarticlesearch;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage database creation and version management.
 * Handles adding, deleting, and fetching articles from the favorites database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FavoritesDB"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version
    public static final String TABLE_FAVORITES = "favorites"; // Table name
    public static final String COLUMN_ID = "_id"; // Column for ID
    public static final String COLUMN_TITLE = "title"; // Column for the article title
    public static final String COLUMN_URL = "url"; // Column for the article URL
    public static final String COLUMN_SECTION = "section"; // Column for the article section

    /**
     * SQL statement to create the favorites table.
     */
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_SECTION + " TEXT" + ");";

    /**
     * Constructs a new DatabaseHelper.
     * @param context The context to use for opening or creating the database.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    /**
     * Called when the database needs to be upgraded.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    /**
     * Adds an article to the favorites table.
     * @param article The article to add.
     */
    public void addFavorite(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, article.getTitle());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_SECTION, article.getSectionName());
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    /**
     * Removes an article from the favorites table.
     * @param articleTitle The title of the article to remove.
     */
    public void removeFavorite(String articleTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_TITLE + " = ?", new String[] { articleTitle });
        db.close();
    }

    /**
     * Retrieves all articles from the favorites table.
     * @return A list of articles.
     */
    public List<Article> getAllFavorites() {
        List<Article> favoritesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES, new String[] {COLUMN_TITLE, COLUMN_URL, COLUMN_SECTION},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Article article = new Article(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SECTION))
                );
                favoritesList.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favoritesList;
    }
}
