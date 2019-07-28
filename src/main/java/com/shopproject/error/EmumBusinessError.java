package com.shopproject.error;

public enum EmumBusinessError implements CommonError{

    PARAMETER_VALIDATION_ERROR(00001,"param not valid"),
    USER_NOT_EXIST(10001, "user not exist"),
    UNKNOWEN_ERROR(10000, "未知错误")
    ;

    private int errCode;
    private String errMsg;

    private EmumBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
