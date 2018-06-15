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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 5. 14.
 */

public class TimetableSearchTab extends Fragment {


    SQLiteDatabase db;

    Spinner daySpin;
    Spinner buildSpin;
    Spinner floorSpin;
    Spinner startSpin;
    Spinner endSpin;

    ArrayAdapter<CharSequence> dayAdapt;
    ArrayAdapter<CharSequence> buildAdapt;
    ArrayAdapter<CharSequence> floorAdapt;
    ArrayAdapter<CharSequence> startAdapt;
    ArrayAdapter<CharSequence> endAdapt;

    ArrayList<String> timeTableDB;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View thisView = inflater.inflate(R.layout.timetablesearch_layout, container, false);

        ImageView background = thisView.findViewById(R.id.search_back_image);
        background.setImageDrawable(getResources().getDrawable(R.drawable.round_rec));
        background.setScaleType(ImageView.ScaleType.FIT_XY);

        daySpin = thisView.findViewById(R.id.day_spin);
        buildSpin = thisView.findViewById(R.id.build_spin);
        floorSpin = thisView.findViewById(R.id.floor_spin);
        startSpin = thisView.findViewById(R.id.start_spin);
        endSpin = thisView.findViewById(R.id.end_spin);

        dayAdapt = ArrayAdapter.createFromResource(thisView.getContext(),
                R.array.day_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        buildAdapt = ArrayAdapter.createFromResource(thisView.getContext(),
                R.array.build_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        floorAdapt = ArrayAdapter.createFromResource(thisView.getContext(),
                R.array.floor_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        startAdapt = ArrayAdapter.createFromResource(thisView.getContext(),
                R.array.time_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        endAdapt = ArrayAdapter.createFromResource(thisView.getContext(),
                R.array.time_spin_ini, android.R.layout.simple_spinner_dropdown_item);

        daySpin.setAdapter(dayAdapt);
        buildSpin.setAdapter(buildAdapt);
        floorSpin.setAdapter(floorAdapt);
        startSpin.setAdapter(startAdapt);
        endSpin.setAdapter(endAdapt);

        this.spinnerInitialize(thisView);
        this.spinnerListenerInitialize();

        Button searchButton = thisView.findViewById(R.id.catSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);

                String query = spinnerItemToQuery();
                if(!query.equals("")){
                    intent.putExtra("QUERY", query);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(getContext().getApplicationContext(), "At least \"Day\" and \"Building\" must be specified", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Calendar calendar = Calendar.getInstance();
        int currentDayInt = calendar.get(Calendar.DAY_OF_WEEK);
        int currentTimeInt = calendar.get(Calendar.HOUR_OF_DAY);

        String currentDayTime = "";
        currentDayTime += currentDayInt+" ";
        currentDayTime += currentTimeInt;



        TextView textView = thisView.findViewById(R.id.current_time_search);

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
        cursor.close();

        String result = "";

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

            String foreQuote = "Last Lecture: \n";
            result = foreQuote + result;
        }


        textView.setText(result);

        return thisView;
    }



    private void spinnerInitialize(View view) {
        daySpin = view.findViewById(R.id.day_spin);
        buildSpin = view.findViewById(R.id.build_spin);
        floorSpin = view.findViewById(R.id.floor_spin);
        startSpin = view.findViewById(R.id.start_spin);
        endSpin = view.findViewById(R.id.end_spin);

        dayAdapt = ArrayAdapter.createFromResource(view.getContext(),
                R.array.day_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        buildAdapt = ArrayAdapter.createFromResource(view.getContext(),
                R.array.build_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                R.array.floor_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        startAdapt = ArrayAdapter.createFromResource(view.getContext(),
                R.array.time_spin_ini, android.R.layout.simple_spinner_dropdown_item);
        endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                R.array.time_spin_ini, android.R.layout.simple_spinner_dropdown_item);

        daySpin.setAdapter(dayAdapt);
        buildSpin.setAdapter(buildAdapt);
        floorSpin.setAdapter(floorAdapt);
        startSpin.setAdapter(startAdapt);
        endSpin.setAdapter(endAdapt);

        buildSpin.setEnabled(false);
        floorSpin.setEnabled(false);
        startSpin.setEnabled(false);
        endSpin.setEnabled(false);
    }

    private void spinnerListenerInitialize(){
        daySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                        buildSpin.setEnabled(false);
                        floorSpin.setEnabled(false);
                        startSpin.setEnabled(false);
                        endSpin.setEnabled(false);
                }
                else if(i >0){
                    buildAdapt = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.build_spin, android.R.layout.simple_spinner_dropdown_item);
                    buildSpin.setAdapter(buildAdapt);
                    buildSpin.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buildSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        floorSpin.setEnabled(false);
                        startSpin.setEnabled(false);
                        endSpin.setEnabled(false);
                        break;
                    case 1:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_1, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 2:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_2, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 3:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_3, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 4:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_4, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 5:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_5, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 6:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_6, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 7:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_7, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 8:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_8, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 9:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_9, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 10:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_10, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 11:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_11, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 12:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_12, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 13:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_13, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 14:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_14, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 15:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_15, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 16:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_16, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    case 17:
                        floorAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.floor_spin_17, android.R.layout.simple_spinner_dropdown_item);
                        floorSpin.setAdapter(floorAdapt);
                        floorSpin.setEnabled(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        floorSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    startSpin.setEnabled(false);
                    endSpin.setEnabled(false);
                }
                else {
                    startAdapt = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.time_start_spin, android.R.layout.simple_spinner_dropdown_item);
                    startSpin.setAdapter(startAdapt);
                    startSpin.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    endSpin.setEnabled(false);
                }
                else{
                    switch (i){
                        case 1:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin1, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 2:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin2, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 3:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin3, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 4:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin4, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 5:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin5, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 6:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin6, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 7:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin7, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 8:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin8, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        case 9:
                            endAdapt = ArrayAdapter.createFromResource(view.getContext(),
                                    R.array.time_end_spin9, android.R.layout.simple_spinner_dropdown_item);
                            break;
                        default:
                            endSpin.setEnabled(false);
                            break;
                    }

                    endSpin.setAdapter(endAdapt);
                    endSpin.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String spinnerItemToQuery(){
        String query = "";
        if(daySpin.getSelectedItemPosition() == 0 || buildSpin.getSelectedItemPosition() == 0)
            return query;
        else {
            query += daySpin.getSelectedItemPosition() - 1;
            query += " " + buildSpin.getSelectedItem().toString();
        }

        if(floorSpin.getSelectedItemPosition() != 0){
            query += " " + floorSpin.getSelectedItem().toString();
            if(startSpin.getSelectedItemPosition() != 0){
                String temp=startSpin.getSelectedItem().toString();
                temp=temp.substring(0,2);
                int time=Integer.parseInt(temp);
                query += " " + Integer.toString(time);
                if(endSpin.getSelectedItemPosition() == 0)
                    query += " " + "1"; // set to 1 hour default
                else{
                    int position = endSpin.getSelectedItemPosition() + 1;
                    query += " " + position;
                }
            }
        }

        return query;
    }

    // end of class
}
