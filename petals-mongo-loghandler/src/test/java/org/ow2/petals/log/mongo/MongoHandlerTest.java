package org.ow2.petals.log.mongo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class MongoHandlerTest {

	@Before
	public void before() {
		LogManager.getLogManager().reset();

		// test if mongo is available
		
		// clear the logs from the database

	}

	@Test
	public void testLogFromLogger() throws Exception {
		final LogManager manager = LogManager.getLogManager();
		final InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("loggers.properties");
		try {
			assertNotNull("Log file configuration not found", is);
			manager.readConfiguration(is);
		} finally {
			is.close();
		}
		
		Logger logger = Logger.getLogger("MyMongoHandlerTestLogger");
		
		String msg = "This is a test message #" + UUID.randomUUID();
		logger.log(Level.INFO, msg);
		
		/// TODO : Connect to mongo and check if we can find the log message...
	}
	
	public void testLogFromHandler() throws Exception {
		
		// all goes to default defined in MongoHandler
		
		MongoHandler handler = new MongoHandler();
		String msg = "This is a test message #" + UUID.randomUUID();
		handler.publish(record(msg));
	}

	protected LogRecord record(String message) {
		LogRecord logRecord = new LogRecord(Level.INFO, message);
		logRecord.setMillis(System.currentTimeMillis());
		logRecord.setLoggerName("MongoHandlerTest");
		return logRecord;
	}

}
