package com.newsbreeze.app.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.newsbreeze.app.R;
import com.newsbreeze.app.models.Article;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper  extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME= "my_news_db";
    public static final String COL1 = "ID";
    public static final String COL2 = "title";
    public static final String COL3 = "image_url";
    public static final String COL4 = "description";
    public static final String COL5 = "author";
    public static final String COL6 = "content";
    public static final String COL7 = "url";
    public static final String COL8 = "date";
    public static final String COL9 = "bookmarkStatus";

    private static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME +"("+ COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL2 + " TEXT,"+COL3+ " TEXT,"+ COL4 +" TEXT,"+ COL5 +" TEXT,"+ COL6 + " TEXT,"+
                    COL7+ " TEXT," + COL8+ " TEXT,"+ COL9 +" TEXT)";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP IF TABLE EXISTS ";
        db.execSQL(drop+TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String title, String image_url, String description,
                           String author, String content, String url,
                           String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,Konstant.INSTANCE.escapingString(title));
        contentValues.put(COL3,image_url);
        contentValues.put(COL4,description);
        contentValues.put(COL5,author);
        contentValues.put(COL6,content);
        contentValues.put(COL7,url);
        contentValues.put(COL8,date);
        contentValues.put(COL9,"1");

        Log.e(TAG,"addDATA: Adding "+title+" to "+TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData(){  // Get all data from table
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }


    public Boolean getSpecificItemsID(String date){
        Log.e("DbHelper","date: "+date);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+COL8+" FROM " + TABLE_NAME +
                " WHERE " + COL8 + " = '" + date + "'";;
        Cursor data = db.rawQuery(query, null);

        while (data.moveToNext()){

            String cDate= data.getString(data.getColumnIndex(DatabaseHelper.COL8));
            if (cDate.equals(date) ){
                Log.e("DbHelper","Data matched: true");
                return true;
            }
        }
        return false;
    }

    //  Delete fun to delete data from table
    public void deleteDataFromDB(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL8 + " = '" + date + "'";;
        Log.e(TAG, "deleteMessage: query: " + query);
        db.execSQL(query);
    }



    public ArrayList<Article> getArticleData() {
        ArrayList<Article> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article articleModel = new Article();
                articleModel.setTitle(cursor.getString(cursor.getColumnIndex(COL2)));
                articleModel.setUrlToImage(cursor.getString(cursor.getColumnIndex(COL3)));
                articleModel.setDescription(cursor.getString(cursor.getColumnIndex(COL4)));
                articleModel.setAuthor(cursor.getString(cursor.getColumnIndex(COL5)));
                articleModel.setContent(cursor.getString(cursor.getColumnIndex(COL6)));
                articleModel.setUrl(cursor.getString(cursor.getColumnIndex(COL7)));
                articleModel.setPublishedAt(cursor.getString(cursor.getColumnIndex(COL8)));
                articleModel.setBookmarkStatus(cursor.getString(cursor.getColumnIndex(COL9)));
                arrayList.add(articleModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }



}
