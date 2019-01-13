package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;

/**
 * Created by Taurus on 2019/1/12.
 */
class MethodInvokerGetPid implements MethodInvoker {

    @Override
    public Bundle invoke(Context context, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        bundle.putInt(OptionParams.KEY_VALUE, Process.myPid());
        return bundle;
    }

}
