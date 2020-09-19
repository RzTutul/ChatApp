package com.rztechtunes.chatapp.repos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GroupRepos {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference groupRef;

    MutableLiveData<String> createGrpLD = new MutableLiveData<>();
    MutableLiveData<List<GroupPojo>> myGrpLD = new MutableLiveData<>();
    MutableLiveData<List<SendGroupMsgPojo>> GrpMsgLD = new MutableLiveData<>();
    MutableLiveData<List<AlluserContractPojo>> GrpUserLD = new MutableLiveData<>();
    public GroupRepos() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void createNewGrp(final List<AlluserContractPojo> selectedContractList, final GroupPojo groupPojo) {


        groupRef = rootRef.child("Group");
        final String grpNameWithDateTime =groupPojo.getName()+ HelperUtils.getDateYearTime();
        groupPojo.setGroupID(grpNameWithDateTime);
        groupRef.child(grpNameWithDateTime).child("Info").setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                for (AlluserContractPojo contractPojo: selectedContractList)
                {
                    groupRef.child(grpNameWithDateTime).child("Users").child(contractPojo.getU_ID()).setValue(contractPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });


                    rootRef.child(contractPojo.getU_ID()).child("Group").child(grpNameWithDateTime).setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
                //insert into my data list
                rootRef.child(firebaseUser.getUid()).child("Group").child(grpNameWithDateTime).setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


                createGrpLD.postValue("Successful");
            }
        });


    }

    public MutableLiveData<String> getCreateGrpStatus()
    {
        return createGrpLD;
    }

    public MutableLiveData<List<GroupPojo>> getMyGroupList() {

       String uid = firebaseUser.getUid();
        Log.i(TAG, "getMyGroupList: "+uid);

        rootRef.child(uid).child("Group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<GroupPojo> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(GroupPojo.class));
                }

                myGrpLD.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myGrpLD;
    }

    public void sendGroupMsg(SendGroupMsgPojo sendGroupMsgPojo) {
        groupRef = rootRef.child("Group").child(sendGroupMsgPojo.getGroupID()).child("Msg");
        String keyValue = groupRef.push().getKey();
        groupRef.child(keyValue).setValue(sendGroupMsgPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public LiveData<List<SendGroupMsgPojo>> getAllGroupMsg(String groupID) {


        rootRef.child("Group").child(groupID).child("Msg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SendGroupMsgPojo> groupMsgPojos = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    groupMsgPojos.add(dataSnapshot.getValue(SendGroupMsgPojo.class));
                }

                GrpMsgLD.postValue(groupMsgPojos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return GrpMsgLD;
    }

    public void sendImages(SendGroupMsgPojo sendGroupMsgPojo) {
        groupRef = rootRef.child("Group").child(sendGroupMsgPojo.getGroupID()).child("Msg");
        String keyValue = groupRef.push().getKey();
        groupRef.child(keyValue).setValue(sendGroupMsgPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public MutableLiveData<List<AlluserContractPojo>> getGroupUser(String groupID) {

        groupRef = rootRef.child("Group").child(groupID).child("Users");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AlluserContractPojo> users =new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    users.add(dataSnapshot.getValue(AlluserContractPojo.class));

                }
                GrpUserLD.postValue(users);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return GrpUserLD;
    }
}
