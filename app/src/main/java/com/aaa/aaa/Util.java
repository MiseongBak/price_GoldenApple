package com.aaa.aaa;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;
/* 자주 쓰는 유틸 클래스 */
public class Util {
    public Util(){/* */}

    /* 토스트 함수 */
    public static void showToast(Activity activity,String msg){
        Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
    }

    /* 파이어베이스 스토리지 이미지 링크 확인 함수 */
    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches() && url.contains(
                "https://firebasestorage.googleapis.com/v0/b/golden-apple-3b2c9.appspot.com/o/post");
    }
}
