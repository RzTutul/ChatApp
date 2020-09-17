package com.rztechtunes.chatapp;

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

import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.adapter.AllContractListAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContractFragment extends Fragment {
    AuthViewModel authViewModel;
    RecyclerView contractRV ;

    List<AlluserContractPojo>contractPojoList = new ArrayList<>();
    public ContractFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contract, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contractRV = view.findViewById(R.id.contractRV);

        authViewModel.getAllUser().observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
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


                AllContractListAdaper allContractListAdaper = new AllContractListAdaper(contractPojoList,getActivity());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                contractRV.setLayoutManager(llm);
                contractRV.setAdapter(allContractListAdaper);


            }
        });
    }
}