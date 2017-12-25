package org.cocos2dx.lua;

public class Constants {
 /*   public static final String API_KEY = "AA169E4B17FD4B45B32B51A8F02DAB4D";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIWOKJ7gti1vTBygj2oJ4UyJO8il66XO4wE/VD/EAEw8l/qxTyzHPFaVvdC+MRM76bsPucw+tz9FaVfIoPMgPPWlR0DRGOwYkAuf307X7D5sJ8tlggrEyRrArg09RLQS7+SPqUo5nLik2W9/SoA2XjhMZgQifvfp8VRDCfEH+AB3AgMBAAECgYBmF7+xg/F/Vd8G2y4GTzssuJM81KkIfNhG8dRDUl8v5/n9p2Dwx/S1843AbkMxgtEvL9xF54NJGJRoiHOQpwoxWcmdWWK0/032injkKt87rki4vqzqiu06UGBCNCCSlX/vcmx5e9XMDsxLaEjzOjFiNSaDI84NsETO9GP091Gd8QJBAMlI+ElSZnCVeeaVkUoxlLFDDCjwW2JhNMPblLcZhSk1AXV9EQXqZKTFoMZoCwb9XUGfinTdVRjsPk9vsNu1gx0CQQCp3AKMlB2BILYAoFdYTV4qg5ScuGRhf1jM/WGo+VOcz9frdSnxIzqKEMqkN86lFB8/V9e4hRBFTNV6VoQZN4mjAkEAoUpSglZ/9akhZC7nnF88egpy3P81CxE/ec4jTdEqTgnGYIHWOTpIaWxfV30ZkQFHUAVqqhiNTwpHJ9olCvlY1QJASttMvEtiHVAfJXOESsP95dqLGe4hPt5ychSQf3kxof/u9jNyQnT2DXaM94YoqZOOmcnKpeTuYLwVsI6ENODG5QJBAMQ2ZN/sd22lA2mpppOPSiitm3kWxJkMofxyAwzgsZaVmrmgnC7a6G646hitO9aaoQ5Jri8O3x1GoViwh0VtneY=";
*//*
 //test1.com
    public static final String API_KEY = "4FFAB0698CDE4C0193849D1B8156FEC1";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIe1m0w6xmEx4Y+R+ajIrOaq7VzjMKMyGf5D7d2oIwMpnntwzxk+irefqvtiLQKERVjTunQbsPEvYSETNt2M95XiMA+Ba2H+41iBF0f3/pZDCyUyWWtoePr2uWSUT2gaV7vOtMwxf6H5CK26pZZsDt/A1qYptryA3dt88NjkA8CPAgMBAAECgYA12wQ+RDTl7X2uFCgXq0uyXrLD4gUzPnbCSQLkrAz4/FNAeYw2YHN8W4jnKhbioMtEbQHZggO+MydAkPd9BKxb4anBULUcQEzDp/Tmm6Rasv6/3rxBMbcZSZaSUQBoGy+x4kqzupyTEKnpRbVJg+W/IE+9GqryXyG0kP+Fi5xFAQJBANR5bM7q0MgMTke5y2aqM1FH7TQyCoV3RwM9ZX3fDINbSTehTE87JXJsTvql4VsmDKf3gnNjoBEXkHUqhqb4jR8CQQCjgnoejkx+zY01lHb4sVJqEhfPky5aOURxAVoe/svsbSkZ++VBT0wf9QSXOR667VyvDK32+UrMBgpGfoUn1+6RAkEAvtST6j+x5opO4FK5oWqqDo2IOMHc+0RGrl09Q1DKX0vktacT73FSz7MBOFM11PSWl2FQc6x9vfwpDWJ1b496xwJBAJ5sznwKri1yZi6S8g8WaF7jtvRli9TZhenkQHilsdobDmbhvQhhl8Wi9la7fn3pfBMTuv8AHiu+cnhJICUxwDECQHVurYc/v+zwkeKUFJ/2s4HzTVMiF6/bwcX/pw4ZEHDFuZ3Ag82RsuqEMBb7JUB6NtdM/kWzkQuMUj0uSuAEb5Y=";
*/

