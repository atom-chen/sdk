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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwInitCallback;
import com.muzhiwan.sdk.core.callback.MzwLoignCallback;
import com.muzhiwan.sdk.core.callback.MzwPayCallback;
import com.muzhiwan.sdk.service.MzwOrder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
   联想             9             0
  */
  public static SDKJavaActivity sdkJavaActivity = null;
  private static final int PLATFROM = 5;
  private static final int USERTYPE = 26;
  protected static final boolean isDebug = false;//是否是测试模式

  //全局变量
  private static MzwOrder order;
  private static Handler mHandler;
  private static final int MSG_INIT = 0X01;
  private static final int MSG_LOGIN = 0X02;
  private static String token="";

  /**
   * 需要进行检测的权限数组  这里只列举了几项  小伙伴可以根据自己的项目需求 来添加
   */
  protected String[] needPermissions = {
          Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
          Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
          Manifest.permission.READ_PHONE_STATE,//读取手机状态权限
          Manifest.permission.RECEIVE_SMS,
          Manifest.permission.READ_SMS,
  };
  private static final int PERMISSON_REQUESTCODE = 0;

  /**
   * 检测是否有网络
   *
   * @param context
   * @return
   */
  public static boolean isNetworkAvailable(Context context) {
      ConnectivityManager cm = (ConnectivityManager) context
              .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cm.getActiveNetworkInfo();
      if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
          return true;
      return false;
  }

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sdkJavaActivity = this;

    mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
          case MSG_INIT:
            if (msg.arg1 == 0) {
              AppActivity.sdkToastLog("SDK初始化失败");
            } else {
              AppActivity.sdkToastLog("SDK初始化成功");
            }
            break;
          case MSG_LOGIN:
            if (msg.arg1 == 1) {
              AppActivity.sdkToastLog("登录成功");
              AppActivity.sdkLoginCallBack("", USERTYPE, PLATFROM,token);
            } else if (msg.arg1 == 6) {
              AppActivity.sdkToastLog("登出成功");
              sdkJavaLogin();
              SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      //此时已在主线程中，可以更新UI了
                      SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                          @Override
                          public void run() {
                              Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("goToLogin", "parm");
                          }
                      });
                  }
              });
            } else if (msg.arg1 == 4) {
              AppActivity.sdkToastLog("取消登录");
            } else {
              AppActivity.sdkToastLog("登录失败");
            }
            break;
        }
      }
    };

    // MzwSdkController.getInstance().init(sdkJavaActivity, MzwSdkController.ORIENTATION_HORIZONTAL, new MzwInitCallback() {
    //   @Override
    //   public void onResult(int code, String msg) {
    //     Message message = new Message();
    //     message.what = MSG_INIT;
    //     message.arg1 = code;
    //     mHandler.handleMessage(message);
    //   }
    // });
  }

  //初始化回调
  public static void sdkJavaInit(){
    sdkJavaLogin();
  }


  //登录游戏 必须调用
  public static void sdkJavaLogin() {
    AppActivity.sdkToastLog("开始登录=======");
    //请不要在回调函数里进行UI操作，如需进行UI操作请使用handler将UI操作抛到主线程
    //调用登录接口
    MzwSdkController.getInstance().doLogin(new MzwLoignCallback() {
      @Override
      public void onResult(int code, String msg) {
        Message message = new Message();
        message.what = MSG_LOGIN;
        message.arg1 = code;
        token=msg;
        mHandler.handleMessage(message);
      }
    });
  }

  //提交游戏角色信息
  //zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("调用用户信息=======");
      String[] submitData = submitDatas.split("\\$");
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号===");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
      MzwSdkController.getInstance().doLogout();
      System.exit(0);
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          String[] payData = payDatas.split("\\$");
          String userId=payData[0];
          String payid = payData[1];
          String costMoney=payData[2];
          String payUrl=payData[3];
          String ip = payData[4];
          String port = payData[5];
          String produceID = payData[7];
          String produceName = payData[8];
          String serverIndex = payData[9];

          // costMoney="1";

          order = new MzwOrder();
          order.setMoney(Double.parseDouble(costMoney));
          order.setProductid(produceID);
          order.setProductname(produceName);
          order.setProductdesc(costMoney+"0钻石");
          order.setExtern(payid);
          AppActivity.sdkToastLog(costMoney+","+produceName+","+costMoney);
          MzwSdkController.getInstance().doPay(order, new MzwPayCallback() {
              @Override
              public void onResult(int code, MzwOrder order) {
              }
          });
       }
    });
  }

  @Override
  public void onBackPressed() {
    AppActivity.sdkToastLog("回退");
    MzwSdkController.getInstance().destory();
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    AppActivity.sdkToastLog("释放");
    super.onDestroy();
  }

  public void checkNetwork() {
        // !!!在调用SDK初始化前进行网络检查
        // 当前没有拥有网络
        if (false == isNetworkAvailable(this)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("网络未连接,请设置网络");
            ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent("android.settings.SETTINGS");
                    startActivityForResult(intent, 0);
                }
            });
            ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            ab.show();
        } else {
            MzwSdkController.getInstance().init(sdkJavaActivity, MzwSdkController.ORIENTATION_HORIZONTAL, new MzwInitCallback() {
              @Override
              public void onResult(int code, String msg) {
                Message message = new Message();
                message.what = MSG_INIT;
                message.arg1 = code;
                mHandler.handleMessage(message);
              }
            });
        }
    }

    // 获取根目录
    public String getRootDirPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            return this.getFilesDir().getAbsolutePath() + "/mzwsdk/";
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >=23) {
            //检查权限，开启必要权限
            checkPermissions(needPermissions);
        }else
        {
            checkNetwork();
        }
    }


    /**
     * 检查权限
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(sdkJavaActivity, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
        }else
        {
            checkNetwork();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(sdkJavaActivity, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否有的权限都已经授权
     *
     * @param grantResults
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                AppActivity.sdkToastLog("请先允许权限,否则会影响程序正常使用");
            }else{
                AppActivity.sdkToastLog("已允许");
            }
        }
    }
}
