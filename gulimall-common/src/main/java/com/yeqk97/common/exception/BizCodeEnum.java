package com.yeqk97.common.exception;

public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "Unknown exception"),
    VALID_EXCEPTION(10001, "Invalid data format");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
