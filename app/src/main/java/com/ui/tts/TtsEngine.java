/**
 * Copy Right (c)塘上科技,tangsci.cn
 * Project					:Tangsonic Android
 * Version					:1.0
 * Modification history		:2013.5.22
 * Author					:BaoWei 
 * 
 * 注意这个文件一般不需要改动
 */


package com.ui.tts;

import java.io.UnsupportedEncodingException;
/*
 *  注意这个文件一般不需要改动
*/
public class TtsEngine
{
	public static final int	TSERR_SUCCESS				= 0;
	public static final int	TSERR_FAIL					= 1;
	public static final int	TSERR_INVALID_PARAM 		= 2;
	public static final int	TSERR_CANNOT_OPEN_FILE		= 3;
	public static final int	TSERR_FILE_EMPTY			= 4;
	public static final int	TSERR_EOF					= 5;
	public static final int	TSERR_MEMORY_ACCESS_ILLEAGEL= 6;
	public static final int	TSERR_CANNOT_ALLOC_MEMORY	= 7;
	public static final int	TSERR_NET_CANNOT_CONNECT	= 8;
	public static final int	TSERR_INVALID_DATA			= 9;
	public static final int	TSERR_NO_DATA				= 10;
	public static final int	TSERR_DATA_READ_ONLY		= 11;
	public static final int	TSERR_DATA_NOT_EXIST		= 12;
	public static final int	TSERR_BUF_NOT_ENOUGH		= 13;
	public static final int	TSERR_CODE_CONV_FAIL		= 14;
	public static final int	TSERR_LOG_NOT_INIT			= 15;
	public static final int	TSERR_CANNOT_READ			= 16;
	public static final int	TSERR_CANNOT_WRITE			= 17;
    
	public static final int	TSERR_LICENSE_INVALID		= 10001;
	public static final int	TSERR_NOT_INIT				= 10002;
	public static final int	TSERR_INIT					= 10003;
	public static final int	TSERR_CANNOT_INIT_RESOURCE	= 10004;
	public static final int	TSERR_INVALID_SESSION		= 10005;
	public static final int	TSERR_INVALID_VOICE			= 10006;
	public static final int	TSERR_LOG_INIT_FAIL			= 10007;
	public static final int	TSERR_LOG_WRITE_FAIL		= 10008;
	public static final int	TSERR_OVERRIDE_BUF			= 10009;
	public static final int	TSERR_NOT_AUTHORIZED		= 10010;
	public static final int	TSERR_SESSION_NUM_LIMIT		= 10011;
	public static final int	TSERR_SESSION_BUSY			= 10012;
	
	public static final String ENCODING_AUTO = "0";
	public static final String ENCODING_UTF8 = "1";
	public static final String ENCODING_UTF16LE = "2";
	public static final String ENCODING_GBK = "3";
	public static final String ENCODING_BIG5 = "4";
	public static final String ENCODING_UTF16BE = "5";
		
	private native int init_jni(byte[] bytesRes);
	private native int uninit_jni();
	private native int newSession_jni(Integer sessionId);
	private native int delSession_jni(int sessionId);
	private native int getParam_jni(int sessionId, String strName, byte[] utf8BytesValue);
	public native int setParam(int sessionId, String strName, String strValue);
	private native int inputText_jni(int sessionId, byte[] inputText);
	private native int outputAudio_jni(int sessionId, byte[] audioOut, Integer outputLen);
	public native int textToFile(int sessionId, byte[] inputText, String strFileName);
	public native String tserrorInfo(int errorCode, int flag);
	
	private static final int m_maxSessionCount = 8;
	//private ByteBuffer m_sessionInputTextBuf[];
	private boolean[] m_isTextNeedFlush;
    private boolean m_isInit;
	public TtsEngine()
	{

	}

