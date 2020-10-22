package com.rztechtunes.chatapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rztechtunes.chatapp.adapter.FriendMediaImageAdaper;
import com.rztechtunes.chatapp.pojo.BlockPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FriendProfileFrag extends Fragment {

    CollapsingToolbarLayout friendNameTV;
    public  static String frndID;
    CardView deleteCard,blockCard,unBlockCard,sendMailCard,callCard;

    TextView phoneTV,gmailTV,StatusTV,noticeTV;
    RecyclerView mediaRV;
    ImageView frndImageView;
    AuthViewModel authViewModel;
    MessageViewModel messageViewModel;
    FriendViewModel friendViewModel;
    Context mcontext;
    String gmail,phone;
    public FriendProfileFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
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
        noticeTV = view.findViewById(R.id.noticeTV);
        frndImageView = view.findViewById(R.id.frndImageView);
        deleteCard = view.findViewById(R.id.deletCard);
        blockCard = view.findViewById(R.id.blockCard);
        unBlockCard = view.findViewById(R.id.unBlockCard);
        sendMailCard = view.findViewById(R.id.sendmailCard);
        callCard = view.findViewById(R.id.callCard);
        friendNameTV.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        friendNameTV.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Toolbar toolbar =view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Navigation.findNavController(v).navigate(R.id.sendMessageFragment);
            }
        });

        sendMailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail(gmail,"");
            }
        });

        callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phoneUri = Uri.parse("tel:"+phone);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, phoneUri);
                if (dialIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(dialIntent);
                }else{
                    Toast.makeText(getContext(), "no component found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover message!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                messageViewModel.deleteMessage(frndID);
                                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
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

                                Navigation.findNavController(v).navigate(R.id.homeFragment);
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

        messageViewModel.getBlockList().observe(getViewLifecycleOwner(), new Observer<List<BlockPojo>>() {
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


        authViewModel.getFriendInformaiton(frndID).observe(getViewLifecycleOwner(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {

                try {

                    gmail  = authPojo.getEmail();
                    phone = authPojo.getPhone();

                    friendNameTV.setTitle(authPojo.getName());
                    gmailTV.setText(gmail);
                    phoneTV.setText(phone);
                    StatusTV.setText(authPojo.getStatus());

                    Glide.with(requireActivity())
                            .load(authPojo.getprofileImage())
                            .placeholder(R.drawable.ic_image_black_24dp)
                            .into(frndImageView);
                }
                catch (Exception e)
                {

                }

            }
        });

       messageViewModel.getAllSharedMedia(frndID).observe(getViewLifecycleOwner(), new Observer<List<SenderReciverPojo>>() {
           @Override
           public void onChanged(List<SenderReciverPojo> senderReciverPojos) {

               if (senderReciverPojos.size()==0)
               {
                   noticeTV.setVisibility(View.VISIBLE);
               }
               else
               {
                   noticeTV.setVisibility(View.GONE);
                   Collections.reverse(senderReciverPojos);
                   FriendMediaImageAdaper friendMediaImageAdaper = new FriendMediaImageAdaper(senderReciverPojos, getContext());
                   LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                   mediaRV.setLayoutManager(llm);
                   mediaRV.setAdapter(friendMediaImageAdaper);
               }

           }
       });



    }

    public void composeEmail(String addresses, String subject) {
        try{
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + addresses));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            getActivity().startActivity(intent);
        }
        catch (Exception e)
        {

        }
    }


}