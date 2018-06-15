package com.example.aquashdw.emptyroomfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 5. 14.
 */

public class TimetableTab extends Fragment{

    SQLiteDatabase db;
    GridView gridView;
    TimeAdapter timeAdapter;
    ArrayList<String> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View thisView = inflater.inflate(R.layout.timetable_layout, container, false);

        Button editButton = thisView.findViewById(R.id.editButton);
        Button delButton = thisView.findViewById(R.id.delButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimetableEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method to reset timetable
                deleteAll();

            }
        });

        if(items == null) {
            DbOpenHelper helper = new DbOpenHelper(getActivity());
            db = helper.getReadableDatabase();
            ArrayList<String> loadTimetable = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM user_table ", null);
            while (cursor.moveToNext()) {
                String result = "";
                for (int i = 0; i < 6; i++) {
                    result += cursor.getString(i) + " ";
                }
                loadTimetable.add(result);
            }

            items = loadTimetable;
            if (loadTimetable.size() != 0)
                timeAdapter = new TimeAdapter(thisView.getContext(), loadTimetable);
            else
                timeAdapter = new TimeAdapter(thisView.getContext());

            cursor.close();
        }


        gridView = thisView.findViewById(R.id.tableGrid);
        gridView.setAdapter(timeAdapter);


        // check timetable
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeAdapter tempAdapter = (TimeAdapter) gridView.getAdapter();
                String info = tempAdapter.getItemInfo(i);

                if(!info.equals("BLANK")){
                    String[] cutInfo = info.split(" ");
                    String releaseInfo = "";
                    releaseInfo += cutInfo[0] + ", ";
                    releaseInfo += cutInfo[1] + ", ";
                    releaseInfo += cutInfo[5];
                    Toast.makeText(getContext(), releaseInfo, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return thisView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == 0) {
            items.clear();
            items = data.getStringArrayListExtra("ITEMS");


            timeAdapter = new TimeAdapter(getContext(), items);
            gridView.setAdapter(timeAdapter);

            DbOpenHelper helper = new DbOpenHelper(getContext());
            db = helper.getWritableDatabase();
            for (int i = 0; i < items.size(); i++) {
                String[] query_entity = items.get(i).split(" ");
                String sql_string = "INSERT INTO user_table VALUES('" +
                        query_entity[0] + "','" + query_entity[1] + "','" + query_entity[2] + "','" + query_entity[3] + "','" + query_entity[4] + "','" + query_entity[5] + "');";
                db.rawQuery(sql_string, null);
            }

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM user_table;", null);
            String result = "";
            while (cursor.moveToNext()) {
                for (int i = 0; i < 6; i++) {
                    result += cursor.getString(i);
                    result += " ";
                }
                result += "\n";
                System.out.println(result);
            }
            cursor.close();
        }

    }




    public void deleteAll() {


        DbOpenHelper helper = new DbOpenHelper(getActivity());

        db = helper.getWritableDatabase();

        db.rawQuery("DELETE FROM \"user_table\"", null);
        timeAdapter = new TimeAdapter(getContext());
        gridView.setAdapter(timeAdapter);

        Toast.makeText(getContext(), "Delete All", Toast.LENGTH_SHORT).show();

    }

    public static class DeleteDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.delete_all_warn)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // clear timetable

                        }
                    })
                    .setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();

        }
    }


}
