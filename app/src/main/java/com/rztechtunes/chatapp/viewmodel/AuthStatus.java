package com.rztechtunes.chatapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rztechtunes.chatapp.repos.AuthRepos;

public class AuthStatus extends ViewModel {

    private AuthRepos authRepos;
    public MutableLiveData<AuthViewModel.AuthenticationState> stateLiveData;
    public AuthStatus() {
        stateLiveData = new MutableLiveData<>();
        authRepos = new AuthRepos(stateLiveData);

    }

    public void setUserSatus(String dateWithTime) {

        authRepos.setUserSatus(dateWithTime);
    }

}
