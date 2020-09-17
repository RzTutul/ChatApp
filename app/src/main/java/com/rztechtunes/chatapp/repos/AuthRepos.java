package com.rztechtunes.chatapp.repos;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.TaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class AuthRepos {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference  userRef;
    DatabaseReference  alluserRef;
    DatabaseReference userInfo;




    private MutableLiveData<AuthViewModel.AuthenticationState> stateLiveData;
    private MutableLiveData<AuthPojo> userInfoLD = new MutableLiveData<>();
    private MutableLiveData<List<AlluserContractPojo>> alluserInfoLD = new MutableLiveData<>();



   public AuthRepos(MutableLiveData<AuthViewModel.AuthenticationState> stateLiveData) {
       firebaseAuth = FirebaseAuth.getInstance();
       firebaseUser = firebaseAuth.getCurrentUser();
       this.stateLiveData = stateLiveData;

    }


    public void addAuthUserInfo(AuthPojo authPojo) {
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.i(TAG, "onComplete: "+firebaseUser.getUid());
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
        AlluserContractPojo alluserContractPojo = new AlluserContractPojo(id,userId,authPojo.getName(),authPojo.getEmail(),authPojo.getPhone(),authPojo.getAbout(),authPojo.getImage(),"Online");
        alluserRef.child(userId).setValue(alluserContractPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    public FirebaseUser  getFirebaseUser() {
        return firebaseUser;
    }

    public MutableLiveData<AuthPojo> getUserInfo() {

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userInfo = userRef.child("Loginfo");


        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AuthPojo userInfo = dataSnapshot.getValue(AuthPojo.class);
                userInfoLD.postValue(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return userInfoLD;
    }

    public MutableLiveData<List<AlluserContractPojo>> getAllUserInfo() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        alluserRef = rootRef.child("AlluserInfo");

        alluserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AlluserContractPojo> contractPojoList = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    contractPojoList.add(data.getValue(AlluserContractPojo.class));

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
        alluserRef.child(firebaseUser.getUid()).child("status").setValue(dateWithTime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }
}


