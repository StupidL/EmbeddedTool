package me.stupideme.embeddedtool.model;

import me.stupideme.embeddedtool.DataType;

/**
 * Created by stupidl on 16-10-14.
 */

public class MessageBean {
    //header
    private String mHeader;
    //tail
    private String mTail;
    //body
    private String mBody;
    //send or receive
    private int mRequestCode;
    //data type
    private DataType mDataType;

    public void setHeader(String header) {
        mHeader = header;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setTail(String tail) {
        mTail = tail;
    }

    public String getTail() {
        return mTail;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getBody() {
        return mBody;
    }

    public void setRequestCode(int type) {
        mRequestCode = type;
    }

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setDataType(DataType type) {
        mDataType = type;
    }

    public DataType getDataType() {
        return mDataType;
    }

    @Override
    public String toString() {
        return mHeader + mRequestCode + mDataType + mBody + mTail;
    }
}
