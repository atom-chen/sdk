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

import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.BDGameSDKSetting;
import com.baidu.gamesdk.BDGameSDKSetting.Domain;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.ResultCode;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.gamesdk.ResultCode;
import com.baidu.platformsdk.PayOrderInfo;
import com.baidu.gamesdk.BDGameSDKSetting.Orientation;

//微信
import java.net.URL;
import java.lang.Exception;
import com.aw.nlmyx.baidu.R;
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

import android.view.KeyEvent;
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
    
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 19;
    private static boolean initSDK = false;
    protected static final boolean isDebug = false;//是否是测试模式
    private static String uid = "";
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
        

        final BDGameSDKSetting mBDGameSDKSetting = new BDGameSDKSetting();
        mBDGameSDKSetting.setAppID(10263164); // APPID设置
        mBDGameSDKSetting.setAppKey("jNneyMWGzq6pyiuGGfngYKYx"); // APPKEY设置
        mBDGameSDKSetting.setDomain(Domain.RELEASE); // 设置为正式模式
        mBDGameSDKSetting.setOrientation(Orientation.LANDSCAPE);


        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                                

                BDGameSDK.init(sdkJavaActivity, mBDGameSDKSetting, new IResponse<Void>() {

            @Override
            public void onResponse(int resultCode, String resultDesc, Void extraData) {
                switch (resultCode) {
                    case ResultCode.INIT_SUCCESS:
                        // 初始化成功
                        initSDK=true;
                        AppActivity.sdkToastLog("SDK启动成功");//获取公告 获取公告在初始化成功后,调用登录接口之前由 cp 主动调用
                        BDGameSDK.getAnnouncementInfo(sdkJavaActivity);
                        break;

                    case ResultCode.INIT_FAIL:
                    default:
                        AppActivity.sdkToastLog("启动失败"+Toast.LENGTH_LONG);
                        // 初始化失败
                }

            }

        });

            }
        });
    }



@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            AppActivity.sdkToastLog("调用返回键=====dispatchKeyEvent");

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {            
            //具体的操作代码
            AppActivity.sdkToastLog("调用退出=====dispatchKeyEvent");

                sdkJavaActivity.runOnUiThread(new Runnable() {
                     @Override
                    public void run() {
               //此时已在主线程中，可以更新UI了
              

                        BDGameSDK.gameExit(sdkJavaActivity, new OnGameExitListener() {
                            @Override
                            public void onGameExit() {
                                // TODO 在此处执行您的游戏退出逻辑
                                BDGameSDK.closeFloatView(sdkJavaActivity); // 关闭悬浮窗
                                System.exit(0);    
                            }
                        });


                    }
                });
             
        }
        return super.dispatchKeyEvent(event);
    }




    @Override
    protected void onDestroy(){
        //ucSDK(aliSDK) onDestroy 开始
        //ucSDK(aliSDK) onDestroy 结束
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume(); 
        BDGameSDK.onResume(sdkJavaActivity);
    }

    @Override
protected void onPause() { 
    super.onPause(); 
    BDGameSDK.onPause(sdkJavaActivity);
}


@Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        BDGameSDK.closeFloatView(sdkJavaActivity); // 关闭悬浮窗
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        sdkJavaLogin();
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        // 调用SDK执行登陆操作
if (initSDK==true){

AppActivity.sdkToastLog("SDK登陆开始");
    sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                                AppActivity.sdkToastLog("SDK登陆开始2");
                BDGameSDK.login(new IResponse<Void>() {

            @Override
            public void onResponse(int resultCode, String resultDesc, Void extraData) {

                AppActivity.sdkToastLog(resultCode+" SDK登陆 ");
                switch (resultCode) {
                    case ResultCode.LOGIN_SUCCESS:
                        AppActivity.sdkToastLog("SDK登陆成功");

                        setSuspendWindowChangeAccountListener(); // 设置切换账号事件监听（个人中心界面切换账号）
                        setSessionInvalidListener(); // 设置会话失效监听
                        BDGameSDK.showFloatView(sdkJavaActivity); // 显示悬浮窗

                        uid = BDGameSDK.getLoginUid(); // TODO 保存登陆后获取的uid到调用支付API时使用
                        AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,BDGameSDK.getLoginAccessToken());
                        break;
                    case ResultCode.LOGIN_CANCEL:
                        AppActivity.sdkToastLog("SDK取消登录");
                        break;
                    case ResultCode.LOGIN_FAIL:
                    default:
                        AppActivity.sdkToastLog("登录失败:" + resultDesc);
                }
                
            }
        });


            }
        });




}
        
        
    }


