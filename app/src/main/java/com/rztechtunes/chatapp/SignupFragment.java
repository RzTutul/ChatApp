package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;

import javax.xml.namespace.QName;

import static android.content.ContentValues.TAG;

public class SignupFragment extends Fragment {

    private EditText nameET,phoneET,aboutET;
    private TextView saveBtn;
    private AuthViewModel authViewModel;
    private AuthPojo authPojo;
    private CountryCodePicker ccp;
    String number;
    public SignupFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameET = view.findViewById(R.id.ed_username);
        phoneET = view.findViewById(R.id.ed_phone);
        aboutET = view.findViewById(R.id.ed_password);
        saveBtn = view.findViewById(R.id.btn_sign);
        ccp = view.findViewById(R.id.ccp);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String phone = phoneET.getText().toString();
                String about = aboutET.getText().toString();

             Character value =phone.charAt(0);
             number = ccp.getSelectedCountryCodeWithPlus();
             if (value.equals('0'))
             {
                 //if phone number start with 0 then it remove 0
                phone = phone.substring(1);
             }
                authPojo = new AuthPojo("",name,phone,about,"");
                //authViewModel.setUserInfo(authPojo);

                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.phoneVarifyFragment);
            }
        });
    }
}