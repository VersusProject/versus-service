/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;

/**
 * Setup Mongo connection.
 * 
 * @author Luigi Marini
 * 
 */
public abstract class MongoConnection {

	private static String host = "127.0.0.1";
	private static String db = "test";
	private static Mongo mongo = null;
	private static GridFS gridFS;

	public static Mongo getMongo() throws UnknownHostException, MongoException {
		if (mongo == null)
			mongo = new Mongo(host);
		return mongo;
	}

	public static DB getDB(String dbname) throws UnknownHostException,
			MongoException {
		if (mongo == null)
			mongo = new Mongo(host);
		return mongo.getDB(dbname);
	}

	public static DB getDB() throws UnknownHostException, MongoException {
		if (mongo == null)
			mongo = new Mongo(host);
		return mongo.getDB(db);
	}

	public static GridFS getGridFS() throws UnknownHostException,
			MongoException {
		if (gridFS == null)
			gridFS = new GridFS(getMongo().getDB(db));
		return gridFS;
	}

}
