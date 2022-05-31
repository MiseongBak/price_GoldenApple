package com.aaa.aaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class mypageEditActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore database;
    private FirebaseUser user;
    private Button Edit;
    private UserInfo userInfo;
    private EditText email,nickname, p_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_edit);
        getSupportActionBar().setTitle("회원 정보 수정");

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        email= (EditText) findViewById(R.id.mypageEmailText);
        email.setText(user.getEmail());
        nickname= (EditText) findViewById(R.id.mypageNickNameEditText);
        p_number= (EditText) findViewById(R.id.mypagePhoneNumberEditText);

        database=FirebaseFirestore.getInstance();
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nickname.setText(document.getData().get("name").toString());
                                p_number.setText(document.getData().get("phone_number").toString());
                            }
                        } else {
                        }
                    }
                });

        Edit=(Button) findViewById(R.id.mypageInformationEditButton);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p_number.getTextSize()>0&&nickname.getTextSize()>0){
                    if(p_number.getText().toString().matches("[+-]?\\d*(\\.\\d+)?") == false){
                        Util.showToast(mypageEditActivity.this,"전화번호는 숫자만 입력해주세요");
                    }else{
                        mypageEdit();

                        finish();
                    }

                }else{
                    Util.showToast(mypageEditActivity.this,"비어있는 칸이 있습니다.");
                }
            }
        });


    }

    private void mypageEdit(){
        String name= ((EditText) findViewById(R.id.mypageNickNameEditText)).getText().toString().trim();
        String phone_num= ((EditText) findViewById(R.id.mypagePhoneNumberEditText)).getText().toString().trim();
        int phone_number = Integer.parseInt(phone_num);
        database=FirebaseFirestore.getInstance();
        final RelativeLayout loaderLayout =findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);
        database=FirebaseFirestore.getInstance();
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userInfo= new UserInfo(name,
                                        phone_number,
                                        document.getData().get("uid").toString(),
                                        document.getData().get("profile_pic").toString());
                                database.collection("user").document(user.getUid()).set(userInfo);
                                loaderLayout.setVisibility(View.GONE);
                                Util.showToast(mypageEditActivity.this,"회원 정보 수정 완료");
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Util.showToast(mypageEditActivity.this,"회원 정보 수정 실패");
                        }
                    }
                });

    }
}