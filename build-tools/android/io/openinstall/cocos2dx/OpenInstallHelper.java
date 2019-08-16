package io.openinstall.cocos2dx;

import android.content.Intent;
import android.util.Log;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

import org.cocos2dx.javascript.AppActivity;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wenki on 2018/6/26.
 */
public class OpenInstallHelper {

    private static final String TAG = "OpenInstallHelper";
    private static boolean isRegister = false;
    private static boolean isCallSuccess = false;
    private static String wakeupDataHolder = null;


    private static String CALLBACK_PATTERN = "openinstall.%s(%s);";
    private static String REQUIRE_OPENINSTALL = "var openinstall = require(\"OpenInstall\");";
    //高版本Cocos Creator构建的项目请使用此语句
//    private static String REQUIRE_OPENINSTALL = "var openinstall = window.__require(\"OpenInstall\");";
    private static String CALLBACK_INSTALL = "_installCallback";
    private static String CALLBACK_WAKEUP = "_wakeupCallback";

    public static void getInstall(int s, final Cocos2dxActivity cocos2dxActivity) {
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
              final String json = toJson(appData);
                Log.d(TAG, "啊啊啊" + "openinstall." + CALLBACK_INSTALL + json);
                cocos2dxActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        callback(CALLBACK_INSTALL, json);
                    }
                });
            }
        }, s * 1000);
    }

    public static void getWakeup(Intent intent, final Cocos2dxActivity cocos2dxActivity) {

        Log.d(TAG, "aaa getWakeup"+isRegister);
        OpenInstall.getWakeUp(intent, new AppWakeUpAdapter() {
            @Override
            public void onWakeUp(AppData appData) {
                final String json = toJson(appData);
                Log.d(TAG, json);
                if (!isRegister) {
                    Log.d(TAG, "wakeupCallback not register , wakeupData = " + json);
                    wakeupDataHolder = json;
                    return;
                }
                cocos2dxActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        callback(CALLBACK_WAKEUP, json);
                    }
                });
            }
        });
    }

    public static void registerWakeupCallback(final Cocos2dxActivity cocos2dxActivity) {
        Log.d(TAG, "registerWakeupCallback"+wakeupDataHolder);
        isRegister = true;
        if (wakeupDataHolder != null) {
            Log.d(TAG, "wakeupDataHolder = " + wakeupDataHolder);
            cocos2dxActivity.runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    callback(CALLBACK_WAKEUP, wakeupDataHolder);
                    wakeupDataHolder = null;
                }
            });
        }
    }

    private static String toJson(AppData appData) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("channelCode", appData.getChannel());
            jsonObject.put("bindData", appData.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static void callback(final String str, final String strRet) {
        String evalStr = REQUIRE_OPENINSTALL + String.format(CALLBACK_PATTERN, str, strRet);
        Log.d(TAG, "aaaaaaa"+evalStr);
        Cocos2dxJavascriptJavaBridge.evalString(evalStr);
    }
}