    public boolean isTtsInit()
    {
        return m_isInit;
    }
    /*
	public int init(String strWorkPath)
	{
		int rt;
		byte[] bytesPath = strWorkPath.getBytes();
		byte[] bytesPathEnd0 = new byte[bytesPath.length + 1];
		for (int i = 0; i < bytesPath.length; ++i)
		{
			bytesPathEnd0[i] = bytesPath[i];
		}
		bytesPathEnd0[bytesPath.length] = 0;
		if (TSERR_SUCCESS == (rt = init_jni(bytesPathEnd0)))
		{
			m_sessionInputText = new byte[m_maxSessionCount + 1][];
			m_isTextNeedFlush = new boolean[m_maxSessionCount + 1];
            m_isInit = true;
		}
		return rt;
	}
	*/
	public int init(byte[] bytesRes)
	{
		int rt = 0;
		if (m_isInit)
		{
	        rt = TSERR_INIT;
		}
		else
		{
			if (null == bytesRes || 0 == bytesRes.length)
	        {
	            return TSERR_INVALID_PARAM;
	        }
	        rt = init_jni(bytesRes);
	 		if (TSERR_SUCCESS == rt)
			{
	 			//m_sessionInputTextBuf = new ByteBuffer[m_maxSessionCount + 1];
				m_isTextNeedFlush = new boolean[m_maxSessionCount + 1];
	            m_isInit = true;
			}
		}
		return rt;
	}
	public int uninit()
	{
		int rt;
		if (TSERR_SUCCESS == (rt = uninit_jni()))
		{
			//m_sessionInputTextBuf = null;
            m_isInit = false;
		}
		return rt;
	}
	public int delSession(int sessionId)
	{
		int rt;
		if (TSERR_SUCCESS == (rt = delSession_jni(sessionId)))
		{
			if (sessionId > m_maxSessionCount)
			{
				rt = TSERR_SESSION_NUM_LIMIT;
			}
			else
			{
				m_isTextNeedFlush[sessionId] = false;
			}
		}
		return rt;
	}
	
	public int newSession(Integer sessionId)
	{
		int rt;
		if (TSERR_SUCCESS == (rt = newSession_jni(sessionId)))
		{
			if (sessionId > m_maxSessionCount)
			{
				delSession_jni(sessionId.intValue());
				sessionId = 0;
				rt = TSERR_SESSION_NUM_LIMIT;
			}
			else
			{
				m_isTextNeedFlush[sessionId] = false;				
			}
		}
        else
        {
            sessionId = 0;
        }
		return rt;
	}
    private String getUtf8CString(byte[] byteString) throws UnsupportedEncodingException
    {
        int i;
        for (i = 0; i < byteString.length; ++i)
        {
            if (0 == byteString[i])
            {
                break;
            }
        }
        byte[] bytesEnd0 = new byte[i];
        System.arraycopy(byteString, 0, bytesEnd0, 0, i);
        return new String(bytesEnd0, "UTF-8");
    }
	public String getParam(int sessionId, String strName)
	{
		int rt;
		byte[] utf8BytesValue = new byte[128];
		String strValue;
		rt = getParam_jni(sessionId, strName, utf8BytesValue);
		if (rt > 0)
		{
			try 
			{
	            strValue = getUtf8CString(utf8BytesValue);
	        } 
			catch (UnsupportedEncodingException e)
			{
	             strValue = new String();
	        }
		}
		else
		{
			strValue = new String();
		}
		return strValue;
	}

	public int inputText(int sessionId, byte[] inputText, boolean isTextToBeContinued)
	{
		int rt;
		if (sessionId > m_maxSessionCount)
		{
			return TSERR_INVALID_PARAM;
		}
		rt = inputText_jni(sessionId, inputText);
		if (TSERR_SUCCESS == rt)
		{
			if (inputText.length > 0)
			{
				m_isTextNeedFlush[sessionId] = !isTextToBeContinued;
			}
			else
			{
				m_isTextNeedFlush[sessionId] = false;
			}
		}
		return rt;
	}

	public int outputAudio(int sessionId, byte[] audioOut, Integer outputLen)
	{
		int rt;
		rt = outputAudio_jni(sessionId, audioOut, outputLen);
		if (TSERR_SUCCESS == rt)
		{
			return rt;
		}
		if (TSERR_NO_DATA == rt && m_isTextNeedFlush[sessionId])
		{
			rt = inputText_jni(sessionId, null);
			if (TSERR_SUCCESS == rt)
			{
				m_isTextNeedFlush[sessionId] = false;
				rt = outputAudio_jni(sessionId, audioOut, outputLen);
			}
		}
		return rt;
	}
	
	/**
	 * 放弃会话当前正在合成的内容和数据，并且清除
	 * @param sessionId,针对的会话
	 */
	public void clear(int sessionId)
	{
		if (TSERR_SUCCESS == outputAudio_jni(sessionId, null, 0))
		{
			m_isTextNeedFlush[sessionId] = false;
		}
	}
	static
	{
		System.loadLibrary("tstts_jni");
	}
}
