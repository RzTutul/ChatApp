package com.rztechtunes.chatapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.FriendRequestPojo;
import com.rztechtunes.chatapp.repos.FriendRepos;

import java.util.List;

public class FirendViewModel extends ViewModel {
    FriendRepos friendRepos;

    public FirendViewModel() {
        friendRepos = new FriendRepos();
    }

    public void sendFriendRequest(String userID, FriendRequestPojo myCurrentInfo) {

        friendRepos.sendFriendRequest(userID,myCurrentInfo);
    }

    public MutableLiveData<List<FriendRequestPojo>> getMyFirendList() {

        return friendRepos.getMyFriendList();
    }

    public LiveData<List<FriendRequestPojo>> getRequestList() {

        return  friendRepos.getRequestList();
    }

    public void acceptedRequest(FriendRequestPojo friendRequestPojo) {
        friendRepos.acceptedRequest(friendRequestPojo);
    }
}
