package com.ge.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

@SuppressWarnings("CanBeFinal")
public class IniOperation {

    public IniOperation() {
        vecIniList = new Vector<>();
    }

    private class Session {
        String strMean;
        String strKey;
        Vector<Object> vecNoteValue;
    }

    private class Note {
        String strMean;
        String strKey;
        String strValue;
    }

    private String strFileName = null;
    private Vector<Object> vecIniList;
    private Session session = null;
    private String strMean = null;
    private boolean bChanged = false;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public synchronized boolean saveIni() throws Exception {
        if (!bChanged) {
            return true;
        }
        String strLineGe = System.getProperty("line.separator");

        File fIniFile = new File(strFileName);
        long nLastTime = fIniFile.lastModified();
        try {
            if (vecIniList == null) {
                return false;
            }
            FileWriter fw = new FileWriter(fIniFile, false);

            Object[] objSession = vecIniList.toArray();
            int nSessionNum = vecIniList.size();

            for (int i = 0; i < nSessionNum; i++) {
                Session sesRoot = (Session) objSession[i];
                if (sesRoot.strMean != null) {
                    fw.write(sesRoot.strMean);
                    fw.write(strLineGe);
                }
                if (sesRoot.strKey != null) {
                    fw.write("[");
                    fw.write(sesRoot.strKey);
                    fw.write("]");
                    fw.write(strLineGe);
                }
                Object[] objNote = sesRoot.vecNoteValue.toArray();
                int nNoteNum = sesRoot.vecNoteValue.size();
                for (int j = 0; j < nNoteNum; j++) {
                    Note noteValue = (Note) objNote[j];
                    if (noteValue.strMean != null) {
                        fw.write(noteValue.strMean);
                        fw.write(strLineGe);
                    }
                    if (noteValue.strKey != null) {
                        fw.write(noteValue.strKey);
                        fw.write("=");
                    }
                    if (noteValue.strValue != null) {
                        fw.write(noteValue.strValue);
                    }
                    fw.write(strLineGe);
                }
            }
            fw.close();
            fIniFile.setLastModified(nLastTime);
        } catch (IOException ex) {
            throw new IOException();
        }
        return true;
    }

    //娓呯┖
    public synchronized boolean cleanIni() {
        if (vecIniList == null) {
            return false;
        }
        if (vecIniList.isEmpty()) {
            return true;
        }
        vecIniList.clear();
        bChanged = true;
        return true;
    }

    //閲嶇疆鏂囨湰鍊�
    public boolean setKeyValue(String strSection, String strKey,
                               String strNewValue) {
        if (vecIniList == null || strSection == null || strKey == null || strNewValue == null) {
            return false;
        }

        Object[] objSession = vecIniList.toArray();
        int nSessionNum = vecIniList.size();
        for (int i = 0; i < nSessionNum; i++) {
            Session sesRoot = (Session) objSession[i];
            if (sesRoot.strKey.compareTo(strSection) != 0) {
                continue;
            }
            Object[] objNote = sesRoot.vecNoteValue.toArray();
            int nNoteNum = sesRoot.vecNoteValue.size();

            for (int j = 0; j < nNoteNum; j++) {
                Note noteValue = (Note) objNote[j];
                if (noteValue.strKey.compareTo(strKey) == 0) {
                    noteValue.strValue = strNewValue;
                    bChanged = true;
                }
                sesRoot.vecNoteValue.set(j, noteValue);
                vecIniList.set(i, sesRoot);
            }
        }
        return true;
    }

    //閲嶇疆long鍨嬪�
    public boolean setKeyValue(String strSection, String strKey,
                               long lNewValue) {
        return !(vecIniList == null || strSection == null || strKey == null || lNewValue < 0)
                && (setKeyValue(strSection, strKey, Long.toString(lNewValue)));
    }

