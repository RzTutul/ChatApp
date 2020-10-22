package com.rztechtunes.chatapp.auth_frag;

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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class SignupFragment extends Fragment {

    private static final int REQUEST_STORAGE_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 123;
    private static final int REQUEST_CAMERA_CODE_COVER = 852;
    private static final int GALLERY_REQUEST_CODE = 456;
    private static final int GALLERY_REQUEST_CODE_COVER = 963;
    private EditText nameET,statusET, emailET, hobbyET;
    private FloatingActionButton saveBtn;
    private AuthViewModel authViewModel;
    UserInformationPojo authPojo;
    private CountryCodePicker ccp;
    public static String country;
    ImageView selectProfile, selectCoverPic, profileImagView,coverImageView;
    private String profilePath,coverPath;
    private File profileFile,coverFile;
    public static String phone;
    String uui;
    int profile;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameET = view.findViewById(R.id.nameET);
        statusET = view.findViewById(R.id.statusET);
        emailET = view.findViewById(R.id.gmilET);
        hobbyET = view.findViewById(R.id.hobbyET);
        saveBtn = view.findViewById(R.id.btn_sign);

        selectProfile = view.findViewById(R.id.selectProfile);
        profileImagView = view.findViewById(R.id.profile_image);
        selectCoverPic = view.findViewById(R.id.selectCover);
        coverImageView = view.findViewById(R.id.coverImageView);

        selectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    pictureSelected();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        selectCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCoverPic();

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameET.getText().toString();
                String status = statusET.getText().toString();
                String email = emailET.getText().toString();
                String hobby = hobbyET.getText().toString();


                if (profileFile == null) {

                    Toast.makeText(getActivity(), "Select a Profile Image", Toast.LENGTH_SHORT).show();
                }
                    if (coverFile == null) {

                    Toast.makeText(getActivity(), "Select a Cover Image", Toast.LENGTH_SHORT).show();
                }


                else if (name.equals("")) {
                    nameET.setError("Set a name!");
                }
                     else if (status.equals("")) {
                    nameET.setError("Set a status!");
                }

                else if (email.equals("")) {
                    emailET.setError("Set a email!");
                } else if (hobby.equals("")) {
                    hobbyET.setError("Set your Hobby!");
                } else {
                    authPojo = new UserInformationPojo("", name, email, phone, hobby, country, "","",status, "Online");
                    authViewModel.setUserInfo(getActivity(), profileFile,coverFile, authPojo);
                }


                //
            }
        });
     /*   authViewModel.stateLiveData.observe(getActivity(), new Observer<AuthViewModel.AuthenticationState>() {
            @Override
            public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
                        break;
                    case UNAUTHENTICATED:
                        break;
                }

            }
        });*/

        authViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<UserInformationPojo>>() {
            @Override
            public void onChanged(List<UserInformationPojo> userInformationPojos) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    uui = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }

                if (userInformationPojos.size() > 0) {
                    for (UserInformationPojo contractPojo : userInformationPojos) {


                        if (contractPojo.getU_ID() !=null)
                        {
                            if ((contractPojo.getU_ID()).equals(uui)) {
                                Navigation.findNavController(view).navigate(R.id.homeFragment);
                                break;
                            }
                        }



                    }

                }


            }
        });


    }

    private void selectCoverPic() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.cameraLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    dispatchCameraIntentForCoverPic();
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.gallaryLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkStoragePermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE_COVER);
                }

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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




        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ((getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && ( getActivity().checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED))
            {
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

            if (requestCode==REQUEST_CAMERA_CODE)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
            }


        }
    }



    private void dispatchCameraIntentForCoverPic()
    {
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
                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE_COVER);
            }
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
        profilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



            if (requestCode == REQUEST_CAMERA_CODE &&
                    resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: " + profilePath);
                profileFile = new File(profilePath);
                Uri fileUri = Uri.fromFile(profileFile);
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                    profileImagView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // addPictureCard.setVisibility(View.GONE);
            } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(projection[0]);
                profilePath = cursor.getString(index);
                Log.e(TAG, "onActivityResultgalary: " + profilePath);
                profileFile = new File(profilePath);
                Uri fileUri = Uri.fromFile(profileFile);
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                    profileImagView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //addPictureCard.setVisibility(View.GONE);

            }


            if (requestCode == REQUEST_CAMERA_CODE_COVER &&
                    resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: " + coverPath);
                coverFile = new File(coverPath);
                Uri fileUri = Uri.fromFile(coverFile);
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                    coverImageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // addPictureCard.setVisibility(View.GONE);
            } else if (requestCode == GALLERY_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(projection[0]);
                coverPath = cursor.getString(index);
                Log.e(TAG, "onActivityResultgalary: " + coverPath);
                coverFile = new File(coverPath);
                Uri fileUri = Uri.fromFile(coverFile);
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                    coverImageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //addPictureCard.setVisibility(View.GONE);

            }



    }
}

