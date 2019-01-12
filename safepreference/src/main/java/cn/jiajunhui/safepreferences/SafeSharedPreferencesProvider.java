package cn.jiajunhui.safepreferences;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class SafeSharedPreferencesProvider extends ContentProvider {

    private InvokerEngine mInvokerEngine;

    @Override
    public boolean onCreate() {
        mInvokerEngine = new InvokerEngine();
        return true;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle call = mInvokerEngine.call(getContext(), method, arg, extras);
        if(call!=null && MethodInvoker.METHOD_EDIT_VALUE.equals(method)){
            StringBuilder keys = getKeys(extras);
            update(SafeSharedPreferences.URI, null,
                    keys!=null?keys.toString():null, null);
        }
        return call;
    }

    private StringBuilder getKeys(Bundle extras){
        ArrayList<Bundle> options = extras.getParcelableArrayList(OptionParams.KEY_VALUE_GROUP);
        if(options!=null && options.size()>0){
            StringBuilder sb = new StringBuilder();
            int size = options.size();
            String PARAM_KEY_HEADER = "notify_key_";
            for(int i=0;i<size;i++){
                Bundle bundle = options.get(i);
                if(bundle!=null){
                    String key = bundle.getString(OptionParams.KEY_OPTION_KEY);
                    sb.append(PARAM_KEY_HEADER)
                            .append(String.valueOf(i))
                            .append("=")
                            .append(key)
                            .append(i==size-1?"":"&");
                }

            }
            return sb;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        Context context = getContext();
        if(context!=null){
            Uri notifyUri = SafeSharedPreferences.URI;
            if(selection!=null){
                notifyUri = SafeSharedPreferences.getParamsUri(selection);
            }
            context.getContentResolver().notifyChange(notifyUri, null);
        }
        return 1;
    }

}
