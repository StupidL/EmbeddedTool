package me.stupideme.embeddedtool.model;

/**
 * Created by stupidl on 16-10-14.
 */

public class MessageBean {
    //header
    private java.lang.String mHeader;
    //tail
    private java.lang.String mTail;
    //body
    private java.lang.String mBody;
    //send or receive
    private int mRequestCode;
    //data type
    private String mDataType;

    public void setHeader(java.lang.String header) {
        mHeader = header;
    }

    public java.lang.String getHeader() {
        return mHeader;
    }

    public void setTail(java.lang.String tail) {
        mTail = tail;
    }

    public java.lang.String getTail() {
        return mTail;
    }

    public void setBody(java.lang.String body) {
        mBody = body;
    }

    public java.lang.String getBody() {
        return mBody;
    }

    public void setRequestCode(int type) {
        mRequestCode = type;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setDataType(String type) {
        mDataType = type;
    }

    public String getDataType() {
        return mDataType;
    }

    @Override
    public java.lang.String toString() {
        return mHeader + mRequestCode + mDataType + mBody + mTail;
    }
}
