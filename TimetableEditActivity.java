package com.example.aquashdw.emptyroomfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 5. 17.
 */

public class TimetableEditActivity extends FragmentActivity {

    SQLiteDatabase db;
    ArrayList<String> timetableDB = new ArrayList<>();
    TimeAdapter timeAdapter;
    LectureAdapter lectureAdapter;
    GridView gridView;


    private int selectedItem;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_edit_layout);


        // Spinner Selector for search features
        final Spinner daySpin = findViewById(R.id.day_spin_);
        final Spinner buildSpin = findViewById(R.id.build_spin_);

        ArrayAdapter<CharSequence> dayAdapt = ArrayAdapter.createFromResource(
                getApplicationContext(),R.array.day_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> buildAdapt = ArrayAdapter.createFromResource(
                getApplicationContext(),R.array.build_spin, android.R.layout.simple_spinner_dropdown_item);

        daySpin.setAdapter(dayAdapt);
        buildSpin.setAdapter(buildAdapt);

        // read pre-inserted timetable db
        // instantiate timetableDB to match user timetable

        DbOpenHelper helper = new DbOpenHelper(this);
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_table", null);

        while(cursor.moveToNext()){
            String result = "";
            for(int i = 0; i < 6; i++){
                result+=cursor.getString(i)+" ";
            }
            timetableDB.add(result);
        }

        if(timetableDB.size() != 0)
            timeAdapter  = new TimeAdapter(getApplicationContext(), timetableDB);
        else
            timeAdapter = new TimeAdapter(getApplicationContext());



        cursor.close();

        // initiate gridview, reflect user timetable
        gridView = findViewById(R.id.tableGrid);
        gridView.setAdapter(timeAdapter);

        // check timetable
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeAdapter tempAdapter = (TimeAdapter) gridView.getAdapter();
                String info = tempAdapter.getItemInfo(i);

                if(!info.equals("BLANK")){
                    Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // initiate listview, first with blank adapter, set OnItemClickListener
        final ListView timeTableList = findViewById(R.id.timetable_list);
        LectureAdapter defaultBlank = new LectureAdapter(getApplicationContext());
        timeTableList.setAdapter(defaultBlank);



        timeTableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = i;
            }
        });

        // Search Button Initialize
        Button searchButton = findViewById(R.id.edit_search);
        searchButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                // Search for more timetable
                if(daySpin.getSelectedItemPosition() == 0 || buildSpin.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please select both options", Toast.LENGTH_SHORT).show();
                }
                else{
                    String query = "";
                    query += daySpin.getSelectedItemPosition()-1;
                    query += " " + buildSpin.getSelectedItem().toString();

                    // function call for lectureAdapter
                    setLectureAdapter(query);
                    timeTableList.setAdapter(lectureAdapter);
                }
            }
        });



        // AddButton Initialize
        final Button addButton = findViewById(R.id.edit_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), lectureAdapter.getItem(selectedItem).toString(), Toast.LENGTH_SHORT).show();
                timetableDB.add(lectureAdapter.getItemString(selectedItem));
                timeAdapter = new TimeAdapter(getApplicationContext(), timetableDB);
                gridView.setAdapter(timeAdapter);

            }
        });

    }

    @Override
    public void onBackPressed(){
        timetableDB = ((TimeAdapter)gridView.getAdapter()).getItemStringArray();


        Intent returnIntent = new Intent();
        returnIntent.putExtra("ITEMS", timetableDB);
        setResult(0, returnIntent);
        finish();
    }

    private void setLectureAdapter(String query){


        ArrayList<String> lectures = new ArrayList<>();
        // query search
        DbOpenHelper helper = new DbOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        String query_list[] = query.split(" ");
        String sql_string="SELECT * FROM cau WHERE building  =\""+ query_list[1] + "\"" + "and day =\""+ query_list[0] + "\"" ;
        cursor = db.rawQuery(sql_string, null);
        while (cursor.moveToNext()) {
            String result = "";

            for(int i=0;i<7;i++)
            {
                if(i==1)
                {
                    continue;
                }
                result+=cursor.getString(i)+" ";
            }

            lectures.add(result);
        }
        cursor.close();

        // items input
        lectureAdapter = new LectureAdapter(getApplicationContext(), lectures);

        db.close();
    }


}
