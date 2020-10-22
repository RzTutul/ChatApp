package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.adapter.AllContractListAdaper;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContractFragment extends Fragment {
    FloatingActionButton searchActionButton;
    AuthViewModel authViewModel;
    RecyclerView contractRV ;
    EditText searchET;
    AllContractListAdaper allContractListAdaper;
    List<UserInformationPojo>contractPojoList = new ArrayList<>();
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


        searchET = view.findViewById(R.id.searchET);
        searchActionButton = view.findViewById(R.id.searchActionButton);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        searchActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            searchET.animate().alpha(1.0f).setDuration(2000);;
                searchET.setVisibility(View.VISIBLE);

            }
        });

        authViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> userInformationPojos) {

                contractPojoList.clear();
                for (UserInformationPojo contractPojo: userInformationPojos)
                {
                    if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(contractPojo.getU_ID()))
                    {

                    }
                    else
                    {
                        contractPojoList.add(contractPojo);
                    }
                }


                 allContractListAdaper = new AllContractListAdaper(contractPojoList,getActivity());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                contractRV.setLayoutManager(llm);
                contractRV.setAdapter(allContractListAdaper);


            }
        });
    }
    private void filter(String text) {
        ArrayList<UserInformationPojo> filteredList = new ArrayList<>();
        for (UserInformationPojo contractPojo : contractPojoList) {
            if (contractPojo.getName().toLowerCase().contains(text.toLowerCase()) ||contractPojo.getPhone().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(contractPojo);
            }
        }
         allContractListAdaper.filterList(filteredList);
    }



    }
