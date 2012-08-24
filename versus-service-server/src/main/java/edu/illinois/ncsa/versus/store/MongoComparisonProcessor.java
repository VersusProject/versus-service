/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;

/**
 * Store comparisons in Mongo.
 * 
 * @author Luigi Marini
 * 
 */
public class MongoComparisonProcessor extends MongoConnection implements
		ComparisonProcessor {

	private static final String comparisonsCol = "comparisons";
	private static Log log = LogFactory.getLog(MongoComparisonProcessor.class);

	@Override
	public void addComparison(Comparison comparison) {
		DBObject doc = new BasicDBObject();
		doc.put("id", comparison.getId());
		doc.put("adapter", comparison.getAdapterId());
		doc.put("extractor", comparison.getExtractorId());
		doc.put("measure", comparison.getMeasureId());
		doc.put("dataset1", comparison.getFirstDataset());
		doc.put("dataset2", comparison.getSecondDataset());
		doc.put("status", comparison.getStatus());
		doc.put("value", comparison.getValue());
        doc.put("error", comparison.getError());
		try {
			getDB().getCollection(comparisonsCol).insert(doc);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		}
	}

	@Override
	public Comparison getComparison(String id) {
		try {
			DBObject query = new BasicDBObject();
			query.put("id", id);
			DBObject doc = getDB().getCollection(comparisonsCol).findOne(query);
			return buildComparison(doc);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		}
		return null;
	}

	@Override
	public Collection<Comparison> listAll() {
		List<Comparison> comparisons = new ArrayList<Comparison>();
		try {
			DBCursor cur = getDB().getCollection(comparisonsCol).find();
			while (cur.hasNext()) {
				comparisons.add(buildComparison(cur.next()));
			}
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		}
		return comparisons;
	}

	@Override
	public void updateValue(String id, String value) {
		try {
			DBCollection collection = getDB().getCollection(comparisonsCol);
			DBObject query = new BasicDBObject();
			query.put("id", id);
			DBObject doc = collection.findOne(query);
			doc.put("value", value);
			collection.update(query, doc);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		}
	}

	@Override
	public void setStatus(String id, ComparisonStatus status) {
		try {
			DBCollection collection = getDB().getCollection(comparisonsCol);
			DBObject query = new BasicDBObject();
			query.put("id", id);
			DBObject doc = collection.findOne(query);
			doc.put("status", status.toString());
			collection.update(query, doc);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		}
	}

	@Override
	public ComparisonStatus getStatus(String id) {
		try {
			DBObject doc = getDB().getCollection(comparisonsCol).findOne(
					new ObjectId(id));
			return ComparisonStatus.valueOf((String) doc.get("status"));
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		}
		return null;
	}
    
    @Override
    public void setError(String id, String error) {
        try {
			DBCollection collection = getDB().getCollection(comparisonsCol);
			DBObject query = new BasicDBObject();
			query.put("id", id);
			DBObject doc = collection.findOne(query);
			doc.put("error", error);
			collection.update(query, doc);
		} catch (MongoException e) {
			log.error("Error connecting to mongo.", e);
		} catch (UnknownHostException e) {
			log.error("Error connecting to mongo.", e);
		}
    }

	private Comparison buildComparison(DBObject doc) {
		Comparison comparison = new Comparison();
		comparison.setAdapterId((String) doc.get("adapter"));
		comparison.setExtractorId((String) doc.get("extractor"));
		comparison.setMeasureId((String) doc.get("measure"));
		comparison.setFirstDataset((String) doc.get("dataset1"));
		comparison.setSecondDataset((String) doc.get("dataset2"));
		comparison.setStatus(ComparisonStatus.valueOf((String) doc
				.get("status")));
		comparison.setValue((String) doc.get("value"));
        comparison.setError((String) doc.get("error"));
		return comparison;
	}
}
