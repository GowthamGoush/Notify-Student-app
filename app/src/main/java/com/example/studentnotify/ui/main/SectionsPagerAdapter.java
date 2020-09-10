package com.example.studentnotify.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.studentnotify.BlankFragment2;
import com.example.studentnotify.MainFragment;
import com.example.studentnotify.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private int logTo;

    public SectionsPagerAdapter(Context context, FragmentManager fm , int logIn) {
        super(fm);
        mContext = context;
        logTo = logIn;
        if(logIn==1){
            TAB_TITLES[0] = R.string.tab_text_3;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(logTo != 1) {
            switch (position) {

                case 0:
                    fragment = BlankFragment.newInstance("Gow",logTo);
                    break;
                case 1:
                    fragment = BlankFragment2.newInstance("Gow", "tham");
                    break;
            }
        }
        else {
            switch (position) {

                case 0:
                    fragment = new MainFragment();
                    break;
                case 1:
                    fragment = BlankFragment2.newInstance("Gow", "tham");
                    break;
            }
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}