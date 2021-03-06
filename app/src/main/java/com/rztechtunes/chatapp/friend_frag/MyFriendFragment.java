package com.rztechtunes.chatapp.friend_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.MyFriendListAdaper;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;

import java.util.List;


public class MyFriendFragment extends Fragment {

    RecyclerView myFriendRV;
    TextView noticeTV;
    FriendViewModel friendViewModel;
    public MyFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        return inflater.inflate(R.layout.fragment_my_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myFriendRV = view.findViewById(R.id.myFriendRV);
        noticeTV = view.findViewById(R.id.noticeTV);

        friendViewModel.getMyFirendList().observe(getViewLifecycleOwner(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> friendRequestPojos) {

                if (friendRequestPojos.size()>0) {
                    noticeTV.setVisibility(View.GONE);
                    MyFriendListAdaper myFriendListAdaper = new MyFriendListAdaper(friendRequestPojos,getContext());
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    myFriendRV.setLayoutManager(llm);
                    myFriendRV.setAdapter(myFriendListAdaper);
                } else {
                    noticeTV.setVisibility(View.VISIBLE);
                }

            }
        });


    }
}