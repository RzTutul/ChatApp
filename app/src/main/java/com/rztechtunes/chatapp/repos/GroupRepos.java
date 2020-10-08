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
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GroupRepos {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference groupRef;
    DatabaseReference userRef;

    MutableLiveData<String> createGrpLD = new MutableLiveData<>();
    MutableLiveData<List<GroupPojo>> myGrpLD = new MutableLiveData<>();
    MutableLiveData<List<SendGroupMsgPojo>> GrpMsgLD = new MutableLiveData<>();
    MutableLiveData<List<UserInformationPojo>> GrpUserLD = new MutableLiveData<>();
    MutableLiveData<GroupPojo> GrpInfoLD = new MutableLiveData<>();
    public GroupRepos() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void createNewGrp(final List<UserInformationPojo> selectedContractList, final GroupPojo groupPojo) {


        groupRef = rootRef.child("Group");
        final String grpNameWithDateTime =groupPojo.getName()+ HelperUtils.getDateYearTime();
        groupPojo.setGroupID(grpNameWithDateTime);
        groupRef.child(grpNameWithDateTime).child("Info").setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                for (UserInformationPojo contractPojo: selectedContractList)
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
            /*    //insert into my data list
                rootRef.child(firebaseUser.getUid()).child("Group").child(grpNameWithDateTime).setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
*/

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
        sendGroupMsgPojo.setId(keyValue);
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
        sendGroupMsgPojo.setId(keyValue);
        groupRef.child(keyValue).setValue(sendGroupMsgPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public MutableLiveData<List<UserInformationPojo>> getGroupUser(String groupID) {

        groupRef = rootRef.child("Group").child(groupID).child("Users");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserInformationPojo> users =new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    users.add(dataSnapshot.getValue(UserInformationPojo.class));

                }
                GrpUserLD.postValue(users);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return GrpUserLD;
    }

    public MutableLiveData<GroupPojo> getGroupInfo(String grpID) {
        groupRef = rootRef.child("Group").child(grpID).child("Info");

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupPojo groupPojo = new GroupPojo();
                    groupPojo = snapshot.getValue(GroupPojo.class);

                GrpInfoLD.postValue(groupPojo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return GrpInfoLD;
    }

    public void addMorePaticipant(GroupPojo groupPojo, List<UserInformationPojo> selectedContractList) {
        groupRef = rootRef.child("Group").child(groupPojo.getGroupID()).child("Users");

        for (UserInformationPojo userInformationPojo: selectedContractList)
        {
            groupRef.child(userInformationPojo.getU_ID()).setValue(userInformationPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    userRef = rootRef.child(userInformationPojo.getU_ID()).child("Group");
                    userRef.child(groupPojo.getGroupID()).setValue(groupPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                }
            });
        }


    }

    public void kickOutFromGroup(String u_id, String groupID) {

        groupRef = rootRef.child("Group").child(groupID).child("Users");
        groupRef.child(u_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                userRef = rootRef.child(u_id).child("Group").child(groupID).child("groupID");
                userRef.setValue("Removed");
            }
        });

    }

    public void removeMessage(String msgID, String grpID) {
        groupRef = rootRef.child("Group").child(grpID).child("Msg");
        groupRef.child(msgID).removeValue();
    }
}
