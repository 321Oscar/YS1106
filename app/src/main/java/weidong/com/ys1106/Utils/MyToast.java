package weidong.com.ys1106.Utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    public static Toast myToast;
    public static void MyToastShow(Context context,String text){
        if(myToast == null){
            myToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            myToast.setText(text);
        }
        myToast.show();
    }
}
