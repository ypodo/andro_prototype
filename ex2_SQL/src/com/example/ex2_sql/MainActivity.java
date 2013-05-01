package com.example.ex2_sql;

import static com.example.ex2_sql.Constants._ID;
import static com.example.ex2_sql.Constants.USER_ID;
import static com.example.ex2_sql.Constants.TABLE_NAME;
import static com.example.ex2_sql.Constants.USER_NAME;

import java.util.ArrayList;

import com.example.ex2_sql.R.id;
import com.example.ex2_sql.AddUser;

import android.R.integer;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {
	Button btnAdd;
	
	Button btnShow;
	Button btnShowTables;
	private static String[] FROM = { _ID, USER_ID, USER_NAME, };
	private static String ORDER_BY = USER_ID + " DESC";
	private static int[] TO = { R.id.rowid, R.id.time, R.id.title, };
	private EventsData events;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		events=new EventsData(this);
		
		Button btnAdd=(Button)findViewById(id.btnAdd);
		Button btnShow=(Button)findViewById(id.btnShow);
		
		showEvents(getEvents());
		
		btnAdd.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent = new Intent(MainActivity.this, AddUser.class);
		        startActivity(intent);
			}
		});
		btnShow.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, AverageScore.class);
		        startActivity(intent);
			}
		});
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showEvents(getEvents());
	}
	
//Logic layer=========================================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	private void showToast(String string) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub		 
		Context context = getApplicationContext();
		CharSequence text = string;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	private void addEvent(String string) {
	      // Insert a new record into the Events data source.
	      // You would do something similar for delete and update.
	      SQLiteDatabase db = events.getWritableDatabase();
	      ContentValues values = new ContentValues();
	      values.put(USER_ID, System.currentTimeMillis());
	      values.put(USER_NAME, string);
	      db.insertOrThrow(TABLE_NAME, null, values);
	   }
	private Cursor getEvents() {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
	      SQLiteDatabase db = events.getReadableDatabase();
	      Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null,
	            null, ORDER_BY);
	      startManagingCursor(cursor);
	      return cursor;
	   }
	private Cursor getAnswersTable() {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
		// TODO Auto-generated method stub
		SQLiteDatabase db = events.getWritableDatabase();		
		final ArrayList<String> dirArray = new ArrayList<String>();
		Cursor cursor = db.rawQuery("SELECT * FROM answers", null);
	      startManagingCursor(cursor);
	      if (cursor.moveToFirst())
	      {
	          while ( !cursor.isAfterLast() ){
	             dirArray.add( cursor.getString( cursor.getColumnIndex("name")) );
	             cursor.moveToNext();
	          }
	      }
	      return cursor;
	   }
	private void showToast(Cursor cursor) {
	      // Stuff them all into a big string
	      StringBuilder builder = new StringBuilder( 
	            "Saved events:\n");
	      while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	         long id = cursor.getLong(0); 
	         String time = cursor.getString(1);
	         String title = cursor.getString(2);
	         builder.append(id).append(": "); 
	         builder.append(time).append(": ");
	         builder.append(title).append("\n");
	      }
	      // Display on the screen
	      Context context = getApplicationContext();
			CharSequence text = builder;
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	      
	   }

	private void showEvents(Cursor cursor) {
		// Set up data binding
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		R.layout.item, cursor, FROM, TO);
		setListAdapter(adapter);
		}
	private void createTable(){
		SQLiteDatabase db = events.getWritableDatabase();		
        String str="create table answers(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id integer not null, answer text, quetion text);";
        db.execSQL(str);
		
	}
	
	protected void onListItemClick (ListView l, View v, int position, long id){		
		Cursor mycursor = (Cursor) getListView().getItemAtPosition(position);
		String user_id=mycursor.getString(1);
		events.close();
		Intent intent = new Intent(MainActivity.this, UserAnswers.class);
        intent.putExtra("id", user_id);        
		startActivity(intent);
        
		
	}
}
