package com.example.ex2_sql;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.ex2_sql.R.id;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class AverageScore  extends Activity implements OnClickListener{
	User user;
	Button btnGo;
	DatePicker dataPic;
	private EventsData events;
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.average_score);
		user=new User();
		events=new EventsData(this);
		btnGo=(Button)findViewById(id.btnGo);
		btnGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//getSqlResolve(dataPic.getDayOfMonth(),dataPic.getMonth());
				showList();
			}
		});
	}
	protected void showList() {
		// TODO Auto-generated method stub
		ListView listView1 = (ListView) findViewById(R.id.listView1);
		EditText txt=(EditText)findViewById(id.editText1);
		user.id=txt.getText().toString();
		String str;
		ArrayList<String>list=new  ArrayList<String>();
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM answers where user_id='"+Integer.parseInt(user.id)+"'",null);
		startManagingCursor(cursor);
		while (cursor.moveToNext()) { 
	         // Could use getColumnIndexOrThrow() to get indexes
	         str ="ID:"+cursor.getString(0)+  " Answer: "+cursor.getString(2);
	         list.add(str);		
	         }	
                
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);        
        listView1.setAdapter(adapter);
	}
	protected void getSqlResolve(int dayOfMonth, int month) {
		// TODO Auto-generated method stub
		
	}
	private Cursor getAllQuestions(String date) {
	      // Perform a managed query. The Activity will handle closing
	      // and re-querying the cursor when needed.
	      SQLiteDatabase db = events.getReadableDatabase();
	      
	      Cursor cursor = db.rawQuery("SELECT * FROM answers where id='"+Integer.parseInt(user.id)+"'",null);
	      startManagingCursor(cursor);
	      return cursor;
	   }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
}
