package com.aaa.aaa.adpater;

/** 카테고리 ViewPager 어댑터 **/
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aaa.aaa.community_QnA;
import com.aaa.aaa.community_donation;
import com.aaa.aaa.community_information;
import com.aaa.aaa.community_recipe;
import com.aaa.aaa.community_trade;

import java.util.ArrayList;

public class communityPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private final ArrayList<String> name = new ArrayList<>();

    public communityPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        //카테고리 이름
        name.add("질문");
        name.add("홍보·판매");
        name.add("나눔");
        name.add("정보");
        name.add("레시피");
    }

    //각 프래그먼트 연결
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                community_QnA qnA = new community_QnA();
                return qnA;
            case 1:
                community_trade trade= new community_trade();
                return trade;
            case 2:
                community_donation donation= new community_donation();
                return donation;
            case 4:
                community_recipe recipe= new community_recipe();
                return recipe;
            case 3:
                community_information information= new community_information();
                return information;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return name.get(position);
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}