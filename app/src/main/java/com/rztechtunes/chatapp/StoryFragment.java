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
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rztechtunes.chatapp.adapter.StoriesAdapter;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class StoryFragment extends Fragment {
    RecyclerView storiesRV;
    CardView addStoryCarview;
    private String currentPhotoPath;
    private File file;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 321;
    private static final int REQUEST_STORAGE_CODE = 456;

    FirendViewModel firendViewModel;
    AuthViewModel authViewModel;
    UserInformationPojo myInfo;
    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storiesRV = view.findViewById(R.id.storiesRV);
        addStoryCarview = view.findViewById(R.id.addStoryCarview);

        addStoryCarview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelected();
            }
        });

        authViewModel.getUserInfo().observe(getActivity(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {
                myInfo = authPojo;
            }
        });

        firendViewModel.addStoriesSuccefulLD.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("1"))
                {
                    getFriendStories();
                }
                else
                {
                    getFriendStories();
                }
            }
        });

        getFriendStories();
    }




    public void getFriendStories() {
        firendViewModel.getStories().observe(getActivity(), new Observer<List<StoriesPojo>>() {
            @Override
            public void onChanged(List<StoriesPojo> storiesPojos) {

                StoriesAdapter storiesAdapter = new StoriesAdapter(storiesPojos,getContext());
                GridLayoutManager glm =new GridLayoutManager(getContext(),2);
                storiesAdapter.notifyDataSetChanged();
                storiesRV.setLayoutManager(glm);
                storiesRV.setAdapter(storiesAdapter);
            }
        });
    }


    private void pictureSelected() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));
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
                resultCode == RESULT_OK) {
            Log.e(TAG, "onActivityResult: " + currentPhotoPath);
            file = new File(currentPhotoPath);
            Uri fileUri = Uri.fromFile(file);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                //  picImageBtn.setImageBitmap(bmp);
                StoriesPojo storiesPojo = new StoriesPojo(myInfo.getU_ID(),myInfo.getName(),myInfo.getImage(),"",HelperUtils.getDateWithTime());
                firendViewModel.addStories(storiesPojo, file, getActivity());

            } catch (IOException e) {
                e.printStackTrace();
            }

            // addPictureCard.setVisibility(View.GONE);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(projection[0]);
            currentPhotoPath = cursor.getString(index);
            Log.e(TAG, "onActivityResultgalary: " + currentPhotoPath);
            file = new File(currentPhotoPath);
            Uri fileUri = Uri.fromFile(file);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                // picImageBtn.setImageBitmap(bmp);
                StoriesPojo storiesPojo = new StoriesPojo(myInfo.getU_ID(),myInfo.getName(),myInfo.getImage(),"",HelperUtils.getDateWithTime());
                firendViewModel.addStories(storiesPojo, file, getActivity());

            } catch (IOException e) {
                e.printStackTrace();
            }

            //addPictureCard.setVisibility(View.GONE);

        }
    }
}