//提交游戏角色信息 必须调用
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
public static void sdkJavaSubmitData(final String submitDatas) {
       String[] info = submitDatas.split("\\$");

}



private static void setSuspendWindowChangeAccountListener() { // 设置切换账号事件监听（个人中心界面切换账号）
     AppActivity.sdkToastLog("SDK切换登录成功11111111");
        BDGameSDK.setSuspendWindowChangeAccountListener(new IResponse<Void>() {
 
            @Override
            public void onResponse(int resultCode, String resultDesc,
                    Void extraData) {
                switch (resultCode) {
                    case ResultCode.LOGIN_SUCCESS:
                        // TODO 登录成功，不管之前是什么登录状态，游戏内部都要切换成新的用户
                        uid = BDGameSDK.getLoginUid(); // TODO 切换账号成功后必须更新uid给调用支付api使用
                        AppActivity.sdkToastLog("SDK切换登录成功");



                        SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                        SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
                            AppActivity.sdkToastLog("java调用lua======");
                            AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,BDGameSDK.getLoginAccessToken());

                        }
                      });

                        

                     }
              });


                        break;
                    case ResultCode.LOGIN_FAIL:
                        // TODO
                        // 登录失败，游戏内部之前如果是已经登录的，要清除自己记录的登录状态，设置成未登录。如果之前未登录，不用处理。
                        AppActivity.sdkToastLog("SDK切换登录失败");
                        break;
                    case ResultCode.LOGIN_CANCEL:
                        // TODO 取消，操作前后的登录状态没变化
                        break;
                    default:
                        // TODO
                        // 此时当登录失败处理，参照ResultCode.LOGIN_FAIL（正常情况下不会到这个步骤，除非SDK内部异常）
                        AppActivity.sdkToastLog("SDK切换登录失败");
                        break;

                }
            }

        });
    }




    //切换账号 必须调用
    public static void sdkJavaLogout() {


    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {
        

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
      

                BDGameSDK.gameExit(sdkJavaActivity, new OnGameExitListener() {
                    @Override
                    public void onGameExit() {
                        // TODO 在此处执行您的游戏退出逻辑
                        BDGameSDK.closeFloatView(sdkJavaActivity); // 关闭悬浮窗
                        System.exit(0);    
                    }
                });


            }
        });


        

    }



/**
     * @Description: 监听session失效时重新登录
     */
    private static void setSessionInvalidListener() {
        BDGameSDK.setSessionInvalidListener(new IResponse<Void>() {

            @Override
            public void onResponse(int resultCode, String resultDesc,
                    Void extraData) {
                if (resultCode == ResultCode.SESSION_INVALID) {
                    // 会话失效，开发者需要重新登录或者重启游戏
                    sdkJavaLogin();
                }

            }

        });
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


        final PayOrderInfo payOrderInfo = new PayOrderInfo();
        payOrderInfo.setCooperatorOrderSerial(payid);
        payOrderInfo.setProductName(produceName);
        int price = Integer.parseInt(costMoney);
        payOrderInfo.setTotalPriceCent(price*100); // 以分为单位
        payOrderInfo.setRatio(10);
        payOrderInfo.setExtInfo(produceID); // 该字段将会在支付成功后原样返回给CP(不超过500个字符)
        payOrderInfo.setCpUid(uid); // 必传字段，需要验证uid是否合法,此字段必须是登陆后或者切换账号后保存的uid




sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                                

            // PayOrderInfo payOrderInfo = buildOrderInfo();
            // if (payOrderInfo == null) {
            //     return;
            // }

                BDGameSDK.pay(payOrderInfo, null, new IResponse<PayOrderInfo>() {

            @Override
            public void onResponse(int resultCode, String resultDesc,
                    PayOrderInfo extraData) {
                String resultStr = "";
                switch (resultCode) {
                    case ResultCode.PAY_SUCCESS: // 支付成功
                        resultStr = "支付成功:" + resultDesc;
                        break;
                    case ResultCode.PAY_CANCEL: // 订单支付取消
                        resultStr = "取消支付";
                        break;
                    case ResultCode.PAY_FAIL: // 订单支付失败
                        resultStr = "支付失败：" + resultDesc;
                        break;
                    case ResultCode.PAY_SUBMIT_ORDER: // 订单已经提交，支付结果未知（比如：已经请求了，但是查询超时）
                        resultStr = "订单已经提交，支付结果未知";
                        break;
                    default:
                        resultStr = "订单已经提交，支付结果未知";
                        break;
                }
                AppActivity.sdkToastLog("SDK支付 "+resultStr);

            }

        });

                

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
