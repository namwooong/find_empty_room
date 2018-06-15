package com.example.aquashdw.emptyroomfinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by aquashdw on 18. 5. 14.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                TimetableTab tab1 = new TimetableTab();
                return tab1;
            case 1:
                TimetableSearchTab tab2 = new TimetableSearchTab();
                return tab2;
            case 2:
                RecommendTab tab3 = new RecommendTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return mNumOfTabs;
    }
}
