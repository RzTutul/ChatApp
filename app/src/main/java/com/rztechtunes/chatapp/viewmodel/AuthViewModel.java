package com.rztechtunes.chatapp.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.repos.AuthRepos;

import static android.content.ContentValues.TAG;

public class AuthViewModel extends ViewModel {
    private AuthRepos authRepos;

    public AuthViewModel() {
        authRepos = new AuthRepos();

    }

}


