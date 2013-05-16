package org.ow2.petals.log.mongo;

import com.mongodb.*;
import org.ow2.petals.nosql.mongo.MongoHelper;

import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.*;

/**
 * A JUL handler which write logs to mongodb
 *
 * chamerling - chamerling@linagora.com
 */
public class MongoHandler extends StreamHandler {

    protected WriteConcern warnOrHigherWriteConcern = WriteConcern.SAFE;
    protected WriteConcern infoOrLowerWriteConcern = WriteConcern.NORMAL;

    public static final String HOST  ="host";
    public static final String DEFAULT_HOST  = "localhost";
    public static final String PORT  ="port";
    public static final String DEFAULT_PORT  ="27017";
    public static final String COLLECTION  ="collection";
    public static final String DEFAULT_COLLECTION  ="logs";
    public static final String DB  ="db";
    public static final String DEFAULT_DB  ="petals";
    public static final String LOGIN  ="login";
    public static final String PASSWORD  ="password";
    public static final String LOGGER_LEVEL  = "level";
    private static final String LOGGER_NAME = "name";
    private static final String LOGGER_THREAD_ID = "thread_id";
    private static final String LOGGER_MESSAGE = "message";
    private static final String LOGGER_TIME_MS = "time_ms";

    protected String host;

    protected String port;

    protected String collectionName;

    protected String db;

    protected String login;

    protected String password;

    protected Mongo mongo;

    protected DB mongoDB;

    protected DBCollection collection;

    public MongoHandler() {
        super();
    }

    @Override
    public synchronized void publish(final LogRecord record) {
        if (null == mongoDB) {
            try {
                doConnect();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        this.pushLog(record);
    }

    /**
     * Write log record to collection
     *
     * @param record
     */
    protected void pushLog(final LogRecord record) {
        try {
            DBObject dbo = new BasicDBObject();
            dbo.put(LOGGER_LEVEL, record.getLevel().getName());
            dbo.put(LOGGER_NAME, record.getLoggerName());
            dbo.put(LOGGER_THREAD_ID, record.getThreadID());
            dbo.put(LOGGER_MESSAGE, record.getMessage());
            dbo.put(LOGGER_TIME_MS, record.getMillis());
            // TODO  : More properties
            
            WriteConcern wc = infoOrLowerWriteConcern;
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                wc = warnOrHigherWriteConcern;
            }
            this.collection.insert(dbo, wc);

        } catch (Exception e) {
            reportError(e.getMessage(), e, ErrorManager.GENERIC_FAILURE);
        }
    }

    /**
     * Configure the log handler
     */
    protected void doConfigure() {
        this.host = getProperty(HOST, DEFAULT_HOST);
        this.port = getProperty(PORT, DEFAULT_PORT);
        this.collectionName = getProperty(COLLECTION, DEFAULT_COLLECTION);
        this.db = getProperty(DB, DEFAULT_DB);
        this.login = getProperty(LOGIN, null);
        this.password = getProperty(PASSWORD, null);
    }

    /**
     * Create all the required stuff to connect to MongoDB
     */
    protected void doConnect() throws UnknownHostException {
        doConfigure();
        List<ServerAddress> addresses = MongoHelper.getServerAddresses(this.host, this.port);

        this.mongo = MongoHelper.getMongo(addresses);
        this.mongoDB = getDatabase(this.mongo, this.db);

        if (login != null && login.trim().length() > 0) {
            if (!this.mongoDB.authenticate(login, password.toCharArray())) {
                throw new RuntimeException(
                        "Unable to authenticate with MongoDB server.");
            }
            // Allow password to be GCed
            password = null;
        }
        this.collection = this.mongoDB.getCollection(this.collectionName);
    }

    protected String getProperty(String name, String def) {
        final LogManager manager = LogManager.getLogManager();
        String result = manager.getProperty(this.getClass().getName() + "." + name);
        if (result == null) {
            result = def;
        }
        return result;
    }

    protected DB getDatabase(Mongo mongo, String databaseName) {
        return mongo.getDB(databaseName);
    }

    protected void closeDB() {
        if (this.mongo != null) {
            this.collection = null;
            this.mongo.close();
        }
    }
}
