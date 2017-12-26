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

// u360头文件 开始
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



//悟饭导包
import com.papa91.pay.callback.PPLoginCallBack;
import com.papa91.pay.callback.PPayCallback;
import com.papa91.pay.callback.PpaLogoutCallback;
import com.papa91.pay.core.StringUtils;
import com.papa91.pay.pa.activity.PaayActivity;
import com.papa91.pay.pa.business.LoginResult;
import com.papa91.pay.pa.business.PPayCenter;
import com.papa91.pay.pa.business.PaayArg;
import com.papa91.pay.pa.business.PayArgsCheckResult;
import com.papa91.pay.pa.dto.LogoutResult;

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
    private static final int USERTYPE = 14;
    protected static final boolean isDebug = false;//是否是测试模式

  
    //悟饭全局变量
    private static  int openUid;


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
       PPayCenter.init(sdkJavaActivity);
     

   
    }
    

  //生命周期

     @Override
    protected void onDestroy() {
        // 销毁之前调用 最好放在super之前
        PPayCenter.destroy();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PPayCenter.onResume(sdkJavaActivity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PPayCenter.onPause(sdkJavaActivity);
    }


  
@Override
    public void onBackPressed() {
      //当客户点击退出按钮时(用户想退出游戏时)调用,SDK显示退出确认窗口。当用户选择确定退出后，
      //回调结果码返回LogoutResult.LOGOUT_CODE_OUT。这时，游戏进行退出游戏操作，关闭整个程序。
          AppActivity.sdkToastLog("点击返回=======");

            SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了
                         PPayCenter.loginOut(sdkJavaActivity,openUid, new PpaLogoutCallback() {
                              @Override
                              public void onLoginOut(LogoutResult logoutResult) {
                                  switch (logoutResult.getCode()) {
                                      case LogoutResult.LOGOUT_CODE_OUT:
                                          finish();
                                          break;
                                      case LogoutResult.LOGOUT_CODE_BBS:

                                          break;
                                  }
                                  Log.e("退出登录", "是否是退出 " + logoutResult.getCode() + "  loggetMessage=" + logoutResult.getMessage());
                              }
                          });


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

           // AppActivity.sdkToastLog("openUid======="+openUid);
         

            SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                      PPayCenter.login(sdkJavaActivity, new PPLoginCallBack() {
                            @Override
                            public void onLoginFinish(LoginResult result) {
                                switch (result.getCode()) {
                                    case LoginResult.LOGIN_CODE_APPID_NOT_FOUND:
                                        //没找到appid
                                        break;
                                    case LoginResult.LOGIN_CODE_SUCCESS://登录成功
                                        openUid = result.getOpenUid();//返回openUid
                                        String token = result.getToken();
                                        AppActivity.sdkToastLog("openUid======="+openUid);
                                        AppActivity.sdkLoginCallBack(openUid+"",USERTYPE,PLATFROM,token);

                                        break;
                                    case LoginResult.LOGIN_CODE_FAILED://登录失败
                                        String message = result.getMessage();//失败详情
                                        break;
                                    case LoginResult.LOGIN_CODE_CANCEL:// 登录取消

                                        break;
                                    case LoginResult.NOT_INIT://没有调用 PPayCenter.init(activity);

                                        break;

                                }
                            }
                        });
                        

                     }
              });












    }

    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        AppActivity.sdkToastLog("调用用户信息=======");

        String[] submitData = submitDatas.split("\\$");
        AppActivity.sdkToastLog("KEY_SERVER_ID====="+submitData[0]);

        AppActivity.sdkToastLog("type====="+submitData[6]);

        //创建角色调用
        if ("createRole".equals(submitData[6])){
             AppActivity.sdkToastLog("发送createRole===");
            PPayCenter.createRole(submitData[3],0,Integer.parseInt(submitData[0]));

        }

        if ("levelUp".equals(submitData[6])){
              AppActivity.sdkToastLog("发送levelUp===");

           
        }

        if ( "enterServer".equals(submitData[6])){
            AppActivity.sdkToastLog("发送enterServer===");

                PPayCenter.enterGame(submitData[3],0,Integer.parseInt(submitData[0]));
         
        }

        
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");



            SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了
                        PPayCenter.changeAccount(new PPLoginCallBack() {
                            @Override
                            public void onLoginFinish(LoginResult result) {
                                switch (result.getCode()) {
                                    case LoginResult.LOGIN_CODE_APPID_NOT_FOUND:
                                        //没找到appid
                                        break;
                                    case LoginResult.LOGIN_CODE_SUCCESS://登录成功
                                        openUid = result.getOpenUid();//返回openUid
                                        String token = result.getToken();
                                        AppActivity.sdkLoginCallBack(openUid+"",USERTYPE,PLATFROM,token);

                                        break;
                                    case LoginResult.LOGIN_CODE_FAILED://登录失败
                                        String message = result.getMessage();//失败详情
                                        break;
                                    case LoginResult.LOGIN_CODE_CANCEL:// 登录取消

                                        break;
                                    case LoginResult.NOT_INIT://没有调用 PPayCenter.init(activity);

                                        break;

                                }
                            }
                        });//这里会直接弹出登录账户窗口不会登录上次登录账户






                    }

            });


         


    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {


            SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                          PPayCenter.loginOut(sdkJavaActivity,openUid, new PpaLogoutCallback() {
                              @Override
                              public void onLoginOut(LogoutResult logoutResult) {
                                  switch (logoutResult.getCode()) {
                                      case LogoutResult.LOGOUT_CODE_OUT:
                                        //退出app
                                          // finish();
                                          System.exit(0);
                                          break;
                                      case LogoutResult.LOGOUT_CODE_BBS:
                                      //进入论坛
                                          break;
                                  }
                                  Log.e("退出登录", "是否是退出 " + logoutResult.getCode() + "  loggetMessage=" + logoutResult.getMessage());
                              }
                          });


                    }
              });


         

    }


 

        //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url


                AppActivity.sdkToastLog("调用充值");
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


                   // AppActivity.sdkToastLog("userName==="+userName);
                   // AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);
                    
                                    
                      final  PaayArg paayArg = new PaayArg();
                        paayArg.APP_NAME = "你来嘛英雄";
                        paayArg.APP_ORDER_ID = payid;
                        paayArg.APP_DISTRICT = 5;
                        paayArg.APP_SERVER = Integer.parseInt(serverIndex);
                        paayArg.APP_USER_ID = userId;
                        paayArg.APP_USER_NAME =userName;
                        
                        paayArg.MONEY_AMOUNT = costMoney;
                        
                //        paayArg.NOTIFY_URI = "http://sdkapi.papa91.com/index.php/pay_center/test";
                        paayArg.NOTIFY_URI = "http://web.davidcamel.com/Interface/wufan/PayCallBackWUFAN.aspx";
                        paayArg.PRODUCT_ID = produceID;
                        paayArg.PRODUCT_NAME = produceName;
                        paayArg.PA_OPEN_UID = openUid;//调用登录方法，得到该值
                        paayArg.APP_EXT1 = "ex1...";
                        paayArg.APP_EXT2 = "ext2...";





        SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                 







                          PPayCenter.pay(paayArg, new PPayCallback() {
                            @Override
                            public void onPayFinished(int status) {
                                Log.e("支付结果", status + "");
                                String mmm = "";
                                switch (status) {

                                    case PayArgsCheckResult.CHECK_RESULT_PAY_CALLBACK_NULL:
                                        mmm = "参数错误:回调函数未配置";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_AMOUNT:
                                        mmm = "参数错误:金额无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_NAME:
                                        mmm = "参数错误:游戏名称无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_ORDER_ID:
                                        mmm = "参数错误:APP_APP_ORDER_ID无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_ID:
                                        mmm = "参数错误:APP_USER_ID无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_NAME:
                                        mmm = "参数错误:APP_USER_NAME无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_NOTIFY_URI:
                                        mmm = "参数错误:NOTIFY_URI无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_OPEN_UID:
                                        mmm = "参数错误:OPEN_UID无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_ID:
                                        mmm = "参数错误:PRODUCT_ID无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_NAME:
                                        mmm = "参数错误:PRODUCT_NAME无效";
                                        break;
                                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_KEY:
                                        mmm = "参数错误:APP_KEY无效";
                                        break;
                                    case PaayActivity.PAPAPay_RESULT_CODE_SUCCESS:
                                        mmm = "支付成功";
                                        break;
                                    case PaayActivity.PAPAPay_RESULT_CODE_FAILURE:
                                        mmm = "支付失败";
                                        break;
                                    case PaayActivity.PAPAPay_RESULT_CODE_CANCEL:
                                        mmm = "支付取消";
                                        break;
                                    case PaayActivity.PAPAPay_RESULT_CODE_WAIT:
                                        mmm = "支付等待";
                                        break;
                                }
                                
                            }
                        });

                    }
          });

        





    }






   









  
    
}
