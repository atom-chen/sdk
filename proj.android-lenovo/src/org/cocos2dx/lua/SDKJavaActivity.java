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

//导入联想包
import com.lenovo.id.pay.sample.data.Config;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.lenovo.lsf.lenovoid.utility.RealAuthConstants;
import com.lenovo.pop.utility.Constants;

import com.lenovo.id.pay.sample.net.HttpJsonParser;
import com.lenovo.id.pay.sample.net.HttpUtil;
import com.lenovo.id.pay.sample.net.HttpUtilException;
import com.lenovo.id.pay.sample.net.HttpUtil.RequestMethod;
import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.pay.api.PayManagerActivity;


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
  private static final int USERTYPE = 17;
  protected static final boolean isDebug = false;//是否是测试模式

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
      LenovoGameApi.doInit(sdkJavaActivity,Config.appid);
  }

  //初始化回调
  public static void sdkJavaInit(){
    sdkJavaLogin();
  }

  //登录游戏 必须调用
  public static void sdkJavaLogin() {
    AppActivity.sdkToastLog("联想开始登录=======");
    //请不要在回调函数里进行UI操作，如需进行UI操作请使用handler将UI操作抛到主线程
    //调用登录接口
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        LenovoGameApi.doAutoLogin(sdkJavaActivity, new IAuthResult() {
          @Override
          public void onFinished(boolean ret, String data) {
            if (ret) {
              AppActivity.sdkToastLog("联想登陆成功");
              AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,data);
            } else {
              //后台快速登录失败(失败原因开启飞行模式、 网络不通等)
              AppActivity.sdkToastLog("联想登录失败");
            }
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
      LenovoGameApi.doQuit(sdkJavaActivity,  new IAuthResult() {
          @Override
          public void onFinished(boolean result, String data) {
              Log.i("demo", "onFinished："+data);
              if(result){
                  sdkJavaActivity.finish();
                  System.exit(0);
              }else{
                  //"用户点击底部返回键或点击弹窗close键"
              }
          }
      });
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    // userID.."$"..payid.."$"..costMoney.."$"..url
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

      /***********
       *  支付LenovoGameApi.doPay（） 接口 调用
       */
      GamePayRequest payRequest = new GamePayRequest();
      // 请填写商品自己的参数
      payRequest.addParam("notifyurl", "");
      payRequest.addParam("appid", Config.appid);
      payRequest.addParam("waresid", produceID);//即chargepoint
      payRequest.addParam("exorderno", payid);
      payRequest.addParam("price", Integer.parseInt(costMoney)*100);
      payRequest.addParam("cpprivateinfo", "");

      LenovoGameApi.doPay(sdkJavaActivity,"", payRequest, new IPayResult() {
          @Override
          public void onPayResult(int resultCode, String signValue,
                                  String resultInfo) {// resultInfo = 应用编号&商品编号&外部订单号
              if (LenovoGameApi.PAY_SUCCESS == resultCode) {
                  String[] strResult = resultInfo.split("&");
//                  new GetServerTask(strResult[2],1).execute();//strResult[2] 订单号
              } else if (LenovoGameApi.PAY_CANCEL == resultCode) {
                  AppActivity.sdkToastLog("sample:取消支付");
//                  Toast.makeText(GoodsListActivity.this, "sample:取消支付",Toast.LENGTH_SHORT).show();
                  // 取消支付处理，默认采用finish()，请根据需要修改
//                  Log.e(Config.TAG, "return cancel");
              } else {
                  String[] strResult = resultInfo.split("&");
//                  new GetServerTask(strResult[2],1).execute();//strResult[2] 订单号
                  // 计费失败处理，默认采用finish()，请根据需要修改
//                  Log.e(Config.TAG, "return Error");
              }
          }
      });
  }  
}
