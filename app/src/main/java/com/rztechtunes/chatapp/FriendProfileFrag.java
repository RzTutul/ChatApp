package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rztechtunes.chatapp.adapter.FriendMediaImageAdaper;
import com.rztechtunes.chatapp.pojo.BlockPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class FriendProfileFrag extends Fragment {

    CollapsingToolbarLayout friendNameTV;
    public  static String frndID;
    CardView deleteCard,blockCard,unBlockCard;

    TextView phoneTV,gmailTV,StatusTV;
    RecyclerView mediaRV;
    ImageView frndImageView;
    AuthViewModel authViewModel;
    MessageViewModel messageViewModel;
    FirendViewModel firendViewModel;
    public FriendProfileFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
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
        StatusTV = view.findViewById(R.id.aboutTV);
        frndImageView = view.findViewById(R.id.frndImageView);
        deleteCard = view.findViewById(R.id.deletCard);
        blockCard = view.findViewById(R.id.blockCard);
        unBlockCard = view.findViewById(R.id.unBlockCard);
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


        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover message!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                messageViewModel.deleteMessage(frndID);
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Message has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        })
                        .show();
            }
        });

        blockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to block this Person!")
                        .setConfirmText("Yes,Block !")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                BlockPojo blockPojo = new BlockPojo(frndID,"name");
                                messageViewModel.blockFriend(blockPojo);

                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
                                sDialog
                                        .setTitleText("Blocked!")
                                        .setContentText("Message has been Blocked!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        })
                        .show();
            }
        });

        messageViewModel.getBlockList().observe(getActivity(), new Observer<List<BlockPojo>>() {
            @Override
            public void onChanged(List<BlockPojo> blockPojos) {
                for (BlockPojo blockPojo: blockPojos)
                {
                    if (blockPojo.getU_id().equals(frndID))
                    {
                        unBlockCard.setVisibility(View.VISIBLE);
                        blockCard.setVisibility(View.GONE);
                    }
                }
            }
        });

        unBlockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageViewModel.UnblockFriend(frndID);
                unBlockCard.setVisibility(View.GONE);
                blockCard.setVisibility(View.VISIBLE);
            }
        });


        authViewModel.getFriendInformaiton(frndID).observe(getActivity(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {

                friendNameTV.setTitle(authPojo.getName());
                gmailTV.setText(authPojo.getEmail());
                phoneTV.setText(authPojo.getPhone());
                StatusTV.setText(authPojo.getStatus());

                Glide.with(getActivity())
                        .load(authPojo.getprofileImage())
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .into(frndImageView);
            }
        });

       messageViewModel.getAllSharedMedia(frndID).observe(getActivity(), new Observer<List<SenderReciverPojo>>() {
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