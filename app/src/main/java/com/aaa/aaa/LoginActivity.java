package com.aaa.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button sign;
    Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser user = mAuth.getCurrentUser();
        //회원가입 버튼
        sign = findViewById(R.id.signin);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, signupActivity.class);
            startActivity(intent);
        });

        //로그인 버튼 클릭 시
        login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            login();
        });

        //자동 로그인
        if (user!=null){
            StartMyActivity(BottomActivity.class);
            finish();
        }
    }

    //로그인 함수
    public void login() {
        String email = ((EditText) findViewById(R.id.loginEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
        final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                loaderLayout.setVisibility(View.GONE);
                                StartMyActivity(BottomActivity.class);
                                toast("로그인에 성공하였습니다.");
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                loaderLayout.setVisibility(View.GONE);
                                toast("아이디 혹은 비밀번호가 맞지 않습니다.");
                            }
                        }
                    });
        }else{
            toast("이메일과 비밀번호를 정확히 입력해주세요.");
        }
    }

    //토스트 메세지
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //액티비티 이동 함수
    public void StartMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}