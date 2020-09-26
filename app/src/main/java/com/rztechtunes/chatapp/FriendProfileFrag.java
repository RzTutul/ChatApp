package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rztechtunes.chatapp.adapter.FriendMediaImageAdaper;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.Collections;
import java.util.List;

public class FriendProfileFrag extends Fragment {

    CollapsingToolbarLayout friendNameTV;
    public  static String frndID;

    TextView phoneTV,gmailTV,aboutTV;
    RecyclerView mediaRV;
    ImageView frndImageView;

    AuthViewModel authViewModel;
    MessageViewModel messageViewModel;

    public FriendProfileFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendNameTV = view.findViewById(R.id.friendNameTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        gmailTV = view.findViewById(R.id.gmailTV);
        mediaRV = view.findViewById(R.id.mediaRV);
        aboutTV = view.findViewById(R.id.aboutTV);
        frndImageView = view.findViewById(R.id.frndImageView);
        friendNameTV.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        friendNameTV.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Toolbar toolbar =view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.sendMessageFragment);
            }
        });



        authViewModel.getFriendInformaiton(frndID).observe(getActivity(), new Observer<AuthPojo>() {
            @Override
            public void onChanged(AuthPojo authPojo) {

                friendNameTV.setTitle(authPojo.getName());
                gmailTV.setText(authPojo.getEmail());
                phoneTV.setText(authPojo.getPhone());
                aboutTV.setText(authPojo.getAbout());

                Glide.with(getActivity())
                        .load(authPojo.getImage())
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .into(frndImageView);
            }
        });

       messageViewModel.getAllMessage(frndID).observe(getActivity(), new Observer<List<SenderReciverPojo>>() {
           @Override
           public void onChanged(List<SenderReciverPojo> senderReciverPojos) {

               Collections.reverse(senderReciverPojos);
               FriendMediaImageAdaper friendMediaImageAdaper = new FriendMediaImageAdaper(senderReciverPojos, getContext());
               LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
               mediaRV.setLayoutManager(llm);
               mediaRV.setAdapter(friendMediaImageAdaper);
           }
       });



    }
}