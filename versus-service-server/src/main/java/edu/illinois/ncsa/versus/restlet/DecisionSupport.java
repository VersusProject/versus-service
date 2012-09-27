package edu.illinois.ncsa.versus.restlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

@SuppressWarnings("serial")
public class DecisionSupport implements Serializable {
	
	private String id;
	private String adapterId;
	private List<String> similarData                                    = new ArrayList<String>(); 
	private List<String> dissimilarData                                 = new ArrayList<String>(); 
	private DS_Status status                                            = DS_Status.UNINITIALZED;
		
	private List<ArrayList<String>> similarComparisonLists              = new ArrayList< ArrayList<String> >();
	private List<ArrayList<String>> dissimilarComparisonLists           = new ArrayList< ArrayList<String> >();
	
	private List<Extractor> availableExtractors;
	private List<Measure> availableMeasures;
	
	private String decidedMethod = "Not Available";
	private String rankedResults = " ";
	
	private List<DSInfo> decisionSupportData = new ArrayList<DSInfo>();
	private boolean computationFinished      = false;
	
	public enum DS_Status {
		UNINITIALZED, STARTED, RUNNING, DONE, FAILED
	}
	
	public DecisionSupport() {
	}
	
	public DecisionSupport(String[] sd, String[] dd, String adapterId) {
		super();
		this.adapterId = adapterId;		
		Collections.addAll(similarData, sd);
		Collections.addAll(dissimilarData, dd);
	}

	public ArrayList<DSInfo> getDecisionSupportData(){
		return (ArrayList<DSInfo>) decisionSupportData;
	}
	
	public void setDecisionSupportData(ArrayList<DSInfo> dsd){
		decisionSupportData = dsd;
	}

	public boolean checkIfComputationComplete(){
		return computationFinished;
	}
	
	public String getBestResultsList(){
		return rankedResults;
	}
	
	public void append2SimilarComparisonList(int index, ArrayList<String> cl){
		this.similarComparisonLists.add(index, cl);
	}
	
	public ArrayList<String> getSimilarComparisonList(int index){
		return this.similarComparisonLists.get(index);
	}
	
	public void append2DissimilarComparisonList(int index, ArrayList<String> cl){
		this.dissimilarComparisonLists.add(index, cl);
	}
	
	public ArrayList<String> getDissimilarComparisonList(int index){
		return this.dissimilarComparisonLists.get(index);
	}
	
	public void setSimilarData(List<String> sd){
		this.similarData = sd;
	}
	
	public List<String> getSimilarData(){
		return this.similarData;
	}
	
	public void setDissimilarData(List<String> dsd){
		this.dissimilarData = dsd;
	}
	
	public List<String> getDissimilarData(){
		return this.dissimilarData;
	}
	
	public String dissimilarData2String(){
		
		String dd = "";		
		for(int i=0; i<dissimilarData.size(); i++){
			dd = dd + dissimilarData.get(i) + "  ";
		}
		return dd;
	}
	
	public String similarData2String(){
		
		String sd = "";		
		for(int i=0; i<similarData.size(); i++){
			sd = sd + similarData.get(i) + "  ";
		}
		return sd;
	}
	
	public int getNumPairs(){
		
		return decisionSupportData.size();
	}
	
	public int getNumExtractors(){
		return availableExtractors.size();
	}
	
