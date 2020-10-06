package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rztechtunes.chatapp.adapter.MyStoriesAdapter;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class UserProfileFrag extends Fragment {
    RelativeLayout msgRL,sendReqRL,unFriendRL;
    public  static String userID;
    String userName;
    String userImage;
    TextView nameTV,emailTV,phoneTV,aboutTV,countyTV;
    ImageView profileImage,fragImage,coverImageView;
    AuthViewModel authViewModel;
    FirendViewModel firendViewModel;
    UserInformationPojo myCurrentInfo;
    APIService apiService;
    RecyclerView mediaRV;

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
        unFriendRL = view.findViewById(R.id.unfriendRL);
        profileImage = view.findViewById(R.id.profile_image);
        coverImageView = view.findViewById(R.id.coverImageView);
        fragImage = view.findViewById(R.id.fragImage);
        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.gmailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        aboutTV = view.findViewById(R.id.aboutTV);
        countyTV = view.findViewById(R.id.countyTV);
        mediaRV = view.findViewById(R.id.mediaRV);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        authViewModel.getFriendInformaiton(userID).observe(requireActivity(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {
                userName = authPojo.getName();
                userImage = authPojo.getprofileImage();
                Glide.with(requireActivity())
                        .load(authPojo.getprofileImage())
                        .centerCrop()
                        .placeholder(R.drawable.ic_perm_)
                        .into(profileImage);
                  Glide.with(requireActivity())
                        .load(authPojo.getCoverImage())
                        .centerCrop()
                        .placeholder(R.drawable.ic_perm_)
                        .into(coverImageView);


                nameTV.setText(authPojo.getName());
                emailTV.setText(authPojo.getEmail());
                phoneTV.setText(authPojo.getPhone());
                aboutTV.setText(authPojo.getStatus());

                String county = authPojo.getCountry();
                String [] splitCounty = county.split("-"); //split county code & name
                countyTV.setText(splitCounty[1]);
                fragImage.setImageResource(Integer.parseInt(splitCounty[0])); //get county code
            }
        });

        firendViewModel.getMyFirendList().observe(requireActivity(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> userInformationPojos) {
                for (UserInformationPojo informationPojo: userInformationPojos)
                {
                    if ((informationPojo.getU_ID()).equals(userID))
                    {
                        unFriendRL.setVisibility(View.VISIBLE);
                        sendReqRL.setVisibility(View.GONE);
                    }
                }
            }
        });


        unFriendRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firendViewModel.unFirend(userID);
                Toast.makeText(requireActivity(), "Unfriend!", Toast.LENGTH_SHORT).show();
                unFriendRL.setVisibility(View.GONE);
                sendReqRL.setVisibility(View.VISIBLE);
            }
        });


        msgRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageFragment.reciverID = userID;
                SendMessageFragment.reciverImage = userImage;
                SendMessageFragment.reciverName = userName;
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.sendMessageFragment);
            }
        });

        sendReqRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCurrentInfo!=null)
                {
                    firendViewModel.sendFriendRequest(userID,myCurrentInfo);
                    sendNotifiaction(userID,myCurrentInfo.getName(),"Send Friend Request");
                    unFriendRL.setVisibility(View.VISIBLE);
                    sendReqRL.setVisibility(View.GONE);
                }
            }
        });


        firendViewModel.getUserStories(userID).observe(requireActivity(), new Observer<List<StoriesPojo>>() {
            @Override
            public void onChanged(List<StoriesPojo> storiesPojos) {
                MyStoriesAdapter myStoriesAdapter = new MyStoriesAdapter(storiesPojos,getContext());
                GridLayoutManager gll = new GridLayoutManager(getContext(),2);
                mediaRV.setLayoutManager(gll);
                mediaRV.setAdapter(myStoriesAdapter);

            }
        });


        authViewModel.getUserInfo().observe(requireActivity(), new Observer<UserInformationPojo>() {
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