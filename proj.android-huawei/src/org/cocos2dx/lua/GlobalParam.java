/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
*/
package org.cocos2dx.lua;

public class GlobalParam
{
    /**
     * 联盟为应用分配的应用ID
     */
    /**
     * APP ID
     */
    public static final String APP_ID = "100131785";
    
    /**
     * 浮标密钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for buoy, the CP need to save the key value on the server for security
     */
    /**************TODO:DELETE*******************/
    public static String BUOY_SECRET = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7jnAOn5iIH3zzv4acwcA6ykHrZ5aVUCa7/hGYAPqFtCVecPmocYm76E0y7Z+RIV+63ShkEM9i85ragip/PYKUuF8L1KhymXyWm1btNawZZOBeQaOa9kv8bOOr7z1SU22ajywHhsM4RQ82mITuyLe2LTqMwctKh/TSyEZBQhsmeEtTcBtjSSd3b2ENz2GZ6ht2MKQ0GPG6vwiy3+h6RmgPjGjRUJ4Wl5x9Ld+e6zJlt8SYSORawDF5FeEV7bbuB7f7wWHAGpgq+MzFcqydlmOPkLDNdM0Z/tU6XXfUGp4tLcLRXsFRYH3aGZOQzCSavMerwEqRTyqKr4ACkcxvnYWBAgMBAAECggEAX4RKzSe6JpjiqcAAbhS+MTKQH7LBnYkUMOlTTHYzH6R+43nlq0MnQessZaNegCre9etrhoqJE3u8gUuJGQPaXKMIxJ2oq7Q/HvQ9cNDEtKoLGfWTvHdyezt5Nza/pA2Y4/3GDo8zHJUPCl7iVDkkVkLybnzDa1GR/UbBgLqJ2SzKja+puhAyjIYjx6y4yTgGQXHsXQcwiO+HsouUSVOUYu3ePmce67LwssOi6HwehhuQxeqdnONCT1VuOxwh7zk6PtwyyGmfUEjpIVUalduJQFxZbv8Htg9vBGs2+Pzd0L6A2ssJT/uJTJjFXlQYnz+8FFgCAc8okxXKuczSEPagAQKBgQDpaeaA3jeb9FxR2ABbEXR7u8pyhFwnnwQ3ZvwOG03bYCXZlHAnPiULyA8IXjM/6lQLtjKTuxpAmp1nZ2rkzfxqthJXLMoDUtUiv2d6jjtJ/Raoraz9DnJsOoXT5p/iMvLmhaPiK8ICuHhO1QMIcQdxKOrPo3Nt6pbrhNgvZs+AUQKBgQDNtJDJkh+vIHYYL4vFE3+Rite6xqjN6WmxX2C7h6eR6Y3SZVWP+2jGapgrl9v5Vl9fxmMcxumwqkEF8ygHTMCdGlAp9Qef76OXiV7sg8XgK1iy3g8QMK3AeKVb+tOn/fCZ1NfNg1eZQkIkpf20b7gS3AWpXaZlOr1KfgkL/i8WMQKBgQC/cNRVrez+RJXTqs3f45keQw/wjQ7okEvtBNKG324kF9/zbQPvq9DGsPdjbZ+rNI5qbK1e7X7Crg2b2f6aWzhEbcDtLxchUrbtIP/GTRHjxWjoQEo547eFfRp2ihlodVE0MTgRMMKXaKWEQsGSEuJ/tYehdfYemOHfmb2WY0SdgQKBgDyGW2+jugTaRUUagEKNWSSmuJDxMOvdOMcCrEChSnHJSXxVapaBv7NZ28o/1W6ItmlEwxbNhCcCAT3R7L4xHB9dhabCvOqXJQhKWA65OviZaHDAm6gE2qreFLF6nNo6ApgQoyqJ8bVbZIDKAlsb4ApUiKnrrzDr1pRbfyfzM0GRAoGAMjwEwGFNUc3eMdE3iR6S/IyoJ2KJSXa1AEQPetlSjP0COxqvq0z18vdc8clkU6gMOoJkyCYzoTv19DNK6+L555rvjX+mKLXhRm/iaCF1lUsb8egORA5A0CNUBqMJWjp8VmxBNDseynMwVwPZYaaqP4aqR7uODJlpdkL6KyqYG2U=";
    
