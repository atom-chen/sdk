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
import android.widget.Toast;

// ucSDK(aliSDK)头文件 开始
import org.cocos2dx.lua.util.Des3Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.anzhi.sdk.middle.manage.AnzhiSDK;
import com.anzhi.sdk.middle.manage.CPInfo;
import com.anzhi.sdk.middle.manage.GameCallBack;
import com.anzhi.sdk.middle.util.LogUtils;
import com.anzhi.sdk.middle.util.MD5;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.telephony.TelephonyManager;


//微信
import java.net.URL;
import java.lang.Exception;
import com.aw.nlmyx.anzhi.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import android.content.res.AssetManager;
import java.io.InputStream;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

// ucSDK(aliSDK)头文件 结束


public class SDKJavaActivity extends Cocos2dxActivity{
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

    protected static SDKJavaActivity sdkJavaActivity = null;
    
    private static final String Appkey="1507879500ijp2sKvDPQREJ9Fe5qh2";
    private static final String AppSecret="qn6ytwll5j3286M4WEzzfcMC";
    private static AnzhiSDK midManage;
    private static boolean isInit = false;//是否初始化成功
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 16;
    protected static final boolean isDebug = false;//是否是测试模式
    
    //小米登陆参数
    

//得到sdkJavaActivity

     public static SDKJavaActivity getSDKJavaActivity(){
        return sdkJavaActivity;
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkJavaActivity = this;
        //ucSDK(aliSDK) onCreate 开始
        //ucSDK(aliSDK) onCreate 结束

        //初始化
        
        final CPInfo info = new CPInfo();
        info.setAppKey(Appkey);
        info.setSecret(AppSecret);
        midManage = AnzhiSDK.getInstance();
        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
            //此时已在主线程中，可以更新UI了
                // -----------SDK初始化部分--------------------
                midManage.init(sdkJavaActivity, Appkey, AppSecret, callback);
                // --------------------------------------    

            }
        });
    }



GameCallBack callback = new GameCallBack() {

        @Override
        public void callBack(final int type, final String result) {
            switch (type) {
            case GameCallBack.SDK_TYPE_INIT: // 初始化操作 3
                sdkJavaActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //此时已在主线程中，可以更新UI了
                        AppActivity.sdkToastLog("Anzhi开始初始化" );

                        sdkJavaActivity.runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                isInit=true;

                                // midManage.login(sdkJavaActivity);
                                // midManage.addPop(sdkJavaActivity);// 创建悬浮球 
                            }
                        });

                        
                                               
                    }
                });
                
                break;
            case GameCallBack.SDK_TYPE_LOGIN: // 登录操作 0
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.optInt("code") == 200) { // 登录成功
                        AppActivity.sdkToastLog("Anzhi开始登陆 "+json.optString("cptoken")+" == "+json.optString("deviceId")+" == "+json.optString("requestUrl"));
                        AppActivity.sdkLoginCallBack(json.optString("cptoken"), USERTYPE, PLATFROM,json.optString("deviceId")+"[0]"+json.optString("requestUrl"));
                    } else { // 登录失败
                        String desc = json.optString("code_desc");
                    }
                } catch (JSONException e) {
                }

                break;

            case GameCallBack.SDK_TYPE_PAY:
                break;
            case GameCallBack.SDK_TYPE_EXIT_GAME: // 退出游戏操作
                finish();
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        boolean killSelf = json.optBoolean("killSelf");
                        if (killSelf) { // 是否为完全退出，如果为true需要游戏方以杀进程方式退出
                            System.exit(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GameCallBack.SDK_TYPE_CANCEL_EXIT_GAME: // 取消退出游戏操作
                break;
            case GameCallBack.SDK_TYPE_LOGOUT: // 注销登录操作
                AppActivity.sdkToastLog("注销登录操作成功" ); 



                 sdkJavaActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                        sdkJavaActivity.runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
                            AppActivity.sdkToastLog("java调用lua======");
                            AppActivity.sdkToastLog("Anzhi开始登陆" ); 
                            midManage.login(sdkJavaActivity);  

                        }
                      });
                        

                     }
                });


                break;

            }
        }
    };




    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        midManage.onNewIntentInvoked(intent);
    }

    protected void onResume(){
        super.onResume();
        midManage.onResumeInvoked();
    }

    protected void onStart(){
        super.onStart();
        midManage.onStartInvoked();
    }

    protected void onPause(){
        super.onPause();
        midManage.onPauseInvoked();
    }

    protected void onStop(){
        super.onStop();
        midManage.onStopInvoked();
    }

    protected void onDestroy(){
        super.onDestroy();
        midManage.onDestoryInvoked();
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        sdkJavaLogin();
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        // 调用SDK执行登陆操作

        if (isInit==true){
            sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                AppActivity.sdkToastLog("Anzhi开始登陆" ); 
                midManage.login(sdkJavaActivity);
                midManage.addPop(sdkJavaActivity);// 创建悬浮球                    
            }
            });
        }
        
        
    }


