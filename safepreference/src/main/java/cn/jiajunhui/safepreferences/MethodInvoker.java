package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Taurus on 2019/1/12.
 */
public interface MethodInvoker {

    String METHOD_GET_VALUE = "method_get_value";
    String METHOD_CONTAINS_KEY = "method_contains_key";
    String METHOD_EDIT_VALUE = "method_edit_value";
    String METHOD_GET_ALL = "method_get_all";
    String METHOD_GET_PID = "method_get_pid";

    Bundle invoke(Context context, String arg, Bundle extras);

}
