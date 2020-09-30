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

import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.FriendRequestAdaper;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.util.List;

public class FriendReqestFrag extends Fragment {
    RecyclerView friendReqRV;
    FirendViewModel firendViewModel;
    public FriendReqestFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_reqest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendReqRV = view.findViewById(R.id.frinedReqRV);

        firendViewModel.getRequestList().observe(getActivity(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> friendRequestPojos) {
                FriendRequestAdaper friendRequestAdaper = new FriendRequestAdaper(friendRequestPojos,getContext());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                friendReqRV.setLayoutManager(llm);
                friendReqRV.setAdapter(friendRequestAdaper);
            }
        });
    }
}