    public static final String API_KEY = "B667D7D3AE9541F6B9BD0692005EAEA3";
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALVrVKYFykcpqyiT3ySH8P8cjQltcq4ognzBTqNAheXU9s/1NMeR8D2Q/+CZONL0PzG13CKAy1MRHTDkE3KoAlCZKYMp9nQTAdM4CHtX4sZ90qAEgogWhGwDURy36JIpBNl4yfs3k12hmlhxkE6teNAm4uS2rMn2z2BzXGNY1+lbAgMBAAECgYBneO8Pvi+vxOlVPHnB+TMQHjWm0eDdykcaZpq2a/+ypXOcCqvuw6L1SnVWoa2STDKa5LHIkBLnrtQqZXteIssmPUxhojVo9RROIBViHPqvGMo0HecZvsr7YLv4Po1M2FNuY21Ip6BzuUdBReVZtRXUfL3XlYgaHMry5sxlPjAr6QJBANncOZnrW+F7l/0mmgr3wZ7zxDkV3xtQ/mUDPE7vdN92MZCmIvIQCFDQ9ecI8yLy9AoQioH+RxcnZFZUizWf6R8CQQDVLfDmv5ezToBmXldiZ20jyaDHML/e3NFKV8JvsCGZB0c+ihioSsKTZdybCy9yHfeLeZVSX4wiR9LKKJaD+2xFAkAWAMsEg6PM+ymXlL18HBe/EQmhZlcZMC3xT8lYg5C/PPW2kUD4R6w/VUtwoRPFVNpW5eLHTEIr0XutyG4i5VqrAkEAjm8fok0pArjUuZEXObx5A4xYTD6aNEG95Lo2Q4GfMSvXwkVqrCcnUmygtUcH8GttVldqGZuVeeIbSM9Noel/MQJAWM839pGTj1EhCk6Zfgz/V8xZ6k1gjpvEMBQcvOPJ4Icd8bD+nuQyESiQGAS4M2Cd31J5uifJk03TBrrgUaYv7Q==//g/KY2BzWRuMx/hOk8J48uOGrR6B9mgDtiMkjI790AWIRHiUXBUduXCUYcm9qtdIm3QeZWtmptCs8a01YmK3Qq1MNggs7kUjKoB6j2U8IZY/w3jirplU+pbcePAgMBAAECgYEAtZTg1mbTAG0Vy8lNegUhzj8OybGXacyE7sEPn84VJi5XYLhMp5KE7NCnKLzD//+8d7BNwpIvW2M4u5oj4GWU9aiCEKO3Ho4obFwwgbRla01/2fI/RNqhYHA5rNii3LUe29sSoBcqPCK+lR7NtTkDHW2ghgecd0qVbav/jEWnCgECQQD9ui4Jy5306RQ+THrLbXQT4cScY6WYDgDdlRgoUUZaDz2JVNtmrsa83lPBdTfcGHCRS9jmo4bCR46Ptdm9GgIPAkEA5N5ertbTgBSZ/lFOnm09dinnIg0eW2NZ8b8gWO2foRRGZQCMAvplFHzKPU6oVebusA1PQIoapC+bQsQugedigQJACKEtca3YOH048Al/361gVlDGdB87gZlwVBj/Elf5UZTVHfeh4rMtFT1mkdzfwn+eTILM5MNJERCH+8FOn4zj5wJBAMyoPKrD16U0xu+v7UmmDcvYdKLycC7wjMGsU1SySIhWBQzUjF8tYa4MWRyxdb47Hz226SOhW2luWLw/igcHzgECQQCg54Y8agbnY4syXqr+bgE7OMW44HRH+oMUCVpQI30hCY/kdK9BqADVK58zWSs8IeIxcDH4+CMXPV/Pb+4lx5DY";



    public final static int LOGIN_REQUEST_CODE = 111;
    public final static int UPGRADE_USING_ACCOUNT_CODE = 222;

    public static class ResultCode {
        public static final int LOGIN_SUCCESS = 1001;
        public static final int UPGRADE_USING_ACCOUNT_SUCCESS = 1007;
    }

    public static final String RE_LOGION = "reLogion";
    public static final String ACCOUNT_STATUS = "accountStatus";
    public static final String ASSOCIATE_STRING = "as";
    public static final String TELEPHONE_NUMBER = "tn";

    public static final String PLAYER_ID = "pid";
    public static final String NAME = "na";
    
    public static String sApiKey = API_KEY;
    public static String sPrivateKey = PRIVATE_KEY;

       public static String sUserId;
    public static String sPlayerId;


}
