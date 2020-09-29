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
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.FriendRequestPojo;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.repos.FriendRepos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

    public MutableLiveData<List<StoriesPojo>> getStories()
    {
        return friendRepos.getStories();
    }

    public void addStories(StoriesPojo storiesPojo, File file, Context context) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Uploading Add Stories...");
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
                    storiesPojo.setImage(downloadUri.toString());
                    friendRepos.addStories(storiesPojo);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}
