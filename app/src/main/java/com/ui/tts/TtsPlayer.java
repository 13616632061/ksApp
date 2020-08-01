/**
 * Copy Right (c)塘上科技,tangsci.cn
 * Project					:TangsonicLite Android版
 * Version					:2
 * Modification history		:2016.1.18
 * Author					:BaoWei
 * brief 即将播放任务会打断优先级低或者相同优先级的播放任务
 */
package com.ui.tts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/*
 *  注意这个文件一般不需要改动
*/
public class TtsPlayer
{
	private static final byte TTSPLAY_STATE_STOP = 0;
    private static final byte TTSPLAY_STATE_PLAY = 1;
    private static final byte TTSPLAY_STATE_PAUSE = 2;
    private static final byte TTSPLAY_STATE_EXIT = 3;
    private static final byte TTSPLAY_STATE_READY = 4;

    private byte[] m_playLock = new byte[0];
    private byte[] m_monitorLock = new byte[0];
    private byte m_state;
    private TtsEngine m_ttsEngine;
    private PlayThread m_playThread;
    private int m_sampleRate = 16000;
    private int m_sessionId = 0;
    private boolean m_isInit = false;
	private int m_currentLevel = -1;
	private int m_nextLevel = -1;
	private String m_nextPlayText = null;
	Handler m_msgHandler = null;
    public TtsPlayer()
    {
    	int sampleRate = 16000;
        if (sampleRate > 22050)
        {
            m_sampleRate = 22050;
        }
        else if (sampleRate < 8000)
        {
            m_sampleRate = 8000;
        }
        else
        {
            m_sampleRate = sampleRate;
        }
       
    }
    public TtsPlayer(Handler msgHandler)
    {
    	this();
    	m_msgHandler = msgHandler;
    }
    public boolean isIdle()
    {
    	synchronized(m_monitorLock)
    	{
    		return getState() == TTSPLAY_STATE_READY;
    	}
    }
    public void setMsgHandler(Handler msgHandler)
    {
    	synchronized(m_monitorLock)
    	{
    		m_msgHandler = msgHandler;
    	}
    }
    protected  byte getState()
    {
    	synchronized(m_monitorLock)
    	{
    		return m_state;
    	}
    }
    private  void setIdle()
    {
    	setStateFlag(TTSPLAY_STATE_READY);
    }
    protected  void setStateFlag(byte state)
    {
    	m_state = state;
    }
    private void setState(byte state)
    {
    	synchronized(m_monitorLock)
    	{
	    	if (state == TTSPLAY_STATE_PAUSE && m_state != TTSPLAY_STATE_PLAY)
	    	{
	    		return;
	    	}
	    	setStateFlag(state);
	        synchronized (m_playLock)
	        {
	            m_playLock.notifyAll();
	        }
    	}
    }
    public boolean initEngine(byte[] ttsResource)
    {
    	synchronized(m_monitorLock)
    	{
	    	if (!m_isInit)
	    	{
		        m_ttsEngine = new TtsEngine();
		        if (TtsEngine.TSERR_SUCCESS == m_ttsEngine.init(ttsResource))
		        {
		            Integer intSessionId = Integer.valueOf(0);
		            if (TtsEngine.TSERR_SUCCESS == m_ttsEngine.newSession(intSessionId))
		            {
		                m_sessionId = intSessionId.intValue();
		                m_ttsEngine.setParam(m_sessionId, "Encoding", TtsEngine.ENCODING_UTF8);
		                m_ttsEngine.setParam(m_sessionId, "SampleRate", Integer.valueOf(m_sampleRate).toString());
		                m_isInit = true;
		            }
		            else
		            {
		                m_sessionId = 0;
		                return false;
		            }
		        }
		        else
		        {
		            m_sessionId = 0;
		            return false;
		        }
		        m_state = TTSPLAY_STATE_STOP;
		        m_playThread = new PlayThread();
		        m_playThread.start();
		        setIdle();
	    	}
	        return true;
    	}
    }
    public int uninitEngine()
    {
    	synchronized(m_monitorLock)
    	{
	        m_state = TTSPLAY_STATE_EXIT;
	        synchronized (m_playLock)
	        {
	            m_playLock.notifyAll();
	        }
	        if (null != m_playThread)
	        {
	            try
	            {
	                m_playThread.join(1000);
	            }
	            catch (InterruptedException e)
	            {
	                e.printStackTrace();
	            }
	        }
	        int rt;
	        rt = m_ttsEngine.delSession(m_sessionId);
	        rt = m_ttsEngine.uninit();
	        m_ttsEngine = null;
	        m_playThread = null;
	        m_isInit = false;
	        m_sessionId = 0;
	        return rt;
    	}
    }
    public  boolean setParam(String strName, String strValue)
    {
    	synchronized(m_monitorLock)
    	{
	        int rt;
	        if (null == m_ttsEngine)
	        {
	            return false;
	        }
	        rt = m_ttsEngine.setParam(m_sessionId, strName, strValue);
	        if (TtsEngine.TSERR_SUCCESS == rt)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
    	}
    }
    public String getParam(String strName)
    {
    	synchronized(m_monitorLock)
    	{
	        String rtValue;
	        if (null == m_ttsEngine)
	        {
	           return "";
	        }
	        rtValue = m_ttsEngine.getParam(m_sessionId, strName);
	        return rtValue;
    	}
    }
    public boolean setGlobalParam(String strName, String strValue)
    {
    	synchronized(m_monitorLock)
    	{
	        int rt;
	        int sessionId = 0;
	        if (null == m_ttsEngine)
	        {
	            return false;
	        }
	        rt = m_ttsEngine.setParam(sessionId, strName, strValue);
	        if (TtsEngine.TSERR_SUCCESS == rt)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
    	}
    }
    public String getGlobalParam(String strName)
    {
    	synchronized(m_monitorLock)
    	{
	        String rtValue;
	        int sessionId = 0;
	        if (null == m_ttsEngine)
	        {
	           return "";
	        }
	        rtValue = m_ttsEngine.getParam(sessionId, strName);
	        return rtValue;
    	}
    }
    //level>=0才有效
    public  boolean playText(String inputText, int level)
    {
    	synchronized(m_monitorLock)
    	{
    		return _doPlay(inputText, level);
    	}
    }
    public  boolean playText(String inputText)
    {
    	return playText(inputText, 0);
    }
	private boolean _doPlay(String inputText, int levelOrFlag)
	{
		if (inputText == null && levelOrFlag == -10000)//合成线程在调用
		{
			boolean needSendStopFinshMsg = (m_currentLevel >= 0);
			if (m_nextLevel >= 0 && m_nextPlayText != null)
			{
				m_currentLevel = m_nextLevel;
				m_nextLevel = -1;
				String strTemp = m_nextPlayText;
				m_nextPlayText = null;
				_playText(strTemp);
			}
			else
			{
				m_currentLevel = -1;
			}
			if (m_msgHandler != null && needSendStopFinshMsg)
			{
				Message msg = new Message();
		        Bundle b = new Bundle();
		        b.putString("play_state", "idle");
		        msg.setData(b);
		        m_msgHandler.sendMessage(msg);///<告诉界面线程（主线程）播放已经结束或者已经终止完成了
			}
			return true;
		}
		else
		{
			int level = levelOrFlag;
			if (-1 == m_currentLevel)
			{
				m_currentLevel = level;
				_playText(inputText);
				return true;
			}
			else if (level >= 0 && level >= m_currentLevel && level >= m_nextLevel)
			{
				m_nextLevel = level;
				m_nextPlayText = inputText;
				setState(TTSPLAY_STATE_STOP);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	private  boolean _playText(String inputText)
    {
        int rt;
        if (null == m_ttsEngine || !isIdle())
        {
        	if (!isIdle())
        	{
        		Log.w("TtsPlayer", "Not ready for next Play");
        	}
            return false;
        }
        rt = m_ttsEngine.inputText(m_sessionId, inputText.getBytes(), false);
        if (TtsEngine.TSERR_SUCCESS == rt)
        {
            setState(TTSPLAY_STATE_PLAY);
            return true;
        }
        else
        {
            Log.w("TtsPlayer", "Input text error");
            return false;
        }
    }
	
    public void pause()
    {
    	synchronized(m_monitorLock)
     	{
    		setState(TTSPLAY_STATE_PAUSE);
     	}
    }

    public void play()
    {
    	synchronized(m_monitorLock)
     	{
    		setState(TTSPLAY_STATE_PLAY);
     	}
    }
		
    public void stop()
    {
    	synchronized(m_monitorLock)
    	{
    		setState(TTSPLAY_STATE_STOP);
    	}
    }

    protected  void  onPlayComplete()///play thread will call the function
    {//合成完成了，看看是否有下个任务

    	synchronized(m_monitorLock)
    	{
    		setIdle();
    		_doPlay(null, -10000);
    	}
  
    }
    private class PlayThread extends Thread
    {
        @Override
        public void run()
        {
            int rt = 0;
            int minBufSize = 0;
            int ttsOutputBufSize = TtsPlayer.this.m_sampleRate / 8;
            int audioBufSize = TtsPlayer.this.m_sampleRate / 2;
            byte[] outputBuf = new byte[ttsOutputBufSize];
            minBufSize = AudioTrack.getMinBufferSize(TtsPlayer.this.m_sampleRate, 
            		AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if (audioBufSize < minBufSize * 2)
            {
            	audioBufSize = minBufSize * 2;
            }
            AudioTrack trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC, m_sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    audioBufSize, AudioTrack.MODE_STREAM);
            Integer outputLen = 0;
            byte state = 0;
            while(true)
            {
            	state = TtsPlayer.this.getState();
                synchronized (TtsPlayer.this.m_playLock)
                {
                	if (state != TTSPLAY_STATE_PLAY)
                	{
                		try
                        {//wait会释放synchronized块
                			TtsPlayer.this.m_playLock.wait();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            TtsPlayer.this.setState(TTSPLAY_STATE_EXIT);
                            break;
                        }
                	}
                }
                state = TtsPlayer.this.getState();
                if (state == TTSPLAY_STATE_PLAY)
                {
                    Log.w("TtsPlayer", "Play!");
                    trackplayer.play();
                    
                    while ((state = TtsPlayer.this.getState()) == TTSPLAY_STATE_PLAY)
                    {
                        outputLen = 0;
                        rt = TtsPlayer.this.m_ttsEngine.outputAudio(TtsPlayer.this.m_sessionId, outputBuf, outputLen);
                        if (rt == TtsEngine.TSERR_SUCCESS)
                        {
                            trackplayer.write(outputBuf, 0, outputLen);
                        }
                        else if (rt == TtsEngine.TSERR_NO_DATA)//syn complete
                        {
                        	Log.w("TtsPlayer", "Complete!");
                        	TtsPlayer.this.onPlayComplete();
                            break;
                        }
                        else
                        {
                            Log.w("TtsPlayer", "TTSEngine maybe init error!");
                            break;
                        }
                    }
                    
                    if (state == TTSPLAY_STATE_PLAY)
                    {
                        
                    }
                    else if (state == TTSPLAY_STATE_PAUSE)
                    {
                    	Log.w("TtsPlayer", "Pause!");
                        trackplayer.pause();
                    }
                    else if (state == TTSPLAY_STATE_STOP)
                    {
                    	Log.w("TtsPlayer", "Stop!");
                        trackplayer.stop();
                        rt = TtsPlayer.this.m_ttsEngine.outputAudio(m_sessionId, null, outputLen);
                        TtsPlayer.this.onPlayComplete();
                    }
                    else if (state == TTSPLAY_STATE_EXIT)
                    {
                    	//TtsPlayer.this.m_ttsEngine.outputAudio(m_sessionId, null, outputLen);
                        break;
                    }
                }
                else if (state == TTSPLAY_STATE_STOP)
                {
                	Log.w("TtsPlayer", "Stop!");
                    trackplayer.stop();
                    TtsPlayer.this.m_ttsEngine.outputAudio(m_sessionId, null, outputLen);
                    TtsPlayer.this.onPlayComplete();
                }
                else if (state == TTSPLAY_STATE_PAUSE)
                {
                	Log.w("TtsPlayer", "Pause!");
                    trackplayer.pause();
                }
                if (state == TTSPLAY_STATE_EXIT)
                {
                	//TtsPlayer.this.m_ttsEngine.outputAudio(TtsPlayer.this.m_sessionId, null, outputLen);
                    break;
                }
            }
            trackplayer.stop();
            trackplayer.release();
            Log.w("TtsPlayer", "trackplayer exit!");
        }
    }
    public int textToFile(byte[] inputText, String strFileName)
    {//直接把一段文本转换成wav音频文件，不要和playText混用,这个函数不允许多线程调用,因为不允许多线程用同一个session id来合成
    	return m_ttsEngine.textToFile(m_sessionId, inputText, strFileName);
    }
}
