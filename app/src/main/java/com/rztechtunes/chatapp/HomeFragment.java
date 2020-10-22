package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.rztechtunes.chatapp.adapter.ViewPagerAdapter;
import com.rztechtunes.chatapp.group_chat.GroupFragment;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;


import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ChatFragment chatFragment;
    private GroupFragment groupFragment;
    private ContractFragment contractFragment;
    private ImageView profile_image;
    private AuthViewModel authViewModel;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        profile_image = view.findViewById(R.id.profile_image);
        chatFragment = new ChatFragment();
        groupFragment = new GroupFragment();
        contractFragment = new ContractFragment();
        contractFragment = new ContractFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),1);
        viewPagerAdapter.addFragment(chatFragment,"Chats");
        viewPagerAdapter.addFragment(groupFragment,"Groups");
        viewPagerAdapter.addFragment(contractFragment,"People");
        viewPager.setAdapter(viewPagerAdapter);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.profileFragment);
            }
        });

        authViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo authPojo) {

                Glide.with(requireActivity())
                        .load(authPojo.getprofileImage())
                        .centerInside()
                        .placeholder(R.drawable.ic_perm_)
                        .into(profile_image);

            }
        });


    }

/*    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.setting_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.setting_menu:
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }*/
}