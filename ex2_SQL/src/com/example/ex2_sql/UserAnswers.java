package com.example.ex2_sql;

import static android.provider.BaseColumns._ID;
import static com.example.ex2_sql.Constants.TABLE_NAME;
import static com.example.ex2_sql.Constants.USER_ID;
import static com.example.ex2_sql.Constants.USER_NAME;
import static com.example.ex2_sql.Constants.ANSWER;
import static com.example.ex2_sql.Constants.QUETION;

import java.util.ArrayList;

import com.example.ex2_sql.R.id;


import android.R.integer;
import android.R.layout;
import android.R.string;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UserAnswers extends ListActivity{
	User user;
	public static final String KEY_LIST_PREFERENCE = "listPref1";
	private EventsData events;	
	TextView textName;
	ListView listView1;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_answers);			
		listView1=(ListView)findViewById(id.listView1);
		events=new EventsData(this);
		user=new User();
		Bundle extras = getIntent().getExtras();
		user.id = extras.getString("id");		
		textName=(TextView)findViewById(id.textName);
		textName.setText("User_id: "+user.id);		
		showUserQuestionsInListItems(getAllAnswers(user.id));
		
		Button btn_getAvrg=(Button)findViewById(id.btnAvrg);
		btn_getAvrg.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast(getScore(getUserScoreAvrg()));
				
			}
		});
	}
	protected Cursor getUserScoreAvrg() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT answer FROM answers WHERE user_id='"+user.id+"'", null);
		startManagingCursor(cursor);
		return cursor;
		
	}
	protected String getScore(Cursor cursor ){				 
	      int yes=0;
	      int no=0;
	      while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	          if (cursor.getString(0)=="yes"){
	        	  yes++;
	          } 
	          else if(cursor.getString(0)=="no"){
	        	no++;  
	          }
          }
	      
	      return Integer.toString(yes);    
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
	private void messageShow(Cursor cursor) {
	      // Stuff them all into a big string
	      StringBuilder builder = new StringBuilder( "Saved events:\n");
	      
	      while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	         long id = cursor.getLong(0); 
	         long time = cursor.getLong(1);
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
	private Cursor getUser(String id) {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
	      SQLiteDatabase db = events.getReadableDatabase();
	      Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id='3'", null);
	      startManagingCursor(cursor);
	      return cursor;
	   }
	private Cursor getAllAnswers(String user_id) {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.	   
		SQLiteDatabase db = events.getReadableDatabase();
	      Cursor cursor = db.rawQuery("SELECT id, user_id, answer FROM answers where user_id="+user.id,null);
	      startManagingCursor(cursor);
	      return cursor;
	   }
	private void showUserQuestionsInListItems(Cursor cursor) {
		// Set up data binding
		ArrayList<String>myList=new ArrayList<String>();
		String str;		
		int index=1;
		while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	         str ="ID:"+cursor.getString(0)+ " Question : "+Integer.toString(index++) + " Answer: "+cursor.getString(2);
	         myList.add(str);
		}		
		setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, myList));
		
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String myItem = (String) getListView().getItemAtPosition(position);
		String question_id=myItem.split(":")[1].split(" ")[0];
				
		showDialog_and_update(question_id);
	}
	
	public void showDialog_and_update(final String question_id){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        
		    	switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	update_answer(question_id,"yes");
		        	showUserQuestionsInListItems(getAllAnswers(user.id));	
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	update_answer(question_id,"no");
		        	showUserQuestionsInListItems(getAllAnswers(user.id));	
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("User_id" +question_id+ "   :  Passed ?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
		
	}
	protected void update_answer(String question_id, String argument) {
		// TODO Auto-generated method stub
		int id= Integer.parseInt(question_id);
		SQLiteDatabase db = events.getWritableDatabase();		
		ContentValues args = new ContentValues();	    
		if(argument=="yes"){
			args.put("answer", "yes");
		    db.update("answers", args, "id=" + question_id, null);		
			db.close();	
		}
		else if(argument=="no"){
			args.put("answer", "no");
		    db.update("answers", args, "id=" + question_id, null);		
			db.close();
		}
		
	}
	
}
