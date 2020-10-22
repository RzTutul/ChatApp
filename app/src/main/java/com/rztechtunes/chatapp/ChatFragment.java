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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rztechtunes.chatapp.Notification.Token;
import com.rztechtunes.chatapp.adapter.ChatFriendListAdaper;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView msgRV;
    MessageViewModel messageViewModel;
    List<SenderReciverPojo> contractList = new ArrayList<>();
    AuthViewModel authViewModel;
    BottomNavigationView bottomNav;
    FloatingActionButton cameraFB;
    TextView noticeTV;

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
        cameraFB = view.findViewById(R.id.cameraFB);
        noticeTV = view.findViewById(R.id.noticeTV);

        //For Bottom Nevigation
        bottomNav = view.findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        cameraFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.storyFragment);
            }
        });


        messageViewModel.getFrndContract().observe(getViewLifecycleOwner(), new Observer<List<SenderReciverPojo>>() {
            @Override
            public void onChanged(List<SenderReciverPojo> senderReciverPojos) {

                if (senderReciverPojos.size()>0) {
                    noticeTV.setVisibility(View.GONE);
                    //new message list come first in row
                    Collections.sort(senderReciverPojos, new Comparator<SenderReciverPojo>() {
                        public int compare(SenderReciverPojo s1, SenderReciverPojo s2) {
                            return s1.getStatus().compareToIgnoreCase(s2.getStatus());
                        }
                    });
                    //reverse the sort by day time
                    Collections.reverse(senderReciverPojos);
                    BuildRV(senderReciverPojos);
                } else {
                    noticeTV.setVisibility(View.VISIBLE);
                }
            }


        });


        updateToken(FirebaseInstanceId.getInstance().getToken());


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.firend_menu:
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.friendFragment);
                            break;
                        case R.id.profile_menu:
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.profileFragment);
                            break;

                        default:
                            break;
                    }

                    return true;
                }
            };




    public void BuildRV(List<SenderReciverPojo> senderReciverPojos) {
            ChatFriendListAdaper chatFriendListAdaper = new ChatFriendListAdaper(senderReciverPojos, getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            msgRV.setLayoutManager(llm);
            msgRV.setAdapter(chatFriendListAdaper);
            

    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }




}