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

//导入努比亚包
import cn.nubia.componentsdk.constant.ConstantProgram;
import org.cocos2dx.lua.util.ErrorMsgUtil;
import org.cocos2dx.lua.util.MD5Signature;
import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;
import android.support.v4.app.ActivityCompat;

// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity{

    public static SDKJavaActivity sdkJavaActivity = null;
    
    /*
     platname       platform      usertype
     体验服（官网）     1             1
     体验服（小米）     1             2
     体验服（360）     1             3
     体验服（UC）      1             4
     小米服            2             0
     360服            3             0
     UC服             4             0
     */
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 20;
    protected static final boolean isDebug = true;//是否是测试模式

  //努比亚全局变量
    private static final int REQUEST_STORAGE_PERMISSION_LOGIN = 100;
    private static final int REQUEST_STORAGE_PERMISSION_PAY = 101;


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

      

   
    }
    



    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();
            
    }

       


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口
          

          AppActivity.sdkToastLog("登录=======");

          
          GameSdk.openLoginActivity(sdkJavaActivity, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                switch (responseCode) {
                    case ErrorCode.SUCCESS:
                        //登陆成功，拿uid和sessionId去CP服务器完成角色创建或查询等操作
                        // String msg = String.format("账号:%s 登录", GameSdk.getLoginGameId());
                        // showText(responseCode, msg);

                        AppActivity.sdkToastLog("GameSdk.getLoginGameId()======="+GameSdk.getLoginGameId());
                        AppActivity.sdkToastLog("GameSdk.getLoginUid()======="+GameSdk.getLoginUid());
                         AppActivity.sdkToastLog("GameSdk.getSessionId()======="+GameSdk.getSessionId());
                        AppActivity.sdkLoginCallBack(GameSdk.getLoginUid(),USERTYPE,PLATFROM,GameSdk.getSessionId()+"|"+GameSdk.getLoginGameId());

                        // Log.i(TAG, "login success");
                        break;
                    case ErrorCode.NO_PERMISSION:
                        // Android6.0没允许安装和更新所需权限，需要运行时请求，主要是存储权限
                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_LOGIN);
//                        Toast.makeText(MainActivity.this, "登录需要安装努比亚联运中心服务，未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                        // Log.e(TAG, "login failure: " + "code=" + responseCode + ", message=未获得安装和更新所需权限");
                        break;
                    default:
                        // 登录失败，包含错误码和错误消息
                        Toast.makeText(sdkJavaActivity, "登录失败：" + ErrorMsgUtil.translate(responseCode), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


       

          

     

    }









    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        AppActivity.sdkToastLog("调用用户信息=======");

        String[] submitData = submitDatas.split("\\$");
     


       

        
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");
         sdkJavaLogin();

    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {

      System.exit(0);

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
                  String sign = payData[6];
                 
                   AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);


                  HashMap<String, String> map = new HashMap<String, String>();
                  map.put(ConstantProgram.TOKEN_ID, GameSdk.getSessionId());
                  map.put(ConstantProgram.UID, GameSdk.getLoginUid());
                  map.put(ConstantProgram.APP_ID, String.valueOf(AppConfig.APP_ID));
                  map.put(ConstantProgram.APP_KEY, AppConfig.APP_KEY);
                  map.put(ConstantProgram.AMOUNT, costMoney+".00");
                  map.put(ConstantProgram.PRICE, (Integer.parseInt(costMoney)/10)+"");
                  map.put(ConstantProgram.NUMBER, "1");
                  map.put(ConstantProgram.PRODUCT_NAME, produceName);
                  map.put(ConstantProgram.PRODUCT_DES, produceName);
                  map.put(ConstantProgram.PRODUCT_ID, produceID);
                  map.put(ConstantProgram.PRODUCT_UNIT, "个");
                  String timeStamp = payUrl;
                  //由CP服务器返回的订单编号，订单号不能重复
                  // String cpOrderId = "nb" + timeStamp;
                  map.put(ConstantProgram.CP_ORDER_ID, payid);
                  // 为了安全性考虑，doSign最好在服务端执行, 时间戳在DATA_TIMESTAMP和签名两个地方传递的必须是一致的
                  map.put(ConstantProgram.SIGN,sign);
                  map.put(ConstantProgram.DATA_TIMESTAMP, timeStamp);
                  map.put(ConstantProgram.CHANNEL_DIS, "1");
                  map.put(ConstantProgram.GAME_ID, GameSdk.getLoginGameId());

        GameSdk.doPay(sdkJavaActivity, map, new CallbackListener<String>() {
            @Override
            public void callback(int responseCode, String message) {
                switch (responseCode) {
                    case 0:
                        // 支付完成
                        showPayResult(responseCode, "支付成功!");
                        break;
                    case -1:
                        // 本次支付失败
                        showPayResult(responseCode, "支付失败!"+ "{" + message + "}");
                        break;
                    case 10001:
                        // 用户取消了本次支付
                        showPayResult(responseCode, "您已经取消本次支付"+ "{" + message + "}");
                        break;
                    case 10002:
                        // 网络异常
                        showPayResult(responseCode, "网络异常，请检查网络设置"+ "{" + message + "}");
                        break;
                    case 10003:
                        // 订单结果确认中，建议客户端向自己业务服务器校验支付结果
                        showPayResult(responseCode, "支付结果确认中，请稍后查看"+ "{" + message + "}");
                        break;
                    case 10004:
                        // 支付服务正在升级
                        showPayResult(responseCode, "支付服务正在升级"+ "{" + message + "}");
                        break;
                    case 10005:
                        // 支付组件安装失败或是未安装
                        showPayResult(responseCode, "支付服务安装失败或未安装"+ "{" + message + "}");
                        break;
                    case 10006:
                        // 订单信息有误
                        showPayResult(responseCode, "订单信息有误"+ "{" + message + "}");
                        break;
                    case 10007:
                        // 获取支付渠道失败
                        showPayResult(responseCode, "获取支付渠道失败，请稍后重试"+ "{" + message + "}");
                        break;
                    case 10008:
                        // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                        Toast.makeText(sdkJavaActivity, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_PAY);
                        break;
                    default:
                        // 其他所有场景统一处理为支付失败
                        showPayResult(responseCode, "支付失败! " + "{" + message + "}");
                        break;
                }
            }
        });


                 
    }





       private static void requestStoragePermission(int requestCode) {
        ActivityCompat.requestPermissions(sdkJavaActivity,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }
   



        public  static void showPayResult(int code, String payResult) {
        String msg =  payResult + " (错误码:" + code + ")";
        Toast.makeText(sdkJavaActivity, msg, Toast.LENGTH_SHORT).show();
    }





  
    
}
