package weidong.com.ys1106.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AnalysisUtils {
    /*
     * 本地的用户配置
     *  account 用户名
     *  pass 密码
     * loginStatus
     * auto_login (1表示自动登录)，是否自动登录
     * rem（1表示记住） 是否记住密码
     * */

    //读取用户名
    public static String readloginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String UserName = sp.getString("account", "");
        return UserName;
    }

    //存入用户名
    public static void addloginUsername(Context context,String name) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account",name);
        editor.commit();
    }

    //读取密码
    public static String readloginpass(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String UserName = sp.getString("pass", "");
        return UserName;
    }

    //存入用户名
    public static void addloginPass(Context context,String pass) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pass",pass);
        editor.commit();
    }

    //读取登录状态

    //读取记住密码与否
    public static String readloginrem(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String UserName = sp.getString("rem", "");
        return UserName;
    }

    //读取自动登录与否
    public static String readloginauto(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String UserName = sp.getString("auto_login", "");
        return UserName;
    }

    //清除登录状态
    public static void cleanlogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", "");
        editor.putString("pass", "");
        editor.putString("auto_login", "");
        editor.putString("rem", "");
        editor.commit();
    }
}
