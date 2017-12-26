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

//导入魅族包
import com.meizu.gamesdk.model.callback.MzExitListener;
import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;
import com.meizu.gamesdk.online.model.model.MzBuyInfo;
import android.view.KeyEvent;



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
    private static final int USERTYPE = 23;
    protected static final boolean isDebug = false;//是否是测试模式

  //魅族全局变量
    MzGameBarPlatform mzGameBarPlatform;
    


     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;
        //gameBar
       mzGameBarPlatform = new MzGameBarPlatform(sdkJavaActivity, MzGameBarPlatform.GRAVITY_RIGHT_BOTTOM);

       mzGameBarPlatform.onActivityCreate();
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

                  MzGameCenterPlatform.login(sdkJavaActivity, new MzLoginListener() {
                    @Override
                  public void onLoginResult(int code, MzAccountInfo accountInfo, String errorMsg) {
                        // TODO 登录结果回调。注意，该回调跑在应用主线程，不能在这里做耗时操作 
                      switch(code){
                      case LoginResultCode.LOGIN_SUCCESS:
                      // TODO 登录成功，拿到uid 和 session到自己的服务器去校验session合法性 
                        String mUid = accountInfo.getUid();
                        // displayMsg("登录成功!\r\n 用户名:" + accountInfo.getName() + "\r\n Uid:" +
                        // accountInfo.getUid() + "\r\n session:" + accountInfo.getSession()); 
                        String session = accountInfo.getSession();
                        AppActivity.sdkToastLog("mUid======="+mUid);
                        

                        AppActivity.sdkLoginCallBack(mUid,USERTYPE,PLATFROM,session);



                        break;
                      case LoginResultCode.LOGIN_ERROR_CANCEL: // TODO 用户取消登陆操作
                        break;
                       default:
                            // TODO 登陆失败，包含错误码和错误消息。
                            // TODO 注意，错误消息(errorMsg)需要由游戏展示给用户，提示失败原因 
                          // displayMsg("登录失败 : " + errorMsg + " , code = " + code);
                           AppActivity.sdkToastLog("登录失败 : " + errorMsg + " , code = " + code);
                          break;
                  } }
                });





                    }
            
          });


          
       

           

           
          

     

    }





        @Override
    protected void onDestroy() {
        super.onDestroy();

        //调一下onActivityDestroy
        mzGameBarPlatform.onActivityDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调一下onActivityResume
        mzGameBarPlatform.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //调一下onActivityPause
        mzGameBarPlatform.onActivityPause();
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
         MzGameCenterPlatform.logout(sdkJavaActivity,new MzLoginListener(){
                  @Override
          public void onLoginResult(int code, MzAccountInfo mzAccountInfo, String msg) {
                   sdkJavaLogin();

          } 
        });

        

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




    //退出游戏 必须调用
    public static void sdkJavaExit() {


         SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                        MzGameCenterPlatform.exitSDK(sdkJavaActivity, new MzExitListener() { 
                          public void callback(int code, String msg) {
                            if (code == MzExitListener.CODE_SDK_EXIT) {
                            //TODO 在这里处理退出逻辑 finish(); 
                              sdkJavaActivity.finish();
                              System.exit(0);
                              } 
                          else if (code == MzExitListener.CODE_SDK_CONTINUE) { //TODO 继续游戏

                            } 
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
                        //此时已在主线程中，可以更新UI了
                
                                        
                          String[] payData = payDatas.split("\\$");
                          String  userId=payData[0];
                          String payid = payData[1];
                          String costMoney=payData[2];
                          String timeStamp=payData[3];
                          String ip = payData[4];
                          String port = payData[5];
                          String produceID = payData[7];
                          String produceName = payData[8];
                          String serverIndex = payData[9];
                          String strSign = payData[6];
                         
                           AppActivity.sdkToastLog("costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID);



                        String orderId = payid; // cp_order_id (不能为空)
                        String sign = strSign;    // sign (不能为空)
                        String signType = "md5"; // sign_type (不能为空)
                        int buyCount = Integer.parseInt(costMoney)*10;    // buy_amount
                        String cpUserInfo = "";  // user_info
                        String amount = costMoney+".00";           // total_price
                        String productId = produceID;    // product_id
                        String productSubject = "购买"+buyCount+"个钻石";   // product_subject
                        String productBody = "钻石";     // product_body
                        String productUnit = "个";  // product_unit
                        String appid = "3185720";       // app_id (不能为空)
                        String uid = userId;         // uid (不能为空)flyme账号用户ID
                        String perPrice = "0.10";      // product_per_price 
                        long createTime = Long.parseLong(timeStamp) ;   // create_time
                        int payType = 0;            //pay_type:0-定额;1-不定额




                        Bundle buyBundle = new MzBuyInfo().setBuyCount(buyCount).setCpUserInfo(cpUserInfo) .setOrderAmount(amount).setOrderId(orderId).setPerPrice(perPrice).setProductBody(productBody).setProductId(productId).setProductSubject(productSubject)
        .setProductUnit(productUnit).setSign(sign).setSignType(signType).setCreateTime(createTime).setAppid(appid).setUserUid(uid).setPayType(payType).toBundle();


                    AppActivity.sdkToastLog("orderId==="+orderId+"sign===="+sign+"buyCount=="+buyCount+"amount==="+amount+"productSubject==="+productSubject+"createTime==="+createTime);




                        // TODO 调用支付接口。注意，该方法必须在应用的主线程中调用。 
                        MzGameCenterPlatform.payOnline(sdkJavaActivity, buyBundle, new MzPayListener() {
                              @Override
                          public void onPayResult(int code, Bundle info, String errorMsg) {
                              // TODO 支付结果回调，该回调跑在应用主线程。注意，该回调跑在应用主线程，不能在这里
                              switch(code){
                                case PayResultCode.PAY_SUCCESS:
                                // TODO 如果成功，接下去需要到自己的服务器查询订单结果 
                                  // MzBuyInfo payInfo = MzBuyInfo.fromBundle(arg1); displayMsg("支付成功 : " + payInfo.getOrderId());
                                break;
                                case PayResultCode.PAY_ERROR_CANCEL:
                                // TODO 用户主动取消支付操作，不需要提示用户失败
                                break;
                                default:
                                // TODO 支付失败，包含错误码和错误消息。
                                // TODO 注意，错误消息(errorMsg)需要由游戏展示给用户，提示失败原因
                                // displayMsg("支付失败 : " + errorMsg + " , code = " + code); 
                                break;

                              } 
                            }
                        });


                          AppActivity.sdkToastLog("调用完成====");


                     }
              });







     
                 
}






   









  
    
}
