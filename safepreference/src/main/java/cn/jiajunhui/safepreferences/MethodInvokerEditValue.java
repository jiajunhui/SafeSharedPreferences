package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;

class MethodInvokerEditValue implements MethodInvoker {

    @Override
    public Bundle invoke(Context context, String arg, Bundle extras) {
        if (context==null|| TextUtils.isEmpty(arg) || extras==null) {
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences(arg, Context.MODE_PRIVATE);
        ArrayList<Bundle> options = extras.getParcelableArrayList(OptionParams.KEY_VALUE_GROUP);
        if (options == null) {
            options = new ArrayList<>();
        }
        SharedPreferences.Editor editor = preferences.edit();
        for (Bundle option : options) {
            int optionType = option.getInt(OptionParams.KEY_OPTION_TYPE);
            switch (optionType) {
                case OptionParams.OPTION_TYPE_EDIT:
                    editor = editValue(editor, option);
                    break;
                case OptionParams.OPTION_TYPE_REMOVE:
                    editor = editor.remove(option.getString(OptionParams.KEY_OPTION_KEY));
                    break;
                case OptionParams.OPTION_TYPE_CLEAR:
                    editor = editor.clear();
                    break;
                default:
                    return null;
            }
        }

        Bundle bundle = new Bundle();
        int optionType = extras.getInt(OptionParams.KEY_OPTION_TYPE);
        if (optionType == OptionParams.OPTION_TYPE_APPLY) {
            editor.apply();
        } else if (optionType == OptionParams.OPTION_TYPE_COMMIT) {
            boolean res = editor.commit();
            bundle.putBoolean(OptionParams.KEY_VALUE_GROUP, res);
        }
        return bundle;
    }

    private SharedPreferences.Editor editValue(SharedPreferences.Editor editor, Bundle opBundle) {
        String key = opBundle.getString(OptionParams.KEY_OPTION_KEY);
        int valueType = opBundle.getInt(OptionParams.KEY_VALUE_TYPE);
        switch (valueType) {
            case OptionParams.VALUE_TYPE_INT:
                return editor.putInt(key, opBundle.getInt(OptionParams.KEY_VALUE));
            case OptionParams.VALUE_TYPE_LONG:
                return editor.putLong(key, opBundle.getLong(OptionParams.KEY_VALUE));
            case OptionParams.VALUE_TYPE_FLOAT:
                return editor.putFloat(key, opBundle.getFloat(OptionParams.KEY_VALUE));
            case OptionParams.VALUE_TYPE_BOOLEAN:
                return editor.putBoolean(key, opBundle.getBoolean(OptionParams.KEY_VALUE));
            case OptionParams.VALUE_TYPE_STRING:
                return editor.putString(key, opBundle.getString(OptionParams.KEY_VALUE));
            case OptionParams.VALUE_TYPE_STRING_SET:
                ArrayList<String> list = opBundle.getStringArrayList(OptionParams.KEY_VALUE);
                if (list == null) {
                    return editor.putStringSet(key, null);
                }
                return editor.putStringSet(key, new HashSet<>(list));
            default:
                throw new IllegalArgumentException("unknown value type:" + valueType);
        }
    }

}
