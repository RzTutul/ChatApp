package com.rztechtunes.chatapp.friend_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.ViewPagerAdapter;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;

import java.util.List;


public class FriendFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyFriendFragment myFriendFragment;
    private FriendReqestFrag friendReqestFrag;
    Toolbar toolbar;
    FriendViewModel friendViewModel;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        toolbar = view.findViewById(R.id.toolbar);


        myFriendFragment  = new MyFriendFragment();
        friendReqestFrag = new FriendReqestFrag();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),1);
        viewPagerAdapter.addFragment(myFriendFragment,"Friends");
        viewPagerAdapter.addFragment(friendReqestFrag,"Request");
        viewPager.setAdapter(viewPagerAdapter);


        try {
            friendViewModel.getRequestList().observe(requireActivity(), new Observer<List<UserInformationPojo>>() {
                @Override
                public void onChanged(List<UserInformationPojo> userInformationPojos) {

                   int size = userInformationPojos.size();
                    BadgeDrawable badgeDrawable = tabLayout.getTabAt(1).getOrCreateBadge();
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(size);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            }
        });


    }
}