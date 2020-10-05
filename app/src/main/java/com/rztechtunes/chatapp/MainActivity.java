package com.rztechtunes.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;

import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    AuthViewModel authViewModel = new AuthViewModel();
    MessageViewModel messageViewModel = new MessageViewModel();
    NavController navController;
    boolean isExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId())
                {
                    case R.id.homeFragment:
                        isExit = true;
                        break;
                    case R.id.sendMessageFragment:
                        isExit = false;
                    default:
                        isExit =false;

                }
            }
        });



        authViewModel.stateLiveData.observe(this, new Observer<AuthViewModel.AuthenticationState>() {
            @Override
            public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        messageViewModel.getNowCalling().observe(MainActivity.this, new Observer<List<CallingPojo>>() {
                            @Override
                            public void onChanged(List<CallingPojo> userInformationPojos) {

                                CallingPojo userinfo = userInformationPojos.get(userInformationPojos.size()-1);
                                IncomingCallFrag.callingPojo  = userinfo;
                                Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment).navigate(R.id.incomingCallFrag);
                            }
                        });                    case UNAUTHENTICATED:
                }
            }
        });



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
                        authViewModel.setUserSatus("last seen "+HelperUtils.getDateWithTime());
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
                        authViewModel.setUserSatus("Online");
                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (isExit)
        {
            authViewModel.stateLiveData.observe(this, new Observer<AuthViewModel.AuthenticationState>() {
                @Override
                public void onChanged(AuthViewModel.AuthenticationState authenticationState) {
                    switch (authenticationState)
                    {
                        case AUTHENTICATED:
                            authViewModel.setUserSatus("Last seen "+HelperUtils.getDateWithTime());
                            MainActivity.this.finish();
                            break;
                        case UNAUTHENTICATED:
                            break;
                    }
                }
            });


        }
        else
        {
            super.onBackPressed();

        }

    }
}