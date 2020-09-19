package com.rztechtunes.chatapp.auth_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rztechtunes.chatapp.R;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


public class PhoneVarifyFragment extends Fragment {

    private TextView noticeTV;
    private MaterialButton submitBtn;

    private String mVerificationId;
    private FirebaseAuth firebaseAuth;
    public static String phoneNumber;
    private TextInputEditText otp1,otp2,otp3,otp4,otp5,otp6;
    private String verificationCode;
    private TextView btnTimer,btnReenter;

    public PhoneVarifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_varify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        noticeTV = view.findViewById(R.id.noticeTV);
        submitBtn = view.findViewById(R.id.btn_submit);
        otp1 = view.findViewById(R.id.ed_otp1);
        otp2 = view.findViewById(R.id.ed_otp2);
        otp3 = view.findViewById(R.id.ed_otp3);
        otp4 = view.findViewById(R.id.ed_otp4);
        otp5 = view.findViewById(R.id.ed_otp5);
        otp6 = view.findViewById(R.id.ed_otp6);
        btnTimer = view.findViewById(R.id.btn_timer);
        btnReenter = view.findViewById(R.id.btn_reenter);

        SignupFragment.phone = phoneNumber;

        noticeTV.setText("We have sent you an SMS on" + phoneNumber + "\n+with 6 digit verification code");


        sendVerificationCode(phoneNumber);

        Log.i(TAG, "onViewCreated: "+phoneNumber);

        try {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    btnTimer.setText(seconds + " Secound Wait");
                }

                @Override
                public void onFinish() {
                    btnReenter.setVisibility(View.VISIBLE);
                    btnTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificationCode = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();

               verifyCode(verificationCode);

                Log.i(TAG, "onClick: "+verificationCode);

            }
        });

        btnReenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnReenter.setVisibility(View.GONE);
                btnTimer.setVisibility(View.VISIBLE);
                try {
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                            btnTimer.setText(seconds + " Secound Wait");
                        }

                        @Override
                        public void onFinish() {
                            btnReenter.setVisibility(View.VISIBLE);
                            btnTimer.setVisibility(View.GONE);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendVerificationCode(phoneNumber);
            }
        });


    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        /*    String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp1.setText("" + code.substring(0, 1));
                otp2.setText("" + code.substring(1, 2));
                otp3.setText("" + code.substring(2, 3));
                otp4.setText("" + code.substring(3, 4));
                otp5.setText("" + code.substring(4, 5));
                otp6.setText("" + code.substring(5, 6));

            }*/
            //verifyCode(verificationCode);

            signInWithCredential(phoneAuthCredential);
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Successful Verified! ", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.signupFragment);
                }
                else
                {
                    Toast.makeText(getContext(), "fails", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}