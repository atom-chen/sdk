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
import android.content.res.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import com.pyw.entity.UserParams;
import com.pyw.open.IDefListener;
import com.pyw.open.IGameExitListener;
import com.pyw.open.IGetRoleListener;
import com.pyw.open.ILogoutCallback;
import com.pyw.open.ILogoutListener;
import com.pyw.open.IPayListener;
import com.pyw.open.PYWSDK;
import com.pyw.open.PYWUser;
import com.pyw.open.PayParams;
import com.pyw.open.PayResult;

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
import com.nlmyx.pyw.R;
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
    
    private static final int PLATFROM = 5;
    private static final int USERTYPE = 21;
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
        

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了
                       

                       /**
         * 必须在主act中onCreate执行
         */
        PYWSDK.init(sdkJavaActivity, new IDefListener() {

            @Override
            public void onSuccess() {
                AppActivity.sdkToastLog("SDK初始化成功");
            }

            @Override
            public void onFail(int code, String message) {
                AppActivity.sdkToastLog("SDK初始化失败");
            }
        });


            }
        });




        /**
         * 非登出接口处的登出回调（如个人中心的注销）
         */
        PYWSDK.setLogoutCallback(new ILogoutCallback() {

            @Override
            public void onLogout() {
                AppActivity.sdkToastLog("非登出接口处的登出回调");
                    // 
        
                    sdkJavaActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //此时已在主线程中，可以更新UI了

                            sdkJavaActivity.runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm"); 
                                AppActivity.sdkToastLog("java调用lua======");
                                sdkJavaLogin();

                            }
                          });

                         }
                    });



            }
        });

    }

    @Override
    protected void onPause() {
        PYWSDK.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        PYWSDK.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        PYWSDK.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        PYWSDK.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        PYWSDK.onRestart();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        PYWSDK.onStop();
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        PYWSDK.newIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        PYWSDK.configurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PYWSDK.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //游戏初始化 必须调用
    public static void sdkJavaInit() {
        sdkJavaLogin();
    }

    //登录游戏 必须调用
    public static void sdkJavaLogin() {
        // 调用SDK执行登陆操作

        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了


                PYWSDK.login(new com.pyw.open.IUserListener() {

                            @Override
                            public void onLoginSuccess(PYWUser user) {
                                AppActivity.sdkToastLog("SDK登录成功");
                                AppActivity.sdkToastLog(""+user.getToken());
                                AppActivity.sdkLoginCallBack(""+user.getUserId(), USERTYPE, PLATFROM,""+user.getToken());
                            }

                            @Override
                            public void onLoginFail(int code, String message) {
                                AppActivity.sdkToastLog("SDK登录失败");
                            }
                        });

                                      

            }
        });
        
    }

private static String setServerAreaName2 = "区服信息";
//提交游戏角色信息 必须调用
// zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip
public static void sdkJavaSubmitData(final String submitDatas) {
       String[] info = submitDatas.split("\\$");
       String setRoleid=info[2];
       String setRoleName=info[3];
       String setRoleLevel=info[4];
       String setServerArea=info[0];
       String setServerAreaName=info[1];
       setServerAreaName2=info[1];


       UserParams params = new UserParams();
                        // 角色id （必填）
                        params.setRoleid(setRoleid);
                        // 角色名 （必填）
                        params.setRoleName(setRoleName);
                        // 角色等級 （必填）
                        params.setRoleLevel(setRoleLevel);
                        // 区服标识 （必填）
                        params.setServerArea(setServerArea);
                        // 区服完整信息（必填）
                        params.setServerAreaName(setServerAreaName);
                        PYWSDK.getRoleMessage(params, new IGetRoleListener() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFail(int code, String message) {

                            }
                        });

}

    //切换账号 必须调用
    public static void sdkJavaLogout() {


sdkJavaActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了


                        sdkJavaActivity.runOnGLThread(new Runnable() {
                        @Override
                        public void run() {
                            AppActivity.sdkToastLog("java调用lua======");



                            PYWSDK.logout(new ILogoutListener() {

                                @Override
                                public void success() {
                                    sdkJavaLogin();
                                }

                                @Override
                                public void fail(int code) {
                                    
                                }
                            });



                        }
                      });

                        

                     }
              });



    }

    //退出游戏 必须调用
    public static void sdkJavaExit() {

        PYWSDK.exitGame(new IGameExitListener() {

            @Override
            public void onExitError(int code, String message) {
                // 退出错误
            }

            @Override
            public void onExit() {
                // 这里执行真正的退出游戏操作
                System.exit(0);
            }

            @Override
            public void onCancel() {
                // 取消退出操作，正常回到游戏

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
        String userName = payData[11];
        // Toast.makeText(sdkJavaActivity, userId, Toast.LENGTH_LONG).show();
//        doSdkPay(null,true,ProtocolConfigs.FUNC_CODE_WEIXIN_PAY,userId,payid,costMoney,payUrl,ip,port);
//AppActivity.sdkToastLog(""+payUrl);


JSONObject jobj = new JSONObject();
        try {
            // 以下为非必要参数，只供参考，厂商可根据自身需求决定传什么参数与值或者不传
            jobj.put("roles_nick", "角色名称1");
            jobj.put("area_name", "游戏区服名称");
            jobj.put("area_num", "游戏区服编号");
            jobj.put("channel", "渠道号");
            jobj.put("product_desc", "商品描述");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final PayParams params = new PayParams();
        // 充值类型 1-定额充值 2-任意充值
        params.setChargeType("1");
        // 游戏附加字段(必须为json字串)
        params.setExtension(jobj.toString());
        // 订单价格
        int price = Integer.parseInt(costMoney);
        params.setPrice(price);
        // 商品id
        params.setProductId(produceID);
        // 商品名称
        params.setProductName(produceName);
        // 订单id
        params.setOrderID(payid);
        // 角色名称
        params.setRoleName(userName);
        // 区服名称
        params.setServerName(setServerAreaName2);



        sdkJavaActivity.runOnUiThread(new Runnable() {
             @Override
            public void run() {
       //此时已在主线程中，可以更新UI了

        
                PYWSDK.pay(params, new IPayListener() {

            @Override
            public void onPaySuccess(PayResult result) {

            }

            @Override
            public void onPayFail(int code, String message) {

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
