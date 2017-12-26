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
import android.app.Activity;
import android.text.TextUtils;
import android.content.ClipboardManager;
import android.content.Context;

//导入sdk包
import com.wett.cooperation.container.TTSDKV2;
import com.wett.cooperation.container.util.log.Logger;

import com.wett.cooperation.container.SdkCallback;
import com.wett.cooperation.container.TTSDKV2;
import com.wett.cooperation.container.bean.GameInfo;
import com.wett.cooperation.container.bean.PayInfo;

import org.json.JSONObject;

import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;
import android.content.res.Configuration;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;

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
  private static final int PLATFROM = 5;
  private static final int USERTYPE = 34;
  protected static final boolean isDebug = true;//是否是测试模式

  //全局变量

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      sdkJavaActivity = this;
      AppActivity.sdkToastLog("开始初始化=======");
      TTSDKV2.getInstance().prepare(sdkJavaActivity.getApplicationContext());
      GameInfo gameInfo = new GameInfo();
      TTSDKV2.getInstance().init(sdkJavaActivity, gameInfo, false, Configuration.ORIENTATION_LANDSCAPE, new SdkCallback<String>() {
            @Override
            protected boolean onResult(int i, String s) {
                if (i == 0) {
                    AppActivity.sdkToastLog("初始化成功=======");
                    TTSDKV2.getInstance().onCreate(sdkJavaActivity);
                    TTSDKV2.getInstance().setLogoutListener(new SdkCallback<String>() {
                        @Override
                        protected boolean onResult(int i, String s) {
                            if (i == 0) {
                                AppActivity.sdkToastLog("登出成功=======");
                                sdkJavaLogin();
                                SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      //此时已在主线程中，可以更新UI了
                                      SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm");
                                          }
                                      });
                                  }
                              });
                            } else {
                                AppActivity.sdkToastLog("登出失败=======");
                            }
                            return false;
                        }
                    });
                } else {
                    AppActivity.sdkToastLog("初始化失败=======");
                }
                return false;
            }
        });
  }

  //初始化回调
  public static void sdkJavaInit(){
    sdkJavaLogin();
  }

  //登录游戏 必须调用
  public static void sdkJavaLogin() {
    AppActivity.sdkToastLog("开始登录=======");
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          //调用登录接口
          TTSDKV2.getInstance().login(sdkJavaActivity,"TT", new SdkCallback<String>() {
            @Override
            protected boolean onResult(int i, String s) {
                if (i == 0) {
                    //登录成功后还需要做登录校验,由游戏自己实现
                    AppActivity.sdkToastLog("登录成功=======");
                    String gameid = TTSDKV2.getInstance().getGameId();
                    String uid = TTSDKV2.getInstance().getUid();
                    String session = TTSDKV2.getInstance().getSession();
                    TTSDKV2.getInstance().showFloatView(sdkJavaActivity);
                    AppActivity.sdkLoginCallBack(uid, USERTYPE, PLATFROM,session);
                } else {
                    AppActivity.sdkToastLog("登录失败=======");
                }
                return false;
            }
        });
       }
    });
  }

  //提交游戏角色信息
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("提交游戏角色信息=======");
      SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            //此时已在主线程中，可以更新UI了
            //调用提交游戏角色信息
            String[] submitData = submitDatas.split("\\$");
            //提交游戏数据
            String zoneId = submitData[0];//玩家区服id 没有传字符串1
            String zoneName = submitData[1];//玩家区服名称，没有传字符串001
            String roleId = submitData[2];//玩家角色Id 没有传字符串1
            String roleName = submitData[3];//玩家角色名称 没有传字符串001
            String roleLevel = submitData[4];//角色等级
            String type = submitData[6];//存储数据的类型（创建角色、进入游戏）
            String power = submitData[8];//角色战力
            String vip = submitData[9];//vip等级
            String partyrolename = submitData[13];//玩家所属帮派
            String diamond = submitData[15];//剩余钻石

            if(type.equals("createRole")){
              type="create";
            }else if(type.equals("enterServer")){
              type="enter";
            }else if(type.equals("levelUp")){
              type="upgrade";
            }

            Map<String,String> exMapParams = new ArrayMap<String, String>();
            exMapParams.put("scene_Id","default");
            exMapParams.put("zoneId",zoneId);
            exMapParams.put("balance",diamond);
            exMapParams.put("partyName",partyrolename);
            JSONObject jsonObject = new JSONObject(exMapParams);
            String exInfo = jsonObject.toString();
            AppActivity.sdkToastLog("*****"+type+","+Integer.parseInt(zoneId)+","+zoneName+","+roleId+","+roleName+","+Integer.parseInt(roleLevel)+","+Integer.parseInt(vip)+","+Long.valueOf(power)+","+exInfo);
            TTSDKV2.getInstance().submitGameRoleInfo(sdkJavaActivity,type,Integer.parseInt(zoneId),zoneName, roleId, roleName, Integer.parseInt(roleLevel),Integer.parseInt(vip), Long.valueOf(power),exInfo);
         }
      });
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号=======");
       TTSDKV2.getInstance().logout(sdkJavaActivity);
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
    AppActivity.sdkToastLog("退出游戏=======");
    TTSDKV2.getInstance().uninit(sdkJavaActivity, new SdkCallback<String>() {
      @Override
      protected boolean onResult(int i, String s) {
        if (i == 0) {
            AppActivity.sdkToastLog("退出游戏成功=======");
            System.exit(0);
        } else {
            AppActivity.sdkToastLog("退出游戏失败=======");
        }
        return false;
      }
    });
  }
  
  @Override
  protected void onRestart() {
     super.onRestart();
     TTSDKV2.getInstance().onRestart(this);
  }

  @Override
  protected void onResume() {
   super.onResume();

   TTSDKV2.getInstance().onResume(sdkJavaActivity);
   if(TTSDKV2.getInstance().isLogin()){
      TTSDKV2.getInstance().showFloatView(sdkJavaActivity);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    TTSDKV2.getInstance().onPause(sdkJavaActivity);
    TTSDKV2.getInstance().hideFloatView(sdkJavaActivity);
   }

  @Override
  protected void onStop() {
      super.onStop();
      TTSDKV2.getInstance().onStop(this);
  }

  @Override
  protected void onDestroy() {
      super.onDestroy();
      TTSDKV2.getInstance().onDestroy(this);
  }
  @Override
  protected void onNewIntent(Intent intent) { //需要在游戏的Launcher Activity中调用
      super.onNewIntent(intent);
      TTSDKV2.getInstance().onNewIntent(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode,resultCode,data);
      TTSDKV2.getInstance().onActivityResult(this,requestCode,resultCode,data);
  }

   //点击返回键退出
        @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            AppActivity.sdkToastLog("调用返回键=====dispatchKeyEvent");

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {            
            //具体的操作代码
            AppActivity.sdkToastLog("调用退出=====dispatchKeyEvent");
            

                sdkJavaExit();





        }
        return super.dispatchKeyEvent(event);
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
          String userName = payData[11];
          String gGroupName = payData[14];

          //以下参数都是必须传入的
          PayInfo payInfo = new PayInfo();
          payInfo.setRoleId(userId);//角色id
          payInfo.setRoleName(userName); //角色名称
          payInfo.setBody(produceName); //商品描述
          payInfo.setCpFee(Float.parseFloat(costMoney));//CP订单金额
          payInfo.setCpTradeNo(payid);//CP订单号
          payInfo.setServerId(Integer.parseInt(serverIndex));//游戏服务器id
          payInfo.setServerName(gGroupName);//游戏服务器名称
          payInfo.setExInfo(produceID); //CP扩展信息，该字段将会在支付成功后原样返回给CP
          payInfo.setSubject(produceName);//订单商品名称
          payInfo.setPayMethod(payInfo.PAY_METHOD_ALL); //支付方式
          payInfo.setCpCallbackUrl("http://web.davidcamel.com/Interface/TT/PayCallBackTT.aspx");//游戏方支付回调
          payInfo.setChargeDate(new Date().getTime());//cp充值时间
          TTSDKV2.getInstance().pay(sdkJavaActivity, payInfo, new SdkCallback<String>() {
              @Override
              protected boolean onResult(int i, String payResponse) {
                  if (i == 0) {
                      AppActivity.sdkToastLog("支付成功=======");
                  } else {
                      AppActivity.sdkToastLog("支付失败=======");
                  }
                  return true;
              }
          });

       }
    });
  }
}
