/**
 *
 * Copyright (c) 2012, PetalsLink
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.petals.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class to manage mongo stuff without any other dependency than the java driver...
 * 
 * @author chamerling
 * 
 */
public abstract class AbstractMongoService {

	private final static String DEFAULT_MONGO_DB_HOSTNAME = "localhost";
	private final static String DEFAULT_MONGO_DB_PORT = "27017";
	private final static String DEFAULT_MONGO_DB_DATABASE_NAME = "defaultdatabase";
	private final static String DEFAULT_MONGO_DB_COLLECTION_NAME = "defaultcollection";

	private String hostname = DEFAULT_MONGO_DB_HOSTNAME;
	private String port = DEFAULT_MONGO_DB_PORT;
	private String databaseName = DEFAULT_MONGO_DB_DATABASE_NAME;
	private String collectionName = DEFAULT_MONGO_DB_COLLECTION_NAME;
	private String userName;
	private String password;
	private Mongo mongo;
	private DBCollection collection;

	private Properties properties;

	private boolean initialized = false;

	private static Logger logger = Logger.getLogger(AbstractMongoService.class
			.getName());

	/**
	 * 
	 */
	public AbstractMongoService() {
	}

	/**
	 * This method needs to be called before all operations on the mongo
	 * collection.
	 * 
	 */
	public void initializeMongo() {
		preInit();
		
		if (logger.isLoggable(Level.FINE))
			logger.fine("Initializing mongo service");

		if (mongo != null) {
			close();
		}

		if (properties != null) {
			hostname = properties.getProperty("mongo.hostname",
					DEFAULT_MONGO_DB_HOSTNAME);
			port = properties.getProperty("mongo.port", DEFAULT_MONGO_DB_PORT);
			userName = properties.getProperty("mongo.username", userName);
			password = properties.getProperty("mongo.password", password);
			collectionName = properties.getProperty("mongo.collection",
					DEFAULT_MONGO_DB_COLLECTION_NAME);
		}

		if (logger.isLoggable(Level.INFO)) {
			logger.info(String.format(
					"Connection to %s %s with credentials %s %s", hostname,
					port, userName, "******"));
		}

		List<ServerAddress> addresses = MongoHelper.getServerAddresses(hostname, port);
		this.mongo = MongoHelper.getMongo(addresses);

		DB database = getDatabase(mongo, databaseName);

		if (userName != null && userName.trim().length() > 0) {
			if (!database.authenticate(userName, password.toCharArray())) {
				throw new RuntimeException(
						"Unable to authenticate with MongoDB server.");
			}

			// Allow password to be GCed
			password = null;
		}

		setCollection(database.getCollection(collectionName));
		initialized = true;
		
		postInit();
	}

	/**
	 * 
	 */
	protected void preInit() {
	}
	
	/**
	 * 
	 */
	protected void postInit() {
	}

	/**
	 * This method could be overridden to provide the DB instance from an
	 * existing connection.
	 */
	protected DB getDatabase(Mongo mongo, String databaseName) {
		return mongo.getDB(databaseName);
	}

	/**
	 * 
	 * @return
	 */
	protected DBCollection getDbCollection() {
		return this.collection;
	}

	protected void close() {
		if (mongo != null) {
			collection = null;
			mongo.close();
		}
	}

	/**
	 * Note: this method is primarily intended for use by the unit tests.
	 * 
	 * @param collection
	 *            The MongoDB collection to use when logging events.
	 */
	public void setCollection(final DBCollection collection) {
		assert collection != null : "collection must not be null";

		this.collection = collection;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName
	 *            the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the mongo
	 */
	public Mongo getMongo() {
		return mongo;
	}

	/**
	 * @param mongo
	 *            the mongo to set
	 */
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialized
	 *            the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * @return the collection
	 */
	public DBCollection getCollection() {
		return collection;
	}

}
