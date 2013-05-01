package com.example.ex2_sql;
import static android.provider.BaseColumns._ID;
import static com.example.ex2_sql.Constants.USER_NAME;
import static com.example.ex2_sql.Constants.TABLE_NAME;
import static com.example.ex2_sql.Constants.USER_ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsData extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "events.db";
   private static final int DATABASE_VERSION = 2;

   /** Create a helper object for the Events database */
   public EventsData(Context ctx) { 
      super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
     
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
	  
      db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ID + " INTEGER," + USER_NAME + " TEXT NOT NULL);");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
   }
   
   public void addTable(SQLiteDatabase db){
	   db.execSQL("CREATE TABLE questions (" +_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ID + " INTEGER, answer TEXT NOT NULL, question TEXT NOT NULL);");
	   
   }
  
}