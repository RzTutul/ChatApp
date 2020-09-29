package com.rztechtunes.chatapp.group_chat;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
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
;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.GroupMessageAdaper;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;



public class GroupSendMessage extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 321;
    private static final int REQUEST_STORAGE_CODE = 456;

    public static String groupName;
    public static String groupID;
    public  static  String groupImage;

    Toolbar toolbar;
    ImageButton sendMsgBtn,imageButn;
    EditText msgET;
    RecyclerView msgRV;
    GroupViewModel groupViewModel;
    AuthViewModel authViewModel;
    AuthPojo CurrentauthPojo;

    ImageView prfileImage;
    TextView nameTV,statusTV;
    private String currentPhotoPath;
    private File file;
    String usersName ="you,";

    public GroupSendMessage() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        return inflater.inflate(R.layout.fragment_group_send_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sendMsgBtn = view.findViewById(R.id.sentMsgBtn);
        msgET = view.findViewById(R.id.messageET);
        msgRV = view.findViewById(R.id.messageRV);
        prfileImage = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);
        statusTV = view.findViewById(R.id.statusTV);
        imageButn = view.findViewById(R.id.imageButn);
        toolbar = view.findViewById(R.id.toolbar);


        nameTV.setText(groupName);
        //Picasso.get().load(reciverImage).into(prfileImage);
        Glide.with(getActivity())
                .load(groupImage)
                .placeholder(R.drawable.ic_perm_)
                .into(prfileImage);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupProfileFrag.grpID = groupID;
                GroupProfileFrag.grpName = groupName;
                GroupProfileFrag.grpImage = groupImage;
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.groupProfileFrag);
            }
        });


        authViewModel.getUserInfo().observe(getActivity(), new Observer<AuthPojo>() {
            @Override
            public void onChanged(AuthPojo authPojo) {
                CurrentauthPojo =authPojo;
            }
        });

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= msgET.getText().toString().trim();

                if (message.equals(""))
                {
                    msgET.setError("Write something");
                }
                else
                {
                    SendGroupMsgPojo sendGroupMsgPojo = new SendGroupMsgPojo(groupID,message,"",CurrentauthPojo.getU_ID(),CurrentauthPojo.getName(),CurrentauthPojo.getImage(), HelperUtils.getDateWithTime());
                    groupViewModel.sendGroupMsg(sendGroupMsgPojo);
                    msgET.setText("");
                }


            }
        });


        groupViewModel.getGroupUser(groupID).observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
            @Override
            public void onChanged(List<AlluserContractPojo> alluserContractPojos) {

                for (AlluserContractPojo contractPojo: alluserContractPojos)
                {
                    usersName = usersName+contractPojo.getName()+", ";
                }
                statusTV.setText(usersName);
                usersName ="";
            }
        });

   /*     authViewModel.getAllUser().observe(getActivity(), new Observer<List<AlluserContractPojo>>() {
            @Override
            public void onChanged(List<AlluserContractPojo> alluserContractPojos) {
                for (AlluserContractPojo contractPojo : alluserContractPojos)
                {
                    if (reciverID.equals(contractPojo.getU_ID()))
                    {
                        statusTV.setText(contractPojo.getStatus());
                    }
                }

            }
        });*/

        imageButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelected();
            }
        });

        groupViewModel.getAllGroupMessage(groupID).observe(getActivity(), new Observer<List<SendGroupMsgPojo>>() {
            @Override
            public void onChanged(List<SendGroupMsgPojo> sendGroupMsgPojos) {


                GroupMessageAdaper messageAdaper = new GroupMessageAdaper(sendGroupMsgPojos,getActivity());
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setStackFromEnd(true);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                msgRV.setLayoutManager(llm);
                msgRV.setAdapter(messageAdaper);


            }

        });

    }


    private void pictureSelected() {


        final BottomSheetDialog bottomSheetDialog =new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_send_image,(LinearLayout)getActivity().findViewById(R.id.bottomSheetContainer));
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
                SendGroupMsgPojo sendGroupMsgPojo = new SendGroupMsgPojo(groupID,"","",CurrentauthPojo.getU_ID(),CurrentauthPojo.getName(),CurrentauthPojo.getImage(),HelperUtils.getDateWithTime());
                groupViewModel.sendImage(sendGroupMsgPojo,file,getActivity());
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
                SendGroupMsgPojo sendGroupMsgPojo = new SendGroupMsgPojo(groupID,"","",CurrentauthPojo.getU_ID(),CurrentauthPojo.getName(),CurrentauthPojo.getImage(),HelperUtils.getDateWithTime());
                groupViewModel.sendImage(sendGroupMsgPojo,file,getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //addPictureCard.setVisibility(View.GONE);

        }
    }

}