//提交游戏角色信息 必须调用
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
public static void sdkJavaSubmitData(final String submitDatas) {
       String[] info = submitDatas.split("\\$");
       String zoneName=info[1];
       String roleLevel=info[4];
       String roleId=info[2];
       String roleName=info[3];


       JSONObject gameInfoJson = new JSONObject();
                try {
                    gameInfoJson.put("gameArea", zoneName);
                    gameInfoJson.put("gameLevel", roleLevel);
                    gameInfoJson.put("roleId", roleId);
                    gameInfoJson.put("userRole", roleName);
                } catch (Exception e) {
                }
                midManage.subGameInfo(gameInfoJson.toString());

}

    //切换账号 必须调用
    public static void sdkJavaLogout() {



                      //   sdkJavaActivity.runOnGLThread(new Runnable() {
                      //   @Override
                      //   public void run() {
                      //       Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
                      //       AppActivity.sdkToastLog("java调用lua======");
                      //       midManage.logout();

                      //   }
                      // });

 midManage.logout();
        
    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {
        // System.exit(0);
        midManage.exitGame(sdkJavaActivity);//退出游戏接口
    }



    //支付 必须调用 userID.."$"..payid.."$"..costMoney.."$"..url.."$"..ip.."$"..port.."$"..signUrl.."$"..produceID.."$"..produceName.."$"..ServerIndex.."$"..myDiamond
    public static void sdkJavaPay(final String payDatas) {
        // userID.."$"..payid.."$"..costMoney.."$"..url
        String[] payData = payDatas.split("\\$");
        String userId=payData[0];
        String payid = payData[1];
        String costMoney=payData[2];
        String payUrl=payData[9];
        String ip = payData[4];
        String port = payData[5];
        String produceID = payData[7];
        String produceName = payData[8];
        String myDiamond = payData[10];
        // Toast.makeText(sdkJavaActivity, userId, Toast.LENGTH_LONG).show();
//        doSdkPay(null,true,ProtocolConfigs.FUNC_CODE_WEIXIN_PAY,userId,payid,costMoney,payUrl,ip,port);
//AppActivity.sdkToastLog(""+payUrl);

        JSONObject json = new JSONObject();
        try {
            // 游戏方生成的订单号,可以作为与安智订单进行关联
            json.put("cpOrderId", payid);
            json.put("cpOrderTime", System.currentTimeMillis());// 下单时间
            int price = Integer.parseInt(costMoney);
            json.put("amount", price*100);// 支付金额(单位：分)
            // json.put("amount", 1);// 支付金额(单位：分)
            json.put("cpCustomInfo", produceID);// 游戏方自定义数据
            json.put("productCount", 1);// 商品数量
            json.put("productName", produceName);// 游戏方商品名称
            json.put("productCode", "商品代码");// 游戏方商品代码
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String data = json.toString();

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                AnzhiSDK.getInstance().pay(Des3Util.encrypt(data, AppSecret), MD5.encodeToString(AppSecret));                         

            }
        });
        
    }



/**
 * 分享文本到朋友圈
 */
