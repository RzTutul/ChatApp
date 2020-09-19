package com.rztechtunes.chatapp.auth_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;


public class LoginFragment extends Fragment {

    private MaterialButton btnSing;
    private CountryCodePicker ccp;
    private EditText phoneET;

    private AuthViewModel authViewModel ;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSing = view.findViewById(R.id.singBtn);
        ccp = view.findViewById(R.id.ccp);
        phoneET = view.findViewById(R.id.ed_phone);

        btnSing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = phoneET.getText().toString();

                //Check number start with 0 or other digit
                if ( (phone.charAt(0)) == '0') {
                    phone = phone.substring(1);
                }
                String code = ccp.getSelectedCountryCodeWithPlus();

                PhoneVarifyFragment.phoneNumber = code+phone;

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.phoneVarifyFragment);
            }
        });


        authViewModel.stateLiveData.observe(getActivity(), new Observer<AuthViewModel.AuthenticationState>() {
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
        });

    }


    }


