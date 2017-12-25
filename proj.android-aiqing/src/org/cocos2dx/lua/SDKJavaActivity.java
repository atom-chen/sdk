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

//导入爱擎包
import com.aiyou.sdk.Constants.Constants;
import com.aiyou.sdk.LGSDK;
import com.aiyou.sdk.base.BaseActivity;
import com.aiyou.sdk.bean.ResponseBean;
import com.aiyou.sdk.bean.UserInfo;
import com.aiyou.sdk.http.HttpSender;
import com.aiyou.sdk.listener.SDKListener;
import com.aiyou.toolkit.common.JsonUtil;
import com.aiyou.toolkit.common.LogUtils;
import com.aiyou.toolkit.common.Utils;
import com.aiyou.toolkit.http.response.HttpResponse;
import com.aiyou.toolkit.tractor.listener.impl.LoadListenerImpl;
// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity implements SDKListener {

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
    private static final int USERTYPE = 35;
    protected static final boolean isDebug = false;//是否是测试模式

  //爱擎全局变量
    public String userOpenid = "0";
    


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
        LGSDK.LGSetListener(sdkJavaActivity);

   
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
            

       
          
           

           
         
   
         
          SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
            @Override
                    public void run() {

                           LGSDK.LGLogin(sdkJavaActivity);
                    }
            
          });
          

     

    }




    @Override
    public void onLoginSuccess(UserInfo userinfo) {
        // toast("登录成功");
        String username = userinfo.username;
        // LogUtils.e("aiyou onLoginSuccess auth=" + userinfo.auth + ";openid=" + userinfo.openId + ";type=" + userinfo.userType + ";username=" + username);
        
          AppActivity.sdkToastLog("userinfo.auth ===="+userinfo.auth );
          AppActivity.sdkToastLog("userinfo.userType===="+userinfo.userType );
          final String openid = userinfo.openId;


          if ("0".equals(userOpenid)){   //第一次登录
               AppActivity.sdkLoginCallBack(userinfo.openId,USERTYPE,PLATFROM,"");
               userOpenid= userinfo.openId;

          }else{

              SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm");
                                       AppActivity.sdkLoginCallBack(openid,USERTYPE,PLATFROM,"");
                      }
                  });
          }

          
        

         
        

        
    }

    @Override
    public void onLoginFail(String msg) {
        // LogUtils.e("onLoginFail msg=" + msg);
    }

    @Override
    public void onPayFinished(int code, String msg) {
        switch (code) {
            case Constants.PAY_CANCEL:
                //用户取消付款
            case Constants.PAY_FAIL:
                //支付失败
                //这里支付没有成功，不需要发货
                break;
            default:
                //去后台查询支付结果
                AppActivity.sdkToastLog("支付成功");

                break;
        }
        // toast(msg);
    }

    @Override
    public void onSdkExit() {
        // LogUtils.e("aiyou onSdkExit");
    }

    private static boolean hasRequestFloatballPermission = false;

    @Override
    public boolean onRequestFloatBallPermission() {
        //当用户没有给悬浮球权限的时候，简单的控制下请求权限的次数，应用运行期间只请求一次。
        if (!hasRequestFloatballPermission) {
            LGSDK.LGRequestFloatBallPermission(sdkJavaActivity);
            hasRequestFloatballPermission = true;
        }
        return true;
    }

    @Override
    public void onLoginOut() {
        AppActivity.sdkToastLog("onLoginOut===");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LGSDK.LGRemoveListener();
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

           SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
            @Override
                    public void run() {

                           new AlertDialog.Builder(sdkJavaActivity).setCancelable(true).setTitle("")
                        .setMessage("是否退出游戏？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LGSDK.clear();
                                //LGSDK.clear()执行以后，当再次需要使用sdk时，LGSDK.init()必须先被调用
                                //此处因为在Application的onCreate中调用了LGSDK.init()，所以必须完全退出应用才行。
                                //换句话说，如果在MainActivity的onCreate中执行了LGSDK.init()的话，则下面只需要调用finish()即可。
                                //但是因为悬浮球等业务的关系，最好是在Application的onCreate中调用了LGSDK.init()。
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();







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
                  String userName = payData[11];
                 
                   AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);


                  int money = Integer.parseInt(costMoney)*100;
                  String subject = produceName;
                  String body = produceName;
                  String sign = payUrl;
                  String app_order_id = payid;
                  String app_user_id = userId;
                  String app_userName = userName;
                  String app_notify_url = "http://web.davidcamel.com/Interface/aiqing/PayCallBackAIQING.aspx";
                  String app_attach = payid+"|"+produceID;

                  LGSDK.LGPay(sdkJavaActivity, money, subject, body, app_order_id, app_user_id,
                            app_userName, app_notify_url, app_attach, sign);
                  

                 
    }






   









  
    
}
