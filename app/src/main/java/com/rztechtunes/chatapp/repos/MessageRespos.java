package com.rztechtunes.chatapp.repos;

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
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageRespos {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference userID;
    DatabaseReference recevierID;
    DatabaseReference chatList;
    DatabaseReference reciverChatList;
    int count = 0;
    MutableLiveData<List<SenderReciverPojo>> frndLD = new MutableLiveData<>();
    MutableLiveData<List<SenderReciverPojo>> msgLD = new MutableLiveData<>();
    MutableLiveData<SenderReciverPojo> lastMsgLD = new MutableLiveData<>();

    public MessageRespos() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public void sendMessage(SenderReciverPojo senderReciverPojo) {


        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList").child(senderReciverPojo.getReciverID());
        String id = chatList.push().getKey();
        senderReciverPojo.setId(id);
        senderReciverPojo.setSenderID(firebaseAuth.getUid());
        chatList.child(id).setValue(senderReciverPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        recevierID = rootRef.child(senderReciverPojo.getReciverID());
        reciverChatList = recevierID.child("ChatList").child(senderReciverPojo.getSenderID());
        senderReciverPojo.setStatus(HelperUtils.getDateWithTime());
        reciverChatList.child(id).setValue(senderReciverPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });


    }

    public MutableLiveData<List<SenderReciverPojo>> getFrndContract() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList");
        chatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SenderReciverPojo> contractPojoList = new ArrayList<>();
                List<SenderReciverPojo> finalConractList = new ArrayList<>();

                SenderReciverPojo senderReciverPojo;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //find specific user msg
                    for (DataSnapshot d : data.getChildren()) {

                        contractPojoList.add(d.getValue(SenderReciverPojo.class));
                    }
                    senderReciverPojo = contractPojoList.get(contractPojoList.size() - 1);
                    //only save the last msg
                    finalConractList.add(senderReciverPojo);
                    contractPojoList.clear();
                }

                frndLD.postValue(finalConractList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return frndLD;
    }


    public LiveData<List<SenderReciverPojo>> getallMessage(String reciverID) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList");

        chatList.child(reciverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SenderReciverPojo> contractPojoList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    contractPojoList.add(data.getValue(SenderReciverPojo.class));


                }
                msgLD.postValue(contractPojoList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return msgLD;
    }

    public void sendImages(SenderReciverPojo senderReciverPojo) {
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList").child(senderReciverPojo.getReciverID());
        String id = chatList.push().getKey();
        senderReciverPojo.setId(id);
        senderReciverPojo.setSenderID(firebaseAuth.getUid());
        chatList.child(id).setValue(senderReciverPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        recevierID = rootRef.child(senderReciverPojo.getReciverID());
        reciverChatList = recevierID.child("ChatList").child(senderReciverPojo.getSenderID());
        senderReciverPojo.setStatus(HelperUtils.getDateWithTime());
        reciverChatList.child(id).setValue(senderReciverPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    public MutableLiveData<SenderReciverPojo> geLastmsg(String reciverID) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList");

        chatList.child(reciverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SenderReciverPojo senderReciverPojo = new SenderReciverPojo();
                List<SenderReciverPojo> list = new ArrayList<>();
                for (DataSnapshot d: dataSnapshot.getChildren())
                {
                        list.add(d.getValue(SenderReciverPojo.class));

                }
                if (list.size()>0)
                {
                    senderReciverPojo = list.get(list.size()-1);
                    lastMsgLD.postValue(senderReciverPojo);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return lastMsgLD;
    }

    public void setReadStatus(String reciverID) {
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList").child(reciverID);


        chatList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SenderReciverPojo senderReciverPojo = new SenderReciverPojo();
                String key = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    senderReciverPojo = dataSnapshot.getValue(SenderReciverPojo.class);
                    key = dataSnapshot.getKey();
                }
                if (senderReciverPojo!= null && key!= null)
                {

                        chatList.child(key).child("isRead").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        recevierID = rootRef.child(reciverID);
                        reciverChatList = recevierID.child("ChatList").child(firebaseUser.getUid());

                        reciverChatList.child(key).child("isRead").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

