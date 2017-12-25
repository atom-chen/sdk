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

//导入sdk包
import com.flamingo.sdk.access.Callback;
import com.flamingo.sdk.access.GPApiFactory;
import com.flamingo.sdk.access.GPExitResult;
import com.flamingo.sdk.access.GPPayResult;
import com.flamingo.sdk.access.GPSDKGamePayment;
import com.flamingo.sdk.access.GPSDKInitResult;
import com.flamingo.sdk.access.GPSDKPlayerInfo;
import com.flamingo.sdk.access.GPUploadPlayerInfoResult;
import com.flamingo.sdk.access.GPUserResult;
import com.flamingo.sdk.access.IGPApi;
import com.flamingo.sdk.access.IGPExitObsv;
import com.flamingo.sdk.access.IGPPayObsv;
import com.flamingo.sdk.access.IGPSDKInitObsv;
import com.flamingo.sdk.access.IGPSDKInnerEventObserver;
import com.flamingo.sdk.access.IGPUploadPlayerInfoObsv;
import com.flamingo.sdk.access.IGPUserObsv;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
  private static final int USERTYPE = 22;
  protected static final boolean isDebug = false;//是否是测试模式

  //全局变量
  private String APP_ID = "111152";
  private String APP_KEY = "44CY2A6YA7JIP3US";
  private IGPApi mIGPApi;

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      sdkJavaActivity = this;

      //SDK初始化，传入回调接口的实现类
      // 这是兼容当targetSdk设置为23或者以上的情况，如果targetSdk在22或者以下则可以直接同步调用GPApiFactory.getGPApi()即可
      GPApiFactory.getGPApiForMarshmellow(sdkJavaActivity, new Callback() {
        @Override
        public void onCallBack(IGPApi igpApi) {
          mIGPApi = igpApi;
//          // sdk内部事件回调接口
          mIGPApi.setSDKInnerEventObserver(new IGPSDKInnerEventObserver() {
            @Override
            public void onSdkLogout() {
              // sdk内部登出了，游戏应该回到登录界面，然后重新调用登陆
              AppActivity.sdkToastLog("sdk登出回调:登录成功");
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

            }
//
            @Override
            public void onSdkSwitchAccount() {
              // sdk内部切换了账号，这个时候游戏也应该回到登录界面，然后重新获取新的账号的参数（相当于sdk登录成功回调了）无需重新调用登录
              AppActivity.sdkToastLog("dk切换回调:登录成功");
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
            }
          });
//          // 回调之后才可调用初始化、登陆等接口
          mIGPApi.initSdk(sdkJavaActivity, APP_ID, APP_KEY, mInitObsv);
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
    //请不要在回调函数里进行UI操作，如需进行UI操作请使用handler将UI操作抛到主线程
    //调用登录接口
    GPApiFactory.getGPApi().login(sdkJavaActivity.getApplication(), mUserObsv);
  }

  //提交游戏角色信息
  //zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("调用用户信息======="+submitDatas);
      String[] submitData = submitDatas.split("\\$");

      GPSDKPlayerInfo gpsdkPlayerInfo = new GPSDKPlayerInfo();
      gpsdkPlayerInfo.mType = GPSDKPlayerInfo.TYPE_ENTER_GAME; // 这个字段根据调用时机的不同，填入不同的类型
      gpsdkPlayerInfo.mGameLevel = submitData[4];
      gpsdkPlayerInfo.mPlayerId = submitData[2];
      gpsdkPlayerInfo.mPlayerNickName = submitData[3];
      gpsdkPlayerInfo.mServerId = submitData[0];
      gpsdkPlayerInfo.mServerName = submitData[1];
      gpsdkPlayerInfo.mBalance = Float.parseFloat(submitData[15]);
      gpsdkPlayerInfo.mGameVipLevel = submitData[9];
      gpsdkPlayerInfo.mPartyName = submitData[11];
      // 第一次创建角色调用createPlayerInfo，后续同一个角色调用uploadPlayerInfo
      GPApiFactory.getGPApi().uploadPlayerInfo(gpsdkPlayerInfo, mGPUploadPlayerInfoObsv);
  }

  /**
     * 上报用户信息回调接口
     */
    private static IGPUploadPlayerInfoObsv mGPUploadPlayerInfoObsv = new IGPUploadPlayerInfoObsv() {
        @Override
        public void onUploadFinish(final GPUploadPlayerInfoResult gpUploadPlayerInfoResult) {
            sdkJavaActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (gpUploadPlayerInfoResult.mResultCode == GPUploadPlayerInfoResult.GPSDKUploadSuccess)
                        AppActivity.sdkToastLog("上报数据回调:成功");
                    else
                        AppActivity.sdkToastLog("上报数据回调:失败");
                }
            });

        }
    };

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号===");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
    AppActivity.sdkToastLog("退出回调:调用退出游戏，请执行退出逻辑");
    Intent startMain = new Intent(Intent.ACTION_MAIN);
    startMain.addCategory(Intent.CATEGORY_HOME);
    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    System.exit(0);
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    // userID.."$"..payid.."$"..costMoney.."$"..url
    String[] payData = payDatas.split("\\$");
    String userId=payData[0];
    String payid = payData[1];
    String costMoney=payData[2];
    String payUrl=payData[3];
    String ip = payData[4];
    String port = payData[5];
    String signUrl=payData[6];
    String produceID = payData[7];
    String produceName = payData[8];
    String serverIndex = payData[9];
    String myDiamond=payData[10];
    String userName=payData[11];
    String myName=payData[12];
    String token=payData[13];
    String groupName=payData[14];


    GPSDKGamePayment payParam = new GPSDKGamePayment();

    payParam.mItemName = produceName; // 订单商品的名称

    payParam.mPaymentDes = costMoney+"0钻石";// 订单的介绍

    String priceStr = costMoney;

    if (TextUtils.isEmpty(priceStr.trim())) {
        AppActivity.sdkToastLog("请输入购买金额");
        return;
    }
    payParam.mItemPrice = Float.valueOf(priceStr);// 订单的价格（以元为单位）
    payParam.mItemCount = 1;

    payParam.mCurrentActivity = sdkJavaActivity;// 用户当前的activity

    payParam.mSerialNumber = payid;// 订单号，这里用时间代替（用户需填写订单的订单号）

    payParam.mItemId = produceID;// 商品编号ID

    payParam.mReserved = produceID;// 透传字段
    payParam.mPlayerId = userId;
    payParam.mPlayerNickName = userName;
    payParam.mServerId = serverIndex;
    payParam.mServerName = groupName;
    payParam.mRate = 10.0f; // 人民币兑换游戏内货币的比例，比如1元可购入10钻石，那就是10。
    GPApiFactory.getGPApi().buy(payParam, mPayObsv);
  }

  /**
     * 支付回调接口
     */
    private static IGPPayObsv mPayObsv = new IGPPayObsv() {
        @Override
        public void onPayFinish(GPPayResult payResult) {
            if (payResult == null) {
                return;
            }
        }
    };

  /**
   * 初始化回调接口
   */
  private IGPSDKInitObsv mInitObsv = new IGPSDKInitObsv() {
    @Override
    public void onInitFinish(GPSDKInitResult initResult) {
      switch (initResult.mInitErrCode) {
        case GPSDKInitResult.GPInitErrorCodeConfig:
          AppActivity.sdkToastLog("初始化回调:初始化配置错误");
          break;
        case GPSDKInitResult.GPInitErrorCodeNeedUpdate:
          AppActivity.sdkToastLog("初始化回调:游戏需要更新");
          break;
        case GPSDKInitResult.GPInitErrorCodeNet:
          AppActivity.sdkToastLog("初始化回调:初始化网络错误");
          break;
        case GPSDKInitResult.GPInitErrorCodeNone:
          //TODO 只有回调是成功的时候才能进行下面的操作
          AppActivity.sdkToastLog("初始化回调:初始化成功");
          break;
      }
    }
  };

  /**
   * 登录回调接口
   */
  private static IGPUserObsv mUserObsv = new IGPUserObsv() {
    @Override
    public void onFinish(final GPUserResult result) {
      switch (result.mErrCode) {
        case GPUserResult.USER_RESULT_LOGIN_FAIL:
          AppActivity.sdkToastLog("登录回调:登录失败");
          break;
        case GPUserResult.USER_RESULT_LOGIN_SUCC:
          AppActivity.sdkToastLog("登录成功");
          AppActivity.sdkLoginCallBack(GPApiFactory.getGPApi().getLoginUin(), USERTYPE, PLATFROM,GPApiFactory.getGPApi().getLoginToken());
//          writeLog("可通过getLoginUin获取用户唯一uid");
//          writeLog("可通过getLoginToken获取用户的令牌");
          break;
      }
    }
  };
}
