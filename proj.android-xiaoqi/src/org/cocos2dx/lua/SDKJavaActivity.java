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
import android.os.Handler;
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

//导入SDK包
import com.smwl.smsdk.abstrat.SMInitListener;
import com.smwl.smsdk.abstrat.SMLoginListener;
import com.smwl.smsdk.app.SMPlatformManager;
import com.smwl.smsdk.bean.SMUserInfo;
import com.smwl.smsdk.abstrat.SMPayListener;
import com.smwl.smsdk.app.SMPlatformManager;
import com.smwl.smsdk.bean.PayInfo;
import com.smwl.smsdk.utils.StrUtilsSDK;
import com.smwl.smsdk.utils.ToastUtils;
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
   联想             5             17
   小七             5             18
  */
  public static SDKJavaActivity sdkJavaActivity = null;
  private static final int PLATFROM = 5;
  private static final int USERTYPE = 18;
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
      //SDK初始化，传入回调接口的实现类
      sdkJavaActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              //程序的入口执行初始化操作：一般放在游戏入口的activity里面用主线程调用
              SMPlatformManager.getInstance().init(sdkJavaActivity,
                      "c22f9ebbe22ec673ff31283efb32e10d", new SMInitListener() {
                          @Override
                          public void onSuccess() {
                              AppActivity.sdkToastLog("游戏初始化成功");
                          }
                          @Override
                          public void onFail(String reason) {
                              //reason:失败的原因
                              AppActivity.sdkToastLog("游戏初始化失败");
                          }
                      });
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
      sdkJavaActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              //用户登陆进入游戏和用户登出退出游戏的回调
              SMPlatformManager.getInstance().Login(sdkJavaActivity, new SMLoginListener() {
                  @Override
                  public void onLoginSuccess(SMUserInfo loginInfo) {
                      //客户端登录成功后，会通过如下方式得到token值，游戏客户端传token给自己服务器
                      //服务器做登录验证，登陆成功后，会返回给游戏guid，每一个小7通行证可以拥有至多10
                      //个子账号（guid），guid对应了游戏中的游戏账号
                      String tokenkey = loginInfo.getTokenkey();
                      AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,tokenkey);
                      AppActivity.sdkToastLog("登录成功");
                  }
                  @Override
                  public void onLoginFailed(String reason) {
                      // 登陆失败
                      AppActivity.sdkToastLog("登录失败："+reason);
                  }
                  @Override
                  public void onLoginCancell(String reason) {
                      //登陆取消
                      AppActivity.sdkToastLog("登录取消："+reason);
                  }
                  @Override
                  public void onLogoutSuccess() {
                      AppActivity.sdkToastLog("登出成功：");
                      SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              //此时已在主线程中，可以更新UI了
                              SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm");
                                      sdkJavaLogin();
                                  }
                              });
                          }
                      });
                  }
              });
          }
      });
  }

  //提交游戏角色信息
  //zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("调用用户信息=======");
      String[] submitData = submitDatas.split("\\$");
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号===");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
      SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
              //此时已在主线程中，可以更新UI了
              SMPlatformManager.getInstance().Logout();
              System.exit(0);
          }
      });

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
    String produceID = payData[7];
    String produceName = payData[8];
    String serverIndex = payData[9];

      final PayInfo mPayInfo=new PayInfo();
      //（备注：只有extends_info_data,game_level,game_role_id,
      // game_role_name这4个支付字段可以为空,其他支付字段一律不能为空）
      mPayInfo.extends_info_data=produceID;
      mPayInfo.game_level= "";
      mPayInfo.game_role_id= "";
      mPayInfo.game_role_name= "";
      // 测试账号：x7demo 密码：123456(选择小号1进入游戏)
      // 小号名称：小号1_demoandroid  :对应的game_guid：2018642
      // game_guid:是用户在游戏中的唯一标识
      mPayInfo.game_guid=userId;
      mPayInfo.game_area= serverIndex;
      mPayInfo.game_orderid= payid;
      String trim = costMoney;
      if (StrUtilsSDK.IsKong(trim)) {
          ToastUtils.show(sdkJavaActivity, "请先填写价格");
      }
      mPayInfo.game_price=trim;
      //notify_id:回调通知的id，如果只有一个支付回调地址可以将这里设置成-1，但是不允许设置成0
      //注意：如果以前有接过小7的sdk（sdk版本<=1.0.7）游戏方，要在小7的开放平台上获取回调支付
      //地址所对应的回调id，赋值给notify_id 即：notify_id=回调id（目的是为了兼容以前底的版本
      //如果此处给 -1，服务器会默认回调以前底版本sdk的对调地址：）
      mPayInfo.notify_id= "-1";
      mPayInfo.subject= "钻石";
      //游戏方不可调用此方法产生game_sign字段，这个方法只是让Demo的支付接口可以跑通的临时方法
      //游戏里面game_sign参数是根据2边服务器约定的规则产生的，由游戏的服务器返回给客户端，详见服务器的对接文档
      mPayInfo.game_sign=payUrl;

      //对参数做判空操作，如果有参数为空请不要传进sdk中!!!
      //保证支付接口的调用一定在主线程中
      sdkJavaActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              SMPlatformManager.getInstance().Pay(sdkJavaActivity, mPayInfo, new SMPayListener() {
                  @Override
                  public void onPaySuccess(Object obj) {
                      System.out.println("回调了成功："+(String)obj);
                  }
                  @Override
                  public void onPayFailed(Object obj) {
                      System.out.println("回调了失败:"+(String)obj);
                  }
                  @Override
                  public void onPayCancell(Object obj) {
                      System.out.println("回调了取消："+(String)obj);
                  }
              });
          }
      });







