/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.io.InputStream;

import org.bson.types.ObjectId;

import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Store files in MongoDB using GridFS.
 * 
 * @author Luigi Marini
 * 
 */
public class MongoFileProcessor implements FileProcessor {

	private static String host = "127.0.0.1";
	private static String db = "test";
	private static Mongo mongo = null;
	private static GridFS gridFS;

	private static Mongo getMongo() throws Exception {
		if (mongo == null)
			mongo = new Mongo(host);
		return mongo;
	}

	private static GridFS getGridFS() throws Exception {
		if (gridFS == null)
			gridFS = new GridFS(getMongo().getDB(db));
		return gridFS;
	}

	@Override
	public String addFile(InputStream inputStream, String filename)
			throws Exception {
		GridFS fs = getGridFS();
		GridFSInputFile f = fs.createFile(inputStream, filename);
		f.save();
		f.validate();
		return f.getId().toString();
	}

	@Override
	public InputStream getFile(String id) throws Exception {
		GridFS fs = getGridFS();
		GridFSDBFile file = fs.findOne(new ObjectId(id));
		return file.getInputStream();
	}
}
