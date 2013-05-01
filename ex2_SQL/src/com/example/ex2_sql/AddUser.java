package com.example.ex2_sql;

import com.example.ex2_sql.R.id;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;
import static com.example.ex2_sql.Constants.USER_NAME;
import static com.example.ex2_sql.Constants.USER_ID;
import static com.example.ex2_sql.Constants.TABLE_NAME;
import android.database.Cursor;
public class AddUser extends Activity implements OnClickListener{
	public User user;
	private static String[] FROM = { _ID, USER_ID, USER_NAME, };
	private static String ORDER_BY = USER_ID + " DESC";
	Button btn_add;
	Button btn_show;
	EditText txtName;
	EditText txtId;
	private EventsData events;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user);		
		
		events=new EventsData(this);
		Button btn_add =(Button) findViewById(R.id.btn_add_to_sql);
		Button btn_show=(Button) findViewById(id.btnShow);
		final EditText txtName=(EditText)findViewById(R.id.txtName);		
		final EditText txtId=(EditText)findViewById(R.id.txtId);
		user=new User();		
		btn_add.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				user.id=txtId.getText().toString();
				user.name=txtName.getText().toString();
				String name=txtName.getText().toString();
				
				String id= txtId.getText().toString();
				if(name.length()>0 && id.length()>0){
					addUser(name, id);	
					addUserAnswers();//add 10 records to answers
				}
				
			}
		});
		btn_show.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				messageShow(getUser());
				messageShow(getAllAnswers());
			}
		});
	}
	protected void addUserAnswers() {
		// TODO Auto-generated method stub
		//table "answers"		
		int id= Integer.parseInt(user.id);
		//SQLiteDatabase db = events.getWritableDatabase();	
		//String str="INSERT INTO answers VALUES ("+id+",'0','default')";
		//create table answers(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id integer not null, answer text, quetion text
		SQLiteDatabase db = events.getWritableDatabase();
	      ContentValues values = new ContentValues();
	      values.put("user_id", id);
	      values.put("answer", "0");
	      values.put("quetion", "default");
	      for(int i=0 ; i<10; i++){	    	  
	    	  db.insertOrThrow("answers", null, values);  
	      }
	      
	      db.close();
		
	}
	private void addUser(String name,String id) {
	      // Insert a new record into the Events data source.
	      // You would do something similar for delete and update.
	      SQLiteDatabase db = events.getWritableDatabase();
	      ContentValues values = new ContentValues();
	      values.put(USER_ID, id);
	      values.put(USER_NAME, name);
	      db.insertOrThrow(TABLE_NAME, null, values);
	      db.close();
	      messageShow("New user created");
	   }
	private void messageShow(String string) {
		// TODO Auto-generated method stub
		// Display on the screen
		Context context = getApplicationContext();
		CharSequence text = string;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private void messageShow(Cursor cursor) {
	      // Stuff them all into a big string
	      StringBuilder builder = new StringBuilder( 
	            "Saved events:\n");
	      while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	         long id = cursor.getLong(0); 
	         long time = cursor.getLong(1);
	         String question=cursor.getString(1);
	         String title = cursor.getString(2);
	         builder.append(id).append(": "); 
	         builder.append(time).append(": ");
	         builder.append(question).append(": ");
	         builder.append(title).append("\n");
	      }
	      // Display on the screen
	      Context context = getApplicationContext();
			CharSequence text = builder;
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	      
	   }
	private Cursor getUser() {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
	      SQLiteDatabase db = events.getReadableDatabase();
	      Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null,
	            null, ORDER_BY);
	      startManagingCursor(cursor);
	      return cursor;
	   }
	private Cursor getAllAnswers() {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
	      SQLiteDatabase db = events.getReadableDatabase();
	      Cursor cursor = db.rawQuery("SELECT * FROM answers",null);
	      startManagingCursor(cursor);
	      return cursor;
	   }
}

