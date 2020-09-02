package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.rztechtunes.chatapp.adapter.ViewPagerAdapter;


public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ChatFragment chatFragment;
    private GroupFragment groupFragment;
    private ContractFragment contractFragment;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        chatFragment = new ChatFragment();
        groupFragment = new GroupFragment();
        contractFragment = new ContractFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),1);
        viewPagerAdapter.addFragment(chatFragment,"Chats");
        viewPagerAdapter.addFragment(groupFragment,"Groups");
        viewPagerAdapter.addFragment(contractFragment,"Contracts");
        viewPager.setAdapter(viewPagerAdapter);

    }
}