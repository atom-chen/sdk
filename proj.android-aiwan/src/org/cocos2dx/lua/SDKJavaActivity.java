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

//导入爱玩包
import com.xsdk.common.IDispatcherCallback;
import com.xsdk.common.LoginCallData;
import com.xsdk.common.LoginErrorMsg;
import com.xsdk.common.OnLoginListener;
import com.xsdk.common.OnPayListener;
import com.xsdk.common.PayCallData;
import com.xsdk.matrix.Matrix;
import com.xsdk.model.OrderShop;
import com.xsdk.protocols.ProtocolConfigs;
import com.xsdk.protocols.ProtocolKeys;
import com.xsdk.tool.GlobalVariables;
import com.xsdk.activity.view.login.InadditionLoginLayout;
import com.xsdk.model.AppGlobalData;
import com.xsdk.model.LtUserInfo;
import com.xsdk.model.OrderShop;
import com.xsdk.tool.RemberUser;
import com.xsdk.utils.SdkVersion;
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
    private static final int USERTYPE = 7;
    protected static final boolean isDebug = false;//是否是测试模式

  //爱玩全局变量

    


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
        Matrix.init(sdkJavaActivity);

   
    }
    



    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();
            
    }

       


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口
          

          AppActivity.sdkToastLog("登录=======");

            // 登录回调函数


       

           

            final OnLoginListener mLoginCallback =  new OnLoginListener() {

          
                    @Override
                  public void onLoginSuccess(LoginCallData callData) {

                         AppActivity.sdkToastLog("onLoginSuccess=======");
                     
                        String userId = callData.getUserId();
                        String sign = callData.getSign();
                        String token = callData.getToken();
                       AppActivity.sdkLoginCallBack(userId,USERTYPE,PLATFROM,token);

  
                  }
                   @Override
                  public void onLoginFailer(LoginErrorMsg msg) {
                    //登录失败，还有必要进入游戏吗
                  }

           };
         
   
         
          SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
            @Override
                    public void run() {

                          Matrix.login(sdkJavaActivity, mLoginCallback);
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
                 
                   AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);



                   // 支付回调函数
                   OnPayListener mPayCallback = new OnPayListener() {
                    
                    @Override
                    public void onPayResult(PayCallData callData) {
                      int code = callData.getCode();// code 是0表示成功，其他为失败
                      if  (code==0){

                            AppActivity.sdkToastLog("充值成功");

                      }else{

                          AppActivity.sdkToastLog("充值失败");
                      } 
                      String msg = callData.getMsg();
                    }
                  };

                  //Activity activity, amount, extra_param, mPayCallback

                  Matrix.pay(sdkJavaActivity,costMoney,produceID+"|"+payid+"|"+serverIndex, mPayCallback);

                 
    }






   









  
    
}
