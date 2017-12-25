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

//导入sdk包
import com.downjoy.CallbackListener;
import com.downjoy.CallbackStatus;
import com.downjoy.Downjoy;
import com.downjoy.LoginInfo;
import com.downjoy.LogoutListener;
import com.downjoy.ResultListener;
import com.downjoy.UserInfo;
import com.downjoy.util.Util;

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
  private static final int USERTYPE = 24;
  protected static final boolean isDebug = true;//是否是测试模式

  //全局变量
  private static Downjoy downjoy; // 当乐游戏中心实例
  // CP后台可以查询到前三个参数MERCHANT_ID、APP_ID和APP_KEY。
  // MERCHANT_ID，APP_ID，APP_KEY，SERVER_SEQ_NUM四个参数请前往AndroidMainfest.xml文件设置

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      sdkJavaActivity = this;
      //初始化当乐代码
      initDownjoy();
  }

  /**
     * 初始化当乐代码
     */
    private void initDownjoy() {
        // 崩溃日志：如果你们没拦截的话，那么会在SDK卡downjoy/crashlog目录下生成崩溃日志
        // 获取当乐实例，获取为NULL时，请确定自己是否有预先进入SdkLoadActivity界面。
        // 请参考AndroidMainfest的配置，设置此Activity为启动Activity，删除游戏的原有启动Activity。
        downjoy = Downjoy.getInstance();

        // 设置登录成功后是否显示当乐的悬浮按钮
        // 如果此处设置为false，登录成功后，不显示当乐游戏中心的悬浮按钮。
        // 注意：
        // 此处应在调用登录接口之前设置，默认值是true，即登录成功后自动显示当乐游戏中心的悬浮按钮。
        // 正常使用悬浮按钮还需要实现两个函数,onResume、onPause
        downjoy.showDownjoyIconAfterLogined(true);
        //设置悬浮窗显示位置，在服务器设置了悬浮窗位置时，此设置失效
        downjoy.setInitLocation(Downjoy.LOCATION_LEFT_CENTER_VERTICAL);
        //设置全局注销监听器，浮标中的注销也能接收到回调
        downjoy.setLogoutListener(mLogoutListener);
    }

    /**
     * 登出回调
     */
    private LogoutListener mLogoutListener = new LogoutListener() {
        @Override
        public void onLogoutSuccess() {
            AppActivity.sdkToastLog("注销成功回调->注销成功");
            downjoyLogout();
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
        }

        @Override
        public void onLogoutError(String msg) {
            AppActivity.sdkToastLog("注销失败回调->" + msg);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (downjoy != null) {
            downjoy.resume(sdkJavaActivity);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downjoy != null) {
            downjoy.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downjoy != null) {
            downjoy.destroy();
            downjoy = null;
        }
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
    downjoyLogin();
  }

  //提交游戏角色信息
  //zoneId$zoneName$roleId$roleName$roleLevel$roleCtime$type$gender$power$vip$partyid$partyname$partyroleid$partyrolename$friendlist
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("调用用户信息=======");
      String[] submitData = submitDatas.split("\\$");
      //提交游戏数据
      String zoneId = submitData[0];//玩家区服id 没有传字符串1
      String zoneName = submitData[1];//玩家区服名称，没有传字符串001
      String roleId = submitData[2];//玩家角色Id 没有传字符串1
      String roleName = submitData[3];//玩家角色名称 没有传字符串001
      String roleLevel = submitData[4];//角色等级
      long roleCTime =  1480747870001l;//角色创建时间戳。获取不了创建的时间戳，就传这个值。
      long roleLevelMTime = 1480747870001l;//角色等级变化时间戳。获取不了等级变化时间，就跟角色创建时间传一样的值。
      
      AppActivity.sdkToastLog("*******："+zoneId+","+zoneName+","+roleId+","+roleName+","+roleCTime+","+roleLevelMTime+","+roleLevel);

      //上报游戏角色方法
      downjoy.submitGameRoleData(zoneId, zoneName, roleId, roleName, roleCTime, roleLevelMTime, roleLevel, new ResultListener() {
          @Override
          public void onResult(Object result) {
              //上传角色结果
              String resultStr = (String) result;
              if (resultStr.equals("true")){
                  AppActivity.sdkToastLog("提交角色成功=======");
              }
          }
      });
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号===");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
    downjoy.openExitDialog(sdkJavaActivity, new CallbackListener<String>() {

            @Override
            public void callback(int status, String data) {
                if (CallbackStatus.SUCCESS == status) {
                    //收到回调后，CP自己实现游戏的退出，SDK只会回收SDK的资源
                    System.exit(0);
                } else if (CallbackStatus.CANCEL == status) {
                }
            }
        });
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    // userID.."$"..payid.."$"..costMoney.."$"..url.."$"..ip.."$"..port.."$"..signUrl.."$"..produceID.."$"..produceName.."$"..ServerIndex.."$"..myDiamond
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
    String userName = payData[11];
    String gGroupName = payData[14];

    //所有支付参数不要为空值以及""
    final String productName = produceName; // 商品名称
    final String body = costMoney+"0钻石"; // 商品描述
    final String transNo = payid; // cp订单号，计费结果通知时原样返回，尽量不要使用除字母和数字之外的特殊字符。
    final String ext = produceID;//cp透传字段，非必须，不大于100个字符，不能有特殊字符
    String zoneId = serverIndex;//玩家区服id 没有传字符串1
    String zoneName = gGroupName;//玩家区服名称，没有传字符串001
    String roleId = userId;//玩家角色Id 没有传字符串1
    String roleName = userName;//玩家角色名称 没有传字符串001
    // 打开支付界面,获得订单号
    downjoy.openPaymentDialog(sdkJavaActivity, Float.parseFloat(costMoney), productName, body, transNo,ext, zoneId, zoneName, roleId, roleName,new CallbackListener<String>() {
        @Override
        public void callback(int status, String data) {
            if (status == CallbackStatus.SUCCESS) {
                AppActivity.sdkToastLog("成功支付回调->订单号：" + data);
            } else if (status == CallbackStatus.FAIL) {
                AppActivity.sdkToastLog("失败支付回调->error:" + data);
            } else if (status == CallbackStatus.CANCEL) {
                AppActivity.sdkToastLog("取消支付回调->" + data);
            }
        }
    });
  }

  /**
     * 获取用户信息
     */
    private void downjoyInfo() {
        if (null == downjoy)
            return;
        downjoy.getInfo(this, new CallbackListener<UserInfo>() {

            @Override
            public void callback(int status, UserInfo data) {
                if (status == CallbackStatus.SUCCESS) {
                    Util.alert(sdkJavaActivity, data.toString());
                } else if (status == CallbackStatus.FAIL) {
                    Util.alert(getBaseContext(), "onError:" + data.getMsg());
                }
            }
        });
    }

    /**
     * 客户端支付和服务器端支付，选其中一个
     * 服务器下单支付(推荐)
     * 支付分为服务器下单支付和一般支付，服务器下单需要CP在自己的服务器进行下单，但安全上远大于客户端下单
     *
     * @param orderNo 服务器下单订单号
     */
    private void downjoyServerPayment(String orderNo) {
        if (null == downjoy) {
            return;
        }
        if (null == orderNo) {
            return;
        }
        // 打开支付界面,获得订单号
        downjoy.openServerPaymentDialog(this, orderNo, new CallbackListener<String>() {

            @Override
            public void callback(int status, String data) {
                if (status == CallbackStatus.SUCCESS) {
                    Util.alert(sdkJavaActivity, "成功支付回调->订单号：" + data);
                } else if (status == CallbackStatus.FAIL) {
                    Util.alert(sdkJavaActivity, "失败支付回调->error:" + data);
                } else if (status == CallbackStatus.CANCEL) {
                    Util.alert(sdkJavaActivity, "取消支付回调->" + data);
                }
            }
        });
    }

    /**
     * 客户端支付和服务器端支付，选其中一个
     * 客户端支付
     *
     * @param money 商品价格，单位：元
     */
    private void downjoyPayment(final float money) {
        if (null == downjoy)
            return;

        //所有支付参数不要为空值以及""
        final String productName = "测试商品"; // 商品名称
        final String body = getRandString(); // 商品描述
        final String transNo = getRandString(); // cp订单号，计费结果通知时原样返回，尽量不要使用除字母和数字之外的特殊字符。
        final String ext = "ext";//cp透传字段，非必须，不大于100个字符，不能有特殊字符
        String zoneId = "2";//玩家区服id 没有传字符串1
        String zoneName = "测试区服";//玩家区服名称，没有传字符串001
        String roleId = "222222";//玩家角色Id 没有传字符串1
        String roleName = "测试角色名";//玩家角色名称 没有传字符串001

        // 打开支付界面,获得订单号
        downjoy.openPaymentDialog(sdkJavaActivity, money, productName, body, transNo,
                ext, zoneId, zoneName, roleId, roleName,
                new CallbackListener<String>() {

                    @Override
                    public void callback(int status, String data) {
                        if (status == CallbackStatus.SUCCESS) {
                            Util.alert(sdkJavaActivity, "成功支付回调->订单号：" + data);
                        } else if (status == CallbackStatus.FAIL) {
                            Util.alert(sdkJavaActivity, "失败支付回调->error:" + data);
                        } else if (status == CallbackStatus.CANCEL) {
                            Util.alert(sdkJavaActivity, "取消支付回调->" + data);
                        }
                    }
                });
    }

    /**
     * 注销
     */
    private void downjoyLogout() {
        if (null == downjoy)
            return;
        // 注销后需要弹出登录框
        downjoyLogin();
        //账号注销后，CP在此处自行处理注销后的逻辑
    }

    /**
     * 登录
     */
    private static void downjoyLogin() {
        if (null == downjoy)
            return;
        downjoy.openLoginDialog(sdkJavaActivity, new CallbackListener<LoginInfo>() {

            @Override
            public void callback(int status, LoginInfo data) {
                if (status == CallbackStatus.SUCCESS && data != null) {
                    //当乐提供的openid，用户唯一标识
                    String umid = data.getUmid();
                    String username = data.getUserName();
                    String nickname = data.getNickName();

                    //本次登录生成的token
                    //必接，必须校验,具体看服务器端文档
                    String token = data.getToken();
                    AppActivity.sdkToastLog("登录成功");
                    AppActivity.sdkLoginCallBack(umid, USERTYPE, PLATFROM,token);
                    
                    // Util.alert(sdkJavaActivity, "登录成功回调->" + data.toString());

                    //4.3.5新增，登录成功后，提交游戏数据（也可选择从服务器提交，具体参见服务器端接入文档）
                    //所有参数尽量不要填空值
                    //关于什么时候上报游戏数据：确保每次登录上传一次数据即可，不一定要在登录后马上上传！
                    /*
                    String zoneId = "2";//玩家区服id 没有传字符串1
                    String zoneName = "测试区服";//玩家区服名称，没有传字符串001
                    String roleId = "222222";//玩家角色Id 没有传字符串1
                    String roleName = "测试角色名";//玩家角色名称 没有传字符串001
                    long roleCTime = 1480747870001l;//角色创建时间戳。获取不了创建的时间戳，就传DEMO中的这个值。
                    long roleLevelMTime = 1480747870001l;//角色等级变化时间戳。获取不了等级变化时间，就跟角色创建时间传一样的值。
                    String roleLevel = "22";//角色等级，如果没有这个值，传字符串1
                    downjoy.submitGameRoleData(zoneId, zoneName, roleId, roleName, roleCTime, roleLevelMTime, roleLevel, new ResultListener() {
                        @Override
                        public void onResult(Object result) {
                            //上传角色结果
                            String resultStr = (String) result;
                            if (resultStr.equals("true")){
                                //提交角色成功
                            }
                        }
                    });
                    */
                } else if (status == CallbackStatus.FAIL && data != null) {
                    AppActivity.sdkToastLog("登录失败回调->" + data.getMsg());
                } else if (status == CallbackStatus.CANCEL && data != null) {
                    AppActivity.sdkToastLog("登录取消回调->" + data.getMsg());
                }
            }
        });
    }

    /**
     * 退出对话框
     */
    private boolean downjoyExit() {
        if (null == downjoy)
            return false;
        downjoy.openExitDialog(this, new CallbackListener<String>() {

            @Override
            public void callback(int status, String data) {
                if (CallbackStatus.SUCCESS == status) {
                    //收到回调后，CP自己实现游戏的退出，SDK只会回收SDK的资源
                    finish();
                } else if (CallbackStatus.CANCEL == status) {
                    Util.alert(getBaseContext(), "退出回调-> " + data);
                }
            }
        });
        return true;
    }

    /**
     * 生成CP自定义信息和订单号
     *
     * @return
     */
    private static String getRandString() {
        int[] x = new int[10];
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            x[i] = (int) (Math.random() * 10);
            b.append(x[i]);
        }

        return b.toString() + "测试";
    }

}