    //閲嶇疆bool鍨嬪�
    public boolean setKeyValue(String strSection, String strKey,
                               boolean bNewValue) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return false;
        }
        String strBNewValue = Long.toString(0);
        if (bNewValue) {
            strBNewValue = Long.toString(1);
        }
        return (setKeyValue(strSection, strKey, strBNewValue));
    }

    //閲嶇疆瀛楃鍨嬪�
    public boolean setKeyValue(String strSection, String strKey,
                               char chNewValue) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return false;
        }
        char[] chValue = new char[1];
        chValue[0] = chNewValue;
        String strChNewValue = new String(chValue);
        return (setKeyValue(strSection, strKey, strChNewValue));
    }

    public Map getSectionMap(String strSection) {
        if (vecIniList == null || strSection == null) {
            return null;
        }

        Map<Object, Object> m = new HashMap<>();
        Object[] objSession = vecIniList.toArray();
        int nSessionNum = vecIniList.size();
        for (int i = 0; i < nSessionNum; i++) {
            Session sesRoot = (Session) objSession[i];
            if (sesRoot.strKey.compareTo(strSection) != 0) {
                continue;
            }

            Object[] objNote = sesRoot.vecNoteValue.toArray();
            int nNoteNum = sesRoot.vecNoteValue.size();

            for (int j = 0; j < nNoteNum; j++) {
                Note noteValue = (Note) objNote[j];
                m.put(noteValue.strKey, noteValue.strValue);
            }
        }
        return m;
    }

    //鍙栨枃鏈瀷鍊�
    public String getKeyValue(String strSection, String strKey) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return null;
        }
        Object[] objSession = vecIniList.toArray();
        int nSessionNum = vecIniList.size();
        for (int i = 0; i < nSessionNum; i++) {
            Session sesRoot = (Session) objSession[i];
            if (sesRoot.strKey.compareTo(strSection) != 0) {
                continue;
            }
            Object[] objNote = sesRoot.vecNoteValue.toArray();
            int nNoteNum = sesRoot.vecNoteValue.size();

            for (int j = 0; j < nNoteNum; j++) {
                Note noteValue = (Note) objNote[j];
                if (noteValue.strKey.compareTo(strKey) == 0) {
                    return noteValue.strValue;
                    //return JTools.strChange(noteValue.strValue);
                }
            }
        }
        return null;
    }

    //鍙杔ong鍨嬪�
    public int getKeyValueInt(String strSection, String strKey) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return -1;
        }
        String strReValue = getKeyValue(strSection, strKey);
        if (strReValue == null) {
            return -1;
        }
        if (strReValue.length() == 0) {
            return -1;
        }
        return Integer.valueOf(strReValue);
    }

    //鍙朾ool鍨嬪�
    public boolean getKeyValueBool(String strSection, String strKey) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return false;
        }
        String strReValue = getKeyValue(strSection, strKey);
        if ("true".equals(strReValue)) {
            return true;
        }
        if ("false".equals(strReValue)) {
            return false;
        }
        if ("1".equals(strReValue)) {
            return true;
        }
        if ("0".equals(strReValue)) {
            return false;
        }
        return false;
    }

    //鍙栧瓧绗﹀瀷鍊�
    public char getKeyValueChar(String strSection, String strKey) {
        if (vecIniList == null || strSection == null || strKey == null) {
            return '0';
        }
        String strReValue = getKeyValue(strSection, strKey);
        if (strReValue == null) {
            return '0';
        }
        if (strReValue.length() == 0) {
            return '0';
        }
        char[] chReValue = strReValue.toCharArray();
        return chReValue[0];
    }

    //鍙栨涓殑鍏抽敭瀛椾笂鏂圭殑娉ㄧず
    public String getNoteValue(String strSection, String strKey) {
        if (strSection == null || strKey == null) {
            return null;
        }
        Object[] objSession = vecIniList.toArray();
        int nSessionNum = vecIniList.size();
        for (int i = 0; i < nSessionNum; i++) {
            Session sesRoot = (Session) objSession[i];
            if (sesRoot.strKey.compareTo(strSection) != 0) {
                continue;
            }
            Object[] objNote = sesRoot.vecNoteValue.toArray();
            int nNoteNum = sesRoot.vecNoteValue.size();

            for (int j = 0; j < nNoteNum; j++) {
                Note noteValue = (Note) objNote[j];
                if (noteValue.strKey.compareTo(strKey) == 0) {
                    return noteValue.strMean;
                }
            }
        }
        return null;
    }

    //鍙栨涓婃柟鐨勬敞绀�
    public String getSessionValue(String strSection) {
        if (strSection == null) {
            return null;
        }
        Object[] objSession = vecIniList.toArray();
        int nSessionNum = vecIniList.size();
        for (int i = 0; i < nSessionNum; i++) {
            Session sesRoot = (Session) objSession[i];
            if (sesRoot.strKey.compareTo(strSection) == 0) {
                return sesRoot.strMean;
            }
        }
        return null;
    }

    //杞斤缉锛缉鏂囦欢
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean loadIni(String strIniFileName) {
        try {
            strFileName = strIniFileName;
            FileInputStream fr = new FileInputStream(strFileName); //寤虹珛FileReader瀵硅薄锛屽苟瀹炰緥鍖栦负fr
            InputStreamReader isr = new InputStreamReader(fr, "gbk");
            BufferedReader br = new BufferedReader(isr); //寤虹珛BufferedReader瀵硅薄锛屽苟瀹炰緥鍖栦负br
            String strLine = br.readLine(); //浠庢枃浠惰鍙栦竴琛屽瓧绗︿覆
            //鍒ゆ柇璇诲彇鍒扮殑瀛楃涓叉槸鍚︿笉涓虹┖
            while (strLine != null) {
                LoadLine(strLine);
                strLine = br.readLine();
            }
            if (session != null) {
                vecIniList.add(session);
                session = null;
            }
            br.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    //杞戒竴琛�
    private void LoadLine(String strLine) {
        if (strLine.length() == 0) {
            return;
        }

        String strLineGe = System.getProperty("line.separator");
        int nMeanPos = strLine.indexOf("#");
        if (nMeanPos == 0) {
            if (strMean != null) {
                strMean = strMean.concat(strLineGe).concat(strLine);
            } else {
                strMean = strLine;
            }
            return;
        }

        int pos = strLine.indexOf("=", 0);
        if (pos != -1) {
            if (session == null) {
                return;
            }
            Note note = new Note();
            note.strKey = strLine.substring(0, pos).trim();
            note.strValue = strLine.substring(pos + 1, strLine.length()).trim();
            note.strMean = strMean;
            strMean = null;
            session.vecNoteValue.add(note);
        } else {
            if (session != null) {
                vecIniList.add(session);
                session = null;
            }
            int posSt = strLine.indexOf("[", 0);
            if (posSt != -1) {
                int posEn = strLine.indexOf("]", posSt + 1);
                if (posEn > posSt) {
                    session = new Session();
                    session.strKey = strLine.substring(posSt + 1, posEn);
                    session.vecNoteValue = new Vector<>();
                    session.strMean = strMean;
                    strMean = null;
                }
            }
        }
    }

}