//      /***********
//       *  支付LenovoGameApi.doPay（） 接口 调用
//       */
//      GamePayRequest payRequest = new GamePayRequest();
//      // 请填写商品自己的参数
//      payRequest.addParam("notifyurl", "");//当前版本暂时不用，传空String
//      payRequest.addParam("appid", Config.appid);
//      payRequest.addParam("waresid", produceID);
//      payRequest.addParam("exorderno", payid);
//      payRequest.addParam("price", Integer.parseInt(costMoney)*100);
//      payRequest.addParam("cpprivateinfo", "");
//
//
//      LenovoGameApi.doPay(sdkJavaActivity,Config.appkey, payRequest, new IPayResult() {
//          @Override
//          public void onPayResult(int resultCode, String signValue,
//                                  String resultInfo) {// resultInfo = 应用编号&商品编号&外部订单号
//
//              if (LenovoGameApi.PAY_SUCCESS == resultCode) {
//                  //支付成功
//                  AppActivity.sdkToastLog("sample:支付成功");
//              } else if (LenovoGameApi.PAY_CANCEL == resultCode) {
//                  AppActivity.sdkToastLog("sample:取消支付");
//                  // 取消支付处理，默认采用finish()，请根据需要修改
//                  Log.e(Config.TAG, "return cancel");
//              } else {
//                  AppActivity.sdkToastLog("sample:支付失败");
//                  // 计费失败处理，默认采用finish()，请根据需要修改
//                  Log.e(Config.TAG, "return Error");
//              }
//          }
//      });
  }

    /** 游戏方不可调用此方法产生game_sign字段，这个方法只是让Demo的支付接口可以跑通的临时方法
     *	 游戏里面game_sign参数是根据2边服务器约定的规则产生的，详见服务器的对接文档
     * @param mPayInfo
     * @return
     */
    public static String getgameSign(PayInfo mPayInfo) {
        Map<String,String> map=new HashMap<String,String> ();
        map.put("extends_info_data", mPayInfo.extends_info_data);
        map.put("game_area", mPayInfo.game_area);
        map.put("game_level", mPayInfo.game_level);
        map.put("game_orderid", mPayInfo.game_orderid);
        map.put("game_price", mPayInfo.game_price);
        map.put("game_role_id", mPayInfo.game_role_id);
        map.put("game_role_name", mPayInfo.game_role_name);
        map.put("notify_id", mPayInfo.notify_id);
        map.put("subject", mPayInfo.subject);
        map.put("game_guid", mPayInfo.game_guid);
        String game_sign = StrUtilsSDK.getSParamSort(map);
        return game_sign;

    }
}
