package edu.uci.ics.luci.TerraTower;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import edu.uci.ics.luci.utility.Quittable;

public class TTEventWrapperQueuer implements Quittable{ 
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(TTEventWrapperQueuer.class);
		}
		return log;
	}
	
    private final RingBuffer<TTEventWrapper> ringBuffer;
	private BufferedWriter logWriter = null;
	private boolean quitting  = false;

	/** Constructor that doesn't log to a file
	 * 
	 * @param ringBuffer
	 */
    public TTEventWrapperQueuer(RingBuffer<TTEventWrapper> ringBuffer){
    	this(ringBuffer,null);
    }
    
    /** Constructor that logs to a file
     * 
     * @param ringBuffer
     * @param logFileName
     */
    public TTEventWrapperQueuer(RingBuffer<TTEventWrapper> ringBuffer,String logFileName)
    {
    	if(ringBuffer == null){
    		getLog().fatal("ringBuffer can't be null");
    		throw new IllegalArgumentException("ringBuffer can't be null");
    	}
    	
        this.ringBuffer = ringBuffer;
        
        
        /* Try and set up a file logger */
    	if(logFileName != null){
    		Path newFile = Paths.get(logFileName);
    		try {
    			Files.deleteIfExists(newFile);
    			newFile = Files.createFile(newFile);
				logWriter = Files.newBufferedWriter(newFile, Charset.defaultCharset());
    		} catch (IOException e) {
    			getLog().error("Error creating event Log File, "+logFileName+"\n"+e);
    		}
    	}
    		
    }

    private static final EventTranslatorOneArg<TTEventWrapper, TTEventWrapper> TRANSLATOR =
        new EventTranslatorOneArg<TTEventWrapper,TTEventWrapper>()
        {
            public void translateTo(TTEventWrapper event, long sequence, TTEventWrapper incoming)
            {
                event.set(incoming);
            }
        };

	public void onData(TTEventWrapper incoming) {
		if (!isQuitting()) {
			/* Write event to log */
			if (logWriter != null) {
				try {
					logWriter.append(incoming.toJSON().toJSONString());
					logWriter.newLine();
					logWriter.flush();
				} catch (IOException exception) {
					getLog().error(
							"Error writing event to log file:"
									+ incoming.getEventType().toString());
				}
			}

			/* Submit event for handling */
			ringBuffer.publishEvent(TRANSLATOR, incoming);
		}
	}

	@Override
	public void setQuitting(boolean quitting) {
		if(this.quitting && !quitting){
			getLog().warn("Already quitting, can't unquit");
		}
		else{
			if(quitting){
				this.quitting = quitting;
				if(logWriter != null){
					try {
						logWriter.close();
					} catch (IOException e) {
					}
					logWriter = null;
				}
			}
		}
	}

	@Override
	public boolean isQuitting() {
		return quitting;
	}
}
