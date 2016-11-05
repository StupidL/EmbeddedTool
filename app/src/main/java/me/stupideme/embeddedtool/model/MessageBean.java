package me.stupideme.embeddedtool.model;

/**
 * Created by stupidl on 16-10-14.
 */

/**
 * a message bean to contains commands' info
 */
public class MessageBean {

    /**
     * header of the command
     */
    private String mHeader;

    /**
     * tail of the command
     */
    private String mTail;

    /**
     * body of the command. In usual it is a hex value
     */
    private String mBody;

    /**
     * a request code to identity where this command from
     */
    private String mRequestCode;

    /**
     * a code to identity which type this command operates
     */
    private String mDataType;

    /**
     * setter of header
     * @param header header
     */
    public void setHeader(String header) {
        mHeader = header;
    }

    /**
     * getter of header
     * @return header
     */
    public String getHeader() {
        return mHeader;
    }

    /**
     * setter of tail
     * @param tail tail
     */
    public void setTail(String tail) {
        mTail = tail;
    }

    /**
     * getter of tail
     * @return tail
     */
    public String getTail() {
        return mTail;
    }

    /**
     * setter of body
     * @param body body
     */
    public void setBody(String body) {
        mBody = body;
    }

    /**
     * getter of body
     * @return body
     */
    public String getBody() {
        return mBody;
    }

    /**
     * setter of request code
     * @param code code
     */
    public void setRequestCode(String code) {
        mRequestCode = code;
    }

    /**
     * getter of request code
     * @return request code
     */
    public String getRequestCode() {
        return mRequestCode;
    }

    /**
     * setter of data type
     * @param type data type
     */
    public void setDataType(String type) {
        mDataType = type;
    }

    /**
     * getter if data type
     * @return data type
     */
    public String getDataType() {
        return mDataType;
    }

    /**
     * to string as a command content
     * @return command's content
     */
    @Override
    public String toString() {
        return mHeader + mRequestCode + mDataType + mBody + mTail;
    }
}
