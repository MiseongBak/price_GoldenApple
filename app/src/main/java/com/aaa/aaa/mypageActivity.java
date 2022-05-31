package com.aaa.aaa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;

public class mypageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button logout;
    private FirebaseUser user;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private Button mypost, mycomment,mypageEdit;
    private TextView name;
    private ArrayList<UserInfo> userinfo;

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        getSupportActionBar().setTitle("회원 정보");

        userinfo = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        name = (TextView) findViewById(R.id.mypageNameText);

        imageView = findViewById(R.id.profileimageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //로그아웃 버튼
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mypageActivity.this);
            builder.setTitle("로그아웃");
            builder.setMessage("로그아웃 하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog, int id) {
                    mAuth.signOut();
                    Intent intent = new Intent(mypageActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    toast("로그아웃에 성공하였습니다.");
                }
            });
            builder.setNegativeButton("취소",  new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        });


        mypost = findViewById(R.id.mypostButton);
        mypost.setOnClickListener(v -> {
            Intent intent = new Intent(this, mypostActivity.class);
            startActivity(intent);
        });

        mycomment = findViewById(R.id.mycommentButton);
        mycomment.setOnClickListener(v -> {
            Intent intent = new Intent(this, mycommentActivity.class);
            startActivity(intent);
        });

        mypageEdit = findViewById(R.id.mypageEditButton);
        mypageEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, mypageEditActivity.class);
            startActivity(intent);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    final RelativeLayout loaderLayout =findViewById(R.id.loaderLayout);
                    loaderLayout.setVisibility(View.VISIBLE);
                    final DocumentReference userRef = database.collection("user").document(user.getUid());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Uri file = data.getData();
                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("profilephoto/" + user.getUid() + "/profile.jpg");
                    UploadTask uploadTask = riversRef.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            loaderLayout.setVisibility(View.GONE);
                            toast("업로드 실패");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profile_uri = uri.toString();

                                    userRef.update("profile_pic", profile_uri)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loaderLayout.setVisibility(View.GONE);
                                                    Glide.with(mypageActivity.this).load(file).centerCrop().override(500).into(imageView);
                                                    toast("업로드 성공");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loaderLayout.setVisibility(View.GONE);
                                                    toast("업로드 실패");
                                                }
                                            });
                                }

                            });


                        }
                    });
                } catch (Exception e) {

                }
            }
        } else {

        }
    }


    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //액티비티 이동 함수
    public void StartMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void onResume(){
        super.onResume();
        mypageUpdate();
    }

    public void mypageUpdate(){
        database=FirebaseFirestore.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        userinfo=new ArrayList<>();
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        userinfo.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userinfo.add(new UserInfo(document.getData().get("name").toString(),
                                        document.getData().get("phone_number").hashCode(),
                                        document.getData().get("uid").toString(),
                                        document.getData().get("profile_pic").toString()));

                                name.setText(document.getData().get("name").toString());
                                if (document.getData().get("profile_pic").toString().equals("null")) {
                                    imageView.setImageResource(R.drawable.default_profile);
                                } else {
                                    String url = document.getData().get("profile_pic").toString();
                                    Uri file = Uri.parse(url);
                                    Glide.with(mypageActivity.this).load(url).centerCrop().override(500).into(imageView);
                                }
                            }
                        } else {
                        }
                    }
                });
    }
}
