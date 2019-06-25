package com.fivefivelike.mybaselibrary.view;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/10/27.
 */

public class InnerPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles;

    public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles.toArray(new String[titles.size()]);
    }

    public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        List<String> title = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            title.add("");
        }
        this.fragments = fragments;
        this.titles = title.toArray(new String[title.size()]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
        //super.destroyItem(container, position, object);
    }


    public void setDatas(ArrayList<Fragment> datas) {
        fragments.clear();
        fragments.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}