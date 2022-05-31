/*
  마이페이지 액티비티
 */
package com.aaa.aaa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class writeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private LinearLayout parent;
    private EditText selectedText;
    private RelativeLayout buttonsBackgroundLayout;
    private ImageView selectedImageView;

    private Date created;
    private final ArrayList<String> partList = new ArrayList<>();
    private int successCount = 0;
    private int partCount = 0;

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        parent = findViewById(R.id.contentsLayout);

        Toolbar tb = (Toolbar) findViewById(R.id.write_toolbar);
        setSupportActionBar(tb);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setTitle("글 쓰기"); //툴바 타이틀 설정

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생성

        firebaseAuth = FirebaseAuth.getInstance();
        buttonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);
        buttonsBackgroundLayout.setOnClickListener(onClickListener);
        findViewById(R.id.imageEditButton).setOnClickListener(onClickListener);
        findViewById(R.id.deleteImgButton).setOnClickListener(onClickListener);
        findViewById(R.id.contentsEditText).setOnFocusChangeListener(onFocusChangeListener);
        findViewById(R.id.titleText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                selectedText = null;
            }
        });

        // Spinner
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);


        findViewById(R.id.addimageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonsBackgroundLayout:
                    if (buttonsBackgroundLayout.getVisibility() == View.VISIBLE) {
                        buttonsBackgroundLayout.setVisibility(View.GONE);
                    }
                case R.id.imageEditButton:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;

                case R.id.deleteImgButton:
                    View selectedView = (View)selectedImageView.getParent();
                    partList.remove(parent.indexOfChild(selectedView) - 1);
                    parent.removeView(selectedView);
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /* 가리키는 EditText 설정 메소드 */
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                selectedText = (EditText) view;
            }
        }
    };

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.community_write, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();
                break;
            case R.id.menu_mypage:
                // User chose the "Settings" item, show the app settings UI...
                storageUpload();
                break;
        }
        return true;
    }


    /* 갤러리에서 사진을 가져왔을 경우 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {

                    Uri file = data.getData();
                    String uri = file.toString();

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout linearLayout = new LinearLayout(writeActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    // 사진 사이에 게시글 넣기
                    if (selectedText == null) {
                        parent.addView(linearLayout);
                        partList.add(uri);
                    } else {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            if (parent.getChildAt(i) == selectedText.getParent()) {
                                parent.addView(linearLayout, i + 1);
                                partList.add(i,uri);
                                break;
                            }
                        }
                    }

                    ImageView imageView = new ImageView(writeActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            buttonsBackgroundLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) view;
                            selectedImageView.setBackgroundResource(R.drawable.photo_frame);
                        }
                    });

                    Glide.with(this).load(uri).override(1000).into(imageView);
                    linearLayout.addView(imageView);

                    EditText editText = new EditText(writeActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setBackground(null);
                    editText.setHint("사진 설명");
                    editText.setOnFocusChangeListener(onFocusChangeListener);

                    linearLayout.addView(editText);
                } catch (Exception e) {
                }
            }
            //이미지 수정 시
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri file = data.getData();
                    String uri = file.toString();
                    partList.set(parent.indexOfChild((View) selectedImageView.getParent()) - 1, uri);
                    Glide.with(this).load(uri).override(1000).into(selectedImageView);
                } catch (Exception e) {
                }
            }
        } else {
        }
    }

    /* 게시글 업로드 함수 */
    private void storageUpload() {
        final RelativeLayout loaderLayout =findViewById(R.id.loaderLayout);

        //FireBase user 정보 가져오기
        user = FirebaseAuth.getInstance().getCurrentUser();
        //타이틀
        final String title = ((EditText) findViewById(R.id.titleText)).getText().toString();
        //스피너(카테고리)
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        String category = spinner.getSelectedItem().toString();
        String u_id = user.getUid();
        //시간 가져오기
        created = Calendar.getInstance().getTime();

        //FireBase 객체 설정
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final DocumentReference postRef = firebaseFirestore.collection("post").document();
        String postkey=postRef.getId(); // 게시글 아이디 가져오기

        ArrayList<String> contentList = new ArrayList<>();
        if(category.equals("카테고리를 선택하세요.")==false) {
            if (title.length() > 0) {
                loaderLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) parent.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        View view = linearLayout.getChildAt(j);
                        // EditText 읽기
                        if (view instanceof EditText) {
                            String text = ((EditText) view).getText().toString();
                            if (text.length() > 0) {
                                contentList.add(text);
                            }
                            // 사진 읽기
                        } else {
                            contentList.add(partList.get(partCount));
                            StorageReference riversRef = storageRef.child("post/" + postRef.getId() + "/" + (contentList.size() - 1) + ".jpg");
                            try {
                                //Storage에 사진 업로드
                                String url = partList.get(partCount);
                                Uri file = Uri.parse(url);
                                StorageMetadata metadata = new StorageMetadata.Builder()
                                        .setCustomMetadata("index", "" + (contentList.size() - 1)).build();
                                UploadTask uploadTask = riversRef.putFile(file, metadata);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.e("로그", "url:" + uri);
                                                contentList.set(index, uri.toString());
                                                successCount++;
                                                Log.e("로그", contentList.get(1));
                                                if (partList.size() == successCount) {
                                                    PostInfo PostInfo = new PostInfo(category, title, u_id, created, contentList, postkey);
                                                    postRef.set(PostInfo)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    loaderLayout.setVisibility(View.GONE);
                                                                    toast("글 업로드 완료");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    toast("글 업로드 실패");
                                                                }
                                                            });
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                });
                            } catch (Exception e) {
                            }
                            partCount++;
                        }
                    }
                }
                // 이미지가 없을 시
                if (partList.size() == 0) {
                    PostInfo PostInfo = new PostInfo(category, title, u_id, created, contentList, postkey);
                    postRef.set(PostInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    toast("글 업로드 완료");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("글 업로드 실패");
                                }
                            });
                    finish();
                }
            } else {
                toast("제목이 비어있습니다!");
            }
        }else{
            toast("카테고리를 선택해주세요.");
        }
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
