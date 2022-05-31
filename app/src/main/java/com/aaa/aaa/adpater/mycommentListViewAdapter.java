package com.aaa.aaa.adpater;

/**
 * 내가 쓴 댓글 리사이클러뷰 어댑터
 **/

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aaa.aaa.R;
import com.aaa.aaa.commentInfo;
import com.aaa.aaa.postActivity;
import com.aaa.aaa.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class mycommentListViewAdapter extends RecyclerView.Adapter<mycommentListViewAdapter.mycommentListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<commentInfo> items;
    private FirebaseFirestore database;
    private Intent intent;
    private ArrayList<PostInfo> postInfo;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Adapter 생성자 함수
    public class mycommentListViewHolder extends RecyclerView.ViewHolder {
        protected TextView content;
        protected TextView time;
        protected TextView title;

        public mycommentListViewHolder(@NonNull View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.mycommentText);
            this.time = (TextView) view.findViewById(R.id.mycommentTimeText);
            this.title = (TextView) view.findViewById(R.id.commentPostTitleText);
        }
    }

    public mycommentListViewAdapter(FragmentActivity activity, ArrayList<commentInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public mycommentListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycomment_view, viewGroup, false);
        mycommentListViewAdapter.mycommentListViewHolder mycommentListViewHolder = new mycommentListViewAdapter.mycommentListViewHolder(view);
        postInfo=new ArrayList<>();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), postActivity.class);
                int position = mycommentListViewHolder.getAdapterPosition();
                database.collection("post")
                        // 카테고리에 따라 게시글 받아오기
                        .whereEqualTo("postKey", items.get(position).getPost_id())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        intent.putExtra("postUid", document.getData().get("uid").toString());
                                        intent.putExtra("postContent", (ArrayList<String>) document.getData().get("content"));
                                        intent.putExtra("postCategory", document.getData().get("category").toString());
                                        intent.putExtra("postCreated", new Date(document.getDate("created").getTime()));
                                        intent.putExtra("postpostKey", document.getData().get("postKey").toString());
                                        intent.putExtra("postTitle", document.getData().get("title").toString());
                                        v.getContext().startActivity(intent);
                                    }
                                } else {
                                }
                            }
                        });


            }

        });
        return mycommentListViewHolder;
    }

    /* 리스트 뷰 텍스트 설정 */
    @Override
    public void onBindViewHolder(@NonNull mycommentListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        TextView title = holder.findViewById(R.id.commentPostTitleText);
        String post_id = items.get(position).getPost_id();
        database = FirebaseFirestore.getInstance();
        //FireStore에서 게시글 정보 받아오기
        database.collection("post")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("postKey", post_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                title.setText(document.getData().get("title").toString());
                            }
                        } else {
                        }
                    }
                });
        TextView content = holder.findViewById(R.id.mycommentText);
        content.setText(items.get(position).getComment_content());
        TextView date = holder.findViewById(R.id.mycommentTimeText);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (format_year.format(now).equals(format_year.format(items.get(position).getComment_time()))) {
            if (format_date.format(now).equals(format_date.format(items.get(position).getComment_time()))) {
                date.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(items.get(position).getComment_time()));
            } else {
                date.setText(new SimpleDateFormat("MM-dd", Locale.getDefault()).format(items.get(position).getComment_time()));
            }
        } else {
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(items.get(position).getComment_time()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

