package com.rztechtunes.chatapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rztechtunes.chatapp.Notification.Client;
import com.rztechtunes.chatapp.Notification.Data;
import com.rztechtunes.chatapp.Notification.MyResponse;
import com.rztechtunes.chatapp.Notification.Sender;
import com.rztechtunes.chatapp.Notification.Token;
import com.rztechtunes.chatapp.adapter.MessageAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class SendMessageFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 321;
    private static final int REQUEST_STORAGE_CODE = 456;
    public static String reciverID;
   public  static  String reciverImage;
   public static  String reciverName;
    ImageButton sendMsgBtn,imageButn;
    EditText msgET;
    RecyclerView msgRV;
    MessageViewModel messageViewModel;
    AuthViewModel authViewModel;
    AuthPojo CurrentauthPojo;

    ImageView prfileImage;
    TextView nameTV,statusTV;
    private String currentPhotoPath;
    private File file;

   public static int position=-1;

    APIService apiService;

     boolean notify = false;
   String reciverOnlineStatus;
    String message;
    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //For send notificaiton
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        sendMsgBtn = view.findViewById(R.id.sentMsgBtn);
        msgET = view.findViewById(R.id.messageET);
        msgRV = view.findViewById(R.id.messageRV);
        prfileImage = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);
        statusTV = view.findViewById(R.id.statusTV);
        imageButn = view.findViewById(R.id.imageButn);


        nameTV.setText(reciverName);
        //Picasso.get().load(reciverImage).into(prfileImage);
        Glide.with(getActivity())
                .load(reciverImage)
                .placeholder(R.drawable.ic_perm_)
                .into(prfileImage);


        authViewModel.getUserInfo().observe(getActivity(), new Observer<AuthPojo>() {
            @Override
            public void onChanged(AuthPojo authPojo) {
                CurrentauthPojo =authPojo;
            }
        });

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 message= msgET.getText().toString().trim();

                if (message.equals(""))
                {
                    msgET.setError("Write something");
                }
                else
                {
                    notify = true;
                    SenderReciverPojo senderReciverPojo = new SenderReciverPojo("",message,"",reciverID,reciverName,reciverImage,CurrentauthPojo.getName(),CurrentauthPojo.getImage(),HelperUtils.getDateWithTime());
                    messageViewModel.sendMessage(senderReciverPojo);
                    msgET.setText("");
                }
                position = -1;


            }
        });

        authViewModel.getAllUser().observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
            @Override
            public void onChanged(List<AlluserContractPojo> alluserContractPojos) {
                for (AlluserContractPojo contractPojo : alluserContractPojos)
                {
                    if (reciverID.equals(contractPojo.getU_ID()))
                    {
                        statusTV.setText(contractPojo.getStatus());
                        reciverOnlineStatus = contractPojo.getStatus();

                    }
                }

            }
        });

        imageButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelected();
            }
        });



        if (position==-1)
        {
            messageViewModel.getAllMessage(reciverID).observe(getActivity(), new Observer<List<SenderReciverPojo>>() {
                @Override
                public void onChanged(List<SenderReciverPojo> senderReciverPojos) {

                    MessageAdaper messageAdaper = new MessageAdaper(senderReciverPojos,getActivity());

                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setStackFromEnd(true);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);

                    msgRV.setLayoutManager(llm);
                    msgRV.setAdapter(messageAdaper);

                    if (notify)
                    {

                        //If user status isn't online then send msg with notification
                        if (reciverOnlineStatus.equals("Online")) {

                        }
                        else
                        {
                            sendNotifiaction(reciverID, CurrentauthPojo.getName(), message);
                        }
                    }
                    notify = false;

                }

            });

        }
        else
        {
            messageViewModel.getAllMessage(reciverID).observe(getActivity(), new Observer<List<SenderReciverPojo>>() {
                @Override
                public void onChanged(List<SenderReciverPojo> senderReciverPojos) {


                    MessageAdaper messageAdaper = new MessageAdaper(senderReciverPojos,getActivity());

                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setStackFromEnd(true);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);

                    //goto that positon before image selected
                    llm.scrollToPositionWithOffset(position , 10);
                    msgRV.setLayoutManager(llm);
                    msgRV.setAdapter(messageAdaper);


                }

            });

        }



    }

    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",
                            reciverID);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Log.i(TAG, "onResponse: "+"fails");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        authViewModel.stateLiveData.observe(this, new Observer<AuthViewModel.AuthenticationState>() {
            @Override
            public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        authViewModel.setUserSatus("Online");
                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });
    }



    private void pictureSelected() {


        final BottomSheetDialog bottomSheetDialog =new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,(LinearLayout)getActivity().findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.cameraLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    dispatchCameraIntent();
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.gallaryLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkStoragePermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
                }

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();






    }

    private boolean checkStoragePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, REQUEST_STORAGE_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_CODE && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
          /*  if (requestCode==REQUEST_CAMERA_CODE)
            {
                dispatchCameraIntent();
            }*/


        }
    }


    private void dispatchCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.rztechtunes.chatapp",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        /* Create an image file name */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE &&
                resultCode == RESULT_OK){
            Log.e(TAG, "onActivityResult: "+currentPhotoPath);
            file = new File(currentPhotoPath);
            Uri fileUri = Uri.fromFile(file);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),fileUri);
              //  picImageBtn.setImageBitmap(bmp);
                SenderReciverPojo senderReciverPojo = new SenderReciverPojo("","","",reciverID,reciverName,reciverImage,CurrentauthPojo.getName(),CurrentauthPojo.getImage(),HelperUtils.getDateWithTime());
                messageViewModel.sendImage(senderReciverPojo,file,getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // addPictureCard.setVisibility(View.GONE);
        }

        else if  (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(projection[0]);
            currentPhotoPath = cursor.getString(index);
            Log.e(TAG, "onActivityResultgalary: "+currentPhotoPath);
            file = new File(currentPhotoPath);
            Uri fileUri = Uri.fromFile(file);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),fileUri);
               // picImageBtn.setImageBitmap(bmp);
                SenderReciverPojo senderReciverPojo = new SenderReciverPojo("","","",reciverID,reciverName,reciverImage,CurrentauthPojo.getName(),CurrentauthPojo.getImage(),HelperUtils.getDateWithTime());
                messageViewModel.sendImage(senderReciverPojo,file,getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //addPictureCard.setVisibility(View.GONE);

        }
    }

}
