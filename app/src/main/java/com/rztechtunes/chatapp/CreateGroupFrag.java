package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.adapter.GroupContractListAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CreateGroupFrag extends Fragment {

    RecyclerView contractRV;
    AuthViewModel authViewModel;
    GroupViewModel groupViewModel;
    FloatingActionButton createBtn;

    List<AlluserContractPojo> contractPojoList = new ArrayList<>();
    List<AlluserContractPojo> selectedContractList = new ArrayList<>();
    GroupContractListAdaper groupContractListAdaper;

    EditText groupNameET,descriptionET;

    public CreateGroupFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contractRV = view.findViewById(R.id.contractRV);
        createBtn = view.findViewById(R.id.createGroupBtn);
        groupNameET = view.findViewById(R.id.groupNameET);
        descriptionET = view.findViewById(R.id.groupDiscription);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String grpName = groupNameET.getText().toString().trim();
                String grpDesrption = descriptionET.getText().toString().trim();

                GroupPojo groupPojo = new GroupPojo("",grpName,grpDesrption);



               groupViewModel.createNewGroup(selectedContractList,groupPojo);

            }
        });

        groupViewModel.getCreateGrpStatus().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Successful"))
                {
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
                }
                else
                {

                }
            }
        });


        authViewModel.getAllUser().observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
            @Override
            public void onChanged(List<AlluserContractPojo> alluserContractPojos) {
                contractPojoList.clear();
                for (AlluserContractPojo contractPojo: alluserContractPojos)
                {
                    if ((contractPojo.getU_ID()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        //add iw
                    }
                    else
                    {
                        //add without my myinfo
                        contractPojoList.add(contractPojo);
                    }
                }

                GroupContractListAdaper groupContractListAdaper = new GroupContractListAdaper(contractPojoList,getActivity());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                contractRV.setLayoutManager(llm);
                contractRV.setAdapter(groupContractListAdaper);

                selectedContractList = groupContractListAdaper.getSelectedContract();
            }
        });



    }
}