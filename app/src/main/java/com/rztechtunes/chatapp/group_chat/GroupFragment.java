package com.rztechtunes.chatapp.group_chat;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.AllContractListAdaper;
import com.rztechtunes.chatapp.adapter.GroupContractListAdaper;
import com.rztechtunes.chatapp.adapter.GroupListAdapter;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class GroupFragment extends Fragment {

    RecyclerView contractRV;
    GroupViewModel groupViewModel;
    FloatingActionButton createBtn;



    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contractRV = view.findViewById(R.id.contractRV);
        createBtn = view.findViewById(R.id.createGroupBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.createGroupFrag);

            }
        });


        groupViewModel.getMyGroupList().observe(getActivity(), new Observer<List<GroupPojo>>() {
            @Override
            public void onChanged(List<GroupPojo> groupPojos) {
                Log.i(TAG, "grp: "+groupPojos.size());
                GroupListAdapter groupContractListAdaper = new GroupListAdapter(groupPojos,getActivity());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                contractRV.setLayoutManager(llm);
                contractRV.setAdapter(groupContractListAdaper);

            }
        });

     /*   authViewModel.getAllUser().observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
            @Override
            public void onChanged(List<AlluserContractPojo> alluserContractPojos) {
                contractPojoList.clear();
                for (AlluserContractPojo contractPojo: alluserContractPojos)
                {
                    if ((contractPojo.getU_ID()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {

                    }
                    else
                    {
                        contractPojoList.add(contractPojo);
                    }
                }


            }
        });*/


    }
}