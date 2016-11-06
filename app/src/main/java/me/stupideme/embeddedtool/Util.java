package me.stupideme.embeddedtool;

import android.util.Log;

/**
 * Created by stupidl on 16-11-3.
 */

public class Util {

    public static byte[] hexStringToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            Log.v("Util", "d[" + i + "] = " + d[i]);
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String adjustText(String s) {
        if (s.isEmpty())
            return "00";
        int len = s.length();
        if (len >= 2) {
            return s.substring(0, 1);
        } else if (len == 1)
            return "0" + s;
        else
            return "00";
    }

    public static String adjustInputString(String dataType, String inputString) {
        int type = Integer.parseInt(dataType);
        switch (type) {
            case 0:
                int num = Integer.parseInt(inputString.substring(0, 0));
                switch (num) {
                    case 0:
                        return "3F";
                    case 1:
                        return "06";
                    case 2:
                        return "5B";
                    case 3:
                        return "4F";
                    case 4:
                        return "66";
                    case 5:
                        return "6D";
                    case 6:
                        return "7D";
                    case 7:
                        return "07";
                    case 8:
                        return "7F";
                    case 9:
                        return "67";
                    default:
                        break;
                }
                break;
            case 1:
            case 2:
            case 3:
            default:
                break;
        }
        return inputString;
    }
}
