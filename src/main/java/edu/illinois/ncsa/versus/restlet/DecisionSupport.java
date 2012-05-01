package edu.illinois.ncsa.versus.restlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;
import edu.illinois.ncsa.versus.adapter.Adapter;


@SuppressWarnings("serial")
public class DecisionSupport implements Serializable {
	
	private String id;
	private String adapterId;
	private List<String> similarData                                    = new ArrayList<String>();
	private List<String> dissimilarData                                 = new ArrayList<String>();	
	private DS_Status status                                            = DS_Status.UNINITIALZED;
	private List<String> extractorIds                                   = new ArrayList<String>(); // deprecated
	private HashMap<String, String> E_M_Pair                            = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> extractorMeasurePair     = new HashMap<String, ArrayList<String>>();
	private List<ArrayList<String>> similarComparisonLists              = new ArrayList< ArrayList<String> >();
	private List<ArrayList<String>> dissimilarComparisonLists           = new ArrayList< ArrayList<String> >();
	
	private List<Extractor> availableExtractors;
	private List<Measure> availableMeasures;
	
	private String decidedMethod = "Not Available";
	
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

	public String getExtractor(int index){
		return this.extractorIds.get(index);
	}
	
	public String getMeasure(String extractor){
		return this.E_M_Pair.get(extractor);
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
	
	public String extractors2String(){
		
		String el = "";
		
		for( int i=0; i<this.extractorIds.size(); i++){
			el = el + this.extractorIds.get(i) + "  ";
		}
		return el;
	}
	
	public String measures2String(){
		
		String ms                 = "";
		Collection<String> values = this.E_M_Pair.values();
		Iterator<String> itr      = values.iterator();
		
		while(itr.hasNext()){
			ms = ms + itr.next() + "  ";
		}
		
		return ms;
	}
	
	public int getNumPairs(){
		return this.E_M_Pair.size();
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
		
		String ae = "";
		
		Set<String> extractors = this.extractorMeasurePair.keySet();
		Iterator<String> itr           = extractors.iterator();
		
		while( itr.hasNext() ){			
			ae = ae + itr.next() + " ";
		}		
		return ae;
	}
	
	public String availableMeasures2String(){
		
		String am = "";
		
		Collection<ArrayList<String>> measures = this.extractorMeasurePair.values();
		Iterator<ArrayList<String>> itr        = measures.iterator();
		
		while( itr.hasNext() ){			
			
			Iterator<String> mItr = (itr.next()).iterator();
			
			while( mItr.hasNext() ){
				am = am + mItr.next() + " ";
			}
			
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
			ArrayList<String> supportedMeasures      = new ArrayList<String>();
			boolean extractorIsSupported             = false;
			
			while( a_itr.hasNext() ){ //iterate through all supported adapters for given extractor
				
				//adapter
				String name = a_itr.next().getName();
				
				if( this.adapterId.equals(name) ){					
									
					String descriptorName   = (e.getFeatureType()).getName(); //get the descriptor supported by extractor
					Iterator<Measure> m_itr = this.availableMeasures.iterator(); //all available measures 
					
					while( m_itr.hasNext() ){
						
						Measure m                           = m_itr.next();	
						
						if( m.getFeatureType().equals(descriptorName) ){ //check if the measure supports the descriptor (provided by the extractor)
							
							if( !this.extractorIds.contains( e.getClass().getName() ) ){
								this.extractorIds.add(e.getClass().getName());	//only add extractor if there is a readily available measure
							}
							supportedMeasures.add(m.getClass().getName());
							extractorIsSupported = true;
							E_M_Pair.put( e.getClass().getName(), m.getClass().getName() ); 
							//hashmap <extractor, measure> //need to change as a single extractor can map to multiple values, one though is use a HashMap< Pair<extractor, measure> , ArrayList<String -- comparisonId list> >
						}	
					}
				}		
			}//end adapters
			if(extractorIsSupported){
				this.extractorMeasurePair.put(e.getClass().getName(), supportedMeasures);
			}
		}//end extractors
		return;
	}
	
	//implementation of the math
	public void getBestPair(){
		
		//check both comparison lists to make sure all are done processing
		boolean state = true;
		
		for( int i=0; i<similarComparisonLists.size(); i++){
			ArrayList<String> scL = similarComparisonLists.get(i);
			state = state && checkIfComplete(scL);
		}
		
		for( int i=0; i<dissimilarComparisonLists.size(); i++){
			ArrayList<String> scL = dissimilarComparisonLists.get(i);
			state = state && checkIfComplete(scL);
		}
		
		//all comparisons should be finished. 
		if(state){
			double[] PE_vals  = new double[this.E_M_Pair.size()];
			double smallestPE = 10000000;
			int bestIndex     = 0;
			
			for( int i=0; i< PE_vals.length; i++){
				
				PE_vals[i] = computeComparisonPEValue(similarComparisonLists.get(i), dissimilarComparisonLists.get(i));
				if( PE_vals[i] < smallestPE){
					
					smallestPE = PE_vals[i];
					bestIndex  = i;
				}
			}
			
			this.decidedMethod = this.extractorIds.get(bestIndex) + " " + this.E_M_Pair.get(extractorIds.get(bestIndex));
			this.status        = DS_Status.DONE;
		}
	}
	
	private boolean checkIfComplete(ArrayList<String> al){
				
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		Iterator<String> l_itr                  = al.iterator();
		
		while(l_itr.hasNext()){
			Comparison c = comparisonService.getComparison((String) l_itr.next());
			if( c.getStatus() != ComparisonStatus.DONE ){
				return false;
			}
			else if( c.getStatus() == ComparisonStatus.ABORTED || c.getStatus() == ComparisonStatus.FAILED){
				
			}
		}
		//TODO add checking for failed comparisons, if too many have failed then throw some sort of error (or just ignore that particular extractor / measure combo
		return true;
	}
	
	private double computeComparisonPEValue(ArrayList<String> similarCL, ArrayList<String> dissimilarCL){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
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
	
}