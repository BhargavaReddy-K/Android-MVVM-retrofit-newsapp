package com.company.demoapp.model.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.demoapp.model.dto.NewsArticleModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "demoApp";
    private final static int DATABASE_VERSION = 1;

    //Table
    private static final String TABLE_NEWS = "newsTable";

    private static final String SOURCE_ID = "sourceId";
    private static final String SOURCE_NAME = "sourceName";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String URL = "url";
    private static final String IMAGE_URL = "imageUrl";
    private static final String PUBLISHED_AT = "publishedAt";
    private static final String CONTENT = "content";


    private final String CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NEWS + " ( " +
            SOURCE_ID + " TEXT , " +
            SOURCE_NAME + " TEXT , " +
            AUTHOR + " TEXT , " +
            DESCRIPTION + " TEXT , " +
            URL + " TEXT , " +
            IMAGE_URL + " TEXT , " +
            PUBLISHED_AT + " TEXT , " +
            TITLE + " TEXT , " +
            CONTENT + " TEXT " +
            " ) ";

    public DataBaseLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_NEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(db);
    }

    //*****************************************I  N  S  E  R  T  D  A  T  A***********************************************//


    public void insertNewsData(List<NewsArticleModel> newsArticleModelList) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            for (NewsArticleModel newsArticleModel : newsArticleModelList) {
                contentValues.put(SOURCE_NAME, newsArticleModel.getSource().getName());
                contentValues.put(SOURCE_ID, newsArticleModel.getSource().getId());
                contentValues.put(AUTHOR, newsArticleModel.getAuthor());
                contentValues.put(DESCRIPTION, newsArticleModel.getDescription());
                contentValues.put(URL, newsArticleModel.getUrl());
                contentValues.put(IMAGE_URL, newsArticleModel.getUrlToImage());
                contentValues.put(PUBLISHED_AT, newsArticleModel.getPublishedAt());
                contentValues.put(CONTENT, newsArticleModel.getContent());
                contentValues.put(TITLE, newsArticleModel.getTitle());
                database.insert(TABLE_NEWS, null, contentValues);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }





    //*****************************************T  R  U  N  C  A  T  E***********************************************//

    public void truncateNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, null, null);
        db.close();

    }


    //*****************************************G   E   T    D   A   T   A***********************************************//

    public List<String> getSourceList() {
        List<String> stringList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "Select distinct " + SOURCE_NAME + " from " + TABLE_NEWS;
            @SuppressLint("Recycle")
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        stringList.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
            db.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public List<NewsArticleModel> getNewsList(String source) {
        List<NewsArticleModel> newsArticleModelList = new ArrayList<>();
        NewsArticleModel newsArticleModel;
        try {

            String selectQuery = "SELECT " + AUTHOR + ", " + DESCRIPTION + ", " + URL + ", " + IMAGE_URL + ", " + PUBLISHED_AT + ", " + CONTENT + ", " + TITLE + " FROM " + TABLE_NEWS + " where " + SOURCE_NAME + " = " + "'" + source + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        newsArticleModel = new NewsArticleModel();
                        newsArticleModel.setAuthor(cursor.getString(0));
                        newsArticleModel.setDescription(cursor.getString(1));
                        newsArticleModel.setUrl(cursor.getString(2));
                        newsArticleModel.setUrlToImage(cursor.getString(3));
                        newsArticleModel.setPublishedAt(cursor.getString(4));
                        newsArticleModel.setContent(cursor.getString(5));
                        newsArticleModel.setTitle(cursor.getString(6));
                        newsArticleModelList.add(newsArticleModel);
                    } while (cursor.moveToNext());
                }
            }
            // closing connection
            cursor.close();
            db.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return newsArticleModelList;
    }


}
