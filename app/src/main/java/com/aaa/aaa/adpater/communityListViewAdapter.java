package com.aaa.aaa.adpater;

/**
 * 게시글 리사이클러뷰 어댑터
 **/

import static com.aaa.aaa.Util.isStorageUrl;

import android.content.Intent;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aaa.aaa.R;
import com.aaa.aaa.listener.OnPostListener;
import com.aaa.aaa.postActivity;
import com.aaa.aaa.PostInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class communityListViewAdapter extends RecyclerView.Adapter<communityListViewAdapter.communityListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<PostInfo> items;
    private Intent intent;
    private OnPostListener onPostListener;


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Adapter 생성자 함수
    public class communityListViewHolder extends RecyclerView.ViewHolder {
        public communityListViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public communityListViewAdapter(FragmentActivity activity, ArrayList<PostInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public communityListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_list_view, viewGroup, false);
        communityListViewHolder communityListViewHolder = new communityListViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), postActivity.class);
                int position = communityListViewHolder.getAdapterPosition();
                intent.putExtra("postTitle", items.get(position).getTitle());
                intent.putExtra("postUid", items.get(position).getUid());
                intent.putExtra("postContent", items.get(position).getContent());
                intent.putExtra("postCategory", items.get(position).getCategory());
                intent.putExtra("postCreated", items.get(position).getCreated());
                intent.putExtra("postpostKey", items.get(position).getPostKey());
                Iterator iter = items.get(position).getContent().iterator(); //Iterator 선언
                while (iter.hasNext()) {//다음값이 있는지 체크
                    Log.e("로그", "url:" + iter.next());
                }
                v.getContext().startActivity(intent);
            }
        });
        return communityListViewHolder;
    }

    public void setOnPostListener(OnPostListener onPostListener) {
        this.onPostListener = onPostListener;
    }

    @Override
    public void onBindViewHolder(@NonNull communityListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        TextView title = holder.findViewById(R.id.listTitleText);
        title.setText(items.get(position).getTitle());
        TextView content = holder.findViewById(R.id.listContentText);
        String listcon = "";
        ArrayList<String> con = items.get(position).getContent();
        for (int i = 0; i < con.size(); i++) {
            String contents = con.get(i);
            if (isStorageUrl(contents)) {
                if (listcon.length() > 25) {
                    listcon += "...";
                    break;
                } else {
                    listcon += "(이미지) ";
                }
            } else {
                if (contents.contains("\n")) {
                    contents = contents.replace("\n", "");
                }
                if ((listcon + contents).length() > 30) {
                    contents = contents.substring(0,28 - listcon.length()) + "...";
                    listcon+=contents;
                    break;
                } else {
                    listcon += contents +"";
                }

            }
        }
        content.setText(listcon);
        TextView date = holder.findViewById(R.id.listTimeText);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (format_year.format(now).equals(format_year.format(items.get(position).getCreated()))) {
            if (format_date.format(now).equals(format_date.format(items.get(position).getCreated()))) {
                date.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(items.get(position).getCreated()));
            } else {
                date.setText(new SimpleDateFormat("MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
            }
        } else {
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

