package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.adapter.Add_GrpMore_ParticipantAdapter;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class AddMoreParticipentFrag extends Fragment {

    RecyclerView addMoreParticipentRV;
    FloatingActionButton addMorePersonBtn;
    public static String grpID;
    Add_GrpMore_ParticipantAdapter add_grpMore_participantAdaper;
    FirendViewModel firendViewModel;
    AuthViewModel authViewModel;
    GroupViewModel groupViewModel;
    List<UserInformationPojo> contractPojoList = new ArrayList<>();


    public AddMoreParticipentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_more_participent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addMoreParticipentRV = view.findViewById(R.id.addMoreParticipentRV);
        addMorePersonBtn = view.findViewById(R.id.addMorePersonBtn);




        firendViewModel.getMyFirendList().observe(getActivity(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> friendRequestPojos) {

                groupViewModel.getGroupUser(grpID).observe(getActivity(), new Observer<List<UserInformationPojo>>() {
                    @Override
                    public void onChanged(List<UserInformationPojo> groupUserPojos) {

                        for (UserInformationPojo user: friendRequestPojos)
                        {
                            for (UserInformationPojo grpUser: groupUserPojos)
                            {

                                if ((grpUser.getU_ID()).equalsIgnoreCase(user.getU_ID()) || (grpUser.getU_ID()).equalsIgnoreCase(FirebaseAuth.getInstance().getUid())  )
                                {
                                    Log.i(TAG, "name23: "+user.getName());
                                }
                                else
                                {
                                    contractPojoList.add(user);

                                }


                            }


                        }

                        add_grpMore_participantAdaper = new Add_GrpMore_ParticipantAdapter(contractPojoList, getActivity());
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        addMoreParticipentRV.setLayoutManager(llm);
                        addMoreParticipentRV.setAdapter(add_grpMore_participantAdaper);


                    }
                });



            }


        });


    }
}