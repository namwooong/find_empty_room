package com.example.aquashdw.emptyroomfinder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 5. 23.
 */

public class SearchResultActivity extends AppCompatActivity {

    ArrayList<String> resultStrings = new ArrayList<>();
    String searchQuery;
    LinearLayout scrollChild;
    SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        scrollChild = (LinearLayout) findViewById(R.id.search_result_display);


        Bundle extras = getIntent().getExtras();
        if(extras != null){

            DbOpenHelper helper=new DbOpenHelper(this);
            db=helper.getReadableDatabase();
            Cursor cursor;

            searchQuery = extras.getString("QUERY");

            String[] query_list = searchQuery.split(" ");

            String[] query_index = new String[5];

            query_index[0] = "day";
            query_index[1] = "building";
            query_index[2] = "floor";
            query_index[3] = "start";
            query_index[4] = "end";
            String sql_query = "SELECT * FROM cau WHERE ";
            String useful_query = "SELECT * FROM cau WHERE ";
            int start = 9;
            int end = 17;
            for (int i = 0; i < query_list.length; i++) {
                if (i <= 1) {
                    useful_query += " " + query_index[i] + "=" + "\"" + query_list[i] + "\"";
                }
                sql_query = sql_query + " " + query_index[i] + "=" + "\"" + query_list[i] + "\"";
                if (i <= 1) {
                    useful_query += "AND";
                }

                if (i != query_list.length - 1) {
                    sql_query = sql_query + "AND";

                }
                if (i == 3) {
                    start = Integer.parseInt(query_list[3]);
                }
                if (i == 4) {
                    end =start +Integer.parseInt(query_list[4])-1;
                }
            }


            String room_query = "SELECT * FROM building_room WHERE ";
            room_query += "building = " + "\"" + query_list[1] + "\"";

            if (query_list.length >= 3) {
                room_query += "and floor= " + "\"" + query_list[2] + "\"";
            }
            cursor = db.rawQuery(room_query, null);
            ArrayList<String> room_index = new ArrayList<>();
            while (cursor.moveToNext()) {
                room_index.add(cursor.getString(2));
            }
             //cursor.close();

            int time_size = end - start + 1;
            boolean[][] useful_room = new boolean[time_size][room_index.size()];
            System.out.println(useful_query);
            System.out.println(room_query);
            System.out.println(sql_query);
            System.out.println(time_size);
            System.out.println(room_index.size());
            for(int i=0;i<time_size;i++)
            {
                for(int j=0;j<room_index.size();j++)
                {
                    useful_room[i][j]=true;
                }
            }

            for (int i = 0; i < room_index.size(); i++) {
                String temp = useful_query +" "+ "room= " + "\"" + room_index.get(i) + "\"";
                cursor = db.rawQuery(temp, null);

                while (cursor.moveToNext()) {

                    int now_start = Integer.parseInt(cursor.getString(4));
                    int now_end = Integer.parseInt(cursor.getString(5));
                    System.out.println(now_start);
                    System.out.println(now_end);
                    if (now_start < start) {
                        continue;
                    }
                    if(now_start>=end)
                    {
                        continue;
                    }
                    for(int j=now_start-start;j<=end-start;j++) {
                        if (j > now_end-start)
                        {
                            break;
                        }
                        useful_room[j][i]=false;
                    }

                }
                cursor.close();

            }
            
            for(int i=0;i<room_index.size();i++)
            {	
            	
            	for(int j=0;j<time_size	;j++)
            	{
            		if(useful_room[j][i]==true)
            		{	int query_start=j;
            			int query_end=-1;
            			
            			for(int k=j;k<time_size	;k++)
            			{	j=k;
            				if(useful_room[k][i]==false)
            				{
            					query_end=k;
            					String fffinal=query_list[1]+" "+query_list[2]+" "+room_index.get(i)+" "+query_list[0]+" "+ Integer.toString(query_start+start)+" "+ Integer.toString(query_end+start);
            					 resultStrings.add(fffinal);
            					//insert result

            					break;
            				}
            				
            			}
            			if(query_end==-1)
            			{
            				query_end=time_size;
            				String fffinal=query_list[1]+" "+query_list[2]+" "+room_index.get(i)+" "+query_list[0]+" "+ Integer.toString(query_start+start)+" "+ Integer.toString(query_end+start);
            				resultStrings.add(fffinal);
            				//insert
            			}

            		}

            	}
            }



            //util this 
           

        }

        if(resultStrings != null){
            this.setResults();
        }

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setResults(){
        for(int i = 0; i < resultStrings.size(); i++){
            String input = resultStrings.get(i);
            String[] inputArray = input.split(" ");

            LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams weight3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3f);
            LinearLayout.LayoutParams weight2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2f);

            LinearLayout inputLinear = new LinearLayout(getApplicationContext());
            inputLinear.setOrientation(LinearLayout.HORIZONTAL);
            inputLinear.setWeightSum(12f);
            inputLinear.setLayoutParams(parentParams);


            TextView buildText = new TextView(getApplicationContext());
            buildText.setText(inputArray[0]);
            buildText.setLayoutParams(weight3);
            buildText.setTextSize(20f);

            TextView roomText = new TextView(getApplicationContext());
            roomText.setText(inputArray[2]);
            roomText.setLayoutParams(weight3);
            roomText.setTextSize(20f);

            TextView dayText = new TextView(getApplicationContext());
            switch(Integer.parseInt(inputArray[3])){
                case 0:
                    inputArray[3] = "Mon";
                    break;
                case 1:
                    inputArray[3] = "Tue";
                    break;
                case 2:
                    inputArray[3] = "Wed";
                    break;
                case 3:
                    inputArray[3] = "Thu";
                    break;
                case 4:
                    inputArray[3] = "Fri";
                    break;
                default:
                    break;
            }
            dayText.setText(inputArray[3]);
            dayText.setLayoutParams(weight2);
            dayText.setTextSize(20f);

            TextView startText = new TextView(getApplicationContext());
            inputArray[4] += "00";
            startText.setText(inputArray[4]);
            startText.setLayoutParams(weight2);
            startText.setTextSize(20f);

            TextView endText = new TextView(getApplicationContext());
            inputArray[5] += "00";
            endText.setText(inputArray[5]);
            endText.setLayoutParams(weight2);
            endText.setTextSize(20f);

            inputLinear.addView(buildText);
            inputLinear.addView(roomText);
            inputLinear.addView(dayText);
            inputLinear.addView(startText);
            inputLinear.addView(endText);

            scrollChild.addView(inputLinear);
        }
    }
}
