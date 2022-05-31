package com.aaa.aaa;

/*** 게시글 리스트(정보) **/
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaa.aaa.adpater.communityListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class community_information extends Fragment {
    private RecyclerView recyclerView;
    private communityListViewAdapter adapter;
    private FirebaseFirestore database;
    private View v;
    private ArrayList<PostInfo> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_community_information, container, false);
        postList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        /* 리사이클러 뷰(게시글 리스트) 생성 */
        adapter= new communityListViewAdapter(getActivity(),postList);
        recyclerView=v.findViewById(R.id.informationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        /* 위로 올리면 새로고침 */
        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.informationRefreshLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(community_information.this).attach(community_information.this).commit();
                pullToRefresh.setRefreshing(false);
            }
        });
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        postUpdate();
    }

    private void postUpdate(){
        CollectionReference collectionReference = database.collection("post");
        collectionReference.whereEqualTo("category", "정보").orderBy("created", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postList.add(new PostInfo(
                                        document.getData().get("category").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("uid").toString(),
                                        new Date(document.getDate("created").getTime()),
                                        (ArrayList<String>) document.getData().get("content"),
                                        document.getData().get("postKey").toString()));
                            }
                            adapter.notifyDataSetChanged(); //데이터 변경(삭제, 수정 시)
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}