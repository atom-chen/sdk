package com.lenovo.id.pay.sample.net;

import android.text.TextUtils;

public class ResponseResult {

  public static final ResponseResult EXCEPTION = new ResponseResult("") {
    public String getErrorMessage() {
      return "未知异常";
    };
  };

  private String mResult;

  private ResponseResult(String result) {
    mResult = result;
  }

  public boolean isSuccess() {
    return "200".equals(mResult);
  }

  public String getErrorMessage() {
    return mResult;
  }

  public String getLogErrorMsg() {
    return mResult;
  }

  public static ResponseResult getResponse(String result) {
    if (TextUtils.isEmpty(result)) { return EXCEPTION; }
    return new ResponseResult(result);
  }

}
