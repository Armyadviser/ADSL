package com.ge.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Title: Java工具类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JTools {

    public static String strToMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToTimeStampStr(Date value, String strTimeFormat) {
        if (value == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(strTimeFormat);//("yyyyMMddHHmmss.SSS");
            return sdf.format(value);
        }
    }

    //以字符串的形式得到当前时间信息
    //strFormat:格式说明
    //例如：yyyyMMddHHmmss
    public static String getSysTimeStr(String strFormat) {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat(strFormat, Locale.US);
        return formatter.format(now);
    }

    private static DateFormat formatter1 = null;

    public static String getSysTimeStr() {
        if (formatter1 == null) {
            formatter1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        }
        Date now = new Date();
        return formatter1.format(now);
    }

    private static DateFormat formatter2 = null;

    public static String getSysTimeStamp() {
        if (formatter2 == null) {
            formatter2 = new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.US);
        }
        Date now = new Date();
        return formatter2.format(now);
    }

    public static String getTimeStrFromLongAndFormat(long nTimeMillis, String sFormat) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(nTimeMillis);
        Date data = c.getTime();
        DateFormat df = new SimpleDateFormat(sFormat);
        return df.format(data);
    }

    public static long getLongFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    public static String getZeroTime14() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        GregorianCalendar cal = new GregorianCalendar();

        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);

        return dateFormat.format(new Date(cal.getTimeInMillis()));
    }

    private static final Random sample = new Random();

    public static String getRandomString(int nLen) {
        String strList[] = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        int nRandomRes;
        String strRes = "";

        for (int i = 0; i < nLen; i++) {
            nRandomRes = sample.nextInt(strList.length);
            strRes = strRes + strList[nRandomRes];
        }

        return strRes;
    }

    public static boolean isFilePath(String strFilePath) {
        File fp = new File(strFilePath);
        return fp.isFile();
    }

    public static long getFileSize(String strFilePath) {
        File fp = new File(strFilePath);
        return fp.length();
    }

    public static String getFileType(String strFilePath) {
        int nPos = strFilePath.lastIndexOf('.');
        if (nPos == -1)
            return "";

        return strFilePath.substring(nPos + 1).toLowerCase();
    }

    public static boolean createFile(String strFilePath) {
        int nStartPos = 0;
        int nEndPos1 = strFilePath.indexOf(File.separator, nStartPos);
        int nEndPos2 = strFilePath.indexOf('/', nStartPos);
        int nEndPos = min(nEndPos1, nEndPos2);

        String strTempFilePath = strFilePath.substring(0, nEndPos + 1);
        while (createAFile(strTempFilePath)) {
            nStartPos = nEndPos + 1;

            nEndPos1 = strFilePath.indexOf(File.separator, nStartPos);
            nEndPos2 = strFilePath.indexOf('/', nStartPos);
            nEndPos = min(nEndPos1, nEndPos2);

            if (nEndPos == -1)
                return true;

            strTempFilePath = strFilePath.substring(0, nEndPos + 1);
        }

        return false;
    }

    public static String getPreDay(String strFormat, int nDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, 0 - nDay);

        DateFormat formatter = new SimpleDateFormat(strFormat, Locale.US);
        return formatter.format(cal.getTime());
    }

    private static int min(int n1, int n2) {
        if ((n1 == -1) && (n2 == -1))
            return -1;
        if (n1 == -1)
            return n2;
        if (n2 == -1)
            return n1;
        if (n1 < n2)
            return n1;
        return n2;
    }

    private static boolean createAFile(String strFilePath) {
        boolean bRes = false;
        try {
            File fp = new File(strFilePath);
            bRes = fp.isDirectory() || fp.mkdir();
        } catch (Exception ignored) {
        }
        return bRes;
    }

    public static String getFileName(String strFilePath) {
        int nStart = strFilePath.lastIndexOf(File.separator);
        if (nStart == -1)
            return "";

        int nEnd = strFilePath.lastIndexOf('.');
        if (nEnd == -1)
            return strFilePath.substring(nStart + 1);

        return strFilePath.substring(nStart + 1, nEnd);
    }

    public static String format(String strSrc, Vector vec) {
        if ((strSrc == null) || (strSrc.length() == 0))
            return strSrc;

        if ((vec == null) || (vec.isEmpty()))
            return strSrc;

        String strFlag = "%";
        StringBuilder strRes = new StringBuilder();
        int nItem, nStart = 0,
                nEnd = strSrc.indexOf(strFlag),
                nLen = vec.size(), nFlagLen = strFlag.length() + 1;

        while (nEnd != -1) {
            strRes.append(strSrc.substring(nStart, nEnd));

            int nTemp = nEnd + strFlag.length();
            nItem = Integer.parseInt(strSrc.substring(nTemp, nTemp + 1));
            if (nLen <= nItem)
                break;

            strRes.append(vec.get(nItem));

            if (nEnd + nFlagLen >= strSrc.length()) {
                nStart = -1;
                break;
            }

            nStart = nEnd + nFlagLen;
            nEnd = strSrc.indexOf(strFlag, nStart);
        }

        if (nStart != -1)
            strRes.append(strSrc.substring(nStart));

        return strRes.toString();
    }

    /**
     * 判读是否为闰年
     */
    public static boolean isLeapYear(long nThisYear) {
        return (((nThisYear % 4 == 0) && (nThisYear % 100 != 0)) || (nThisYear % 400 == 0));
    }

    public static String strChange(String str) {
        byte[] data = str.getBytes();
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String addTimeFormat(String strTime) {
        int nPos = strTime.indexOf('.');
        if (nPos == -1) {
            return strTime;
        }
        strTime = strTime + "0";
        return strTime;
    }

    public static String fillStringToOrderLen(String strSrc, int nLen) {
        String strDes = strSrc;

        int nAddLen = nLen - strDes.length();
        if (nAddLen <= 0) {
            return strDes;
        }

        for (int i = 0; i < nAddLen; i++) {
            strDes = '0' + strDes;
        }
        return strDes;
    }

    public static String remove(String str) {
        byte[] bys = str.getBytes();
        byte[] byRes = new byte[bys.length];

        int nNum = 0;
        for (byte by : bys) {
            if ((by != 0x0A) && (by != 0x0D)) {
                byRes[nNum] = by;
                nNum++;
            }
        }
        return new String(byRes);
    }

    public static boolean deleteFile(String strFile) {
        return (new File(strFile)).delete();
    }

    public static String getPreDay() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, -1);

        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return formatter.format(cal.getTime());
    }

    public static String getPreMonth() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.MONTH, -1);

        DateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.US);
        return formatter.format(cal.getTime());
    }

    /**
     * 判断strBig中是否包含strSmall串
     */
    public static boolean isInclusion(String strBig, String strSmall) {
        if ((strBig == null) || (strSmall == null)) {
            return false;
        }
        int nPos = strBig.indexOf(strSmall);
        return nPos != -1;

    }

    /**
     * 把数字转换成指定长度的串
     */
    public static String toFixLen(int nNum, int nLen) {
        String str = String.valueOf(nNum);
        for (int i = str.length(); i < nLen; i++) {
            str = '0' + str;
        }

        return str;
    }

    public static String toFixLen(String strNum, int nLen) {
        String strTemp = strNum;
        for (int i = strTemp.length(); i < nLen; i++) {
            strTemp = '0' + strTemp;
        }

        return strTemp;
    }

    public static String getBeforeDate(int nBeforeDay) {
        GregorianCalendar calendar = new GregorianCalendar(Locale.US);
        calendar.add(GregorianCalendar.DAY_OF_MONTH, nBeforeDay);

        Date now = calendar.getTime();
        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);

        return format.format(now);
    }

    public static long subBetweenMonth(String strBeginTime, String strEndTime) {
        if ((strBeginTime == null) || (strBeginTime.length() < 6)) {
            return 0;
        }
        if ((strEndTime == null) || (strEndTime.length() < 6)) {
            return 0;
        }

        String strBeginYear = strBeginTime.substring(0, 4);
        String strBeginMonth = strBeginTime.substring(4, 6);
        String strEndYear = strEndTime.substring(0, 4);
        String strEndMonth = strEndTime.substring(4, 6);

        long nBeginMonthCount = Long.parseLong(strBeginYear) * 12 + Long.parseLong(strBeginMonth);
        long nEndMonthCount = Long.parseLong(strEndYear) * 12 + Long.parseLong(strEndMonth);

        return (nEndMonthCount - nBeginMonthCount);
    }

    public static String ipToRaw(String strIp) {
        if (strIp == null) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(strIp, ".");

        StringBuilder strBuf = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int iToken = Integer.parseInt(token);
            token = Integer.toHexString(iToken);
            if (token.length() == 1) {
                strBuf.append('0');
                strBuf.append(token);
            } else {
                strBuf.append(token);
            }
        }

        return strBuf.toString().toUpperCase();
    }
}
