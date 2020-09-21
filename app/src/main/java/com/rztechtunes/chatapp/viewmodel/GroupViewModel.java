package com.rztechtunes.chatapp.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.repos.GroupRepos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GroupViewModel extends ViewModel {

    GroupRepos groupRepos;

    public GroupViewModel() {
        groupRepos = new GroupRepos();
    }


    public void createNewGroup(final List<AlluserContractPojo> selectedContractList, final GroupPojo groupPojo, File file, Context context) {


        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Wait creating group...");
        pd.show();

        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(file);
        final StorageReference imageRef = rootRef.child("ChatImages/" + fileUri.getLastPathSegment());

        ///For image Compress
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(),fileUri);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);


        //For get URI Link of Image

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Uri downloadUri = task.getResult();
                    groupPojo.setImages(downloadUri.toString());
                    groupRepos.createNewGrp(selectedContractList,groupPojo);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });



    }

    public MutableLiveData<String>getCreateGrpStatus()
    {
     return groupRepos.getCreateGrpStatus();
    }

    public MutableLiveData<List<GroupPojo>> getMyGroupList() {

        return groupRepos.getMyGroupList();

    }

    public void sendGroupMsg(SendGroupMsgPojo sendGroupMsgPojo) {
        groupRepos.sendGroupMsg(sendGroupMsgPojo);
    }

    public LiveData<List<SendGroupMsgPojo>> getAllGroupMessage(String groupID) {

        return groupRepos.getAllGroupMsg(groupID);
    }

    public void sendImage(final SendGroupMsgPojo sendGroupMsgPojo, File file, Context context) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Wait Sending Image...");
        pd.show();

        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(file);
        final StorageReference imageRef = rootRef.child("ChatImages/" + fileUri.getLastPathSegment());

        ///For image Compress
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(),fileUri);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);


        //For get URI Link of Image

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Uri downloadUri = task.getResult();
                    sendGroupMsgPojo.setImage(downloadUri.toString());
                    groupRepos.sendImages(sendGroupMsgPojo);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public MutableLiveData<List<AlluserContractPojo>> getGroupUser(String groupID) {

        return groupRepos.getGroupUser(groupID);
    }

    public MutableLiveData<GroupPojo> getGroupInfo(String grpID) {

        return groupRepos.getGroupInfo(grpID);
    }
}
