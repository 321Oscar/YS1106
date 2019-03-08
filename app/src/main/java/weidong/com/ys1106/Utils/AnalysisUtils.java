package weidong.com.ys1106.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class AnalysisUtils {
    /*
     * 本地的用户配置
     *  account 用户名
     *  pass 密码
     * boolean loginStatus (true/false)
     * */

    //读取用户名
    public static String readloginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String UserName = sp.getString("account", "");
        return UserName;
    }

    //存入用户名
    public static void saveLoginStatus(Context context,boolean status,String account){
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loginStatus",status);
        editor.putString("account",account);
        editor.apply();
    }

    //根据用户名读取密码
    public static String readloginpass(Context context,String account) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String psw = sp.getString(account, "");
        return psw;
    }

    //以用户名作为key存入密码
    public static void addloginPass(Context context,String pass,String account) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(account,pass);
        editor.apply();
    }

    //读取登录状态
    public static boolean readLoginStatus(Context context){
        SharedPreferences sp = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        return sp.getBoolean("loginStatus",false);
    }

    //清除登录状态
    public static void cleanlogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("loginStatus",false);
        editor.putString(AnalysisUtils.readloginUserName(context),"");
        editor.apply();
    }

    //获取视频某一帧作为图片显示
    /*
    * @param context 上下文
    * @param uri 视频地址
    * @param imageview 目标image
    * @param frameTimeMicros 获取某一时间帧*/
//    public static void LoadVideoScreenShot(final Context context, String uri, ImageView imageView,long frameTimeMicros){
//        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
//        requestOptions.set(null, MediaMetadataRetriever.OPTION_CLOSEST);
//        requestOptions.transform(new BitmapTransformation() {
//            @Override
//            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
//                return toTransform;
//            }
//
//            @Override
//            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
//                try {
//                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
//    }
}
