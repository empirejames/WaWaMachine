package com.james.wawamachine;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        //passwordLayout.setErrorEnabled(true);
        //accoutLayout.setErrorEnabled(true);
        signUpBtn = (Button) view.findViewById(R.id.signup_button);
        //...
    }
    private boolean inputCheck(){
        account = emailEditText.getText().toString();
        if (!isValidEmail(account)) {
            accoutLayout.setError("Email格式錯誤");
        }
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
    private void setLoginData(final String account,final String password){
        mAuth.signInWithEmailAndPassword(account, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (!task.isSuccessful()) {
                            Log.e(TAG, task.getException().toString());
                            if (task.getException().toString().contains("The email address is already in use by another account.")) {
                                alertDialog("此 Email信箱已被註冊使用");
                            } else if (task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                                alertDialog("密碼輸入錯誤");
                            } else if (task.getException().toString().contains("There is no user record corresponding to this identifier. The user may have been deleted")) {
                                register(account, password);
                            }
                        } else {
                            //writeData();
//                            Intent i = new Intent(getActivity(), SetDataActivity.class);
//                            i.putExtra("name", user.getDisplayName());
//                            i.putExtra("uid", user.getUid());
//                            i.putExtra("email", user.getEmail());
//                            startActivity(i);
                            Intent i = new Intent(getActivity(),MemberActivity.class);
                            i.putExtra("name", user.getDisplayName());
                            i.putExtra("uid", user.getUid());
                            i.putExtra("email", user.getEmail());
                            startActivity(i);
                        }
                    }
                });

    }
    private void alertDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("確認", null)
                .show();
    }
    private void register(final String email, final String password) {
        new AlertDialog.Builder(getActivity())
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createUser(email, password);
                            }
                        })
                .setNeutralButton("取消", null)
                .show();
    }
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //String message = task.isSuccessful() ? "註冊成功" : "註冊失敗";
                Log.e(TAG, task.getException() + "");
                if (task.isSuccessful()) {
                    alertDialog("註冊成功");
                } else {
                    if (task.getException().toString().contains("The email address is already in use by another account.")) {
                        alertDialog("此 Email信箱已被註冊使用");
                    }
                }
            }
        });
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
