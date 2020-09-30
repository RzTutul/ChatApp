package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rztechtunes.chatapp.Notification.Client;
import com.rztechtunes.chatapp.Notification.Data;
import com.rztechtunes.chatapp.Notification.MyResponse;
import com.rztechtunes.chatapp.Notification.Sender;
import com.rztechtunes.chatapp.Notification.Token;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class UserProfileFrag extends Fragment {
    RelativeLayout msgRL,sendReqRL;
    public  static String userID;
    String userName;
    String userImage;
    TextView nameTV,emailTV,phoneTV,aboutTV;
    ImageView profileImage;
    AuthViewModel authViewModel;
    FirendViewModel firendViewModel;
    UserInformationPojo myCurrentInfo;
    APIService apiService;

    public UserProfileFrag() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msgRL = view.findViewById(R.id.messageRL);
        sendReqRL = view.findViewById(R.id.sendReqRL);
        profileImage = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.gmailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        aboutTV = view.findViewById(R.id.aboutTV);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        authViewModel.getFriendInformaiton(userID).observe(getActivity(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {
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

        sendReqRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCurrentInfo!=null)
                {
                    firendViewModel.sendFriendRequest(userID,myCurrentInfo);
                    sendNotifiaction(userID,myCurrentInfo.getName(),"Send Friend Request");
                }
            }
        });



        authViewModel.getUserInfo().observe(getActivity(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {
               myCurrentInfo = authPojo;
            }
        });

    }


    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            userID);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Log.i(TAG, "onResponse: " + "fails");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}