package com.james.wawamachine;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ThirdFragment extends Fragment{
    String TAG = ThirdFragment.class.getSimpleName();
    private EditText emailEditText;
    private EditText passEditText;
    private TextInputLayout accoutLayout,passwordLayout;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private View view;
    private String account, password;


    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.third_fragment, container, false);
        initView(view);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCheck()){
                    setLoginData(account, password);
                }

            }
        });



        return view;
    }
    private void initView(View view) {
        mAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) view.findViewById(R.id.username);
        passEditText = (EditText) view.findViewById(R.id.password);
        accoutLayout = (TextInputLayout) view.findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.password_layout);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
        signUpBtn = (Button) view.findViewById(R.id.signup_button);
        //...
    }
    private boolean inputCheck(){
        account = emailEditText.getText().toString();
        password = passEditText.getText().toString();
        if(TextUtils.isEmpty(account)){
            accoutLayout.setError(getString(R.string.plz_input_accout));
            passwordLayout.setError("");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            accoutLayout.setError("");
            passwordLayout.setError(getString(R.string.plz_input_pw));
            return false;
        }
        accoutLayout.setError("");
        passwordLayout.setError("");
        return true;
    }
    private void setLoginData(String account, String password){
        mAuth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
