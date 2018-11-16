package weidong.com.ys1106.Utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
