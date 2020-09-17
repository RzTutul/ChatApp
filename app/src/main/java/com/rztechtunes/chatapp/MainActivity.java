package com.rztechtunes.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    AuthViewModel authViewModel = new AuthViewModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "MainonPause: ");
        authViewModel.stateLiveData.observe(this, new Observer<AuthViewModel.AuthenticationState>() {
            @Override
            public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        authViewModel.setUserSatus(HelperUtils.getDateWithTime());
                    case UNAUTHENTICATED:
                }
            }
        });


    }




    @Override
    public void onResume() {
        Log.i(TAG, "MainonResume: ");
        super.onResume();

        authViewModel.stateLiveData.observe(this, new Observer<AuthViewModel.AuthenticationState>() {
            @Override
            public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        authViewModel.setUserSatus("ONLINE");
                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });


    }
}