package com.lvds2000.logging;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 
 * This will log to the file "NAME.%g.log" where 0 <= %g <= 9 
 */
public class AppLogger{

	private static final String NAME = "WebWechat";

	private static Logger logger;

	/**
	 * Initializes this <code>AppLogger</code>
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static void setUp() throws SecurityException, IOException{
		FileHandler handler = new FileHandler(NAME + ".%g.log", 1024 * 1024, 10, true);
	    logger = Logger.getLogger(NAME);
	    logger.addHandler(handler);
	    // does not put logs to the console
	    //logger.setUseParentHandlers(false);
	    logger.setLevel(Level.FINER);
	    // uses a custom formatter to represent the logs better
	    Formatter formatter = new Formatter(){
			@Override
			public String format(LogRecord record) {
				Date date = new Date();
				date.setTime(record.getMillis());
				return date.toString() + " [" + record.getLoggerName() + "] " + record.getLevel() + ": " + record.getMessage() + "\n";
			}
	    };
	    handler.setFormatter(formatter);
	    logger.fine("Program start");
	}
	
	/**
	 * Gets the <code>Logger</code>
	 * @return the <code>Logger</code>
	 */
	public static Logger getLogger(){
		return logger;
	}
}
