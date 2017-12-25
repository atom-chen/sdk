/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
 */
package org.cocos2dx.lua;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.Configuration;

import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameEventHandler;
import com.huawei.gameservice.sdk.util.LogUtil;
import com.huawei.gb.huawei.net.ReqTask;
import com.huawei.gb.huawei.net.ReqTask.Delegate;

public class GameBoxUtil
{
    public static final String TAG = GameBoxUtil.class.getSimpleName();
    
    public static void pay(
        final Activity activity,
        final String price,
        final String productName,
        final String productDesc,
        final String requestId,
        final GameEventHandler handler)
    {
        
        Map<String, String> params = new HashMap<String, String>();
        // 必填字段，不能为null或者""，请填写从联盟获取的支付ID
        // the pay ID is required and can not be null or "" 
        params.put(GlobalParam.PayParams.USER_ID, GlobalParam.PAY_ID);
        // 必填字段，不能为null或者""，请填写从联盟获取的应用ID
        // the APP ID is required and can not be null or "" 
        params.put(GlobalParam.PayParams.APPLICATION_ID, GlobalParam.APP_ID);
        // 必填字段，不能为null或者""，单位是元，精确到小数点后两位，如1.00
        // the amount (accurate to two decimal places) is required
        params.put(GlobalParam.PayParams.AMOUNT, price);
        // 必填字段，不能为null或者""，道具名称
        // the product name is required and can not be null or "" 
        params.put(GlobalParam.PayParams.PRODUCT_NAME, productName);
        // 必填字段，不能为null或者""，道具描述
        // the product description is required and can not be null or "" 
        params.put(GlobalParam.PayParams.PRODUCT_DESC, productDesc);
        // 必填字段，不能为null或者""，最长30字节，不能重复，否则订单会失败
        // the request ID is required and can not be null or "". Also it must be unique.
        params.put(GlobalParam.PayParams.REQUEST_ID, requestId);

        String noSign = getSignData(params);
        LogUtil.d("startPay", "noSign：" + noSign);
        
        // CP必须把参数传递到服务端，在服务端进行签名，然后把sign传递下来使用；服务端签名的代码和客户端一致
        // the CP need to send the params to the server and sign the params on the server , 
        // then the server passes down the sign to client;
        String sign = RSAUtil.sign(noSign, GlobalParam.PAY_RSA_PRIVATE);
        LogUtil.d("startPay", "sign： " + sign);


        Map<String, Object> payInfo = new HashMap<String, Object>();
        // 必填字段，不能为null或者""
        // the amount is required and can not be null or "" 
        payInfo.put(GlobalParam.PayParams.AMOUNT, price);
        // 必填字段，不能为null或者""
        // the product name is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.PRODUCT_NAME, productName);
        // 必填字段，不能为null或者""
        // the request ID is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.REQUEST_ID, requestId);
        // 必填字段，不能为null或者""
        // the product description is required and can not be null or ""
        payInfo.put(GlobalParam.PayParams.PRODUCT_DESC, productDesc);
        // 必填字段，不能为null或者""，请填写自己的公司名称
        // the user name is required and can not be null or "". Input the company name of CP.
        payInfo.put(GlobalParam.PayParams.USER_NAME, "青岛大卫骆驼网络科技有限公司");
        // 必填字段，不能为null或者""
        // the APP ID is required and can not be null or "". 
        payInfo.put(GlobalParam.PayParams.APPLICATION_ID, GlobalParam.APP_ID);
        // 必填字段，不能为null或者""
        // the user ID is required and can not be null or "". 
        payInfo.put(GlobalParam.PayParams.USER_ID, GlobalParam.PAY_ID);
        // 必填字段，不能为null或者""
        // the sign is required and can not be null or "".
        payInfo.put(GlobalParam.PayParams.SIGN, sign);
        // 选填字段，建议使用RSA256
        // recommended to use RSA256.
        payInfo.put(GlobalParam.PayParams.SIGN_TYPE, GlobalParam.SIGN_TYPE);
        
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            payInfo.put(GlobalParam.PayParams.SCREENT_ORIENT,
                    GlobalParam.PAY_ORI_LAND);
        }
        else
        {
            payInfo.put(GlobalParam.PayParams.SCREENT_ORIENT,
                    GlobalParam.PAY_ORI);
        }
        
        GameServiceSDK.startPay(activity, payInfo, handler);
        
    }
    
    public static void startPay(final Activity activity, final String price, final String productName, final String productDesc,
            final String requestId, final GameEventHandler handler)
    {
        if ("".equals(GlobalParam.PAY_RSA_PRIVATE))
        {
            // ReqTask getPayPrivate = new ReqTask(new Delegate()
            // {

            //     @Override
            //     public void execute(String privateKey)
            //     {
            //         /**
            //          * 从服务端获取的支付私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取， 服务端代码参考华为Demo 请查阅 华为游戏中心SDK开发指导书.docx 的2.5节
            //          */
            //     	/**
            //     	 * The demo app did not deployed the server, so the return value is written fixed.For real app,the CP need to get the  key from server.
            //     	 */
            //         privateKey = "";
            //         GlobalParam.PAY_RSA_PRIVATE = privateKey;
            //         pay(activity, price, productName, productDesc, requestId, handler);
            //     }
            // }, null, GlobalParam.GET_PAY_PRIVATE_KEY);
            // getPayPrivate.execute();

            pay(activity, price, productName, productDesc, requestId, handler);
        }
        else
        {
            pay(activity, price, productName, productDesc, requestId, handler);
        }
        
    }
    
    public static String getSignData(Map<String, String> params)
    {
      StringBuffer content = new StringBuffer();
      
      List keys = new ArrayList(params.keySet());
      Collections.sort(keys);
      for (int i = 0; i < keys.size(); i++)
      {
        String key = (String)keys.get(i);
        if (!"sign".equals(key))
        {
          String value = (String)params.get(key);
          if (value != null) {
            content.append((i == 0 ? "" : "&") + key + "=" + value);
          }
        }
      }
      return content.toString();
    }
}
