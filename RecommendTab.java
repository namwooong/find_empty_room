package com.example.aquashdw.emptyroomfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 5. 14.
 */

public class RecommendTab extends Fragment {
    SQLiteDatabase db;
    ArrayList<String> timeTableDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View thisView = inflater.inflate(R.layout.recommend_layout,container,false);

        Button recomButton = thisView.findViewById(R.id.recommendButton);

        ImageView background1 = thisView.findViewById(R.id.search_back_image);
        background1.setImageDrawable(getResources().getDrawable(R.drawable.round_rec));
        background1.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView background2 = thisView.findViewById(R.id.recommend_back_image);
        background2.setImageDrawable(getResources().getDrawable(R.drawable.round_rec));
        background2.setScaleType(ImageView.ScaleType.FIT_XY);





        Calendar calendar = Calendar.getInstance();
        int currentDayInt = calendar.get(Calendar.DAY_OF_WEEK);
        final int currentTimeInt = calendar.get(Calendar.HOUR_OF_DAY);

        String currentDayTime = "";
        currentDayTime += currentDayInt+" ";
        currentDayTime += currentTimeInt;

        TextView textView = thisView.findViewById(R.id.current_time_rec);

        textView.setText(currentDayTime);

        timeTableDB = new ArrayList<>();

        DbOpenHelper helper = new DbOpenHelper(getContext());
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_table WHERE day = " + Integer.toString(currentDayInt - 2) + " ;", null);

        while(cursor.moveToNext()){
            String result = "";
            for(int i = 0; i < 6; i++){
                result += cursor.getString(i) + " ";
            }
            timeTableDB.add(result);
        }

        String result = "";
        String lecInfo = "1";

        cursor.close();


        if(timeTableDB.size() == 0){
            result += "You don't have any lectures today!";

        }
        else{
            int last = 32;

            for(int i = 0; i < timeTableDB.size(); i++){
                String[] input = timeTableDB.get(i).split(" ");
                if(Integer.parseInt(input[4]) < currentTimeInt){
                    if(currentTimeInt - Integer.parseInt(input[4]) < last) {
                        last = currentTimeInt - Integer.parseInt(input[4]);
                        result = timeTableDB.get(i);
                    }
                }
            }
            lecInfo = result;
            String foreQuote = "Last Lecture: \n";
            result = foreQuote + result;
        }

        textView.setText(result);

        String temp_query = "";
        if(!lecInfo.equals("1")){
            String[] aLecInfo = lecInfo.split(" ");
            temp_query += aLecInfo[2] + " ";
            temp_query += aLecInfo[0] + " ";
            temp_query += "* ";
            temp_query += aLecInfo[3] + " ";
            temp_query += aLecInfo[4] + " ";
        }

        final String query = temp_query;

        recomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method to set data input based on user timetable
                if(currentTimeInt > 17){
                    Toast.makeText(getContext(),"Recommend only works befor 1700/", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Method to move to SearchResultActivity
                    if(!query.equals("")){
                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);

                        intent.putExtra("QUERY", query);
                        startActivity(intent);
                    }
                    else Toast.makeText(getContext(),"We cannot find rooms with this information.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return thisView;
    }
}
