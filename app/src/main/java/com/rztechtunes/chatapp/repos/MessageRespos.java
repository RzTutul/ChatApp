package com.rztechtunes.chatapp.repos;

import android.telecom.Call;
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
import com.rztechtunes.chatapp.pojo.BlockPojo;
import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MessageRespos {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference userID;
    DatabaseReference recevierID;
    DatabaseReference chatList;
    DatabaseReference blockList;
    DatabaseReference reciverChatList;
    MutableLiveData<List<SenderReciverPojo>> frndLD = new MutableLiveData<>();
    MutableLiveData<List<SenderReciverPojo>> msgLD = new MutableLiveData<>();
    MutableLiveData<List<SenderReciverPojo>> mediaLD = new MutableLiveData<>();
    MutableLiveData<SenderReciverPojo> lastMsgLD = new MutableLiveData<>();
    MutableLiveData<String> blockStatus = new MutableLiveData<>();
    MutableLiveData<List<BlockPojo>> blockListLD = new MutableLiveData<>();
    MutableLiveData<List<CallingPojo>> CallingLD = new MutableLiveData<>();
    MutableLiveData<List<CallingPojo>> receivingLD = new MutableLiveData<>();
    MutableLiveData<String> insertRecivingLD = new MutableLiveData<>();

    boolean isBlocked;

    public MessageRespos() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    public MutableLiveData<String> sendMessage(SenderReciverPojo senderReciverPojo) {

        userID = rootRef.child(firebaseUser.getUid());

        //Check blockList
        blockList = rootRef.child(senderReciverPojo.getReciverID()).child("BlockList");
        blockList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if ((firebaseUser.getUid()).equals(dataSnapshot.getKey())) {
                        blockStatus.postValue("You can't conversion with this person!");
                        isBlocked = true;
                        break;
                    } else {
                        isBlocked = false;
                    }

                }

                //Now Check myBlockList
                blockList = rootRef.child(firebaseUser.getUid()).child("BlockList");
                blockList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot d : snapshot.getChildren()) {
                            if ((senderReciverPojo.getReciverID()).equals(d.getKey())) {
                                blockStatus.postValue("This person is blocked by you");
                                isBlocked = true;
                                break;
                            }
                        }


                        if (!isBlocked) {
                            //if noOne block then send message
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
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return blockStatus;

    }


    public MutableLiveData<String> sendImages(SenderReciverPojo senderReciverPojo) {

        userID = rootRef.child(firebaseUser.getUid());

        //Check blockList
        blockList = rootRef.child(senderReciverPojo.getReciverID()).child("BlockList");
        blockList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if ((firebaseUser.getUid()).equals(dataSnapshot.getKey())) {
                        blockStatus.postValue("You can't conversion with this person!");
                        isBlocked = true;
                        break;
                    } else {
                        isBlocked = false;
                    }

                }

                //Now Check myBlockList
                blockList = rootRef.child(firebaseUser.getUid()).child("BlockList");
                blockList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot d : snapshot.getChildren()) {
                            if ((senderReciverPojo.getReciverID()).equals(d.getKey())) {
                                blockStatus.postValue("This person is blocked by you");
                                isBlocked = true;
                                break;
                            }
                        }


                        if (!isBlocked) {
                            //if noOne block then send message
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
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return blockStatus;

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



    public LiveData<List<SenderReciverPojo>> getSharedMedia(String frndID) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        userID = rootRef.child(firebaseUser.getUid());
        chatList = userID.child("ChatList");

        chatList.child(frndID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SenderReciverPojo> contractPojoList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    SenderReciverPojo media = data.getValue(SenderReciverPojo.class);
                    assert media != null;
                    if (media.getImage() != null)
                    {
                        contractPojoList.add(data.getValue(SenderReciverPojo.class));
                    }



                }
                mediaLD.postValue(contractPojoList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mediaLD;
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
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(SenderReciverPojo.class));

                }
                if (list.size() > 0) {
                    senderReciverPojo = list.get(list.size() - 1);
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    senderReciverPojo = dataSnapshot.getValue(SenderReciverPojo.class);
                    key = dataSnapshot.getKey();
                }
                if (senderReciverPojo != null && key != null) {

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

    public void deleteMessage(String friendID) {
        userID = rootRef.child(firebaseUser.getUid()).child("ChatList").child(friendID);
        userID.removeValue();
    }

    public void blockFriend(BlockPojo blockPojo) {

        blockList = rootRef.child(firebaseUser.getUid()).child("BlockList");
        blockList.child(blockPojo.getU_id()).setValue(blockPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    public MutableLiveData<List<BlockPojo>> getBlockList() {

        userID = rootRef.child(firebaseUser.getUid()).child("BlockList");
        userID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BlockPojo> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(BlockPojo.class));
                }
                blockListLD.postValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return blockListLD;
    }

    public void UnblockFriend(String frndID) {
        userID = rootRef.child(firebaseUser.getUid()).child("BlockList");

        userID.child(frndID).removeValue();

    }

    public void CallToFriend(String reciverID, CallingPojo myInformation) {
        userID = rootRef.child(reciverID).child("Calling");
        String key = userID.push().getKey();
        userID.child(key).setValue(myInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });


    }

    public MutableLiveData<List<CallingPojo>> getNowCalling() {

        userID = rootRef.child(firebaseUser.getUid()).child("Calling");

        userID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CallingPojo> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(CallingPojo.class));
                    CallingLD.postValue(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return CallingLD;
    }

    public MutableLiveData<List<CallingPojo>> getRecvingStatus() {

        userID = rootRef.child(firebaseUser.getUid()).child("Receiving");

        userID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CallingPojo> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(CallingPojo.class));
                    receivingLD.postValue(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return receivingLD;
    }


    public void deleteRecivingStatus()
    {
        userID = rootRef.child(firebaseUser.getUid()).child("Receiving");
        userID.removeValue();
    }


    public void CallCanel(String reciverID) {
        userID = rootRef.child(reciverID).child("Calling");
        userID.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                userID = rootRef.child(firebaseUser.getUid()).child("Calling");
                userID.removeValue();
            }
        });


    }

    public MutableLiveData<String> ReceiveCall(CallingPojo callingPojo) {
        userID = rootRef.child(callingPojo.getU_id()).child("Receiving");
        String key = userID.push().getKey();
        userID.child(key).setValue(callingPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                insertRecivingLD.postValue("1");
            }
        });

        return insertRecivingLD;

    }


    public void removeMessage(String msgID, String senderID, String receiverID) {

        userID = rootRef.child(senderID).child("ChatList").child(receiverID);

        userID.child(msgID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                recevierID = rootRef.child(receiverID).child("ChatList").child(senderID);

                recevierID.child(msgID).removeValue();
            }
        });


    }
}

