package com.lokarz.ligchatup.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lokarz.ligchatup.BuildConfig;
import com.lokarz.ligchatup.R;
import com.lokarz.ligchatup.Utils.ViewUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText userNameEt, passwordEt;
    private Button loginSignUpBtn;
    private String userName, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ViewUtil.getProgressDialog(this);
        checkCurrentUser();
        setContentView(R.layout.activity_login);
        userNameEt = (EditText) findViewById(R.id.userName_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        loginSignUpBtn = (Button) findViewById(R.id.login_signup_btn);

        loginSignUpBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_signup_btn:
                String err = "";
                userName = userNameEt.getText().toString();
                password = passwordEt.getText().toString();
                if(userName.isEmpty() || password.isEmpty()){
                    err = getString(R.string.sign_in_error_empty_field);
                }else if(password.length() < 8){
                    err = getString(R.string.sign_in_error_password);
                }

                if(err.isEmpty()){
                    signInUser(userName, password);
                }else{
                    ViewUtil.displayToastMsg(this, err);
                }

                break;
        }
    }

    private void checkCurrentUser(){
        if(firebaseAuth.getCurrentUser() != null){
            goToChatUp();
        }
    }

    private void createUser(String email, String pass){
        progressDialog.setMessage(getString(R.string.progress_sign_up));
        firebaseAuth.createUserWithEmailAndPassword(email + BuildConfig.LIG_DOMAIN, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            firebaseAuth.getInstance().getCurrentUser()
                                    .updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                goToChatUp();
                                            }else{
                                                manageTaskError(task);
                                            }
                                        }
                                    });

                        }else{
                            manageTaskError(task);
                        }
                    }
                });
    }

    private void signInUser(String userName, String pass){
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.progress_sign_in));
        firebaseAuth.signInWithEmailAndPassword(userName + BuildConfig.LIG_DOMAIN, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            goToChatUp();
                        }else{
                            manageTaskError(task);
                        }
                    }
                });
    }

    private void manageTaskError(Task task ){
        String errMsg = task.getException().getMessage();
        if(errMsg.equals(getString(R.string.error_task_noRecord))){
            createUser(userName, password);
        }else{
            ViewUtil.displayToastMsg(this, errMsg);
            progressDialog.dismiss();
        }


    }

    private void goToChatUp(){
        progressDialog.dismiss();
        Intent intent = new Intent(this, LigChatActivity.class);
        this.startActivity (intent);
    }

}
