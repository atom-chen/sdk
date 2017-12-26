/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：tzl
    平台：UC(阿里)
****************************************************************************/
package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;

// ucSDK(aliSDK)头文件 开始
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.AliLackActivityException;
import cn.uc.gamesdk.exception.AliNotInitException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.UCOrientation;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;
import cn.uc.gamesdk.open.OrderInfo;

// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity{

    protected static SDKJavaActivity sdkJavaActivity = null;
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
    private static final int USERTYPE = 4;
    protected static final boolean isDebug = false;//是否是测试模式
    private static String pullupInfo;


      //得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){

        return sdkJavaActivity;
     }


    private Handler handler;

    public boolean mRepeatCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;
        //ucSDK(aliSDK) onCreate 开始
        handler = new Handler(Looper.getMainLooper());

        UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        //ucSDK(aliSDK) onCreate 结束

        Intent intent = getIntent();
        pullupInfo = intent.getDataString();
       
        pullupInfo = intent.getStringExtra("data");
        

    }






    
    @Override
    protected void onDestroy(){
        //ucSDK(aliSDK) onDestroy 开始
        UCGameSdk.defaultSdk().unregisterSDKEventReceiver(receiver);
        receiver = null;
        //ucSDK(aliSDK) onDestroy 结束
        super.onDestroy();
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        AppActivity.sdkToastLog("调用初始化");
        GameParamInfo gameParamInfo = new GameParamInfo();
        //cpID
        gameParamInfo.setCpId(UCSdkConfig.cpId);
        //游戏ID
        gameParamInfo.setGameId(UCSdkConfig.gameId);
        //服务器ID 默认为0
        gameParamInfo.setServerId(UCSdkConfig.serverId);
        gameParamInfo.setOrientation(UCOrientation.PORTRAIT);

        //横屏显示
        gameParamInfo.setOrientation(UCOrientation.LANDSCAPE);

        SDKParams sdkParams = new SDKParams();
        //游戏参数
        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
        //是否开启调试模式
        sdkParams.put(SDKParamKey.DEBUG_MODE, UCSdkConfig.debugMode);

        sdkParams.put(SDKParamKey.PULLUP_INFO,pullupInfo);



        try {
            UCGameSdk.defaultSdk().initSdk(sdkJavaActivity, sdkParams);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        }
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        try {
            UCGameSdk.defaultSdk().login(sdkJavaActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
    }

    //提交游戏角色信息 必须调用
    // zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
    public static void sdkJavaSubmitData(final String submitDatas) {
        if(submitDatas != null && !"".equals(submitDatas)){
            String[] submitData = submitDatas.split("\\$");
            if (submitData.length >= 10) {
                AppActivity.sdkToastLog("提交字符："+submitDatas);
                if(!"exitServer".equals(submitData[6])){
                    SDKParams sdkParams = new SDKParams();
                    sdkParams.put(SDKParamKey.STRING_ZONE_ID, submitData[0]);//区服ID
                    sdkParams.put(SDKParamKey.STRING_ZONE_NAME, submitData[1]);//区服名
                    sdkParams.put(SDKParamKey.STRING_ROLE_ID, submitData[2]);//角色ID
                    sdkParams.put(SDKParamKey.STRING_ROLE_NAME, submitData[3]);//角色名
                    sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, Long.valueOf(submitData[4]));//角色等级
                    sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, Long.valueOf(submitData[5]));//角色穿件时间 单位：秒
                    try {
                        UCGameSdk.defaultSdk().submitRoleData(sdkJavaActivity, sdkParams);
                    } catch (AliNotInitException e) {
                        e.printStackTrace();
                    } catch (AliLackActivityException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    AppActivity.sdkToastLog("提交成功");
                }
            }   
        }
    }

    //切换账号 必须调用
    public static void sdkJavaLogout() {
        try {
            UCGameSdk.defaultSdk().logout(sdkJavaActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {
        sdkJavaActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            // TODO Auto-generated method stub
                try {
                    UCGameSdk.defaultSdk().exit(sdkJavaActivity, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //游戏充值 调用
        public static void sdkJavaPay(final String payDatas) {
                // userID.."$"..payid.."$"..costMoney.."$"..url
                  String[] payData = payDatas.split("\\$");
                  String  userId=payData[0];   
                  String payid = payData[1];
                  String costMoney=payData[2];
                  String payUrl=payData[3];
                  String ip = payData[4];
                  String port = payData[5];
                  // String signUrl = payData[6];
                  String produceID = payData[7];
                  String  serverIndex = payData[9];
                  String sign =  payUrl;
                 

                  String notifyUrl  = "http://web.davidcamel.com/Interface/ucgame/PayCallBackUC.aspx";


            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(SDKParamKey.CALLBACK_INFO,serverIndex+"|"+produceID);
            paramMap.put(SDKParamKey.NOTIFY_URL,notifyUrl);
            paramMap.put(SDKParamKey.AMOUNT,costMoney);
            paramMap.put(SDKParamKey.CP_ORDER_ID,payid);
            paramMap.put(SDKParamKey.ACCOUNT_ID,userId);
            paramMap.put(SDKParamKey.SIGN_TYPE, "MD5");
            paramMap.put(SDKParamKey.SIGN, sign);
            

        AppActivity.sdkToastLog("=========sign====="+sign+",CALLBACK_INFO"+serverIndex+"|"+produceID+",notifyUrl===="+notifyUrl+",costMoney====="+costMoney+",payid======"+payid+",userId====="+userId);


            SDKParams sdkParams = new SDKParams();

            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(paramMap);
            sdkParams.putAll(map);

            // sdkParams.put(SDKParamKey.SIGN, sign);


            //生成签名串
            // String signString = "accountId="+userId+"amount="+costMoney+"callbackInfo="+ip+":"+port+"cpOrderId="+payid+"notifyUrl="+payUrl+"74dbc814cf91a5cb708fe6cc6398b8dd";
            // String sign = "8a6c2288228af467afcb79e7f179bf0b";
            // Log.d("debug",signString);
            // AppActivity.sdkToastLog("dayin========="+signString);
            // AppActivity.sdkToastLog("dayin========="+sign);
            




            // AppActivity.sdkToastLog("========="+sdkParams.toString());
            try {
                UCGameSdk.defaultSdk().pay(sdkJavaActivity, sdkParams);
            } catch (Exception e) {
                e.printStackTrace();
                // addOutputResult("charge failed - Exception: " + e.toString() + "\n");
            }
           

    }


        /**
     * 签名工具方法
     *
     * @param reqMap
     * @return
     */
    private static String sign(Map<String, String> reqMap) {
        //TODO 游戏服务器需要提供签名接口，参考服务端接入包合集内PayChargeSignService签名实现
        return "abcdefghijklmnopqrstuvwxyz0123456789";
    }






    //SDK接口 回调通知
    private SDKEventReceiver receiver = new SDKEventReceiver() {
        @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
        private void onInitSucc() {
            /*****初始化成功后 必须调用 登录*****/
            sdkJavaLogin();
            //初始化成功
            AppActivity.sdkToastLog("init succ");
        }

        @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
        private void onInitFailed(String data) {
            //初始化失败
            AppActivity.sdkToastLog("init failed");
        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
        private void onLoginSucc(String sid) {
            /*****登录成功后 必须调用 Lua中的登录回调*****/
            AppActivity.sdkLoginCallBack(sid, USERTYPE, PLATFROM,"");
            AppActivity.sdkToastLog("login succ,sid=" + sid);
        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
        private void onLoginFailed(String desc) {
            //登录失败
            AppActivity.sdkToastLog(desc);
        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
        private void onLogoutSucc() {
            /*****切换账号成功后 必须调用 重新登录*****/
            sdkJavaLogin();
            AppActivity.sdkToastLog("logout succ");
        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
        private void onLogoutFailed() {
            //切换账号失败
            AppActivity.sdkToastLog("logout failed");
        }

        @Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
        private void onExit(String desc) {
            //退出游戏
            sdkJavaActivity.finish();
            //退出程序
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sdkJavaActivity.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        @Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
        private void onExitCanceled(String desc) {
            //取消退出
            AppActivity.sdkToastLog(desc);
        }

        //支付相关
        @Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
        private void onCreateOrderSucc(OrderInfo orderInfo) {
            // dumpOrderInfo(orderInfo);
            // if (orderInfo != null) {
            //     String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
            //     addOutputResult("此处为订单生成回调，客户端无支付成功回调，订单是否成功以服务端回调为准: " + txt + "\n");
            // }
            
        }

        @Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
        private void onPayUserExit(OrderInfo orderInfo) {
            // dumpOrderInfo(orderInfo);
            // if (orderInfo != null) {
            //     String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
            //     addOutputResult("支付界面关闭:" + txt + "\n");
            // }
          
        }



    };
    
}