    /**
     * 支付ID
     */
	 /**
     * Pay ID
     */
    public static final String PAY_ID = "890086000102067737";
    
    /**
     * 支付私钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for pay, the CP need to save the key value on the server for security
     */
    /**************TODO:DELETE*******************/
    public static String PAY_RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYnEx7jH45igOn1aoyWzd3+TnkN1WDVPKDv1u7SET77p8cXqfDOMCh8+V4WW33yrzo2+BaffXrtPZ15pIeCr3bzkHT1JGbmaN5eNpbiN9nrdKd0qqwCSrzBy3QY4NycbmmW3nIpl0bEle03IPbKoihIJ9FgfJdWe/TP/TChx1G7PPqjXCqM/ASfTwE31U4/aNL92vuz9XTHOnaEfrq/T6JyCPz3+mbsBinX/RtxUfuPe2fuJwTw/UJpRLHcJS2vMbbRrvO4PhLyWM2Wmm3nnkZ1+OTPuVXeEU6laTsfvkruJ2/JdccyJr5pcoqjV4BZHFa8+sLpe0cES4Krps6i0LXAgMBAAECggEAI95snYdObaCyLkLqb06AMYsa37hfuXAAVhXmbhTU3e/eZtEalihjIk2MbCZ80cZNc1+mki5T3DQaz2hDhJsat6x5TVwDYzKPuS++BtCbTaeNvrlyk9B7z0YrLvUMwpvXOjH65D2x2XiMs742oY8UMObVeIVL3UQTua+pDAWszGvr8CC2M9u2sSI89BB3tzOa9fErP4qJXc4f5JRUvrIw/uhPDTtk/C2JYUl6slV68VAcPpW51wORn14vTLZSs2tRd3ne/sN/eSeXeWDADpIYEEh3X0CEv/nN9oD/1nLj1JZ5S/f44ZEysGdRhpjvze0+dDOeYhDs1QYKjD/cDLoEBQKBgQDnawLstRgjqV7/TvAwojN2c1SeVr7/nn0z12GsWZagYzIBkC+HnTJFOwxb36aVdxltNEienf8DHC+EBHIno+t5JORR19dcvSFJuAn8ku1Ke0leRwJhM5p1zbyHGA+O7f2xMIuR5z1SWyvK0QZek9dYzbkfhOAUFof0K+lYULPiAwKBgQCo0kM8n/4htHbX1T8torV/aV+3BWbA1/yC86LMT7ij/8qPwQaLIn08I0tHME7BpH0AA264TUkVz3KJ1fAq+0zwTA4Pxsha68zTpmndHM/Z8c3FtjRsIhh2OyGWw2E1e4qt5M/0Q0MiPAFbYdRWtUFv6duYt578CBsaeI/CBuqNnQKBgDJVFgfBw4ROHDlPQ/Jv9N14bBkcKOKT6AtUdwvzWE1+BaxNXekTCZIBK8j3tdWgV8lmq7zEBGj6wmLmTKXOgzKQEZCzb0XeZNUR0Co9Eps2gVid/SiC/r9Mhgqj8w5VOO5G7rRHJRE6fyXXnJkJcrb/15nBXrM/y0YSHuh0dQtdAoGAXabm7cbDr+bw1qLRvXs5vEi0dE9mxSwMRIjrkvPWtgO8Vyr5ek3Ts3zQ0dkWl42sWkmNLmiRVqwSGiy4Yt+tutA2LMJVezq8Ed2LdMH6/CiLwlZllmcDsvLymzQfxQ+XywfX5OU7ZM5s4kjPBprehjrxOzMB20aT4odnGHfFvnECgYEAsxMsdRwbSmBX7t4UfjEUHk1CRX2h5BglDr9Z01OpJ5LP46zYwj2Akzj4wxrG0ns065afFI6IRPQRHBNWCeLEbVL/QZIR6KuVpGJjpAEtuN9AGR9g0e0rRO6mc3CToK18XZfdGS/1gKY4aE02NHSyWWKCsN0Gfe/BRr/QYhuF6nc=";

