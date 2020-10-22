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


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.Add_GrpMore_ParticipantAdapter;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.ArrayList;
import java.util.List;


public class AddMoreParticipentFrag extends Fragment {

    RecyclerView addMoreParticipentRV;
    CardView noticeCardview;
    FloatingActionButton addMorePersonBtn;
    public static String grpID;
    Add_GrpMore_ParticipantAdapter add_grpMore_participantAdaper;
    FriendViewModel friendViewModel;
    AuthViewModel authViewModel;
    GroupViewModel groupViewModel;
    List<UserInformationPojo> contractPojoList = new ArrayList<>();
    List<UserInformationPojo> selectedContractList = new ArrayList<>();
    GroupPojo CgrpPojo;
    int index =0;

    Toolbar toolbar;

    public AddMoreParticipentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_more_participent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        addMoreParticipentRV = view.findViewById(R.id.addMoreParticipentRV);
        noticeCardview = view.findViewById(R.id.noticeCardview);
        addMorePersonBtn = view.findViewById(R.id.addMorePersonBtn);
        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });


        groupViewModel.getGroupInfo(grpID).observe(requireActivity(), new Observer<GroupPojo>() {
            @Override
            public void onChanged(GroupPojo groupPojo) {
                CgrpPojo= groupPojo;
            }
        });

        addMorePersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CgrpPojo!= null)
                {
                    selectedContractList =  add_grpMore_participantAdaper.getSelectedContract();
                    groupViewModel.addMorePaticipant( CgrpPojo,selectedContractList);
                    Navigation.findNavController(v).navigate(R.id.homeFragment);
                }


            }
        });



        friendViewModel.getMyFirendList().observe(getViewLifecycleOwner(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> friendList) {
                contractPojoList = friendList;
                index =0;
                groupViewModel.getGroupUser(grpID).observe(getViewLifecycleOwner(), new Observer<List<UserInformationPojo>>() {
                    @Override
                    public void onChanged(List<UserInformationPojo> groupUserPojos) {
                        try {
                            for (int i =0; i<friendList.size();i++)
                            {
                                UserInformationPojo user = friendList.get(i);
                                for (int j =0; j<groupUserPojos.size();j++)
                                {
                                    UserInformationPojo grpUser = groupUserPojos.get(j);

                                    if ((user.getU_ID()).equals(grpUser.getU_ID()))
                                    {
                                        contractPojoList.remove(index);

                                    }

                                }
                                index++;

                            }
                        }
                        catch (Exception e)
                        {

                        }



                        if (contractPojoList.size() == 0)
                        {
                            noticeCardview.setVisibility(View.VISIBLE);
                            addMorePersonBtn.setVisibility(View.GONE);
                        }
                        else
                        {
                            addMorePersonBtn.setVisibility(View.VISIBLE);
                            noticeCardview.setVisibility(View.GONE);
                            add_grpMore_participantAdaper = new Add_GrpMore_ParticipantAdapter(contractPojoList, getActivity());
                            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                            addMoreParticipentRV.setLayoutManager(llm);
                            addMoreParticipentRV.setAdapter(add_grpMore_participantAdaper);
                        }

                    }
                });



            }


        });


    }
}