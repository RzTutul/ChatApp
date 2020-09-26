package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;


public class UserProfileFrag extends Fragment {
    RelativeLayout msgRL;
    public  static String userID;
    String userName;
    String userImage;
    TextView nameTV,emailTV,phoneTV,aboutTV;
    ImageView profileImage;
    AuthViewModel authViewModel;

    public UserProfileFrag() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msgRL = view.findViewById(R.id.messageRL);
        profileImage = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.gmailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        aboutTV = view.findViewById(R.id.aboutTV);


        authViewModel.getFriendInformaiton(userID).observe(getActivity(), new Observer<AuthPojo>() {
            @Override
            public void onChanged(AuthPojo authPojo) {
                userName = authPojo.getName();
                userImage = authPojo.getImage();
                Glide.with(getActivity())
                        .load(authPojo.getImage())
                        .centerCrop()
                        .placeholder(R.drawable.ic_perm_)
                        .into(profileImage);
                nameTV.setText(authPojo.getName());
                emailTV.setText(authPojo.getEmail());
                phoneTV.setText(authPojo.getPhone());
                aboutTV.setText(authPojo.getAbout());
            }
        });

        msgRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageFragment.reciverID = userID;
                SendMessageFragment.reciverImage = userImage;
                SendMessageFragment.reciverName = userName;
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.sendMessageFragment);
            }
        });

    }
}