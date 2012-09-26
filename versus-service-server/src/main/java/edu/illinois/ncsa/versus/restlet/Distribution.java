package edu.illinois.ncsa.versus.restlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

@SuppressWarnings("serial")
public class Distribution implements Serializable {
	private String id;
	private String[] data;
	private String adapterId;
	private String extractorId;
	private String measureId;
	
	private Double sampleMean                   = new Double(0);
	private Double stdDeviation                 = new Double(0);
	private DistributionStatus status           = DistributionStatus.UNINITIALZED;
	private List<String> failedComparisonIds    = new ArrayList<String>();;
	private List<String> completedComparisonIds = new ArrayList<String>();;
	private List<String> pendingComparisonIds   = new ArrayList<String>();;
	
	public enum DistributionStatus {
		UNINITIALZED, STARTED, RUNNING, DONE, FAILED
	}
	
	public Distribution() {
	}

	public Distribution(String[] inputData,String adapterId, String extractorId, String measureId) {
		super();
		this.adapterId              = adapterId;
		this.extractorId            = extractorId;
		this.measureId              = measureId;
		this.data                   = new String[inputData.length];
		System.arraycopy(inputData,0,this.data,0,inputData.length);		
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String[] getDataset() {
		return data;
	}

	public String dataset2String(){
		
		String list = "";
		
		for(int i=0; i<data.length; i++){
			list = list + data[i] + "\n";
		}
		return list;
	}
	
	public void setDataset(String[] inputData) {
		this.data = new String[inputData.length];
		System.arraycopy(inputData,0,this.data,0,inputData.length);		
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

	public String getMean(){
		return sampleMean.toString();
	}
	
	public void setMean(double mean){
		this.sampleMean = new Double(mean);
	}
	
	public String getDeviation(){
		return stdDeviation.toString();
	}
	
	public void setDeviation(double sigma){
		this.stdDeviation = new Double(sigma);
	}
	
	public void setStatus(DistributionStatus status){
		this.status = status;
	}
	
	public DistributionStatus getStatus(){
		
		if (pendingComparisonIds == null){
			status = DistributionStatus.UNINITIALZED;
		}
		else if( pendingComparisonIds.size() == 0 ){
			status = DistributionStatus.DONE;
		}
		else{
			
			Injector injector                       = ServerApplication.getInjector();
			ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
			Collection<Comparison> comparisons      = comparisonService.listAll();
			Iterator<Comparison> itr                = comparisons.iterator();
			
			while(itr.hasNext()){
				
				Comparison c = itr.next();
				String id    = c.getId();
				
				if(c.getStatus() == ComparisonStatus.DONE){
					pendingComparisonIds.remove(id);
					completedComparisonIds.add(id);
				}
				if(c.getStatus() == ComparisonStatus.FAILED || c.getStatus() == ComparisonStatus.ABORTED){
					pendingComparisonIds.remove(id);
					failedComparisonIds.add(id);					
				}
			}
			if( pendingComparisonIds.size() > 0 ){
				status = DistributionStatus.RUNNING;
			}
			else if( failedComparisonIds.size() > 0 && completedComparisonIds.size() == 0 && pendingComparisonIds.size() == 0 ){
				status = DistributionStatus.FAILED;
			}
		}			
		return status;
	}
	
	public void setComparisonIds(List<String> ids){
		
		Iterator<String> itr = ids.iterator();
		while(itr.hasNext()){
			this.pendingComparisonIds.add(itr.next());			
		}
	}
	
	public List<String> getPendingComparisonIds(){
		return this.pendingComparisonIds;
	}
	
	public List<String> getCompletedComparisonIds(){
		return this.completedComparisonIds;
	}
	
	public List<String> getFailedComparisonIds(){
		return this.failedComparisonIds;
	}
	
	public void calculateStatistics(){
		
		double mean  = 0;
		double sigma = 0;
		
		if( completedComparisonIds != null ){
		
			if( completedComparisonIds.size() > 1 ){
				
				int count                               = 0;			
				Injector injector                       = ServerApplication.getInjector();
				ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
				comparisonService.getComparison(id);			
				Iterator<String> itr                    = completedComparisonIds.iterator();
				
				while( itr.hasNext() ){
					Comparison c = comparisonService.getComparison( itr.next() );
					mean        += Double.valueOf( c.getValue() ).doubleValue();				
					count++;
				}
				
				mean = mean / count;				
				itr  = completedComparisonIds.iterator();
				
				while( itr.hasNext() ){
					Comparison c = comparisonService.getComparison( itr.next() );
					sigma       += Math.pow( ((Double.valueOf( c.getValue() ).doubleValue() ) - mean), 2 );				
				}
				sigma = Math.sqrt( sigma / (count - 1) );			
			}
		}
		
		this.sampleMean   = mean;
		this.stdDeviation = sigma;
	}
	
}
