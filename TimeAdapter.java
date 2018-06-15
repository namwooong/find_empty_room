package com.example.aquashdw.emptyroomfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


/**
 * Created by aquashdw on 18. 5. 24.
 */

public class TimeAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Item> items;

    private final LayoutInflater inflater;

    public TimeAdapter(Context context){
        this.context = context;
        this.items = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            setItem(i);
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TimeAdapter(Context context, ArrayList<String> items){

        this.context = context;
        this.items = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            setItem(i);
        }
        for(int i = 0; i < items.size(); i++){
            setItem(items.get(i));
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public Item getItem(int position){
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        // get item in position
        Item item = getItem(position);

        if(view == null){
            view = inflater.inflate(R.layout.time_item_blank, parent, false);
        }
        if(item.getFill()){
            view = inflater.inflate(R.layout.time_item_fill, parent, false);
        }


        return view;
    }

    private void setItem(int position) {
        items.add(position, new Item());
    }

    private void setItem(String data){
        Item item = new Item(data);
        setItem(item.getPosition(), data);
    }

    private void setItem(int position, String data){
        Item item = new Item(data);
        items.remove(position);
        items.add(position, item);
        if(item.getDuration() > 0){
            int jump = 0;
            for(int i = 0; i < item.getDuration(); i++){
                jump += 5;
                setItemSpace(item.getPosition() + jump);
            }
        }
    }

    private void setItemSpace(int position){
        items.remove(position);
        Item item = new Item(0);
        items.add(position, item);
    }

    public void add(String data){
        this.setItem(data);
    }

    public String getItemInfo(int position){
        if(!getItem(position).getFill()) return "EMPTY";
        else if(getItem(position).getFill() && getItem(position).getEmpty()){
            return getItemInfo(position - 5);
        } else return getItem(position).toString();
    }

    public ArrayList<String> getItemStringArray(){
        ArrayList<String> savedArray = new ArrayList<>();

        for(int i = 0; i < 50; i++){
            if(getItem(i).getFill() && !(getItem(i).getEmpty())){
                savedArray.add(getItem(i).toString());
            }
        }

        return savedArray;
    }



    private class Item{
        boolean fill;
        boolean empty;
        String lecName;
        String lecBuild;
        String lecRoom;
        String lecDay;
        String lecStart;
        String lecEnd;
        int duration;
        private int position;

        private Item(){
            fill = false;
            empty = true;
        }

        private Item(int flag){
            fill = true;
            empty = true;
        }

        private Item(String input){
            fill = true;
            empty = false;
            String[] inputArray = input.split(" ");

            setLecBuild(inputArray[0]);
            setLecRoom(inputArray[1]);
            setLecDay(inputArray[2]);
            setLecStart(inputArray[3]);
            setLecEnd(inputArray[4]);
            setLecName(inputArray[5]);
            this.setDuration();
            this.setPosition();
        }

        private void setPosition() {
            int intDay = Integer.parseInt(lecDay);  // mon == 0, fri == 4
            position = intDay + (Integer.parseInt(lecStart) - 9) * 5;
        }

        private void setLecName(String lecName){
            this.lecName = lecName;
        }

        private void setLecBuild(String lecBuild){
            this.lecBuild = lecBuild;
        }

        private void setLecRoom(String lecRoom){
            this.lecRoom = lecRoom;
        }

        private void setLecDay(String lecDay) {
            this.lecDay = lecDay;
        }

        private void setLecStart(String lecStart){
            this.lecStart = lecStart;
        }

        private void setLecEnd(String lecEnd){
            this.lecEnd = lecEnd;
        }

        private void setDuration(){
            duration = Integer.parseInt(lecEnd) - Integer.parseInt(lecStart) - 1;
        }

        private String getLecName() {
            return lecName;
        }

        private String getLecBuild() {
            return lecBuild;
        }

        private String getLecRoom() {
            return lecRoom;
        }

        private int getDuration(){
            return duration;
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

        private boolean getFill() {
            return fill;
        }

        private boolean getEmpty() {
            return empty;
        }

        private int getPosition() {
            return position;
        }
    }
}
