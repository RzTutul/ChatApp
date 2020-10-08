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
import com.rztechtunes.chatapp.pojo.BlockPojo;
import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.repos.MessageRespos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessageViewModel extends ViewModel {

    MessageRespos messageRespos ;

   public MutableLiveData<String> blockStatusLD = new MutableLiveData<>();

    public MessageViewModel() {

        messageRespos = new MessageRespos();
    }

    public MutableLiveData<String> sendMessage(SenderReciverPojo senderReciverPojo) {

       return messageRespos.sendMessage(senderReciverPojo);

    }

    public MutableLiveData<List<SenderReciverPojo>> getFrndContract()
    {
        return messageRespos.getFrndContract();
    }

    public LiveData<List<SenderReciverPojo>> getAllMessage(String reciverID) {
        return messageRespos.getallMessage(reciverID);
    }

    public void sendImage(final SenderReciverPojo senderReciverPojo, File file, Context context) {
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
                    senderReciverPojo.setImage(downloadUri.toString());
                    messageRespos.sendImages(senderReciverPojo);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }


    public MutableLiveData<SenderReciverPojo> getLastMsg(String reciverID) {

        return messageRespos.geLastmsg(reciverID);
    }

    public void setReadStatus(String reciverID) {
        messageRespos.setReadStatus(reciverID);
    }

    public void deleteMessage(String friendID) {
        messageRespos.deleteMessage(friendID);
    }

    public void blockFriend(BlockPojo blockPojo) {
        messageRespos.blockFriend(blockPojo);
    }

    public MutableLiveData<List<BlockPojo>> getBlockList() {

       return messageRespos.getBlockList();
    }

    public void UnblockFriend(String frndID) {
        messageRespos.UnblockFriend(frndID);
    }

    public void CallToFriend(String reciverID, CallingPojo CurrentauthPojo) {
        messageRespos.CallToFriend(reciverID, CurrentauthPojo);
    }

    public MutableLiveData<List<CallingPojo>> getNowCalling() {

        return messageRespos.getNowCalling();
    }

  public MutableLiveData<List<CallingPojo>> getRecivingStatus() {

        return messageRespos.getRecvingStatus();
    }


    public void CallCancel(String reciverID) {
        messageRespos.CallCanel(reciverID);
    }

    public MutableLiveData<String> ReceiveCall(CallingPojo callingPojo) {
      return   messageRespos.ReceiveCall(callingPojo);
    }

    public void deleteRecivingStatus() {

        messageRespos.deleteRecivingStatus();
    }

    public LiveData<List<SenderReciverPojo>> getAllSharedMedia(String frndID) {
        return messageRespos.getSharedMedia(frndID);
    }

    public void removeMessage(String msgID,String senderID,String receiverID) {
        messageRespos.removeMessage(msgID,senderID,receiverID);
    }
}
