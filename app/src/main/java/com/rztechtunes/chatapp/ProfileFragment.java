package com.rztechtunes.chatapp;

import android.Manifest;
import android.content.Context;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hbb20.CountryCodePicker;
import com.rztechtunes.chatapp.adapter.MyStoriesAdapter;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {

    private static final int REQUEST_STORAGE_CODE = 123;
    private static final int REQUEST_CAMERA_CODE = 123;
    private static final int REQUEST_CAMERA_CODE_COVER = 852;
    private static final int GALLERY_REQUEST_CODE = 456;
    private static final int GALLERY_REQUEST_CODE_COVER = 963;
    private String profilePath,coverPath;
    private File profileFile,coverFile;
    TextView nameTV, emailTV, phoneTV, hobbyTV, countyTV, statusTV;
    ImageView profileImage, coverImageView, flagImageView,changeProfile,changeCoverPic;
    AuthViewModel authViewModel;
    RecyclerView medidaRV;
    FriendViewModel friendViewModel;
    TextView editStatus,editNameTV,editHobbyTV;

    private CountryCodePicker ccp;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profile_image);
        coverImageView = view.findViewById(R.id.coverImageView);
        nameTV = view.findViewById(R.id.nameTV);
        editNameTV = view.findViewById(R.id.editNameTV);
        emailTV = view.findViewById(R.id.gmailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        hobbyTV = view.findViewById(R.id.aboutTV);
        editHobbyTV = view.findViewById(R.id.editHobby);
        statusTV = view.findViewById(R.id.statusTV);
        editStatus = view.findViewById(R.id.editStatus);
        countyTV = view.findViewById(R.id.countyTV);
        medidaRV = view.findViewById(R.id.mediaRV);
        changeProfile = view.findViewById(R.id.changeProfileImage);
        changeCoverPic= view.findViewById(R.id.chnageCoverPhoto);
        medidaRV = view.findViewById(R.id.mediaRV);
        flagImageView = view.findViewById(R.id.fragImage);
        ccp = new CountryCodePicker(getContext());


        changeCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoverPic();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelected();
            }
        });


        editNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_update_name, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));

                EditText nameET =  bottomSheetView.findViewById(R.id.nameET);

                bottomSheetView.findViewById(R.id.saveNameBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = nameET.getText().toString();

                        try {
                            if (newName.equals(""))
                            {
                                nameET.setError("Enter name");
                            }
                            else
                            {
                                authViewModel.updateName(newName);
                                Toast.makeText(requireActivity(), "Name Updated!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
                            Log.i(TAG, "ex: "+e.getLocalizedMessage());
                        }


                        bottomSheetDialog.dismiss();
                    }
                });



                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_update_status, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));

                EditText statusET =  bottomSheetView.findViewById(R.id.statusET);

                bottomSheetView.findViewById(R.id.saveSatusBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newStatus = statusET.getText().toString();

                            try {
                                if (newStatus.equals(""))
                                {
                                    statusET.setError("Enter new status");
                                }
                                else
                                {
                                    authViewModel.updateStatus(newStatus);
                                    Toast.makeText(requireActivity(), "Status Updated!", Toast.LENGTH_SHORT).show();

                                }

                            }
                            catch (Exception e)
                            {
                                Log.i(TAG, "ex: "+e.getLocalizedMessage());
                            }


                        bottomSheetDialog.dismiss();
                    }
                });



                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });


        editHobbyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_update_hobby, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));

                EditText hobbyET =  bottomSheetView.findViewById(R.id.hobbyET);

                bottomSheetView.findViewById(R.id.saveHobbyBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newHobby = hobbyET.getText().toString();

                        try {
                            if (newHobby.equals(""))
                            {
                                hobbyET.setError("Enter new Hobby");
                            }
                            else
                            {
                                authViewModel.updateHobby(newHobby);
                                Toast.makeText(requireActivity(), "Hobby Updated!", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e)
                        {
                            Log.i(TAG, "ex: "+e.getLocalizedMessage());
                        }


                        bottomSheetDialog.dismiss();
                    }
                });



                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();



            }
        });


        try {
            authViewModel.getUserInfo().observe(getActivity(), new Observer<UserInformationPojo>() {
                @Override
                public void onChanged(UserInformationPojo authPojo) {



                    //Picasso.get().load(authPojo.getImage()).into(profileImage);
                    Glide.with(getActivity())
                            .load(authPojo.getprofileImage())
                            .centerCrop()
                            .placeholder(R.drawable.ic_perm_)
                            .into(profileImage);
                    Glide.with(getActivity())
                            .load(authPojo.getCoverImage())
                            .centerCrop()
                            .placeholder(R.drawable.ic_perm_)
                            .into(coverImageView);

                    nameTV.setText(authPojo.getName());
                    emailTV.setText(authPojo.getEmail());
                    phoneTV.setText(authPojo.getPhone());
                    hobbyTV.setText(authPojo.gethobby());
                    statusTV.setText(authPojo.getStatus());
                    countyTV.setText(authPojo.getCountry());

                    String county = authPojo.getCountry();
                    Log.i(TAG, "coutnyName: " + county);
                    String[] splitCounty = county.split("-");
                    countyTV.setText(splitCounty[1]);
                    flagImageView.setImageResource(Integer.parseInt(splitCounty[0])); //get county code


                }
            });

            friendViewModel.getMyStories().observe(getActivity(), new Observer<List<StoriesPojo>>() {
                @Override
                public void onChanged(List<StoriesPojo> storiesPojos) {
                    MyStoriesAdapter myStoriesAdapter = new MyStoriesAdapter(storiesPojos, getContext());
                    GridLayoutManager gll = new GridLayoutManager(getContext(), 2);
                    medidaRV.setLayoutManager(gll);
                    medidaRV.setAdapter(myStoriesAdapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
         /*   if (requestCode==REQUEST_CAMERA_CODE)
            {
                dispatchCameraIntent();
            }*/


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
                profileImage.setImageBitmap(bmp);
                authViewModel.changePhoto(getContext(),profileFile,0);

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
                profileImage.setImageBitmap(bmp);
                authViewModel.changePhoto(getContext(),profileFile,0);
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
                authViewModel.changePhoto(getContext(),coverFile,1);
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
            coverFile = new File(coverPath);
            Uri fileUri = Uri.fromFile(coverFile);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                coverImageView.setImageBitmap(bmp);
                authViewModel.changePhoto(getContext(),coverFile,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //addPictureCard.setVisibility(View.GONE);

        }



    }
}