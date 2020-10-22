package com.rztechtunes.chatapp.group_chat;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rztechtunes.chatapp.video_calling.JoinRoomFrag;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.GrpParticipantAdaper;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class GroupProfileFrag extends Fragment {

    CollapsingToolbarLayout groupNameTV;
    ImageView groupImageView;
    TextView createTimeTV,descriptionTV;
    RecyclerView participantRV;
    CardView exitCardView;
    public  static String grpID;
    public  static String grpName;
    public  static String grpImage;
    LinearLayout joinRoomLL;
    Toolbar toolbar;
    GroupViewModel groupViewModel;
    String adminID;
    LinearLayout addPrticipentLL;

    public GroupProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupNameTV = view.findViewById(R.id.groupNameTV);
        descriptionTV = view.findViewById(R.id.descriptionTV);
        groupImageView = view.findViewById(R.id.groupImageView);
        createTimeTV = view.findViewById(R.id.createTimeTV);
        participantRV = view.findViewById(R.id.participantRV);
        addPrticipentLL = view.findViewById(R.id.addPrticipentLL);
        joinRoomLL = view.findViewById(R.id.joinRoomLL);
        exitCardView = view.findViewById(R.id.exitCardView);
        Toolbar toolbar =view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Navigation.findNavController(v).navigate(R.id.groupSendMessage);
            }
        });

        groupNameTV.setTitle(grpName);
        groupNameTV.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        groupNameTV.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        Glide.with(requireActivity())
                .load(grpImage)
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(groupImageView);


        joinRoomLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRoomFrag.groupName = grpName;
                JoinRoomFrag.groupID = grpID;
                Navigation.findNavController(v).navigate(R.id.joinRoomFrag);
            }
        });

        exitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to delete group!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                groupViewModel.deleteGroup(grpID);
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Group has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                Navigation.findNavController(v).navigate(R.id.homeFragment);

                            }
                        })
                        .show();
            }
        });


        groupViewModel.getGroupInfo(grpID).observe(getViewLifecycleOwner(), new Observer<GroupPojo>() {
            @Override
            public void onChanged(GroupPojo groupPojo) {

                createTimeTV.setText("Created "+groupPojo.getCreateTime());
                adminID = groupPojo.getGrpAdmin();
                descriptionTV.setText(groupPojo.getDescription());


                //When get who is admin then call bellow code for show admin status
               groupViewModel.getGroupUser(grpID).observe(requireActivity(), new Observer<List<UserInformationPojo>>() {
                    @Override
                    public void onChanged(List<UserInformationPojo> userInformationPojos) {

                        GrpParticipantAdaper grpParticipantAdaper = new GrpParticipantAdaper(userInformationPojos,getContext(),adminID,grpID);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        participantRV.setLayoutManager(llm);
                        participantRV.setAdapter(grpParticipantAdaper);
                    }
                });


            }
        });


        addPrticipentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoreParticipentFrag.grpID = grpID;
                Navigation.findNavController(v).navigate(R.id.addMoreParticipentFrag);
            }
        });




    }
}