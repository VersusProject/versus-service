package edu.illinois.ncsa.versus.restlet;

import java.io.Serializable;

/**
 * DTO for a single pairwise comparison.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
@SuppressWarnings("serial")
public class Comparison implements Serializable {
	private String id;
	private String firstDataset;
	private String secondDataset;
	private String adapterId;
	private String extractorId;
	private String measureId;
	private String value;

	public Comparison() {
	}

	public Comparison(String id, String firstDataset, String secondDataset,
			String adapterId, String extractorId, String measureId) {
		super();
		this.id = id;
		this.firstDataset = firstDataset;
		this.secondDataset = secondDataset;
		this.adapterId = adapterId;
		this.extractorId = extractorId;
		this.measureId = measureId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getFirstDataset() {
		return firstDataset;
	}

	public void setFirstDataset(String firstDataset) {
		this.firstDataset = firstDataset;
	}

	public String getSecondDataset() {
		return secondDataset;
	}

	public void setSecondDataset(String secondDataset) {
		this.secondDataset = secondDataset;
	}

	public String getAdapterId() {
		return adapterId;
	}

	public void setAdapterId(String adapterId) {
		this.adapterId = adapterId;
	}

	public String getExtractorId() {
		return extractorId;
	}

	public void setExtractorId(String extractorId) {
		this.extractorId = extractorId;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	@Override
	public String toString() {
		return measureId;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
