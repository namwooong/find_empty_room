package com.example.aquashdw.emptyroomfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aquashdw on 18. 6. 11.
 */

public class LectureAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> items;

    public LectureAdapter(Context context){
        this.context = context;
        this.items = new ArrayList<>();
    }

    public LectureAdapter(Context context, ArrayList<String> items){
        this.context = context;
        this.items = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            setItem(items.get(i));
        }
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    public String getItemString(int position){
        return items.get(position).toString();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Item currentItem = (Item) getItem(position);

        TextView lecName = convertView.findViewById(R.id.lec_name);
        TextView lecRoom = convertView.findViewById(R.id.lec_room);
        TextView lecStart = convertView.findViewById(R.id.lec_start_time);
        TextView lecEnd = convertView.findViewById(R.id.lec_end_time);

        lecName.setText(currentItem.getLecName());
        lecRoom.setText(currentItem.getLecRoom());

        String setLecStart = currentItem.getLecStart() + "00";
        String setLecEnd = currentItem.getLecEnd() + "00";
        lecStart.setText(setLecStart);
        lecEnd.setText(setLecEnd);

        return convertView;
    }

    public void updateResults(ArrayList<String> items){
        this.items.clear();
        for(int i = 0; i < items.size(); i ++){
            this.items.add(new Item(items.get(i)));
        }

        notifyDataSetChanged();
    }

    public void setItem(String input){
        items.add(new Item(input));
    }



    private class Item{
        String lecName;
        String lecBuild;
        String lecRoom;
        String lecDay;
        String lecStart;
        String lecEnd;

        private Item(String input){
            String[] inputArray = input.split(" ");

            setLecBuild(inputArray[0]);
            setLecRoom(inputArray[1]);
            setLecDay(inputArray[2]);
            setLecStart(inputArray[3]);
            setLecEnd(inputArray[4]);
            setLecName(inputArray[5]);
        }

        private void setLecName(String lecName){
            this.lecName = lecName;
        }

        private void setLecRoom(String lecRoom){
            this.lecRoom = lecRoom;
        }

        private void setLecStart(String lecStart){
            this.lecStart = lecStart;
        }

        private void setLecEnd(String lecEnd){
            this.lecEnd = lecEnd;
        }

        private String getLecName() {
            return lecName;
        }

        private String getLecRoom() {
            return lecRoom;
        }

        private String getLecStart() {
            return lecStart;
        }

        private String getLecEnd() {
            return lecEnd;
        }

        public String toString(){
            String string = "";
            string += this.lecBuild;
            string += " " + this.lecRoom;
            string += " " + this.lecDay;
            string += " " + this.lecStart;
            string += " " + this.lecEnd;
            string += " " + this.lecName;
            return string;
        }

        private void setLecBuild(String lecBuild) {
            this.lecBuild = lecBuild;
        }

        private void setLecDay(String lecDay) {
            this.lecDay = lecDay;
        }
    }
}
