package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

class MethodInvokerContainsKey implements MethodInvoker {

    @Override
    public Bundle invoke(Context context, String arg, Bundle extras) {
        if(context==null|| TextUtils.isEmpty(arg)||extras==null)
            return null;
        String key = extras.getString(OptionParams.KEY_OPTION_KEY);
        SharedPreferences sharedPreferences = context.getSharedPreferences(arg, Context.MODE_PRIVATE);
        boolean contains = sharedPreferences.contains(key);
        Bundle bundle = new Bundle();
        bundle.putBoolean(OptionParams.KEY_VALUE, contains);
        return bundle;
    }

}
