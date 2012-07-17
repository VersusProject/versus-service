/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.io.InputStream;

import org.bson.types.ObjectId;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Store files in MongoDB using GridFS.
 * 
 * @author Luigi Marini
 * 
 */
public class MongoFileProcessor extends MongoConnection implements
		FileProcessor {

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
