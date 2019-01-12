package cn.jiajunhui.safepreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class OptionParams {

    static final String KEY_OPTION_KEY  = "key_option_key";
    static final String KEY_VALUE       = "key_value";
    static final String KEY_VALUE_TYPE  = "key_value_type";
    static final String KEY_OPTION_TYPE = "key_option_type";

    static final String KEY_VALUE_GROUP = "key_value_group";

    static final int VALUE_TYPE_INT        = 1;
    static final int VALUE_TYPE_LONG       = 2;
    static final int VALUE_TYPE_FLOAT      = 3;
    static final int VALUE_TYPE_BOOLEAN    = 4;
    static final int VALUE_TYPE_STRING     = 5;
    static final int VALUE_TYPE_STRING_SET = 6;

    static final int OPTION_TYPE_EDIT      = 1;
    static final int OPTION_TYPE_GET_VALUE = 2;
    static final int OPTION_TYPE_REMOVE    = 3;
    static final int OPTION_TYPE_COMMIT    = 4;
    static final int OPTION_TYPE_APPLY     = 5;
    static final int OPTION_TYPE_CLEAR     = 6;

    private Bundle mData;

    private OptionParams(){
        this.mData = new Bundle();
    }

    OptionParams(@NonNull Bundle bundle){
        this.mData = bundle;
    }

    public Bundle getData() {
         return mData;
    }

    private OptionParams setOptionType(int optionType){
        mData.putInt(KEY_OPTION_TYPE, optionType);
        return this;
    }

    private OptionParams setKey(String key){
        mData.putString(KEY_OPTION_KEY, key);
        return this;
    }

    public OptionParams setIntValue(int value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_INT);
        mData.putInt(KEY_VALUE, value);
        return this;
    }

    public int getIntValue(int defValue){
        return mData.getInt(KEY_VALUE, defValue);
    }

    public OptionParams setLongValue(long value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_LONG);
        mData.putLong(KEY_VALUE, value);
        return this;
    }

    public long getLongValue(long defValue){
        return mData.getLong(KEY_VALUE, defValue);
    }

    public OptionParams setFloatValue(float value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_FLOAT);
        mData.putFloat(KEY_VALUE, value);
        return this;
    }

    public float getFloatValue(float defValue){
        return mData.getFloat(KEY_VALUE, defValue);
    }

    public OptionParams setBooleanValue(boolean value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_BOOLEAN);
        mData.putBoolean(KEY_VALUE, value);
        return this;
    }

    public boolean getBooleanValue(boolean defValue){
        return mData.getBoolean(KEY_VALUE, defValue);
    }

    public OptionParams setStringValue(String value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_STRING);
        mData.putString(KEY_VALUE, value);
        return this;
    }

    public String getStringValue(String defValue){
        return mData.getString(KEY_VALUE, defValue);
    }

    public OptionParams setStringSetValue(Set<String> value){
        mData.putInt(KEY_VALUE_TYPE, VALUE_TYPE_STRING_SET);
        mData.putStringArrayList(KEY_VALUE, value==null?null:new ArrayList<>(value));
        return this;
    }

    public Set<String> getStringSet(){
        ArrayList<String> list = mData.getStringArrayList(KEY_VALUE);
        return list == null ? null : new HashSet<>(list);
    }

    static OptionParams optionEdit(String key){
        return new OptionParams()
                .setKey(key)
                .setOptionType(OPTION_TYPE_EDIT);
    }

    static OptionParams optionGetValue(String key){
        return new OptionParams()
                .setKey(key)
                .setOptionType(OPTION_TYPE_GET_VALUE);
    }

    static OptionParams optionRemove(String key){
        return new OptionParams()
                .setKey(key)
                .setOptionType(OPTION_TYPE_REMOVE);
    }

    static OptionParams optionClear(){
        return new OptionParams()
                .setOptionType(OPTION_TYPE_CLEAR);
    }

}
