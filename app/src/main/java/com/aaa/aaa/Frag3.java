package com.aaa.aaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.aaa.aaa.adpater.communityPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Frag3 extends Fragment {

    private View view;

    ViewPager mViewPager;
    FloatingActionButton write;
    communityPagerAdapter adapter;
    private FragmentActivity myContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        setHasOptionsMenu(true); //메뉴 보이기
        mViewPager = (ViewPager) view.findViewById(R.id.categoryViewpager);
        adapter= new communityPagerAdapter(getChildFragmentManager(),5);
        mViewPager = (ViewPager) view.findViewById(R.id.categoryViewpager);
        mViewPager.setAdapter(adapter);
        mViewPager.setSaveEnabled(false);

        //글쓰기 화면으로 이동
        write= (FloatingActionButton) view.findViewById(R.id.writeButton);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),writeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    //메뉴 설정 함수
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.community_menu, menu);
    }

    //메뉴 버튼 설정 함수
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //마이페이지 이동 버튼
        switch (item.getItemId()){
            case R.id.menu_mypage:
                Intent intent = new Intent(getActivity(), mypageActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //액션바 타이틀 설정 함수
   @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((BottomActivity) activity).setActionBarTitle("커뮤니티");
        }
    }
}



