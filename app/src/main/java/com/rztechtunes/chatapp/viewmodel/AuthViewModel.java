package com.rztechtunes.chatapp.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.repos.AuthRepos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AuthViewModel extends ViewModel {
    private AuthRepos authRepos;
    public MutableLiveData<AuthenticationState> stateLiveData;
    public AuthViewModel() {
        stateLiveData = new MutableLiveData<>();
        authRepos = new AuthRepos(stateLiveData);

       // errMsg = firebaseLoginRepository.getErrMsg();
        if (authRepos.getFirebaseUser() != null)
        {
            stateLiveData.postValue(AuthenticationState.AUTHENTICATED);

        }
        else
        {
            stateLiveData.postValue(AuthenticationState.UNAUTHENTICATED);
        }

    }

    public MutableLiveData<UserInformationPojo> getUserInfo() {

        return authRepos.getUserInfo();
    }

    public  MutableLiveData<UserInformationPojo> getFriendInformaiton(String frndID) {

        return authRepos.getFriendInformaiton(frndID);

    }


    public MutableLiveData<List<UserInformationPojo>> getAllUser() {
        return authRepos.getAllUserInfo();
    }
    public void setUserSatus(String dateWithTime) {

        authRepos.setUserSatus(dateWithTime);
    }


    public enum AuthenticationState
    {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    public void getLogoutUser()
    {
        FirebaseAuth.getInstance().signOut();
        stateLiveData.postValue(AuthenticationState.UNAUTHENTICATED);
    }

    public void setUserInfo(Context context, File profile,File coverFile, final UserInformationPojo authPojo) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Wait a moment...");
        pd.show();


        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(profile);
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
                     authPojo.setprofileImage(downloadUri.toString());
                     //Now store Cover Image
                     getCoverFileURL(context,coverFile, authPojo);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void getCoverFileURL(Context context,File coverFile, UserInformationPojo authPojo) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Wait a moment...");
        pd.show();

        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(coverFile);
        final StorageReference imageRef = rootRef.child("ChatImages/" + fileUri.getLastPathSegment());
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
                    authPojo.setCoverImage(downloadUri.toString()); ;
                    authRepos.addAuthUserInfo(authPojo);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }
}


