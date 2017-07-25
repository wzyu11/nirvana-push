package com.nirvana.push.protocol;

public enum MessageCharset {

    UTF8(0x01, "UTF-8"), UTF16(0x02, "UTF-16"), GB2312(0x03, "GB2312"), GB18030(0x04, "GB18030");

    private int code;

    private String charset;

    MessageCharset(int code, String charset) {
        this.code = code;
        this.charset = charset;
    }

    public int getCode() {
        return code;
    }

    public String getCharset() {
        return charset;
    }
}
