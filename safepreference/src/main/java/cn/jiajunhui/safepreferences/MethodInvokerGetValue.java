package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Set;

class MethodInvokerGetValue implements MethodInvoker {

    @Override
    public Bundle invoke(Context context, String arg, Bundle extras) {
        if(context==null|| TextUtils.isEmpty(arg)||extras==null)
            return null;
        SharedPreferences preferences = context.getSharedPreferences(arg, Context.MODE_PRIVATE);
        int valueType = extras.getInt(OptionParams.KEY_VALUE_TYPE);
        String optionKey = extras.getString(OptionParams.KEY_OPTION_KEY);
        switch (valueType){
            case OptionParams.VALUE_TYPE_INT:
                int intValue = preferences.getInt(optionKey, extras.getInt(OptionParams.KEY_VALUE));
                extras.putInt(OptionParams.KEY_VALUE, intValue);
                return extras;
            case OptionParams.VALUE_TYPE_LONG:
                long longValue = preferences.getLong(optionKey, extras.getLong(OptionParams.KEY_VALUE));
                extras.putLong(OptionParams.KEY_VALUE, longValue);
                return extras;
            case OptionParams.VALUE_TYPE_FLOAT:
                float floatValue = preferences.getFloat(optionKey, extras.getFloat(OptionParams.KEY_VALUE));
                extras.putFloat(OptionParams.KEY_VALUE, floatValue);
                return extras;
            case OptionParams.VALUE_TYPE_BOOLEAN:
                boolean booleanValue = preferences.getBoolean(optionKey, extras.getBoolean(OptionParams.KEY_VALUE));
                extras.putBoolean(OptionParams.KEY_VALUE, booleanValue);
                return extras;
            case OptionParams.VALUE_TYPE_STRING:
                String stringValue = preferences.getString(optionKey, extras.getString(OptionParams.KEY_VALUE));
                extras.putString(OptionParams.KEY_VALUE, stringValue);
                return extras;
            case OptionParams.VALUE_TYPE_STRING_SET:
                Set<String> stringSetValue = preferences.getStringSet(optionKey, null);
                extras.putStringArrayList(OptionParams.KEY_VALUE, stringSetValue==null?null:new ArrayList<>(stringSetValue));
                return extras;
        }
        return null;
    }

}
