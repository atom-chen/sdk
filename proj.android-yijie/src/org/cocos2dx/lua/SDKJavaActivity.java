/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：zjd
    平台：360
****************************************************************************/
package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.cocos2dx.lib.Cocos2dxActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.text.TextUtils;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.KeyEvent;

//导入sdk包
import com.snowfish.cn.ganga.base.SFConst;
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
//登录
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;
import com.snow.cn.sdk.demo.utils.LoginHelper;
//支付
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
//others
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class SDKJavaActivity extends Cocos2dxActivity{

  /*
   platname       platform      usertype
   体验服（官网）     1             1
   体验服（小米）     1             2
   体验服（360）     1             3
   体验服（UC）      1             4
   小米服            2             0
   360服            3             0
   UC服             4             0
   联想             9             0
  */
  public static SDKJavaActivity sdkJavaActivity = null;
  private static final int PLATFROM = 10;
  private static int USERTYPE = 1001;
  protected static final boolean isDebug = true;//是否是测试模式

  //全局变量
  private static LoginHelper helper = null;
  private static String info;

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      sdkJavaActivity = this;
      sdkJavaActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            // AppActivity.sdkToastLog("开始初始化=======");
            //此时已在主线程中，可以更新UI了
            SFOnlineHelper.onCreate(sdkJavaActivity);
            try{
              ApplicationInfo appInfo = sdkJavaActivity.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
              final String msg=appInfo.metaData.getString("usertype");
              AppActivity.sdkToastLog(msg);
              String[] msgS = msg.split("\\$");
              USERTYPE = Integer.parseInt(msgS[1]);
            }catch(Exception e){
              USERTYPE = 1002;
            }
          }
      });
  }

  @Override
  public void onStop() {
    super.onStop();
    //在游戏Activity中的onStop中调用
    SFOnlineHelper.onStop(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    //在游戏Activity中的onDestroy中调用
    SFOnlineHelper.onDestroy(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    //在游戏Activity中的onResume中调用
    SFOnlineHelper.onResume(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    //在游戏Activity中的onPause中调用
    SFOnlineHelper.onPause(this);
  }
  @Override
  public void onRestart() {
    super.onRestart();
    //在游戏Activity中的onRestart中调用
    SFOnlineHelper.onRestart(this);
  }
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    AppActivity.sdkToastLog("调用返回键=====dispatchKeyEvent");
    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN
            && event.getRepeatCount() == 0) {            
        //具体的操作代码
        sdkJavaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                  @Override
                  public void run() {
                    sdkJavaExit();
                  }
                });
            }
        });
    }
    return super.dispatchKeyEvent(event);
  }

  //初始化回调
  public static void sdkJavaInit(){
    setLoginListener();
    sdkJavaLogin();
  }

  public static void setLoginListener(){
    helper = LoginHelper.instance();
    SFOnlineHelper.setLoginListener(sdkJavaActivity, new SFOnlineLoginListener() {
      @Override
      public void onLoginSuccess(SFOnlineUser user, Object customParams) {
        //登陆成功回调
        AppActivity.sdkToastLog("登录成功,开始验证");
        helper.setOnlineUser(user);
        LoginCheck(user);
      }
      @Override
      public void onLoginFailed(String reason, Object customParams) { 
        //登陆失败回调
        AppActivity.sdkToastLog("登录失败");
      }
      @Override
      public void onLogout(Object customParams) { 
        //登出回调
        AppActivity.sdkToastLog("登出回调");
        sdkJavaLogout();
        sdkJavaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                sdkJavaActivity.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm");
                    }
                });
            }
        });
      }
    });
  }

  /**
     *  LoginCheck
     *  从服务器端验证用户是否登陆
     * @param user 登陆账户
     */
  public static void LoginCheck(final SFOnlineUser user) {
    if (helper.isDebug()) {
      helper.setLogin(true);
      return;
    }
    // AppActivity.sdkToastLog("LoginCheck user:"+user.toString());
    new Thread(new Runnable() {
      @Override
      public void run() { 
        try {
          String url = LoginHelper.CP_LOGIN_CHECK_URL + createLoginURL();
          if (url == null)
            return;
          String result = LoginHelper.executeHttpGet(url);
          // AppActivity.sdkToastLog("url:"+LoginHelper.CP_LOGIN_CHECK_URL+" : "+url+" : "+result);
          if (result == null || !result.equalsIgnoreCase("SUCCESS")) {
            if(helper != null){
              helper.setLogin(false);
            }
            // AppActivity.sdkToastLog("未登录1");
          } else {
            if(helper != null){
              helper.setLogin(true);
            }
            String channelUserId = "";
            String token = "";
            try{
              channelUserId = URLEncoder.encode(user.getChannelUserId(), "utf-8");
              token = URLEncoder.encode(user.getToken(), "utf-8");
            }catch(Exception e){
            }
            AppActivity.sdkLoginCallBack(channelUserId, USERTYPE, PLATFROM, token);
            AppActivity.sdkToastLog(channelUserId +","+ USERTYPE +","+ PLATFROM +","+ token);
          }
        } catch (Exception e) {
          AppActivity.sdkToastLog("LoginCheck ERROR:"+e.toString());
        }
      }
    }).start();
  }

  public static String createLoginURL() throws UnsupportedEncodingException {
    if (helper == null || helper.getOnlineUser()  == null) {
      return null;
    }
    SFOnlineUser user = helper.getOnlineUser();
    StringBuilder builder = new StringBuilder();
    builder.append("?app=");
    builder.append(URLEncoder.encode(user.getProductCode(), "utf-8"));
    builder.append("&sdk=");
    builder.append(URLEncoder.encode(user.getChannelId(), "utf-8"));
    builder.append("&uin=");
    builder.append(URLEncoder.encode(user.getChannelUserId(), "utf-8"));
    builder.append("&sess=");
    builder.append(URLEncoder.encode(user.getToken(), "utf-8"));
    return builder.toString();
  }

  //登录游戏 必须调用
  public static void sdkJavaLogin() {
    AppActivity.sdkToastLog("开始登录=======");
    sdkJavaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          //调用登录接口
          SFOnlineHelper.login(sdkJavaActivity, "Login");
       }
    });
  }

  //提交游戏角色信息
  public static void sdkJavaSubmitData(final String submitDatas) {
    AppActivity.sdkToastLog("提交游戏角色信息=======");
    sdkJavaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        //此时已在主线程中，可以更新UI了
        //调用提交游戏角色信息
        String[] submitData = submitDatas.split("\\$");
        String zoneId = submitData[0];//玩家区服id 没有传字符串1
        String zoneName = submitData[1];//玩家区服名称，没有传字符串001
        String roleId = submitData[2];//玩家角色Id 没有传字符串1
        String roleName = submitData[3];//玩家角色名称 没有传字符串001
        String roleLevel = submitData[4];//角色等级
        SFOnlineHelper.setRoleData(sdkJavaActivity, roleId, roleName, roleLevel, zoneId, zoneName);
     }
    });
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号=======");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
    AppActivity.sdkToastLog("退出游戏=======");
    SFOnlineHelper.exit(sdkJavaActivity, new SFOnlineExitListener() { 
      /* onSDKExit
      * @description 当SDK有退出方法及界面，回调该函数 * @param bool SDK是否退出标志位
      */
      @Override
      public void onSDKExit(boolean bool) { 
        if (bool){
          //SDK已经退出，此处可以调用游戏的退出函数
          System.exit(0);
        } 
      }
      /* onNoExiterProvide
      * @description SDK没有退出方法及界面，回调该函数，可在此使用游戏退出界面
      */
      @Override
      public void onNoExiterProvide() {
        System.exit(0);
      } 
    });
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          String[] payData = payDatas.split("\\$");
          String  userId=payData[0];
          String payid = payData[1];
          String costMoney=payData[2];
          String payUrl=payData[3];
          String ip = payData[4];
          String port = payData[5];
          String produceID = payData[7];
          String produceName = payData[8];
          String serverIndex = payData[9];
          /* pay定额计费接口
           * public static void pay (Context context, int unitPrice, String unitName,
           * int count, String callBackInfo, String callBackUrl,SFOnlinePayResultListener payResultListener)
           * 
           *  @param context       上下文Activity
           *  @param unitPrice     当前商品需要支付金额，单位为人民币分
           *  @param itemName      虚拟货币名称
           *  @param count         用户选择购买道具界面的默认道具数量。（总价 count*unitPrice）
           *  @param callBackInfo  由游戏开发者定义传入的字符串，会与支付结果一同发送给游戏服务器，游戏服务器可通过该字段判断交易的详细内容（金额角色等）
           *  @param callBackUrl   将支付结果通知给游戏服务器时的通知地址url，交易结束后，系统会向该url发送http请求，通知交易的结果金额callbackInfo等信息
           *  @param payResultListener  支付回调接口
          */
          Integer price = Integer.parseInt(costMoney)*100;
          SFOnlineHelper.pay(sdkJavaActivity, price, produceName, 1, payid+"|"+produceID, "http://qdbt.davidcamel.com/Interface/yijie/PayCallBackYIJIE.aspx", new SFOnlinePayResultListener() {
            @Override
            public void onSuccess(String remain) {
              AppActivity.sdkToastLog("支付成功");
            }
            @Override
            public void onFailed(String remain) {
              AppActivity.sdkToastLog("支付失败");
            }
            @Override
            public void onOderNo(String orderNo) {
              AppActivity.sdkToastLog("订单号:" + orderNo);
            }
          });
       }
    });
  }
}
