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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rztechtunes.chatapp.Notification.Token;
import com.rztechtunes.chatapp.adapter.FriendListAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ChatFragment extends Fragment {

    RecyclerView msgRV;
    MessageViewModel messageViewModel;
    List<SenderReciverPojo> contractList = new ArrayList<>();
    AuthViewModel authViewModel;
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set position so that go to the recylerview item postion for image selected


        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msgRV = view.findViewById(R.id.messageRV);


        messageViewModel.getFrndContract().observe(getActivity(), new Observer<List<SenderReciverPojo>>() {
            @Override
            public void onChanged(List<SenderReciverPojo> senderReciverPojos) {

                //new message list come first in row
                Collections.sort(senderReciverPojos, new Comparator<SenderReciverPojo>() {
                    public int compare(SenderReciverPojo s1, SenderReciverPojo s2) {
                        return s1.getStatus().compareToIgnoreCase(s2.getStatus());
                    }
                });
                //reverse the sort by day time
                Collections.reverse(senderReciverPojos);
                BuildRV(senderReciverPojos);
            }


        });


        updateToken(FirebaseInstanceId.getInstance().getToken());


    }

    public void BuildRV(List<SenderReciverPojo> senderReciverPojos) {
            FriendListAdaper friendListAdaper = new FriendListAdaper(senderReciverPojos, getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            msgRV.setLayoutManager(llm);
            msgRV.setAdapter(friendListAdaper);
            

    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }


}