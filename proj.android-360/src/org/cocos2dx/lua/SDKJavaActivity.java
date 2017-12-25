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

//导入360包
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr.MatrixCallBack;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;
import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;


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
    private static final int USERTYPE = 3;
    protected static final boolean isDebug = false;//是否是测试模式

  //360全局变量
    protected static  QihooUserInfo mQihooUserInfo;

    protected boolean mIsLandscape;

    protected static String mAccessToken = null;

    private static boolean mIsInOffline = false;

    public static final String SHOW_CHAT_FROM_QINJIA = "show_im_from_qinjia";

    /**
     * AccessToken是否有效
     */
    protected static boolean isAccessTokenValid = true;
    /**
     * QT是否有效
     */
    protected static boolean isQTValid = true;
    
    // 用户信息
    public static String gameUserInfo ="";

     //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;




     MatrixCallBack mSDKCallback = new MatrixCallBack() {
        @Override
        public void execute(Context context, int functionCode, String functionParams) {
            if (functionCode == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
                doSdkSwitchAccount(true);
            }else if (functionCode == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
                //这里返回成功之后才能调用SDK 其它接口
                // ...
                // sdkJavaLogin();

            }
            /* else if (functionCode == IntegrationConfigs.FUNC_CODE_LOGOUTSUCCESS) {
                // 融合需要，和360sdk本身业务无关
                // 注销成功,调用登录接口
                doSdkLogin(getLandscape(context));
            }*/
        }

    };

        //SDK初始化，传入回调接口的实现类
         Matrix.setActivity(sdkJavaActivity, mSDKCallback, false);

   
    }



     /**
     * 使用360SDK的切换账号接口
     *
     * @param isLandScape 是否横屏显示登录界面
     */
    protected static void doSdkSwitchAccount(boolean isLandScape) {

      SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                        SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
                            AppActivity.sdkToastLog("java调用lua======");

                        }
                      });

                        

                     }
              });




                     // 切换账号的回调
                       IDispatcherCallback mAccountSwitchCallback = new IDispatcherCallback() {

                          @Override
                          public void onFinished(String data) {
                              // press back
                              if (isCancelLogin(data)) {
                                  return;
                              }
                              if(data!=null){
                                 // 显示一下登录结果
                                  // Toast.makeText(SdkUserBaseActivity.this, data, Toast.LENGTH_LONG).show();
                              }
                              // 解析access_token
                              mAccessToken = parseAccessTokenFromLoginResult(data);

                              if (!TextUtils.isEmpty(mAccessToken)) {
                                  // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
                                  // getUserInfo();
                                 AppActivity.sdkLoginCallBack(mAccessToken, USERTYPE, PLATFROM,"");

                              } else {
                                  // Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
                              }
                          }
                      };



                  Intent intent = getSwitchAccountIntent(isLandScape);
                  IDispatcherCallback callback = mAccountSwitchCallback;
                  Matrix.invokeActivity(sdkJavaActivity, intent, callback);
    }
    






        /***
     * 生成调用360SDK切换账号接口的Intent
     *
     * @param isLandScape 是否横屏
     * @return Intent
     */
    private  static Intent getSwitchAccountIntent(boolean isLandScape) {

        Intent intent = new Intent(sdkJavaActivity, ContainerActivity.class);
        // 可选参数，是否在自动登录的过程中显示切换账号按钮
        // intent.putExtra(ProtocolKeys.IS_SHOW_AUTOLOGIN_SWITCH, getCheckBoxBoolean(R.id.isShowSwitchButton));
        
        // 必须参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的切换账号模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);

        return intent;
    }




    //游戏初始化 必须调用
    public static void sdkJavaInit() {
            sdkJavaLogin();
            
    }

       

     private static boolean isCancelLogin(String data) {
        try {
            JSONObject joData = new JSONObject(data);
            int errno = joData.optInt("errno", -1);
            if (-1 == errno) {
                // Toast.makeText(SdkUserBaseActivity.this, data, Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception e) {}
        return false;
    }



     private static String parseAccessTokenFromLoginResult(String loginRes) {
        try {
            JSONObject joRes = new JSONObject(loginRes);
            JSONObject joData = joRes.getJSONObject("data");
            return joData.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //登录游戏 必须调用
    public static void sdkJavaLogin() {
          // TODO 调用登录接口
          

          AppActivity.sdkToastLog("登录=======");

            // 登录回调函数

          mIsInOffline = false;

         final Intent intent = new Intent(sdkJavaActivity, ContainerActivity.class);

        // 界面相关参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE,true);

        // 必需参数，使用360SDK的登录模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        
       
         // 登录、注册的回调
       final IDispatcherCallback mLoginCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
                return;
            }
            // 显示一下登录结果
            mIsInOffline = false;
            mQihooUserInfo = null;
            // 解析access_token
            mAccessToken = parseAccessTokenFromLoginResult(data);

            if (!TextUtils.isEmpty(mAccessToken)) {
                // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
                 AppActivity.sdkLoginCallBack(mAccessToken, USERTYPE, PLATFROM,"");
            } else {
                // Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
            }
        }
    };


       SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
            @Override
                    public void run() {

                           Matrix.execute(sdkJavaActivity, intent, mLoginCallback);
                    }
            
          });

       
       

           

          

     

    }




    @Override
    protected void onDestroy(){
        // 游戏退出前，不再调用SDK其他接口时，需要调用Matrix.destroy接口
         super.onDestroy();
        Matrix.destroy(sdkJavaActivity);
       
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Matrix.onResume(sdkJavaActivity);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Matrix.onStart(sdkJavaActivity);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Matrix.onRestart(sdkJavaActivity);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Matrix.onActivityResult(sdkJavaActivity,requestCode, resultCode, data);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Matrix.onPause(sdkJavaActivity);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Matrix.onStop(sdkJavaActivity);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Matrix.onNewIntent(sdkJavaActivity,intent);
    }









    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
    public static void sdkJavaSubmitData(final String submitDatas) {
        AppActivity.sdkToastLog("调用用户信息=======");
        gameUserInfo = submitDatas;


        String[] submitData = submitDatas.split("\\$");
     
                 if (submitData.length >= 10) {
                 HashMap eventParams=new HashMap();
        //----------------------------模拟数据------------------------------ 
             eventParams.put("zoneid",submitData[0]);//（必填）游戏区服ID
              eventParams.put("zonename",submitData[1]);  //（必填）游戏区服名称
              eventParams.put("roleid",submitData[2]); //（必填）玩家角色ID
              eventParams.put("rolename",submitData[3]); //（必填）玩家角色名
              eventParams.put("professionid",0); //（必填）职业ID
               eventParams.put("profession","无"); //（必填）职业名称
               eventParams.put("type",submitData[6]);  //（必填）角色状态（enterServer（登录），levelUp（升级），createRole（创建角色），exitServer（退出））
               eventParams.put("gender",submitData[7]); //（必填）性别
               eventParams.put("rolelevel",submitData[4]); //（必填）玩家角色等级
               eventParams.put("power",submitData[8]); //（必填）战力数值
                eventParams.put("vip",submitData[9]);  //（必填）当前用户VIP等级
                eventParams.put("partyid",submitData[10]); //（必填）所属帮派帮派ID
                eventParams.put("partyname",submitData[11]); //（必填）所属帮派名称
                eventParams.put("partyroleid",submitData[12]); //（必填）帮派称号ID
                 eventParams.put("partyrolename",submitData[13]); //（必填）帮派称号名称
                 eventParams.put("friendlist","无");
                
                        //帐号余额
                JSONArray balancelist = new JSONArray();
                JSONObject balance1 = new JSONObject();
                  try {

                     balance1.put("balanceid","1");
                    balance1.put("balancename",submitData[3]);
                    balance1.put("balancenum",submitData[15]);
                     balancelist.put(balance1);
           

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }


               
                 eventParams.put("balance",balancelist.toString());  //（必填）帐号余额

                 //参数eventParams相关的 key、value键值对 相关具体使用说明，请参考文档。
        //----------------------------模拟数据------------------------------
            Matrix.statEventInfo(sdkJavaActivity.getApplicationContext(), eventParams);

          }

        

}

    //切换账号 必须调用
    public static void sdkJavaLogout() {

         AppActivity.sdkToastLog("调用切换账号===");
          doSdkSwitchAccount(true);

    }



    //退出游戏 必须调用
    public static void sdkJavaExit() {
           Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, true);

        // 必需参数，使用360SDK的退出模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);

        // 可选参数，登录界面的背景图片路径，必须是本地图片路径
        // bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");

        Intent intent = new Intent(sdkJavaActivity, ContainerActivity.class);
        intent.putExtras(bundle);





           // 退出的回调
      IDispatcherCallback mQuitCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
