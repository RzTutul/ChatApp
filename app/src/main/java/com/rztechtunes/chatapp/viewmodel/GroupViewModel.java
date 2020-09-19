package com.rztechtunes.chatapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.repos.GroupRepos;

import java.util.List;

public class GroupViewModel extends ViewModel {

    GroupRepos groupRepos;

    public GroupViewModel() {
        groupRepos = new GroupRepos();
    }


    public void createNewGroup(List<AlluserContractPojo> selectedContractList, GroupPojo groupPojo) {

        groupRepos.createNewGrp(selectedContractList,groupPojo);
    }

    public MutableLiveData<String>getCreateGrpStatus()
    {
     return groupRepos.getCreateGrpStatus();
    }

    public MutableLiveData<List<GroupPojo>> getMyGroupList() {

        return groupRepos.getMyGroupList();

    }
}
