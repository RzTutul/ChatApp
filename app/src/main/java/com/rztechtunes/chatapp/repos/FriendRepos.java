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
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.FriendRequestPojo;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FriendRepos {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    DatabaseReference myRef;
MutableLiveData<List<FriendRequestPojo>> myFrndLD = new MutableLiveData<>();
MutableLiveData<List<FriendRequestPojo>> requestlistLD = new MutableLiveData<>();
     AuthPojo authPojo = new AuthPojo() ;
    public FriendRepos() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void sendFriendRequest(String userID, FriendRequestPojo myCurrentInfo) {
        userRef = rootRef.child(userID);
        userRef.child("Request").child(myCurrentInfo.getU_ID()).setValue(myCurrentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public MutableLiveData<List<FriendRequestPojo>> getRequestList() {
        userRef = rootRef.child(firebaseUser.getUid());

        userRef.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FriendRequestPojo> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(FriendRequestPojo.class));
                }
                requestlistLD.postValue(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return requestlistLD;
    }


    public MutableLiveData<List<FriendRequestPojo>> getMyFriendList() {
        userRef = rootRef.child(firebaseUser.getUid());

        userRef.child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FriendRequestPojo> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(FriendRequestPojo.class));
                }
                myFrndLD.postValue(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return myFrndLD;
    }

    public void acceptedRequest(FriendRequestPojo friendRequestPojo) {

        myRef = rootRef.child(firebaseUser.getUid()).child("Friends");
        //add that user to my friendlist
        myRef.child(friendRequestPojo.getU_ID()).setValue(friendRequestPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //set my info at that user so first get my info
                rootRef = FirebaseDatabase.getInstance().getReference();
                userRef = rootRef.child(firebaseUser.getUid());
                userRef = userRef.child("Loginfo");
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        authPojo = dataSnapshot.getValue(AuthPojo.class);
                        userRef = rootRef.child(friendRequestPojo.getU_ID()).child("Friends");
                        userRef.child(firebaseUser.getUid()).setValue(authPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //Now remove request from my list
                                myRef = rootRef.child(firebaseUser.getUid()).child("Request");
                                myRef.child(friendRequestPojo.getU_ID()).removeValue();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                }



        });


    }

    public AuthPojo getCurrentUserInfo() {

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userRef = userRef.child("Loginfo");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 authPojo = dataSnapshot.getValue(AuthPojo.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return authPojo;
    }
}
