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

//导入金立包
import com.gionee.gamesdk.floatwindow.AccountInfo;
import com.gionee.gamesdk.floatwindow.GamePlatform;
import com.gionee.gamesdk.floatwindow.GamePlatform.LoginListener;
import com.gionee.gamesdk.floatwindow.QuitGameCallback;
import com.gionee.gamesdk.floatwindow.utils.LogUtils;
import com.gionee.gsp.GnEFloatingBoxPositionModel;

import com.gionee.gamesdk.floatwindow.GamePayCallBack;
import com.gionee.gamesdk.floatwindow.GamePayManager;



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
    private static final int USERTYPE = 25;
    protected static final boolean isDebug = false;//是否是测试模式

  //金立全局变量

    


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

            // 登录回调函数
           SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                      @Override
                    public void run() {
                            GamePlatform.loginAccount(sdkJavaActivity, true, new LoginListener() {

                            @Override
                            public void onSuccess(AccountInfo accountInfo) {
                                // 登录成功，处理自己的业务。

                                // 获取playerId
                                Constants.sPlayerId = accountInfo.mPlayerId;


                                // 获取amigoToken
                                String amigoToken = accountInfo.mToken;

                                // 获取用户ID
                                Constants.sUserId = accountInfo.mUserId;

                               AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,amigoToken.replace(",","|"));


                            }

                            @Override
                            public void onError(Object e) {
                                // Toast.makeText(mActivity, "登录失败:" + e, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                                // Toast.makeText(mActivity, "取消登录", Toast.LENGTH_SHORT).show();
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

                                 GamePlatform.quitGame(sdkJavaActivity, new QuitGameCallback() {
                                    @Override
                                    public void onQuit() {
                                      System.exit(0);
                                        // Toast.makeText(MainActivity.this, "结束游戏", Toast.LENGTH_SHORT).show();
                                        // finish();
                                    }

                                    @Override
                                    public void onCancel() {
                                        // Toast.makeText(MainActivity.this, "取消退出", Toast.LENGTH_SHORT).show();
                                    }
                                });




                    }
                });

             


    }


 

        //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url




        SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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

                               

                  /**
                 * orderInfo 为服务器创建订单成功后返回的全部数据
                 */
                  String orderInfo =payUrl ;
                   GamePayManager mGamePayManager =  GamePayManager.getInstance();
                  mGamePayManager.pay(sdkJavaActivity, orderInfo, new GamePayCallBack() {

                  /**
                   * 因为是服务器下单，所以这个接口不会返回
                   * 
                   * @param s
                   */
                  @Override
                  public void onCreateOrderSuccess(String s) {

                  }

                  /**
                   * 支付成功
                   */
                  @Override
                  public void onPaySuccess() {
                      // Toast.makeText(NewPayOnlineTestActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                  }

                  /**
                   * 支付失败
                   */
                  @Override
                  public void onPayFail(Exception e) {
                      // Toast.makeText(NewPayOnlineTestActivity.this, "支付失败：" + e, Toast.LENGTH_SHORT).show();
                  }
              });


             }
         });

        
                  
    }





 









  
    
}
