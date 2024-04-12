package com.example.guardiannewsarticlesearch;
/**
 * Represents an article from The Guardian newspaper.
 * This class holds details about a news article, including its title, URL, and the section it belongs to.
 */
public class Article {
    // Title of the article
    private String title;
    // URL link to the full article
    private String url;
    // Section name of article
    private String sectionName;
    /**
     * Constructor for creating an instance of Article.
     * @param title The title of the article.
     * @param url The web URL of the article.
     * @param sectionName The section in which the article is published.
     */
    public Article(String title, String url, String sectionName) {
        this.title = title;
        this.url = url;
        this.sectionName = sectionName;
    }

    /**
     * Retrieves the title of the article.
     * @return The article's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the URL of the article.
     * @return The article's URL as a String.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retrieves the section name of the article.
     * @return The section name of article
     */
    public String getSectionName() {
        return sectionName;
    }
}