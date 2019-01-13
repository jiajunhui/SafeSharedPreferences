package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Taurus on 2019/1/12.
 */
class MethodInvokerGetAll implements MethodInvoker {

    @Override
    public Bundle invoke(Context context, String arg, Bundle extras) {
        if(context==null|| TextUtils.isEmpty(arg))
            return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(arg, Context.MODE_PRIVATE);
        Map<String, ?> all = sharedPreferences.getAll();
        if(all!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(OptionParams.KEY_VALUE, (Serializable) all);
            return bundle;
        }
        return null;
    }

}
