package com.rztechtunes.chatapp.repos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.TaskExecutor;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rztechtunes.chatapp.R;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class AuthRepos {

    private FirebaseAuth firebaseAuth;
    String mVerificationId;
   public AuthRepos() {
        firebaseAuth = FirebaseAuth.getInstance();
    }



}


