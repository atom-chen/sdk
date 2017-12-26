package com.lenovo.id.pay.sample.net;

public class HttpUtilException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errCode = 0;
	
	public HttpUtilException(String message) {
		super(message);
	}
	
	public HttpUtilException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public HttpUtilException(String message, int errCode) {
		super(message);
		this.errCode = errCode;
	}

	public int getErrCode() {
		return errCode;
	}
}