//            Log.d(TAG, "mQuitCallback, data is " + data);
            JSONObject json;
            try {
                json = new JSONObject(data);
                int which = json.optInt("which", -1);
                String label = json.optString("label");

                // Toast.makeText(SdkUserBaseActivity.this,
                //         "按钮标识：" + which + "，按钮描述:" + label, Toast.LENGTH_LONG)
                //         .show();

                switch (which) {
                    case 0: // 用户关闭退出界面
                        return;
                    default:// 退出游戏
                       System.exit(0);
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

      };

        Matrix.invokeActivity(sdkJavaActivity, intent, mQuitCallback);
      

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
                 
                   AppActivity.sdkToastLog("userId======="+userId+"costMoney===="+costMoney+"    payid====="+payid+"      produceName===="+produceName+"      produceID======"+produceID+"   userName=="+userName);
                 // 必需参数，使用360SDK的支付模块:CP可以根据需求选择使用 带有收银台的支付模块 或者 直接调用微信支付模块或者直接调用支付宝支付模块。
                  //functionCode 对应三种支付模块：
                  //ProtocolConfigs.FUNC_CODE_PAY;//表示 带有360收银台的支付模块。
                  //ProtocolConfigs.FUNC_CODE_WEIXIN_PAY;//表示 微信支付模块。
                  //ProtocolConfigs.FUNC_CODE_ALI_PAY;//表示支付宝支付模块。

                // QihooPayInfo payInfo = getQihooPayInfo(true,ProtocolConfigs.FUNC_CODE_PAY);
              
                // 创建QihooPay
                QihooPayInfo qihooPay = new QihooPayInfo();
                qihooPay.setQihooUserId(userId);   
                qihooPay.setMoneyAmount(String.valueOf(Integer.parseInt(costMoney)*100));
                AppActivity.sdkToastLog("setMoneyAmount结束====");

                qihooPay.setExchangeRate("10");

                qihooPay.setProductName(produceName);
                qihooPay.setProductId(produceID);

                qihooPay.setNotifyUri("http://web.davidcamel.com/Interface/qihu/PayCallBackQIHU.aspx");

                qihooPay.setAppName("你来嘛英雄");
                qihooPay.setAppUserName(userName);
                qihooPay.setAppUserId(userId);

                // 可选参数
                // qihooPay.setAppExt1(getString(R.string.demo_pay_app_ext1));
                // qihooPay.setAppExt2(getString(R.string.demo_pay_app_ext2));
                qihooPay.setAppOrderId(payid);

                AppActivity.sdkToastLog("qihooPay结束====");



                Intent intent = getPayIntent(true, qihooPay,ProtocolConfigs.FUNC_CODE_PAY);
                intent.putExtra(ProtocolKeys.FUNCTION_CODE,ProtocolConfigs.FUNC_CODE_PAY);
                

                AppActivity.sdkToastLog("参数结束====");

                               // 支付的回调
               IDispatcherCallback mPayCallback = new IDispatcherCallback() {

                    @Override
                    public void onFinished(String data) {
            //            Log.d(TAG, "mPayCallback, data is " + data);
                        if(TextUtils.isEmpty(data)) {
                            return;
                        }

                        boolean isCallbackParseOk = false;
                        JSONObject jsonRes;
                        try {
                            jsonRes = new JSONObject(data);
                            // error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中, 4010201和4009911 登录状态已失效，引导用户重新登录
                            // error_msg 状态描述
                            int errorCode = jsonRes.optInt("error_code");
                            isCallbackParseOk = true;
                            switch (errorCode) {
                                case 0:
                                case 1:
                                case -1:
                                case -2: {
                                    isAccessTokenValid = true;
                                    isQTValid = true;
                                    String errorMsg = jsonRes.optString("error_msg");
                                    // String text = getString(R.string.pay_callback_toast, errorCode, errorMsg);
                                    // Toast.makeText(SdkUserBaseActivity.this, text, Toast.LENGTH_SHORT).show();

                                }
                                    break;
                                case 4010201:
                                    //acess_token失效
                                    isAccessTokenValid = false;
                                    // Toast.makeText(SdkUserBaseActivity.this, R.string.access_token_invalid, Toast.LENGTH_SHORT).show();
                                    break;
                                case 4009911:
                                    //QT失效
                                    isQTValid = false;
                                    // Toast.makeText(SdkUserBaseActivity.this, R.string.qt_invalid, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 用于测试数据格式是否异常。
                        if (!isCallbackParseOk) {
                            // Toast.makeText(SdkUserBaseActivity.this, getString(R.string.data_format_error),
                            //         Toast.LENGTH_LONG).show();
                        }
                    }
                };





                // 启动接口
                Matrix.invokeActivity(sdkJavaActivity, intent, mPayCallback);
                 
    }


         /***
     * 生成调用360SDK支付接口的Intent
     *
     * @param isLandScape
     * @param pay
     * @return Intent
     */
    protected  static Intent getPayIntent(boolean isLandScape, QihooPayInfo pay,int functionCode) {
        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // *** 以下非界面相关参数 ***

        // 设置QihooPay中的参数。

        // 设置QihooPay中的参数。
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, pay.getQihooUserId());

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, pay.getMoneyAmount());

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, pay.getProductName());

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, pay.getProductId());

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, pay.getNotifyUri());

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, pay.getAppName());

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        

        bundle.putString(ProtocolKeys.APP_USER_NAME, pay.getAppUserName());
        // if(appUserNameEdit != null && !TextUtils.isEmpty(appUserNameEdit.getEditableText().toString())){
        //   bundle.putString(ProtocolKeys.APP_USER_NAME, appUserNameEdit.getEditableText().toString());
        // }else{
        //   bundle.putString(ProtocolKeys.APP_USER_NAME, pay.getAppUserName());
        // }
        
        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        bundle.putString(ProtocolKeys.APP_USER_ID, pay.getAppUserId());

        // 可选参数，应用扩展信息1，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_1, pay.getAppExt1());

        // 可选参数，应用扩展信息2，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_2, pay.getAppExt2());

        // 可选参数，应用订单号，应用内必须唯一，最大32字符。
        bundle.putString(ProtocolKeys.APP_ORDER_ID, pay.getAppOrderId());

        // 必需参数，使用360SDK的支付模块:CP可以根据需求选择使用 带有收银台的支付模块 或者 直接调用微信支付模块或者直接调用支付宝支付模块。
        //functionCode 对应三种支付模块：
        //ProtocolConfigs.FUNC_CODE_PAY;//表示 带有360收银台的支付模块。
        //ProtocolConfigs.FUNC_CODE_WEIXIN_PAY;//表示 微信支付模块。
        //ProtocolConfigs.FUNC_CODE_ALI_PAY;//表示支付宝支付模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE,functionCode);


         AppActivity.sdkToastLog("bundle结束====");

         AppActivity.sdkToastLog("gameUserInfo===="+gameUserInfo);


        

          Intent intent = new Intent(sdkJavaActivity, ContainerActivity.class);
          
        if (!gameUserInfo.equals("") && gameUserInfo!=null){

            String[] submitData = gameUserInfo.split("\\$");
             // 必需参数，商品数量（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putInt(ProtocolKeys.PRODUCT_COUNT, 1);

        // 必需参数，服务器id（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.SERVER_ID,submitData[0]);

        // 必需参数，服务器名称（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.SERVER_NAME,submitData[1]);

        // 必需参数，兑换比例（demo模拟数据）（游戏内虚拟币兑换人民币） ,游戏内逻辑请传递游戏内真实数据
        bundle.putInt(ProtocolKeys.EXCHANGE_RATE, 10);

        // 必需参数，货币名称（demo模拟数据）（比如：钻石）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.GAMEMONEY_NAME, "钻石");

        // 必需参数，角色id（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.ROLE_ID,submitData[2]);

        // 必需参数，角色名称（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.ROLE_NAME,submitData[3]);

        // 必需参数，角色等级（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putInt(ProtocolKeys.ROLE_GRADE,Integer.parseInt(submitData[4]));

        // 必需参数，虚拟币余额（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putInt(ProtocolKeys.ROLE_BALANCE,Integer.parseInt(submitData[15]));

        // 必需参数，vip等级（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.ROLE_VIP,submitData[9]);

        // 必需参数，工会名称（demo模拟数据）,游戏内逻辑请传递游戏内真实数据
        bundle.putString(ProtocolKeys.ROLE_USERPARTY, "");

        
        }



       intent.putExtras(bundle);
        
       AppActivity.sdkToastLog("intent结束====");
        

        return intent;
    }


   









  
    
}
