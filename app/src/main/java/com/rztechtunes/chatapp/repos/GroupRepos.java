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
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
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
    public GroupRepos() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void createNewGrp(final List<AlluserContractPojo> selectedContractList, final GroupPojo groupPojo) {

        groupRef = rootRef.child("Group");
        final String grpNameWithDateTime =groupPojo.getName()+ HelperUtils.getDateYearTime();

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
}
