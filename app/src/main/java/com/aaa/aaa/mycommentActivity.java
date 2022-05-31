package com.aaa.aaa;

/**
 * 게시글 리스트 (내 댓글 보기)
 **/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aaa.aaa.adpater.mycommentListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class mycommentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private mycommentListViewAdapter adapter;
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ArrayList<commentInfo> commentList;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomment);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        getSupportActionBar().setTitle("내가 작성한 댓글");
        final ArrayList<PostInfo> postList = new ArrayList<>();
        final ArrayList<String> postIdList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        //FireStore에서 게시글 정보 받아오기
        database.collection("comment")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                //시간순 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postIdList.add(document.getData().get("postKey").toString());
                            }
                        } else {
                        }
                    }
                });

        /** 리사이클러 뷰(게시글 리스트) 생성**/
        commentList=new ArrayList<>();
        database= FirebaseFirestore.getInstance();

        //리사이클러 뷰 생성
        recyclerView= findViewById(R.id.mycommentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mycommentActivity.this));
        adapter = new mycommentListViewAdapter(mycommentActivity.this,commentList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        commentUpdate();
    }

    private void commentUpdate(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = database.collection("comment");
        collectionReference
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("comment_uid",user.getUid())
                //시간순 정렬
                .orderBy("comment_time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            commentList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                commentList.add(new commentInfo(
                                        document.getData().get("comment_id").toString(),
                                        document.getData().get("comment_uid").toString(),
                                        document.getData().get("comment_content").toString(),
                                        document.getData().get("post_id").toString(),
                                        new Date(document.getDate("comment_time").getTime())));
                            }

                            //리사이클러 뷰 생성
                            recyclerView= findViewById(R.id.mycommentRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mycommentActivity.this));
                            adapter = new mycommentListViewAdapter(mycommentActivity.this,commentList);
                            recyclerView.setAdapter(adapter);
                        } else {
                        }
                    }
                });
    }
}
