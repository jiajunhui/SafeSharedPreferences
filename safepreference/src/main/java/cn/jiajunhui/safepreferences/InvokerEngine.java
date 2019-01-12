package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

class InvokerEngine {

    private Map<String, MethodInvoker> mMethodGroup = new HashMap<>();

    InvokerEngine(){
        mMethodGroup.put(MethodInvoker.METHOD_CONTAINS_KEY, new MethodInvokerContainsKey());
        mMethodGroup.put(MethodInvoker.METHOD_EDIT_VALUE, new MethodInvokerEditValue());
        mMethodGroup.put(MethodInvoker.METHOD_GET_VALUE, new MethodInvokerGetValue());
        mMethodGroup.put(MethodInvoker.METHOD_GET_ALL, new MethodInvokerGetAll());
        mMethodGroup.put(MethodInvoker.METHOD_GET_PID, new MethodInvokerGetPid());
    }

    public Bundle call(Context context, String method, String arg, Bundle extras){
        MethodInvoker methodInvoker = mMethodGroup.get(method);
        if(methodInvoker!=null){
            return methodInvoker.invoke(context, arg, extras);
        }
        return null;
    }

}
