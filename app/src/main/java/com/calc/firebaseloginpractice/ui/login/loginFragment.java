package com.calc.firebaseloginpractice.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.calc.firebaseloginpractice.R;
import com.calc.firebaseloginpractice.ui.home.homeFragment;
import com.calc.firebaseloginpractice.utils.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class loginFragment extends Fragment
{
    private View mainView;
    private EditText emailFiled;
    private EditText passwordFiled;
    private TextView loginBtn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView=inflater.inflate(R.layout.fragment_login,null);
        return mainView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        constants.initProgress(requireContext(),"Please Wait...");
    }

    private void initView()
    {
        emailFiled=mainView.findViewById(R.id.login_email_filed);
        passwordFiled=mainView.findViewById(R.id.login_password_filed);
        loginBtn=mainView.findViewById(R.id.login_login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = emailFiled.getText().toString();
                String password= passwordFiled.getText().toString();

                if (email.isEmpty()|| password.isEmpty())
                {
                constants.showToast(requireContext(),"Please write your mail and password");
                return;
                }
                constants.showProgress();
                loginFirebase(email,password);
            }
            });
        }

    private void loginFirebase(String email, String password)
    {
    constants.getAuth().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    constants.dismissProgress();
                    if (task.isSuccessful())
                    {
                        if (task.getResult().getUser().isEmailVerified())
                        {
                            constants.saveUid(requireActivity(),task.getResult().getUser().getUid());
                            constants.replaceFragment(loginFragment.this,new  homeFragment(),false);
                        } else if (!task.getResult().getUser().isEmailVerified())
                        {

                            constants.showToast(requireContext(),"Please Check your mailbox to verify your Account");

                        }

                    }  else

                    {
                      constants.showToast(requireContext(),task.getException().getMessage());

                    }
                }
            });
    }
}