public static void wxSdkJavaShareTextToMoments(final String textContent) {
AppActivity.sdkToastLog("这是一段------------测试文本");
    // 实例化
    String APP_ID = "wx9d31c3a4640b546f";
    IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
    wxApi.registerApp(APP_ID);

    String text = "这是一段测试文本";
    // 初始化一个WXTextObject对象
    WXTextObject textObj = new WXTextObject();
    textObj.text = text;

    // 用WXTextObject对象初始化一个WXMediaMessage对象
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = textObj;
    msg.description = text;

    // 构造一个Req
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
    req.message = msg;
    req.scene = SendMessageToWX.Req.WXSceneTimeline;

//Req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
    //        req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友
//Req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送给微信朋友圈
//Req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友

AppActivity.sdkToastLog("这是一段测试文本");
    // 调用api接口发送数据到微信
    wxApi.sendReq(req);
}



/**
 * 分享图片到微信朋友圈
 */
public static void wxSdkJavaShareImageToMoments() {
//    Bitmap bmp = null
//    try {
//        URL url = new URL("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
//        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        if (bmp.getByteCount() > 4096000) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = bmp.getByteCount() / 4096000;//缩放比例
//            options.inJustDecodeBounds = false;
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//        bmp = null;
//    }

    Bitmap bmp = getUrlBitmap();
    if (bmp != null) {
        int THUMB_SIZE = 100;
        // 实例化
        String APP_ID = "wx9d31c3a4640b546f";
        IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
        wxApi.registerApp(APP_ID);

        WXImageObject imgObj = new WXImageObject(bmp);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送到朋友圈
        //            req.scene = SendMessageToWX.Req.WXSceneSession;//发送给微信好友
        wxApi.sendReq(req);
    }
}



//private static Resources getResources() {
//    // TODO Auto-generated method stub
//    Resources mResources = null;
//    mResources = getResources();
//    return mResources;
//}
/*
 * 分享链接到微信朋友圈
 */
public static void wxSdkJavaShareWebToMoments() {
AppActivity.sdkToastLog("图片不能为空2222");
    int THUMB_SIZE = 100;
    // 实例化
    String APP_ID = "wx9d31c3a4640b546f";
    IWXAPI wxApi = WXAPIFactory.createWXAPI(sdkJavaActivity, APP_ID);
    wxApi.registerApp(APP_ID);

    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = "https://www.baidu.com/";
    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = "你来嘛英雄官网";
    msg.description = "欢迎下载";

//    Bitmap bmp = getUrlBitmap();
//    if (bmp != null) {


//        BufferedInputStream bis=new BufferedInputStream(getAssets().open("res.Word.zhuangbei.png"));
//        Bitmap bmp = BitmapFactory.decodeStream(bis);





//    AssetManager am = AppActivity.getContext().getResources().getAssets();
////   assets/res/Word/zhuangbei.png
////        AssetManager am = getResources().getAssets();
////        InputStream is = am.open("assets/res/Word/zhuangbei.png");
//        try {
//            InputStream is = am.open("assets/res/Word/zhuangbei.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//            AppActivity.sdkToastLog("图片不能为空333");
//        }
//
//        Bitmap bmp = BitmapFactory.decodeStream(is);
//        is.close();

        Bitmap bmp = BitmapFactory.decodeResource(AppActivity.getContext().getResources(), R.drawable.icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();

        msg.thumbData = bmpToByteArray(thumbBmp, true);
//    }else{
//        AppActivity.sdkToastLog("图片不能为空");
//    }

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("webpage");
    req.message = msg;
    req.scene = SendMessageToWX.Req.WXSceneTimeline;//发送到朋友圈
    wxApi.sendReq(req);
}

/**
 * 获取图片
 *
 * @return
 */
private static Bitmap getUrlBitmap() {
    try {
        URL url = new URL("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        if (bitmap.getByteCount() > 4096000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = bitmap.getByteCount() / 4096000;//缩放比例
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
        }
        return bitmap;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

private static String buildTransaction(final String type) {
    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
}

private static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
    if (needRecycle) {
        bmp.recycle();
    }
    byte[] result = output.toByteArray();
    try {
        output.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}







}
