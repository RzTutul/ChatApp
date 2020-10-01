package com.rztechtunes.chatapp.repos;

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
import com.rztechtunes.chatapp.pojo.StoriesPojo;

import java.util.ArrayList;
import java.util.List;

public class FriendRepos {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    DatabaseReference myRef;
    MutableLiveData<List<UserInformationPojo>> myFrndLD = new MutableLiveData<>();
    MutableLiveData<List<UserInformationPojo>> requestlistLD = new MutableLiveData<>();
    MutableLiveData<List<StoriesPojo>> storiesLD = new MutableLiveData<>();
    MutableLiveData<List<StoriesPojo>> myStoriesLD = new MutableLiveData<>();
    MutableLiveData<List<StoriesPojo>> userStoriesLD = new MutableLiveData<>();
    MutableLiveData<String> addStoriesSuccefulLD = new MutableLiveData<>();
    UserInformationPojo authPojo = new UserInformationPojo();

    public FriendRepos() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void sendFriendRequest(String userID, UserInformationPojo myCurrentInfo) {
        userRef = rootRef.child(userID);
        userRef.child("Request").child(myCurrentInfo.getU_ID()).setValue(myCurrentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public MutableLiveData<List<UserInformationPojo>> getRequestList() {
        userRef = rootRef.child(firebaseUser.getUid());

        userRef.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserInformationPojo> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(UserInformationPojo.class));
                }
                requestlistLD.postValue(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return requestlistLD;
    }


    public MutableLiveData<List<UserInformationPojo>> getMyFriendList() {
        userRef = rootRef.child(firebaseUser.getUid());

        userRef.child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserInformationPojo> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(UserInformationPojo.class));
                }
                myFrndLD.postValue(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return myFrndLD;
    }

    public void acceptedRequest(UserInformationPojo friendRequestPojo) {

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
                        authPojo = dataSnapshot.getValue(UserInformationPojo.class);
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

    public UserInformationPojo getCurrentUserInfo() {

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userRef = userRef.child("Loginfo");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                authPojo = dataSnapshot.getValue(UserInformationPojo.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return authPojo;
    }



    public MutableLiveData<String> addStories(StoriesPojo storiesPojo) {
        myRef = rootRef.child(firebaseUser.getUid()).child("Stories");

        String key = myRef.push().getKey();
        myRef.child(key).setValue(storiesPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                addStoriesSuccefulLD.postValue("1");
            }
        });

        return addStoriesSuccefulLD;
    }

    public MutableLiveData<List<StoriesPojo>> getStories(){
        List<StoriesPojo> storiesPojoList = new ArrayList<>();
        DatabaseReference friendRef;
        friendRef = rootRef.child(firebaseUser.getUid()).child("Friends");
        myRef = rootRef.child(firebaseUser.getUid()).child("Stories");
        myRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    storiesPojoList.add(dataSnapshot.getValue(StoriesPojo.class));
                }

                storiesLD.postValue(storiesPojoList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Find out my friend list add get their last stories;
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (dataSnapshot.getKey() != null)
                    {
                        userRef = rootRef.child(dataSnapshot.getKey()).child("Stories");
                        userRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1: snapshot.getChildren())
                                {
                                    storiesPojoList.add(dataSnapshot1.getValue(StoriesPojo.class));
                                }
                                storiesLD.postValue(storiesPojoList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return storiesLD;
    }

    public MutableLiveData<List<StoriesPojo>> getmyStories() {

        myRef = rootRef.child(firebaseUser.getUid()).child("Stories");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StoriesPojo> list =new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(StoriesPojo.class));
                }
                myStoriesLD.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myStoriesLD;

    }

    public MutableLiveData<List<StoriesPojo>> getUserStories(String userID) {

        userRef = rootRef.child(userID).child("Stories");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StoriesPojo> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(StoriesPojo.class));
                }
                userStoriesLD.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return userStoriesLD;
    }

    public void unFriend(String u_id) {
        userRef = rootRef.child(firebaseUser.getUid()).child("Friends").child(u_id);
        userRef.removeValue();
    }


}
