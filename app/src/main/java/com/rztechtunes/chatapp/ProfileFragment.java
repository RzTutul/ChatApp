package com.rztechtunes.chatapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbb20.CountryCodePicker;
import com.rztechtunes.chatapp.adapter.MyStoriesAdapter;
import com.rztechtunes.chatapp.adapter.StoriesAdapter;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {

    TextView nameTV, emailTV, phoneTV, hobbyTV, countyTV, statusTV;
    ImageView profileImage, coverImageView, flagImageView;
    AuthViewModel authViewModel;
    RecyclerView medidaRV;
    FirendViewModel firendViewModel;
    private CountryCodePicker ccp;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        firendViewModel = ViewModelProviders.of(this).get(FirendViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profile_image);
        coverImageView = view.findViewById(R.id.coverImageView);
        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.gmailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        hobbyTV = view.findViewById(R.id.aboutTV);
        statusTV = view.findViewById(R.id.statusTV);
        countyTV = view.findViewById(R.id.countyTV);
        medidaRV = view.findViewById(R.id.mediaRV);
        flagImageView = view.findViewById(R.id.fragImage);
        ccp = new CountryCodePicker(getContext());


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

        firendViewModel.getMyStories().observe(getActivity(), new Observer<List<StoriesPojo>>() {
            @Override
            public void onChanged(List<StoriesPojo> storiesPojos) {
                MyStoriesAdapter myStoriesAdapter = new MyStoriesAdapter(storiesPojos, getContext());
                GridLayoutManager gll = new GridLayoutManager(getContext(), 2);
                medidaRV.setLayoutManager(gll);
                medidaRV.setAdapter(myStoriesAdapter);
            }
        });

    }
}