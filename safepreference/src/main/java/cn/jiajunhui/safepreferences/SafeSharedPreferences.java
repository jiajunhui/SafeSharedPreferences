package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class SafeSharedPreferences implements SharedPreferences {

    private static final String AUTHORITY = "cn.jiajunhui.safepreferences";
    static final Uri URI = Uri.parse("content://" + AUTHORITY);

    private static AtomicInteger mPidFlag = new AtomicInteger(0);

    private Context mContext;
    private String mPreferName;

    private static Map<String, SharedPreferences> mSafePreferMap;

    private Map<OnSharedPreferenceChangeListener, SharedPreferencesContentObserver> mListenerMap;

    static Uri getParamsUri(String params){
        return Uri.parse("content://" + AUTHORITY + "?" + params);
    }

    private SafeSharedPreferences(Context context, String preferName){
        this.mContext = context;
        this.mPreferName = preferName;
        mListenerMap = new HashMap<>();
    }

    @Override
    public Map<String, ?> getAll() {
        Bundle bundle = call(mContext, URI, MethodInvoker.METHOD_GET_ALL, mPreferName, null);
        Map<String, ?> result = null;
        if(bundle!=null){
            Serializable serializable = bundle.getSerializable(OptionParams.KEY_VALUE);
            if(serializable!=null)
                result = (Map<String, ?>) serializable;
        }
        return result;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setStringValue(defValue));
        return params==null?defValue:params.getStringValue(defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setStringSetValue(defValues));
        return params==null?defValues:params.getStringSet();
    }

    @Override
    public int getInt(String key, int defValue) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setIntValue(defValue));
        return params==null?defValue:params.getIntValue(defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setLongValue(defValue));
        return params==null?defValue:params.getLongValue(defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setFloatValue(defValue));
        return params==null?defValue:params.getFloatValue(defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        OptionParams params = optionGetValue(OptionParams.optionGetValue(key).setBooleanValue(defValue));
        return params!=null && params.getBooleanValue(defValue);
    }

    @Override
    public boolean contains(String key) {
        Bundle bundle = new Bundle();
        bundle.putString(OptionParams.KEY_OPTION_KEY, key);
        Bundle call = call(mContext, URI, MethodInvoker.METHOD_CONTAINS_KEY, mPreferName, bundle);
        return call!=null && call.getBoolean(OptionParams.KEY_VALUE, false);
    }

    @Override
    public Editor edit() {
        return new SafeEditor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        if(listener==null || mListenerMap.containsKey(listener))
            return;
        SharedPreferencesContentObserver sharedPreferencesContentObserver = new SharedPreferencesContentObserver(this, listener);
        if(!mListenerMap.containsKey(listener))
            mListenerMap.put(listener, sharedPreferencesContentObserver);
        mContext.getContentResolver().registerContentObserver(URI, true, sharedPreferencesContentObserver);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        SharedPreferencesContentObserver sharedPreferencesContentObserver = mListenerMap.remove(listener);
        if(sharedPreferencesContentObserver!=null)
            mContext.getContentResolver().unregisterContentObserver(sharedPreferencesContentObserver);
    }

    private OptionParams optionGetValue(OptionParams params){
        Bundle bundle = call(mContext, URI, MethodInvoker.METHOD_GET_VALUE, mPreferName, params.getData());
        if(bundle!=null)
            return new OptionParams(bundle);
        return null;
    }

    static SharedPreferences getSharedPreference(Context context, String preferName){
        if(mPidFlag.get()==0){
            Bundle bundle = call(context, URI, MethodInvoker.METHOD_GET_PID, null, null);
            int pid = bundle!=null?bundle.getInt(OptionParams.KEY_VALUE):0;
            if(pid==0){
                //get pid failure.
                return getDefaultSharedPreferences(context, preferName);
            }
            mPidFlag.set(Process.myPid()==pid?1:-1);
            return getSharedPreference(context, preferName);
        }else if(mPidFlag.get() > 0){
            mPidFlag.set(0);
            return getDefaultSharedPreferences(context, preferName);
        }else{
            mPidFlag.set(0);
            return getSafeSharedPreference(context, preferName);
        }
    }

    private static SharedPreferences getDefaultSharedPreferences(Context context, String preferName){
        return context.getSharedPreferences(preferName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSafeSharedPreference(Context context, String preferName){
        synchronized (SafeSharedPreferences.class){
            if(mSafePreferMap==null)
                mSafePreferMap = new HashMap<>();
            SharedPreferences sharedPreferences = mSafePreferMap.get(preferName);
            if(sharedPreferences==null){
                sharedPreferences = new SafeSharedPreferences(context, preferName);
                mSafePreferMap.put(preferName, sharedPreferences);
            }
            return sharedPreferences;
        }
    }

    private static Bundle call(Context context, Uri uri, String method, String arg, Bundle extras){
        return context.getContentResolver().call(uri, method, arg, extras);
    }

    class SafeEditor implements Editor{

        private List<OptionParams> mEditGroups = new ArrayList<>();

        @Override
        public Editor putString(String key, @Nullable String value) {
            return addEdit(OptionParams.optionEdit(key).setStringValue(value));
        }

        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            return addEdit(OptionParams.optionEdit(key).setStringSetValue(values));
        }

        @Override
        public Editor putInt(String key, int value) {
            return addEdit(OptionParams.optionEdit(key).setIntValue(value));
        }

        @Override
        public Editor putLong(String key, long value) {
            return addEdit(OptionParams.optionEdit(key).setLongValue(value));
        }

        @Override
        public Editor putFloat(String key, float value) {
            return addEdit(OptionParams.optionEdit(key).setFloatValue(value));
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            return addEdit(OptionParams.optionEdit(key).setBooleanValue(value));
        }

        @Override
        public Editor remove(String key) {
            return addEdit(OptionParams.optionRemove(key));
        }

        @Override
        public Editor clear() {
            return addEdit(OptionParams.optionClear());
        }

        @Override
        public boolean commit() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(OptionParams.KEY_VALUE_GROUP, packageEditGroup());
            bundle.putInt(OptionParams.KEY_OPTION_TYPE, OptionParams.OPTION_TYPE_COMMIT);
            Bundle call = call(mContext, URI, MethodInvoker.METHOD_EDIT_VALUE, mPreferName, bundle);
            return call!=null && call.getBoolean(OptionParams.KEY_VALUE, false);
        }

        @Override
        public void apply() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(OptionParams.KEY_VALUE_GROUP, packageEditGroup());
            bundle.putInt(OptionParams.KEY_OPTION_TYPE, OptionParams.OPTION_TYPE_APPLY);
            call(mContext, URI, MethodInvoker.METHOD_EDIT_VALUE, mPreferName, bundle);
        }

        private SafeEditor addEdit(OptionParams params){
            synchronized (this){
                mEditGroups.add(params);
                return this;
            }
        }

        private ArrayList<Bundle> packageEditGroup(){
            synchronized (this){
                ArrayList<Bundle> bundles = new ArrayList<>(mEditGroups.size());
                for(OptionParams params:mEditGroups){
                    bundles.add(params.getData());
                }
                return bundles;
            }
        }

    }

    static class SharedPreferencesContentObserver extends ContentObserver{

        private OnSharedPreferenceChangeListener mListener;
        private SharedPreferences mSharedPreferences;

        /**
         * Creates a content observer.
         */
        SharedPreferencesContentObserver(SharedPreferences sharedPreferences, OnSharedPreferenceChangeListener listener) {
            super(null);
            this.mListener = listener;
            this.mSharedPreferences = sharedPreferences;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Set<String> names = uri.getQueryParameterNames();
            for(String key:names){
                mListener.onSharedPreferenceChanged(mSharedPreferences, uri.getQueryParameter(key));
            }
        }

    }

}
