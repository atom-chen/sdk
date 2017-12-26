/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：zjd
    平台：vivo
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

//导入vivo包
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoPayCallback;
import com.vivo.unionsdk.open.VivoPayInfo;
import com.vivo.unionsdk.open.VivoRoleInfo;
import com.vivo.unionsdk.open.VivoUnionSDK;
import com.vivo.unionsdk.open.VivoExitCallback;

// vivo头文件 结束


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
    private static final int USERTYPE = 9;
    protected static final boolean isDebug = false;//是否是测试模式

  //vivo全局变量

     private static final String APPID = "e6e9640bb3d24a8587c36d36060a6b76";


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
        //SDK初始化, 请传入自己游戏的appid替换demo中的appid。



                // VivoUnionSDK.initSdk(getApplicationContext(),APPID,true);   

                  VivoUnionSDK.registerAccountCallback(sdkJavaActivity, new VivoAccountCallback(){

            @Override
          public void onVivoAccountLogin(String userName, String openId, String authtoken) {
              //收到登录成功回调后，调用服务端接口校验登录有效性。arg2返回值为authtoken。服务端接口详见文档。校验登录代码略。
                
                        
            AppActivity.sdkLoginCallBack(authtoken,USERTYPE,PLATFROM,"");



          }

            @Override
            public void onVivoAccountLoginCancel() {
                //取消登录操作

            }

            @Override
            public void onVivoAccountLogout(int arg0) {
                //注销

            }

          });

      

   
    }
    



    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();
            
    }

       


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口

          AppActivity.sdkToastLog("登录=======");

          VivoUnionSDK.login(sdkJavaActivity);
          

          
      


    }









    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        AppActivity.sdkToastLog("调用用户信息=======");

        String[] submitData = submitDatas.split("\\$");
     
        String roleId = submitData[2];
        String lvl = submitData[4];
        String roleName = submitData[3];
        String zoneId = submitData[0];
         String zoneName = submitData[1];

         VivoUnionSDK.reportRoleInfo(new VivoRoleInfo(roleId,lvl,roleName,zoneId,zoneName));
       

        
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");
         sdkJavaLogin();

    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {


        System.exit(0);

        // VivoUnionSDK.exit(sdkJavaActivity,new VivoExitCallback(){
        //     @Override
        //     public void onExitCancel(){
        //       //取消退出
        //     }
        //     @Override
        //     public void onExitConfirm(){
        //         //确认退出
        //           // System.exit(0);
        //           android.os.Process.killProcess(android.os.Process.myPid()); 

        //     }



        // });

    }


 

        //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url
                  String[] payData = payDatas.split("\\$");
                  String  userId=payData[0];
                  String payid = payData[1];
                  String costMoney=(Integer.parseInt(payData[2])*100)+"";
                  String payUrl=payData[3];
                  String ip = payData[4];
                  String port = payData[5];
                  String produceID = payData[7];
                  String produceName = payData[8];
                  String serverIndex = payData[9];
                  String sign = payUrl;
                  String appId = APPID;
                  String transNo = payData[6];
                
                  AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID+"      sign======"+sign+"    transNo====="+transNo);

                   //productName,productDes,productPrice,vivoSignature,appId,,transNo,uid
                  VivoPayInfo mVivoPayInfo = new VivoPayInfo(produceName,produceName,costMoney,sign,appId,transNo,userId,"余额", "VIP等级", "15","工会","角色Id","角色名称","区服名称",serverIndex+"|"+produceID+"|"+payid);

                  // mVivoPayInfo.setExtInfo(serverIndex+"|"+produceID);


                  VivoPayCallback mVivoPayCallback = new VivoPayCallback() {
                    //客户端返回的支付结果不可靠，请以服务器端最终的支付结果为准；
                    public void onVivoPayResult(String arg0, boolean arg1, String arg2) {
                        if (arg1) {
                          AppActivity.sdkToastLog("支付成功");
                        } else {
                          AppActivity.sdkToastLog("支付失败");
                        }
                    };
                   };

                  VivoUnionSDK.pay(sdkJavaActivity,mVivoPayInfo,mVivoPayCallback);
                   
    }






   









  
    
}