	public int getNumMeasures(){
		return availableMeasures.size();
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getAdapterId() {
		return adapterId;
	}

	public void setAdapterId(String adapterId) {
		this.adapterId = adapterId;
	}
	
	public void setStatus(DS_Status status){
		this.status = status;
	}
	
	public DS_Status getStatus(){		
		return status;
	}
	
	public void setAvailableExtractors(List<Extractor> e){
		this.availableExtractors = e;
	}
	
	public void setAvailableMeasures(List<Measure> m){
		this.availableMeasures = m;
	}
	
	public String getDecidedMethod(){
		return this.decidedMethod;
	}
	
	public String availableExtractors2String(){
		
		String ae            = "";
		Iterator<DSInfo> itr = decisionSupportData.iterator();
		
		while( itr.hasNext() ){			
			ae = ae + itr.next().extractorID + " ";
		}		
		return ae;
	}
	
	public String availableMeasures2String(){
		
		String am            = "";
		Iterator<DSInfo> itr = decisionSupportData.iterator();
		
		while( itr.hasNext() ){			
			am = am + itr.next().measureID + " ";	
		}		
		
		return am;
	}
	
	public void getSupportedMethods(){
		
		Iterator<Extractor> e_itr = this.availableExtractors.iterator();
		
		while( e_itr.hasNext() ){ //iterate through all available extractors	
			
			//extractor
			Extractor e                              = e_itr.next();
			Set<Class<? extends Adapter>> a          = e.supportedAdapters();			
			Iterator<Class<? extends Adapter>> a_itr = a.iterator();
			
			while( a_itr.hasNext() ){ //iterate through all supported adapters for given extractor
				
				//adapter
				String name = a_itr.next().getName();
				
				if( this.adapterId.equals(name) ){					
									
					String descriptorName   = (e.getFeatureType()).getName(); //get the descriptor supported by extractor
					Iterator<Measure> m_itr = this.availableMeasures.iterator(); //all available measures 
					
					while( m_itr.hasNext() ){
						
						Measure m  = m_itr.next();	
						
						if( isDescriptorSupportedByMeasure(descriptorName, m) ){ //check if the measure supports the descriptor (provided by the extractor)
							
							if( !this.availableExtractors.contains( e.getClass().getName() ) ){// if there is an EM pair, create DSInfo and push to list
								decisionSupportData.add( new DSInfo(this.adapterId, e.getClass().getName(),m.getClass().getName()));
							}
						}	
					}
				}		
			}//end adapters
		}//end extractors
	}
    
    private static boolean isDescriptorSupportedByMeasure(String descriptorName,
            Measure measure) {
        for(Class<? extends Descriptor> feature :
                measure.supportedFeaturesTypes()) {
            if(feature.getName().equals(descriptorName)) {
                return true;
            }
        }
        return false;
    }
	
	private boolean checkIfComplete(){
		
		Injector injector                       = ServerApplication.getInjector();
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		for( int i=0; i<decisionSupportData.size(); i++){
			
			ArrayList<String> sc = decisionSupportData.get(i).getSimilarComparisons();
			ArrayList<String> dc = decisionSupportData.get(i).getDissimilarComparisons();
			boolean scErrorFlag    = false;
			boolean dcErrorFlag    = false;

			//check all similarComparisons
			for( int j=0; j < sc.size(); j++){
			
				Comparison c = comparisonService.getComparison( sc.get(j) );
				
				if( c.getStatus() != ComparisonStatus.DONE ){
					return false;
				}
				else if( c.getStatus() == ComparisonStatus.ABORTED || c.getStatus() == ComparisonStatus.FAILED){
					scErrorFlag = true;
					break;
				}
			}
			if(!scErrorFlag){
				//check all dissimilarComparisons
				for( int j=0; j < dc.size(); j++){
					
					Comparison c = comparisonService.getComparison( dc.get(j) );
					
					if( c.getStatus() != ComparisonStatus.DONE ){
						return false;
					}
					else if( c.getStatus() == ComparisonStatus.ABORTED || c.getStatus() == ComparisonStatus.FAILED){
						dcErrorFlag = true;
						break;
					}
				}
			}
			else{
				decisionSupportData.remove(i);
			}
			
			if(dcErrorFlag){
				decisionSupportData.remove(i);
			}
		}
		return true;
	}
	
	//implementation of the math, this is slow, need to thread the computation of the individual DSInfos
	public void getBestPair(){
		
		if(!computationFinished){
			this.status = DS_Status.RUNNING;
			
			//check all comparisons to make sure all are done processing
			//if there are any errors, then the E-M tuple is discarded
			boolean state        = checkIfComplete();
			String extractorID_d = "";
			String measureID_d   = "";
			
			//all comparisons should be finished. 
			if(state){
					
				double[] PE_vals     = new double[decisionSupportData.size()];
				Double smallestPE    = Double.MAX_VALUE;
				Iterator<DSInfo> eid = decisionSupportData.iterator();
				
				while( eid.hasNext() ){
					
					DSInfo dsTuple = eid.next();								
					Double val     = new Double( computeComparisonPEValue( dsTuple.getSimilarComparisons(), dsTuple.getDissimilarComparisons() ) );
					dsTuple.setPEValue(val);
					
					if( val.doubleValue() < smallestPE.doubleValue()){
						smallestPE    = val;
						extractorID_d = dsTuple.getExtractorID();
						measureID_d   = dsTuple.getMeasureID();
					}
				}
				
				decidedMethod = extractorID_d + " " + measureID_d;
				
		       Collections.sort(decisionSupportData,new Comparator<DSInfo>() {
		            public int compare(DSInfo info1, DSInfo info2) {
		                return info1.getPEValue().compareTo( info2.getPEValue() );
		            }
		        });
				
		       //return top 5 or 25% of methods
		       int numTopMethods = (int)(decisionSupportData.size() * 0.30);
		       
		       if( numTopMethods > 5 ){
		    	   numTopMethods = 5;
		       }
		       
		       for( int i=0; i<=numTopMethods; i++){
		    	   rankedResults = rankedResults + (i+1) + ".) " + decisionSupportData.get(i).extractorID + " " + decisionSupportData.get(i).measureID + " ";
		       }
				status              = DS_Status.DONE;
				computationFinished = true;
			}
		}
	}
		
	private double computeComparisonPEValue(ArrayList<String> similarCL, ArrayList<String> dissimilarCL){
		
		Injector injector                       = ServerApplication.getInjector();
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		Iterator<String> s_itr                  = similarCL.iterator();
		Iterator<String> d_itr                  = dissimilarCL.iterator();

		//get all the values (distances)
		
		double[] similarVals    = new double[similarCL.size()];
		double[] dissimilarVals = new double[dissimilarCL.size()];

		double max = 0;
		double min = 10000000;
		
		int index=0;
		while(s_itr.hasNext()){
			String value =  ((Comparison) comparisonService.getComparison( s_itr.next() )).getValue();
			similarVals[index] = Double.parseDouble( value );
			if( similarVals[index] > max){ 
				max = similarVals[index];
			}
			if( similarVals[index] < min){ 
				min = similarVals[index];
			}
			index++;
		}
		index=0;
		while(d_itr.hasNext()){
			dissimilarVals[index] = Double.parseDouble( comparisonService.getComparison(d_itr.next()).getValue() );
			if( dissimilarVals[index] > max){ 
				max = similarVals[index];
			}
			if( dissimilarVals[index] < min){ 
				min = similarVals[index];
			}
			index++;
		}
		
		//integration using the gaussian kernels over the distance support 
		max+=2; //account for sigma in integration
		min+=-2;
		double perrorComplement = 0;
		double resolution       = 0.10;
		int range               = (int)((max-min)/resolution);
		double[] fnSimilar      = new double[ range ];
		double[] fnDissimilar   = new double[ range ];
		Arrays.fill(fnSimilar, 0);
		Arrays.fill(fnDissimilar, 0);
			
		for( int x=0; x<range; x++){
			
			for(int s=0; s<similarVals.length; s++){
				fnSimilar[x] += fnSimilar[x] + Math.exp( -1*Math.pow( (double)x -similarVals[s],2) );
			}
			for(int d=0; d<dissimilarVals.length; d++){
				fnDissimilar[x] += Math.exp( -1*Math.pow( (double)x -dissimilarVals[d],2) );
			}
			
			perrorComplement += Math.max(fnDissimilar[x], fnSimilar[x]);
		}
				
		return 1-perrorComplement;
	}
	
	public class DSInfo {
		
		private String            adapterID;
		private String            extractorID;
		private String            measureID;
		private ArrayList<String> similarComparisons;
		private ArrayList<String> dissimilarComparisons;
		private Double            PE_Value;
		
		public DSInfo(){
		}
		
		public DSInfo(String a, String e, String m){
			this.adapterID   = a;
			this.extractorID = e;
			this.measureID   = m;
		}
		
		
		public Double getPEValue(){
			return PE_Value;
		}
		
		public void setPEValue(double val){
			PE_Value = new Double(val);
		}
		
		/**
		 * Setter function for adapterID member.
		 * @param a ID of the Adapter
		 */
		public void setAdapterID(String a){
			adapterID = a;
		}
		/**
		 * Getter function for adapterID member.
		 * @return The ID of the Adapter.
		 */
		public String getAdapterID(){
			return adapterID;
		}	
		/**
		 * Setter function for extractorID member.
		 * @param The ID of the Extractor.
		 */
		public void setExtractorID(String e){
			extractorID = e;
		}
		/**
		 * Getter function for extractorID member.
		 * @return The ID of the Extractor.
		 */
		public String getExtractorID(){
			return extractorID;
		}
		/**
		 * Setter function for measureID member.
		 * @param The ID of the Measure.
		 */
		public void setMeasureID(String m){
			measureID = m;
		}
		/**
		 * Getter function for measureID member.
		 * @return The ID of the Measure.
		 */
		public String getMeasureID(){
			return measureID;
		}
		/**
		 * Setter function for the Similar Comparison list.
		 * @param The ArrayList of similar comparison IDs generated by the Comparison service resource.
		 */
		public void setSimilarComparisons(ArrayList<String> sc){
			similarComparisons = sc;
		}
		/**
		 * Getter function for the Similar Comparison list.
		 * @param The ArrayList of similar comparison IDs generated by the Comparison service resource.
		 */
		public ArrayList<String> getSimilarComparisons(){
			return similarComparisons;
		}		
		/**
		 * Setter function for the dissimilar Comparison list.
		 * @param The ArrayList of dissimilar comparison IDs generated by the Comparison service resource.
		 */
		public void setDissimilarComparisons(ArrayList<String> dsc){
			dissimilarComparisons = dsc;
		}
		/**
		 * Getter function for the dissimilar Comparison list.
		 * @param The ArrayList of dissimilar comparison IDs generated by the Comparison service resource.
		 */
		public ArrayList<String> getDissimilarComparisons(){
			return dissimilarComparisons;
		}
	}
	
}