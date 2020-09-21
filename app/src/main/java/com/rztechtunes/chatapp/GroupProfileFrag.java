package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.rztechtunes.chatapp.adapter.GrpParticipantAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.List;

import static android.content.ContentValues.TAG;


public class GroupProfileFrag extends Fragment {

    CollapsingToolbarLayout groupNameTV;
    ImageView groupImageView;
    TextView createTimeTV,descriptionTV;
    RecyclerView participantRV;
    public  static String grpID;
    public  static String grpName;
    public  static String grpImage;
    Toolbar toolbar;
    GroupViewModel groupViewModel;
    String adminID;

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
        Toolbar toolbar =view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.groupSendMessage);
            }
        });

        groupNameTV.setTitle(grpName);
        groupNameTV.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        groupNameTV.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        Glide.with(getActivity())
                .load(grpImage)
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(groupImageView);



        groupViewModel.getGroupInfo(grpID).observe(getActivity(), new Observer<GroupPojo>() {
            @Override
            public void onChanged(GroupPojo groupPojo) {

                createTimeTV.setText("Created "+groupPojo.getCreateTime());
                adminID = groupPojo.getGrpAdmin();
                descriptionTV.setText(groupPojo.getDescription());


                //When get who is admin then call bellow code for show admin status
               groupViewModel.getGroupUser(grpID).observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
                    @Override
                    public void onChanged(List<AlluserContractPojo> alluserContractPojos) {

                        GrpParticipantAdaper grpParticipantAdaper = new GrpParticipantAdaper(alluserContractPojos,getContext(),adminID);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        participantRV.setLayoutManager(llm);
                        participantRV.setAdapter(grpParticipantAdaper);
                    }
                });


            }
        });






    }
}