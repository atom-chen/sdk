/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：zjd
    平台：应用宝
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


//导入应用宝包
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.app.ProgressDialog;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.BaseRet;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import com.tencent.tmgp.nlmyx.R;
import  java.lang.Thread;


// 应用宝 结束


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
    private static final int PLATFROM = 6;
    private static final int USERTYPE = 12;
    protected static final boolean isDebug = false;//是否是测试模式

  //应用宝全局变量
    public LocalBroadcastManager lbm;
     public BroadcastReceiver mReceiver;
      public static ProgressDialog mAutoLoginWaitingDlg;
      private final static String TAG = "SDKJavaActivity";
       public static String openId = "";
    public static String pf = "";
    public static String pfKey = "";
    public static String accessToken = "";
    public static String payToken = "";
    public static int logintype = 0;

    public static int retPlatform=0;   //自动登录方式

     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;

        //SDK初始化，传入回调接口的实现类
        // TODO GAME YSDK初始化
        YSDKApi.onCreate(sdkJavaActivity);
        // TODO GAME 设置java层或c++层回调,如果两层都设置了则会只回调到java层
        // 全局回调类，游戏自行实现
            YSDKApi.setUserListener(ysdkUser);
            // YSDKApi.setBuglyListener(new YSDKCallback(sdkJavaActivity));
            YSDKApi.handleIntent(sdkJavaActivity.getIntent());

   
    }


     // TODO GAME 在onActivityResult中需要调用YSDKApi.onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       
        super.onActivityResult(requestCode, resultCode, data);
        YSDKApi.onActivityResult(requestCode, resultCode, data);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onRestart()
    @Override
    protected void onRestart() {
        super.onRestart();
        YSDKApi.onRestart(sdkJavaActivity);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onResume()
    @Override
    protected void onResume() {
        super.onResume();
        YSDKApi.onResume(sdkJavaActivity);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onPause()
    @Override
    protected void onPause() {
        super.onPause();
        YSDKApi.onPause(sdkJavaActivity);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onStop()
    @Override
    protected void onStop() {
        super.onStop();
        YSDKApi.onStop(sdkJavaActivity);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onDestory()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YSDKApi.onDestroy(sdkJavaActivity);
       

        // if (null != lbm) {
        //     lbm.unregisterReceiver(mReceiver);
        // }

        // if(null != mAutoLoginWaitingDlg){
        //     mAutoLoginWaitingDlg.cancel();
        // }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        YSDKApi.handleIntent(intent);
    }




     public void showToastTips(String tips) {
        Toast.makeText(this,tips,Toast.LENGTH_LONG).show();
    }





       public static void startWaiting() {
        sdkJavaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Log.d(LOG_TAG,"startWaiting");
                mAutoLoginWaitingDlg = new ProgressDialog(sdkJavaActivity);
                mAutoLoginWaitingDlg.setTitle("游戏登录中...");
                mAutoLoginWaitingDlg.show();
            }
        });

    }


       public static void stopWaiting() {
        sdkJavaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Log.d(LOG_TAG,"stopWaiting");
                if (mAutoLoginWaitingDlg != null && mAutoLoginWaitingDlg.isShowing()) {
                    mAutoLoginWaitingDlg.dismiss();
                }
            }
        });

    }

    



    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();

            
    }

    //获取用户信息

    public static void sdkJavaUserInfo(final String info){
      AppActivity.sdkToastLog("获取用户信息=======");

        String arg = ""; 

        if(logintype==1 || retPlatform==1){  //qq
          arg=info+"|"+payToken+"|"+pf+"|"+pfKey;

        }else if(logintype==2 || retPlatform==2){ //wx
           arg=info+"|"+accessToken+"|"+pf+"|"+pfKey;

        }
        final String parmStr = arg;


         sdkJavaActivity.runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              //此时已在主线程中，可以更新UI了


                                          sdkJavaActivity.runOnGLThread(new Runnable() {
                                          @Override
                                          public void run() {
                                               AppActivity.sdkToastLog("parmStr======="+parmStr);

                              Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("YYBGetUserInfo",parmStr); 
                                        
                                              AppActivity.sdkToastLog("调用YYBGetUserInfo结束=======");

                                          }
                                        });

                                          

                                       }
                                  });





      




    }

       


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口

          AppActivity.sdkToastLog("登录=======");
           // YSDKApi.login(ePlatform.WX);
            if (logintype==0){

                        if (retPlatform==1){  //qq

                          AppActivity.sdkToastLog("进入ret.platform===1");

                                   sdkJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                                       @Override
                                      public void run() {
                                 //此时已在主线程
                                       AppActivity.sdkToastLog("进入run"); 

                                 
                                       AppActivity.sdkLoginCallBack(openId,12,PLATFROM,accessToken);
                                        AppActivity.sdkToastLog("玩run");

                                      }
                                  });




                         }else if (retPlatform==2){ //微信

                                     sdkJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                                           @Override
                                          public void run() {
                                     //此时已在主线程
                                            
                                          AppActivity.sdkLoginCallBack(openId,13,PLATFROM,accessToken);
                                          AppActivity.sdkToastLog("调用玩ret.platform===2");


                                            }
                                        });
                        

                  }

                     

                 } 


          
         

    }



     /*--------------------------------监听回调-----------------------------------------*/
    UserListener ysdkUser = new UserListener() {
 
        @Override
        public void OnWakeupNotify(WakeupRet ret) {
            // TODO Auto-generated method stub
            Log.d(TAG, "called");
            Log.d(TAG, "flag:" + ret.flag);
            Log.d(TAG, "msg:" + ret.msg);
            Log.d(TAG, "platform:" + ret.platform);
            int platform = ret.platform;
            // TODO GAME 游戏需要在这里增加处理异账号的逻辑
            if (eFlag.Wakeup_YSDKLogining == ret.flag) {
                // 用拉起的账号登录，登录结果在OnLoginNotify()中回调
            } else if (ret.flag == eFlag.Wakeup_NeedUserSelectAccount) {
                // 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
                Log.d(TAG, "diff account");
            } else if (ret.flag == eFlag.Wakeup_NeedUserLogin) {
                // 没有有效的票据，登出游戏让用户重新登录
                Log.d(TAG, "need login");
            } else {
                Log.d(TAG, "logout");
            }
        }
 
        @Override
        public void OnRelationNotify(UserRelationRet ret) {
            // TODO Auto-generated method stub
            String result = "";
            result = result + "flag:" + ret.flag + "\n";
            result = result + "msg:" + ret.msg + "\n";
            result = result + "platform:" + ret.platform + "\n";
            if (ret.persons != null && ret.persons.size() > 0) {
                PersonInfo personInfo = (PersonInfo) ret.persons.firstElement();
                StringBuilder builder = new StringBuilder();
                builder.append("UserInfoResponse json: \n");
                builder.append("nick_name: " + personInfo.nickName + "\n");
                builder.append("open_id: " + personInfo.openId + "\n");
                builder.append("userId: " + personInfo.userId + "\n");
                builder.append("gender: " + personInfo.gender + "\n");
                builder.append("picture_small: " + personInfo.pictureSmall
                        + "\n");
                builder.append("picture_middle: " + personInfo.pictureMiddle
                        + "\n");
                builder.append("picture_large: " + personInfo.pictureLarge
                        + "\n");
                builder.append("provice: " + personInfo.province + "\n");
                builder.append("city: " + personInfo.city + "\n");
                builder.append("country: " + personInfo.country + "\n");
                result = result + builder.toString();
            } else {
                result = result + "relationRet.persons is bad";
            }
            Log.d(TAG, "OnRelationNotify" + result);
 
            // 发送结果到结果展示界面
        }
 
        @Override
        public void OnLoginNotify(UserLoginRet ret) {
            // TODO Auto-generated method stub
            AppActivity.sdkToastLog("进入OnLoginNotify====");

            switch (ret.flag) {
            case eFlag.Succ:
                Log.e(TAG, "Login success:" + ret.toString());
                openId = ret.open_id;
                payToken = ret.getPayToken();
                pf = ret.pf;
                pfKey = ret.pf_key;
                accessToken = ret.getAccessToken();
                Log.i(TAG, "platform:" + ret.platform);
                // logintype = ret.platform;
                // token和uid
                Log.d(TAG, "uid:" + openId + "token:" + accessToken);
 
                // new NetHelper().execute();
                AppActivity.sdkToastLog("openId======="+openId+"payToken===="+payToken+"pf====="+pf+"pfKey======"+pfKey+"accessToken====="+accessToken);

                retPlatform=ret.platform;
                 AppActivity.sdkToastLog("ret.platform==="+ret.platform);

                 int yplatform = ret.platform;   //记住账号登录类型
                 
                 
                   



                if (logintype==1) {
                     sdkJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppActivity.sdkLoginCallBack(openId,12,PLATFROM,accessToken);

                                    }
                                  });
                    
                     
                }else if (logintype==2) {


                     sdkJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         AppActivity.sdkLoginCallBack(openId,13,PLATFROM,accessToken);
                                    }
                                  });
                    
                     
                }else if (logintype==3){

                      sdkJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         AppActivity.sdkLoginCallBack(openId,13,PLATFROM,accessToken);
                                    }
                                  });

                }
               
                stopWaiting();

 
                break;
            // 游戏逻辑，对登录失败情况分别进行处理
            case eFlag.QQ_UserCancel:
                Log.e(TAG, "用户取消授权，请重试");
                AppActivity.sdkToastLog("用户取消授权，请重试");
                // showTips("用户取消授权，请重试",localContext);
                break;
            case eFlag.QQ_LoginFail:

                Log.e(TAG, "QQ登录失败，请重试");
                AppActivity.sdkToastLog("QQ登录失败，请重试eFlag.QQ_LoginFai");
                // showTips("QQ登录失败，请重试",localContext);
                break;
            case eFlag.QQ_NetworkErr:
                Log.e(TAG, "QQ登录失败，请重试");
                 AppActivity.sdkToastLog("QQ登录失败，请重试eFlag.QQ_NetworkErr");
                // showTips("QQ登录失败，请重试",localContext);
                break;
            case eFlag.QQ_NotInstall:
                Log.e(TAG, "手机未安装手Q，请安装后重试");
                 AppActivity.sdkToastLog("手机未安装手Q，请安装后重试");
                // showTips("手机未安装手Q，请安装后重试", localContext);
                break;
            case eFlag.QQ_NotSupportApi:
                Log.e(TAG, "手机手Q版本太低，请升级后重试");
                 AppActivity.sdkToastLog("手机手Q版本太低，请升级后重试");
                // showTips("手机手Q版本太低，请升级后重试", localContext);
                break;
            case eFlag.WX_NotInstall:
                Log.e(TAG, "手机未安装微信，请安装后重试");
                 AppActivity.sdkToastLog("手机未安装微信，请安装后重试");
                // showTips("手机未安装微信，请安装后重试", localContext);
                break;
            case eFlag.WX_NotSupportApi:
                Log.e(TAG, "手机微信版本太低，请升级后重试");
                AppActivity.sdkToastLog("手机微信版本太低，请升级后重试");
                // showTips("手机微信版本太低，请升级后重试", localContext);
                break;
            case eFlag.WX_UserCancel:
                Log.e(TAG, "用户取消授权，请重试");
                  AppActivity.sdkToastLog("用户取消授权，请重试");
                // showTips("用户取消授权，请重试",localContext);
                break;
            case eFlag.WX_UserDeny:
                Log.e(TAG, "用户拒绝了授权，请重试");
                  AppActivity.sdkToastLog("用户拒绝了授权，请重试");
                // showTips("用户拒绝了授权，请重试",localContext);
                break;
            case eFlag.WX_LoginFail:
                Log.e(TAG, "微信登录失败，请重试");
                 AppActivity.sdkToastLog("微信登录失败，请重试");
                // showTips("微信登录失败，请重试",localContext);
                break;
            case eFlag.Login_TokenInvalid:
                Log.e(TAG, "您尚未登录或者之前的登录已过期，请重试");
                 AppActivity.sdkToastLog("您尚未登录或者之前的登录已过期，请重试");
                // showTips("您尚未登录或者之前的登录已过期，请重试",localContext);
                break;
            case eFlag.Login_NotRegisterRealName:
                // 显示登录界面
                Log.e(TAG, "您的账号没有进行实名认证，请实名认证后重试");
                 AppActivity.sdkToastLog("您的账号没有进行实名认证，请实名认证后重试");
                // showTips("您的账号没有进行实名认证，请实名认证后重试", localContext);
                break;
            default:
                // 显示登录界面
                 AppActivity.sdkToastLog("显示登录界面");
                break;
            }
        }
    };











    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        // AppActivity.sdkToastLog("调用用户信息=======");

        String[] submitData = submitDatas.split("\\$");
     
        String roleId = submitData[2];
        String lvl = submitData[4];
        String roleName = submitData[3];
        String zoneId = submitData[0];
         String zoneName = submitData[1];

               

        
    }


    //获取登录方式
      public static void sdkJavaLoginType(final String type) {
            logintype=Integer.parseInt(type);


                //将线程切换到ui主线程中运行
         sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                      if(logintype==1){

                        YSDKApi.login(ePlatform.QQ);
                        startWaiting();
                      }else if (logintype==2){
                         YSDKApi.login(ePlatform.WX);
                         startWaiting();
                      }
                      else if(logintype==3){
                          YSDKApi.login(ePlatform.Guest);
                          startWaiting();
                      }
                        
                      

            }
        });

          

      }
        
    



    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");
        YSDKApi.logout();

    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {

             sdkJavaActivity.runOnUiThread(new Runnable() {
                   @Override
                  public void run() {

                          AlertDialog.Builder builder = new AlertDialog.Builder(sdkJavaActivity);
                        builder.setTitle("退出游戏");
                        builder.setMessage("你确定退出你来嘛英雄么？");
                        builder.setPositiveButton("确定退出",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        sdkJavaActivity.finish();
                                        System.exit(0);
                                    }
                                });
                        builder.setNeutralButton("暂不退出",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                });
                        builder.show();



       
            }
        });



     
        


    }


 

        //支付 必须调用
    public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url
                AppActivity.sdkToastLog("payDatas==="+payDatas);
                  String[] payData = payDatas.split("\\$");
                  String  userId=payData[0];
                  final String payid = payData[1];
                  String costMoney=Integer.parseInt(payData[2])*10+"";
                  String payUrl=payData[3];
                  String ip = payData[4];
                  String port = payData[5];
                  String produceID = payData[7];
                  String produceName = payData[8];
                  String serverIndex = payData[9];
                  String sign = payUrl;
                  String sdkPayDiamond = payData[10];
                  
                  String transNo = payData[6];
                  boolean isCanChange = false;
                  AppActivity.sdkToastLog("sdkPayDiamond==="+sdkPayDiamond+"costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID+"      sign======"+sign+"    transNo====="+transNo);
                    
                  Bitmap bmp = BitmapFactory.decodeResource(sdkJavaActivity.getResources(), R.drawable.zuanshi);
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                  bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                  byte[] appResData = baos.toByteArray();
                  String ysdkExt = "ysdkExt";

                  

   YSDKApi.recharge(serverIndex,costMoney,isCanChange,appResData,ysdkExt,new PayListener() {
    @Override
    public void OnPayNotify(PayRet ret) {
        if(PayRet.RET_SUCC == ret.ret){
            //支付流程成功
            switch (ret.payState){
                //支付成功
                case PayRet.PAYSTATE_PAYSUCC:

                    AppActivity.sdkToastLog("用户支付成功，支付金额"+ret.realSaveNum+";" +
                            "使用渠道："+ret.payChannel+";" +
                            "发货状态："+ret.provideState+";" +
                            "业务类型："+ret.extendInfo+";建议查询余额："+ret.toString());
                              
                                      sdkJavaActivity.runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              //此时已在主线程中，可以更新UI了


                                          sdkJavaActivity.runOnGLThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              String parm="";
                                              if (logintype==1 || retPlatform==1){  //qq
                                                  parm=payid+"|"+payToken+"|"+pf+"|"+pfKey;
                                               
                                              }else if (logintype==2 || retPlatform==2){
                                                parm=payid+"|"+accessToken+"|"+pf+"|"+pfKey;
                                              }

                                               AppActivity.sdkToastLog("parm======="+parm);
                                              Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("YYBPay",parm); 
                                        
                                             

                                          }
                                        });

                                          

                                       }
                                  });




                    break;
                //取消支付
                case PayRet.PAYSTATE_PAYCANCEL:


                    AppActivity.sdkToastLog("用户取消支付："+ret.toString());
                    break;
                //支付结果未知
                case PayRet.PAYSTATE_PAYUNKOWN:

                     AppActivity.sdkToastLog("用户支付结果未知，建议查询余额："+ret.toString());
                    break;
                //支付失败
                case PayRet.PAYSTATE_PAYERROR:
                    AppActivity.sdkToastLog("支付异常"+ret.toString());
                    break;
            }
        }else{
            switch (ret.flag){
                case eFlag.Login_TokenInvalid:
                    //用户取消支付
                    AppActivity.sdkToastLog("登陆态过期，请重新登陆："+ret.toString());

                    break;
                case eFlag.Pay_User_Cancle:
                    //用户取消支付
                     AppActivity.sdkToastLog("用户取消支付："+ret.toString());
                    break;
                case eFlag.Pay_Param_Error:
                 AppActivity.sdkToastLog("支付失败，参数错误"+ret.toString());
                    break;
                case eFlag.Error:
                default:
                    AppActivity.sdkToastLog("支付异常"+ret.toString());

                    break;
            }
        }
    }
});




                 







                   
    }






   









  
    
}
