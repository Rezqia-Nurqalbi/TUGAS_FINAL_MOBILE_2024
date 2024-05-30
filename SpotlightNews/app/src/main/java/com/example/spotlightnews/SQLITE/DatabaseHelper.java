package com.example.spotlightnews.SQLITE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.spotlightnews.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "spotlight.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "news";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SOURCE_NAME = "source_name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URL_TO_IMAGE = "url_to_image";
    public static final String COLUMN_PUBLISHED_AT = "published_at";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_LAST_OPENED = "last_opened";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SOURCE_NAME + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_URL_TO_IMAGE + " TEXT, " +
                COLUMN_PUBLISHED_AT + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_LAST_OPENED + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addNewsHistory(String sourceName, String author, String title, String description,
                                  String url, String urlToImage, String publishedAt, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SOURCE_NAME, sourceName);
        contentValues.put(COLUMN_AUTHOR, author);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_URL, url);
        contentValues.put(COLUMN_URL_TO_IMAGE, urlToImage);
        contentValues.put(COLUMN_PUBLISHED_AT, publishedAt);
        contentValues.put(COLUMN_CONTENT, content);
        String currentTime = getCurrentTime();
        contentValues.put(COLUMN_LAST_OPENED, currentTime);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean updateLastOpened(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String currentTime = getCurrentTime();
        contentValues.put(COLUMN_LAST_OPENED, currentTime);
        int result = db.update(TABLE_NAME, contentValues, COLUMN_TITLE + " = ?", new String[]{title});
        return result > 0;
    }

    @SuppressLint("Range")
    public List<Article> getAllData() {
        List<Article> articles = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_LAST_OPENED + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setSourceName(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE_NAME)));
                article.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                article.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                article.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                article.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                article.setUrlToImage(cursor.getString(cursor.getColumnIndex(COLUMN_URL_TO_IMAGE)));
                article.setPublishedAt(cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHED_AT)));
                article.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                article.setLastOpened(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_OPENED)));
                articles.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }
}
