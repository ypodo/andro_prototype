package com.example.ex2_sql;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
   public static final String TABLE_NAME = "User";
   // Columns in the Events database
   public static final String USER_NAME = "Name";
   public static final String USER_ID = "id";
   public static final String ANSWER = "answer";
   public static final String QUETION = "quetion";
}