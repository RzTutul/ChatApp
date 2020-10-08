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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.adapter.SelectGroupContractListAdaper;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class CreateGroupFrag extends Fragment {

    RecyclerView contractRV;
    AuthViewModel authViewModel;
    GroupViewModel groupViewModel;
    FriendViewModel friendViewModel;
    FloatingActionButton createBtn;

    List<UserInformationPojo> contractPojoList = new ArrayList<>();
    List<UserInformationPojo> selectedContractList = new ArrayList<>();
    SelectGroupContractListAdaper selectGroupContractListAdaper;
    UserInformationPojo myContractInfo ;
    ImageView groupImage;
    EditText groupNameET,descriptionET;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 321;
    private static final int REQUEST_STORAGE_CODE = 456;
    private String currentPhotoPath;
    private File file;
    public CreateGroupFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contractRV = view.findViewById(R.id.contractRV);
        createBtn = view.findViewById(R.id.createGroupBtn);
        groupNameET = view.findViewById(R.id.groupNameET);
        descriptionET = view.findViewById(R.id.groupDiscription);
        groupImage = view.findViewById(R.id.imageView);
        myContractInfo = new UserInformationPojo();

        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelected();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedContractList = selectGroupContractListAdaper.getSelectedContract();
                String grpName = groupNameET.getText().toString().trim();
                String grpDesrption = descriptionET.getText().toString().trim();

                GroupPojo groupPojo = new GroupPojo("","",grpName,grpDesrption,HelperUtils.getDateWithTime(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (file != null)
                {
                    if (grpName.equals(""))
                    {
                        groupNameET.setError("Give Group Name");

                    }
                    else if (grpDesrption.equals(""))
                    {
                        descriptionET.setError("Give a Description");
                    }
                    else if (selectedContractList.size()<=0)
                    {
                        Toast.makeText(getActivity(),"Select at list one participant", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        selectedContractList.add(myContractInfo);
                        groupViewModel.createNewGroup(selectedContractList,groupPojo,file,getContext());
                    }

                }
                else
                {
                    Toast.makeText(getActivity(),"Select group Image", Toast.LENGTH_SHORT).show();


                }


            }
        });

        groupViewModel.getCreateGrpStatus().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Successful"))
                {
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
                }
                else
                {

                }
            }
        });



        friendViewModel.getMyFirendList().observe(getActivity(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> userInformationPojos) {
                //for add my contract to addmin in this group
                authViewModel.getUserInfo().observe(getActivity(), new Observer<UserInformationPojo>() {
                    @Override
                    public void onChanged(UserInformationPojo authPojo) {
                        myContractInfo = authPojo;
                        myContractInfo.setSelected(true);

                        selectGroupContractListAdaper = new SelectGroupContractListAdaper(userInformationPojos,getActivity());
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        contractRV.setLayoutManager(llm);
                        contractRV.setAdapter(selectGroupContractListAdaper);
                    }
                });





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
                 groupImage.setImageBitmap(bmp);

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
                groupImage.setImageBitmap(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //addPictureCard.setVisibility(View.GONE);

        }
    }
}