    /**
     * 支付公钥
     */
    /**
     * public key for pay
     */
    public static final String PAY_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmJxMe4x+OYoDp9WqMls3d/k55DdVg1Tyg79bu0hE++6fHF6nwzjAofPleFlt98q86NvgWn3167T2deaSHgq9285B09SRm5mjeXjaW4jfZ63SndKqsAkq8wct0GODcnG5plt5yKZdGxJXtNyD2yqIoSCfRYHyXVnv0z/0wocdRuzz6o1wqjPwEn08BN9VOP2jS/dr7s/V0xzp2hH66v0+icgj89/pm7AYp1/0bcVH7j3tn7icE8P1CaUSx3CUtrzG20a7zuD4S8ljNlppt555Gdfjkz7lV3hFOpWk7H75K7idvyXXHMia+aXKKo1eAWRxWvPrC6XtHBEuCq6bOotC1wIDAQAB";
    
    /**
     * 登录签名公钥
     */
	
    //Demo //wen dang
    public static final String LOGIN_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKLBMs2vXosqSR2rojMzioTRVt8oc1ox2uKjyZt6bHUK0u+OpantyFYwF3w1d0U3mCF6rGUnEADzXiX/2/RgLQDEXRD22er31ep3yevtL/r0qcO8GMDzy3RJexdLB6z20voNM551yhKhB18qyFesiPhcPKBQM5dnAOdZLSaLYHzQkQKANy9fYFJlLDo11I3AxefCBuoG+g7ilti5qgpbkm6rK2lLGWOeJMrF+Hu+cxd9H2y3cXWXxkwWM1OZZTgTq3Frlsv1fgkrByJotDpRe8SwkiVuRycR0AHsFfIsuZCFwZML16EGnHqm2jLJXMKIBgkZTzL8Z+201RmOheV4AQIDAQAB";
    // public static final String LOGIN_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu45wDp+YiB9887+GnMHAOspB62eWlVAmu/4RmAD6hbQlXnD5qHGJu+hNMu2fkSFfut0oZBDPYvOa2oIqfz2ClLhfC9Socpl8lptW7TWsGWTgXkGjmvZL/Gzjq+89UlNtmo8sB4bDOEUPNpiE7si3ti06jMHLSof00shGQUIbJnhLU3AbY0knd29hDc9hmeobdjCkNBjxur8Ist/oekZoD4xo0VCeFpecfS3fnusyZbfEmEjkWsAxeRXhFe227ge3+8FhwBqYKvjMxXKsnZZjj5CwzXTNGf7VOl131BqeLS3C0V7BUWB92hmTkMwkmrzHq8BKkU8qiq+AApHMb52FgQIDAQAB";

    /*
     * 支付页面横竖屏参数：1表示竖屏，2表示横屏，默认竖屏
     */
    // portrait view for pay UI
	public static final int PAY_ORI = 1;
	// landscape view for pay UI
	public static final int PAY_ORI_LAND = 2;
    
    /**
     * 支付签名类型：RSA256
     */
    public static final String SIGN_TYPE = "RSA256";
    

	/**
	 * 生成签名时需要使用RSA的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the pay private key.The CP need to modify the
	 * value for the real server.
	 */
	public static final String GET_PAY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getPayPrivate";

	/**
	 * 调用浮标时需要使用浮标的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the buoy private key.The CP need to modify the
	 * value for the real server.
	 */
	public static final String GET_BUOY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getBuoyPrivate";
    
    public interface PayParams
    {
        public static final String USER_ID = "userID";
        
        public static final String APPLICATION_ID = "applicationID";
        
        public static final String AMOUNT = "amount";
        
        public static final String PRODUCT_NAME = "productName";
        
        public static final String PRODUCT_DESC = "productDesc";
        
        public static final String REQUEST_ID = "requestId";
        
        public static final String USER_NAME = "userName";
        
        public static final String SIGN = "sign";
        
        public static final String NOTIFY_URL = "notifyUrl";
        
        public static final String SERVICE_CATALOG = "serviceCatalog";
        
        public static final String SHOW_LOG = "showLog";
        
        public static final String SCREENT_ORIENT = "screentOrient";
        
        public static final String SDK_CHANNEL = "sdkChannel";
        
        public static final String URL_VER = "urlver";
        
        public static final String EXT_RESERVED = "extReserved";
        
        public static final String SIGN_TYPE = "signType";
    }
    
}
