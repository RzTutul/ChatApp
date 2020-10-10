package com.rztechtunes.chatapp.repos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rztechtunes.chatapp.pojo.BlockPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AuthRepos {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference  userRef;
    DatabaseReference  alluserRef;
    DatabaseReference userInfo;
    DatabaseReference blockList;


    private MutableLiveData<AuthViewModel.AuthenticationState> stateLiveData;
    private MutableLiveData<UserInformationPojo> userInfoLD = new MutableLiveData<>();
    private MutableLiveData<List<UserInformationPojo>> alluserInfoLD = new MutableLiveData<>();
    private MutableLiveData<UserInformationPojo> friendInfoLD = new MutableLiveData<>();



   public AuthRepos(MutableLiveData<AuthViewModel.AuthenticationState> stateLiveData) {
       firebaseAuth = FirebaseAuth.getInstance();
       firebaseUser = firebaseAuth.getCurrentUser();
       this.stateLiveData = stateLiveData;

    }


    public void addAuthUserInfo(UserInformationPojo authPojo) {
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userInfo = userRef.child("Loginfo");

        alluserRef = rootRef.child("AlluserInfo");
        userInfo.keepSynced(true);
        alluserRef.keepSynced(true);
        String userId = firebaseUser.getUid();
        authPojo.setU_ID(userId);

        userInfo.setValue(authPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                stateLiveData.postValue(AuthViewModel.AuthenticationState.AUTHENTICATED);

            }
        });

        //Store All User in a tree
        String id = alluserRef.push().getKey();

        alluserRef.child(userId).setValue(authPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });


        //This will help when user block a person then create a node name as BlockList so that check when use send message it's block or not
        blockList = userRef.child("BlockList");
        BlockPojo blockPojo  = new BlockPojo("7zzzzzzzzzzzBBBB","intital for block person");
        blockList.child(blockPojo.getU_id()).setValue(blockPojo);
    }


    public FirebaseUser  getFirebaseUser() {
        return firebaseUser;
    }

    public MutableLiveData<UserInformationPojo> getUserInfo() {

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userInfo = userRef.child("Loginfo");


        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformationPojo userInfo = dataSnapshot.getValue(UserInformationPojo.class);
                userInfoLD.postValue(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return userInfoLD;
    }

    public MutableLiveData<List<UserInformationPojo>> getAllUserInfo() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        alluserRef = rootRef.child("AlluserInfo");

        alluserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserInformationPojo> contractPojoList = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    contractPojoList.add(data.getValue(UserInformationPojo.class));

                }
                alluserInfoLD.postValue(contractPojoList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       return alluserInfoLD;
    }

    public void setUserSatus(String dateWithTime) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        alluserRef = rootRef.child("AlluserInfo");
        alluserRef.child(firebaseUser.getUid()).child("time").setValue(dateWithTime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public MutableLiveData<UserInformationPojo> getFriendInformaiton(String frndID) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        alluserRef = rootRef.child("AlluserInfo");


        alluserRef.child(frndID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformationPojo userInfo = dataSnapshot.getValue(UserInformationPojo.class);
                friendInfoLD.postValue(userInfo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return friendInfoLD;
    }

    public void changeCoverPhoto(String imageUrl) {
       userInfo = rootRef.child(firebaseUser.getUid()).child("Loginfo");
       userInfo.child("coverImage").setValue(imageUrl);

       alluserRef = rootRef.child("AlluserInfo").child(firebaseUser.getUid());
       alluserRef.child("coverImage").setValue(imageUrl);
    }

    public void changeProfile(String imageUrl) {
        userInfo = rootRef.child(firebaseUser.getUid()).child("Loginfo");
        userInfo.child("profileImage").setValue(imageUrl);

        alluserRef = rootRef.child("AlluserInfo").child(firebaseUser.getUid());
        alluserRef.child("profileImage").setValue(imageUrl);
    }

    public void updateStatus(String newStatus) {
        userInfo = rootRef.child(firebaseUser.getUid()).child("Loginfo");
        userInfo.child("status").setValue(newStatus);

        alluserRef = rootRef.child("AlluserInfo").child(firebaseUser.getUid());
        alluserRef.child("status").setValue(newStatus);

    }

    public void updateName(String newName) {
        userInfo = rootRef.child(firebaseUser.getUid()).child("Loginfo");
        userInfo.child("name").setValue(newName);

        alluserRef = rootRef.child("AlluserInfo").child(firebaseUser.getUid());
        alluserRef.child("name").setValue(newName);
    }

    public void updateHobby(String newHobby) {
        userInfo = rootRef.child(firebaseUser.getUid()).child("Loginfo");
        userInfo.child("hobby").setValue(newHobby);

        alluserRef = rootRef.child("AlluserInfo").child(firebaseUser.getUid());
        alluserRef.child("hobby").setValue(newHobby);
    }
}


