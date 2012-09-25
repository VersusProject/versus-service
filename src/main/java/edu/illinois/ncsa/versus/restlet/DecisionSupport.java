package edu.illinois.ncsa.versus.restlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.rest.ComparisonResource;
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
		
	private List<ArrayList<String>> similarComparisonLists              = new ArrayList< ArrayList<String> >();
	private List<ArrayList<String>> dissimilarComparisonLists           = new ArrayList< ArrayList<String> >();
	
	private List<Extractor> availableExtractors;
	private List<Measure> availableMeasures;
	
	private String decidedMethod = "Not Available";
	private String rankedResults = " ";
	private String values="";       // new variable added
	private HashMap<String,ArrayList<String>> measureHashList=new HashMap<String,ArrayList<String>>(); //added by Smruti
	private String measureSpread="";
	
	private List<DSInfo> decisionSupportData = new ArrayList<DSInfo>();
	private boolean computationFinished      = false;
	
	private static Log log = LogFactory.getLog(DecisionSupport.class); //added by Smruti
	
	private ArrayList<Comparison> cs=new ArrayList<Comparison>();
	
	
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
	
	public String getValues(){ // method added by Smruti to send all distance values to Client
		return values;
	}
	public String getmeasureSpread(){
		return measureSpread;
	}
	public void append2measureHashList(String measureID, String distValue){
		if(measureHashList.containsKey(measureID)){
			measureHashList.get(measureID).add(distValue);
		}
		else{
		    ArrayList<String> distList=new ArrayList<String>();
		    distList.add(distValue);
			measureHashList.put(measureID,distList);
		}
	}
	public HashMap<String,ArrayList<String>> getmeasureHashList(){
		return measureHashList;
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
						
						if( m.getFeatureType().equals(descriptorName) ){ //check if the measure supports the descriptor (provided by the extractor)
							
							if( !this.availableExtractors.contains( e.getClass().getName() ) ){// if there is an EM pair, create DSInfo and push to list
								decisionSupportData.add( new DSInfo(this.adapterId, e.getClass().getName(),m.getClass().getName()));
							}
						}	
					}
				}		
			}//end adapters
		}//end extractors
	}
	
	private boolean checkIfComplete(){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		
		
		//HashMap<String,ArrayList<Comparison>> cs=new HashMap<String,ArrayList<Comparision>>();
		
		for( int i=0; i<decisionSupportData.size(); i++){
			
			ArrayList<String> sc = decisionSupportData.get(i).getSimilarComparisons();
			ArrayList<String> dc = decisionSupportData.get(i).getDissimilarComparisons();
			boolean scErrorFlag    = false;
			boolean dcErrorFlag    = false;

			log.debug("Similar Comparision status:");
			//check all similarComparisons
			for( int j=0; j < sc.size(); j++){
			
				Comparison c = comparisonService.getComparison( sc.get(j) );
				
				log.debug("Scomparison id "+c.getId()+" ="+c.getStatus());
				if( c.getStatus() != ComparisonStatus.DONE ){
				 // while(c.getStatus()!=ComparisonStatus.DONE){	
				    try {
						//Thread.sleep(5);
				    	synchronized(c){
				    	c.wait();
				    	}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// log.debug("GOING TO SLEEP");
					/*while(c.getStatus()!=ComparisonStatus.DONE){
						try{
							//log.debug("GOING TO SLEEP");
							c.wait();
							}catch(InterruptedException e){
								
							}
						*/
						//log.debug("Scomparison id "+c.getId()+" ="+c.getStatus());	
					//}
				    //cs.add(c); //added by smruti
					
					log.debug("Scomparison id "+c.getId()+" ="+c.getStatus());
					//return false;
				}
				else if( c.getStatus() == ComparisonStatus.ABORTED || c.getStatus() == ComparisonStatus.FAILED){
					scErrorFlag = true;
					break;
				}
			}
			if(!scErrorFlag){
				//check all dissimilarComparisons
				log.debug("DisSimilar Comparision status:");
				for( int j=0; j < dc.size(); j++){
					
					Comparison c = comparisonService.getComparison( dc.get(j) );
					log.debug("comparison id "+c.getId()+" ="+c.getStatus());
					if( c.getStatus() != ComparisonStatus.DONE ){
						//log.debug("GOING TO SLEEP");
						//while(c.getStatus()!=ComparisonStatus.DONE){
							//log.debug("comparison id "+c.getId()+" ="+c.getStatus());
							/*try{
								wait();
								}catch(InterruptedException e){
									
								}*/
						       try {
								//Thread.sleep(5);
						    	   synchronized(c){
						    		   c.wait();
						    	   }
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//}
						log.debug(" Dcomparison id "+c.getId()+" ="+c.getStatus());
						//cs.add(c); //added by smruti
						//return false;
					}
					else if( c.getStatus() == ComparisonStatus.ABORTED || c.getStatus() == ComparisonStatus.FAILED){
						dcErrorFlag = true;
						break;
					}
				}
			}
			else{
				//decisionSupportData.remove(i);
				//sc.remove(c.getId());
				log.debug("Error in ComparisonStatus: similar Files");
			}
			
			if(dcErrorFlag){
				//decisionSupportData.remove(i);
				log.debug("Error in ComparisonStatus: dissimilar Files");
			}
		}
		
		//log.debug("Size of Comparison Status List whose Status is not DONE"+cs.size());
		int m=0;
		/*for(int k=0;k<cs.size();k++){
		//int k=1;
		//while(cs.size()!=k){
			log.debug("k="+k+"cs.size()="+cs.size()+"  cs.get(k).id= "+ cs.get(k).getId());
			while(cs.get(k).getStatus()!=ComparisonStatus.DONE){
			//if(cs.get(k).getStatus()==ComparisonStatus.DONE){
				//m++;
				//if(m==cs.size())
					//break;
				//k++;
				//cs.remove(k);
				//log.debug("comparison id: "+cs.get(k).getId()+" = "+cs.get(k).getStatus());
			}
			
			log.debug("comparison id: "+cs.get(k).getId()+" = "+cs.get(k).getStatus());
		}*/
		
		return true;
	}
	
	//implementation of the math, this is slow, need to thread the computation of the individual DSInfos
	public void getBestPair(){
		
		Injector injector= Guice.createInjector(new RepositoryModule());//added by Smruti
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);//added by Smruti
		String value=""; //added by Smruti ---contains all the comparison values for each pair
		
		log.debug("Entered getBestPair()");
		
		if(!computationFinished){
			this.status = DS_Status.RUNNING;
			
			//check all comparisons to make sure all are done processing
			//if there are any errors, then the E-M tuple is discarded
			boolean state        = checkIfComplete();
			String extractorID_d = "";
			String measureID_d   = "";
			
			//all comparisons should be finished. 
			/*if(!state){
				while(checkIfComplete()){}
			}*/
			if(state){
					
				double[] PE_vals     = new double[decisionSupportData.size()];
				Double smallestPE    = Double.MAX_VALUE;
				Iterator<DSInfo> eid = decisionSupportData.iterator();
				String itr;
				while( eid.hasNext() ){
					
					DSInfo dsTuple = eid.next();								
					Double val     = new Double( computeComparisonPEValue( dsTuple.getSimilarComparisons(), dsTuple.getDissimilarComparisons() ) );
					dsTuple.setPEValue(val);
					log.debug("Extractor"+dsTuple.getExtractorID());
					log.debug("Measure"+dsTuple.getMeasureID());
					
					log.debug("SimilarComparisons list size"+dsTuple.getSimilarComparisons().size());
					log.debug("DissimilarComparisons list size"+dsTuple.getDissimilarComparisons().size());
					log.debug("PEValue"+val);
					//Iterator<String> sm_itr = dsTuple.getSimilarComparisons().iterator();
					for(int k=0;k<dsTuple.getSimilarComparisons().size();k++){
					//while(sm_itr.hasNext()){
						itr=dsTuple.getSimilarComparisons().get(k);
						append2measureHashList(((Comparison) comparisonService.getComparison( itr)).getMeasureId(),((Comparison) comparisonService.getComparison(itr)).getValue());
					//}
					}
					
					if( val.doubleValue() < smallestPE.doubleValue()){
						smallestPE    = val;
						extractorID_d = dsTuple.getExtractorID();
						measureID_d   = dsTuple.getMeasureID();
					}
				}
				
				decidedMethod = extractorID_d + " " + measureID_d; // contains the decided method with lowest PE value
				String ms;
				int l=0;
				double mean,sd;
				for(Entry<String, ArrayList<String>> mp: measureHashList.entrySet()){
					
					ms=mp.getKey().substring(mp.getKey().lastIndexOf('.')+1);
					//log.debug("measure ID from measure HashList"+ms);
					//log.debug("distance values coressponding to a measure ID:");
					measureSpread=measureSpread+ms+"("+l+")";
					ArrayList<String> mpList=mp.getValue();
					mean=computeMean(mpList);
				    sd=computeSD(mean,mpList);
					/*for(String dv:mpList){
						log.debug(dv+", ");
						measureSpread=measureSpread+dv+",";
					}*/
				    measureSpread=measureSpread+Double.toString(mean)+","+Double.toString(sd);
					measureSpread+="{"+l+"}";
					l++;
				}
				
				
				
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
		    	   int exIn=decisionSupportData.get(i).extractorID.lastIndexOf('.');
		    	   String exName=decisionSupportData.get(i).extractorID.substring(exIn+1);
		    	   int meIn=decisionSupportData.get(i).measureID.lastIndexOf('.');
		    	   String meName=decisionSupportData.get(i).measureID.substring(meIn+1);
		    	   
		    	  // rankedResults = rankedResults + (i+1) + ".) " + decisionSupportData.get(i).extractorID + " " + decisionSupportData.get(i).measureID + " ";
		    	   rankedResults = rankedResults + (i+1) + ".) " + exName +(i+1)+"-" +meName+ (i+1)+":"+decisionSupportData.get(i).getPEValue() +" ";
		    	   log.debug(rankedResults);
		    	   //write  code to send arrays of number to clients----------------------------------added by Smruti
		    	  
		    	   Iterator<String> s_itr                  = decisionSupportData.get(i).getSimilarComparisons().iterator();
		   		   Iterator<String> d_itr                  = decisionSupportData.get(i).getDissimilarComparisons().iterator();
		    	   double[] similarVals    = new double[decisionSupportData.get(i).getSimilarComparisons().size()];
		   		   double[] dissimilarVals = new double[decisionSupportData.get(i).getDissimilarComparisons().size()];

		   		double max = 0;
		   		double min = 10000000;
		   		int numOfbin=10;
		   		
		   		int index=0;
		   		try{
		   		while(s_itr.hasNext()){
		   			String value1 =  ((Comparison) comparisonService.getComparison( s_itr.next() )).getValue();
		   			
		   			similarVals[index] = Double.parseDouble( value1 );
		   			//log.debug("similarVals["+index+"]"+similarVals[index]);
		   			if( similarVals[index] > max){ 
		   				max = similarVals[index];
		   			}
		   			if( similarVals[index] < min){ 
		   				min = similarVals[index];
		   			}
		   			index++;
		   		   }
		   		
		   		}catch(Exception e){
		   			e.printStackTrace(System.out);
		   		}
		   		
		   		index=0;
		   		try{
		   		while(d_itr.hasNext()){
		   			dissimilarVals[index] = Double.parseDouble( comparisonService.getComparison(d_itr.next()).getValue() );
		   			//log.debug("dissimilarVals["+index+"]"+dissimilarVals[index]);
		   			if( dissimilarVals[index] > max){ 
		   				max = dissimilarVals[index];
		   			}
		   			if( dissimilarVals[index] < min){ 
		   				min = dissimilarVals[index];
		   			}
		   			index++;
		   		}
		   		}catch(Exception e1){
		   			e1.printStackTrace(System.out);
		   		}
		   		//log.debug("Max="+max+" Min="+min);
		   		double binSize=(max-min)/(double)numOfbin;
		   		//log.debug("Max="+max+" Min="+min+" binSize="+binSize);
		   		binSize=0.1;
		   		numOfbin=(int)((max-min)/binSize);
		   	    log.debug("Max="+max+" Min="+min+" binSize="+binSize+" numOfbin="+numOfbin);
		   		double[] dvSimilar=new double[numOfbin];
		   		double[] dvDissimilar=new double[numOfbin];
		   		Arrays.fill(dvSimilar,0);
		   		Arrays.fill(dvDissimilar,0);
		   		
		   		for(int s=0;s<similarVals.length;s++){
		   			
		   			for(int j=0;j<numOfbin;j++){
		   				if(similarVals[s]>=min+j*binSize  && similarVals[s]<(min+(j+1.0)*binSize))
		   				{   
		   					dvSimilar[j]++;
		   					break;
		   				}
		   				if(similarVals[s]==(min+numOfbin*binSize))
		   				       dvSimilar[numOfbin-1]++;
		   			}
		   			
		   			
		           	}
		   		     //double dist=0.0;
		   		     
		   		    for(int j=0;j<dvSimilar.length;j++)
		   		    {
		   		    	//log.debug("Before :j="+j+" dvSimilar[j]="+dvSimilar[j]);
		   		    	dvSimilar[j]=dvSimilar[j]/(binSize*similarVals.length);
		   		    	//dist=dist+ dvSimilar[s];
		   		    	//dvSimilar[s]=dist;
		   		    	//log.debug("After: s="+j+" dvSimilar[s]="+dvSimilar[j]);
		   		    	value=value+","+Double.toString(dvSimilar[j]);
		   		    } 	
		    	   value=value+"("+(i+1)+")";
		    	   log.debug(value);
		    	   
		    	   
		    	   
		    	   
		    	   for(int d=0;d<dissimilarVals.length;d++){
		    		   
		    		      			
				   			for(int j=0;j<numOfbin;j++){
				   				if(dissimilarVals[d]>=min+j*binSize  && dissimilarVals[d]<(min+(j+1.0)*binSize))
				   				{   
				   					dvDissimilar[j]++;
				   					break;
				   				}
				   				if(dissimilarVals[d]==(min+numOfbin*binSize))
				   				{    
				   					dvDissimilar[numOfbin-1]++;
				   				}
				   			}
		    		   
			   			
			           	}
			   		
			   		    for(int d=0;d<dvDissimilar.length;d++)
			   		    {
			   		    	dvDissimilar[d]=dvDissimilar[d]/(binSize*dissimilarVals.length);
			   		    	value=value+","+Double.toString(dvDissimilar[d]);
			   		    	//log.debug("d= "+d+" dvDissimilar[d]="+dvDissimilar[d]);
			   		    } 	
			    	   value=value+"{"+(i+1)+"}";
			    	   log.debug(value);
			    	   
		    	       values=value;
		    	       
		    	       log.debug(values);
		    	   
		    	   
		    	   
		    	   
		    	   /* Iterator<String> s_itr= decisionSupportData.get(i).getSimilarComparisons().iterator();
		   			Iterator<String> d_itr=decisionSupportData.get(i).getDissimilarComparisons().iterator() ;
		    	   while(s_itr.hasNext()){
		   			value = value+","+ ((Comparison) comparisonService.getComparison( s_itr.next() )).getValue();
		   			
		   			//similarVals[index] = Double.parseDouble( value );
		   			  	   
		    	   }
		    	   value=value+"("+(i+1)+")";
		    	   
		    	   while(d_itr.hasNext()){
			   			value = value+","+ ((Comparison) comparisonService.getComparison( d_itr.next() )).getValue();
			   			
			   			//similarVals[index] = Double.parseDouble( value );
			   			  	   
			    	   }
			    	   value=value+"{"+(i+1)+"}";
		    	   //---------------------------------------added by Smruti
		    	   values=value;
		    	   log.debug(values);*/
		       }
				status              = DS_Status.DONE;
				computationFinished = true;
			}
		}
	}
		private double computeMean(ArrayList<String> array){
			
			int n=0;
			double mean=0.0;
			
			
			for(n=0;n<array.size();n++){
				mean=mean+Double.valueOf(array.get(n));
			}
			mean=mean/(double)(array.size()-1);
			return mean;
			
		}
		
		private double computeSD(double mean,ArrayList<String> array){
			double sum=0.0;
			double sd;
			
		        for(int i=0;i<array.size();i++){
					
					sum=sum+(Double.valueOf(array.get(i))-mean)*(Double.valueOf(array.get(i))-mean);
				}
					sd=Math.sqrt(sum/(array.size()-1));
					return sd;

		}
		
	private double[] estimatePDensityfunction(double [] filesCL, double max, double min){
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		
		//Iterator<String> s_itr                  = filesCL.iterator();
		
		// get the distance values
		
		double[] filesVals=new double[filesCL.length];
		
		// For finding optimal h for similar files	
		//double sd,mean=0.0;
				
		/*for(int i=0;i<filesVals.length;i++){
					
					mean=mean+filesVals[i];
				}
				mean=mean/filesVals.length;
				double sum=0.0;*/
				
				
		      /*   for(int i=0;i<filesVals.length;i++){
					
					sum=sum+(filesVals[i]-mean)*(filesVals[i]-mean);
				}
					sd=Math.sqrt(sum/(filesVals.length-1));
					
					Arrays.sort(filesVals);*/
					/*for(int i=0;i<similarVals.length;i++){
						
						log.debug("Sorted similarVals "+i+"="+similarVals[i]);
					}*/
					
					/*double q2=filesVals[(int)filesVals.length/2];
					double q1=filesVals[(int)filesVals.length/4];
					double q3=filesVals[(int)3*filesVals.length/4];
					double R=(q3-q1)/1.34;
					//log.debug("q1="+q1+" q2="+q2+" q3="+q3);
					double esig=Math.min(sd,R);
					double hopt=(0.9*esig)/Math.pow((double)filesVals.length,5.0);*/
					//log.debug("sd="+sd+" R="+R+" hopt="+hopt);
					
					double hopt=0.1;
					double resolution=0.1;
					
					int range=(int)((max-min)/resolution);
					double[] fnDistance      = new double[ range ];
					Arrays.fill(fnDistance, 0);
					double x1=min;
				
			for(int i=0;i<range;i++)
			{
				
				for(int s=0; s<filesVals.length; s++){
					//fnSimilar[x] += fnSimilar[x] + Math.exp( -1*Math.pow( (double)x -similarVals[s],2) );
					//log.debug("x="+x+" : similarVals["+s+"]="+similarVals[s]);
					fnDistance[i] += fnDistance[i] + Math.exp( -1*Math.pow( (double)x1 -filesVals[s],2)/(2.0*hopt*hopt) );
				}
				/*for(int d=0; d<dissimilarVals.length; d++){
					//log.debug("x="+x+" : dissimilarVals["+d+"]="+dissimilarVals[d]);
					fnDissimilar[i] += Math.exp( -1*Math.pow( (double)x1 -dissimilarVals[d],2)/(2.0*hopt*hopt));
				}*/
				fnDistance[i]=1.0/(hopt*(filesVals.length)*Math.sqrt(2.0*Math.PI))*fnDistance[i];
				//fnDissimilar[i]=1.0/(hopt*(dissimilarVals.length)*Math.sqrt(2.0*Math.PI))*fnDissimilar[i];
				//perrorComplement += Math.max(fnDissimilar[x], fnSimilar[x]);
				//perrorComplement += Math.min(fnDissimilar[i], fnSimilar[i])*resolution;
				x1=x1+hopt;
				
				
			}	
					
					
		
		return fnDistance;
		
	}
	
	
	private double computeComparisonPEValue(ArrayList<String> similarCL, ArrayList<String> dissimilarCL){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		Iterator<String> s_itr                  = similarCL.iterator();
		Iterator<String> d_itr                  = dissimilarCL.iterator();

		//get all the values (distances)
		
		double[] similarVals    = new double[similarCL.size()];
		double[] dissimilarVals = new double[dissimilarCL.size()];

		double s_max = 0.0,ds_max=0.0;
		double s_min = 1000000000,ds_min=1000000000;
		//int numOfbin=10;
		
		int index=0;
		while(s_itr.hasNext()){
			String value =  ((Comparison) comparisonService.getComparison( s_itr.next() )).getValue();
			
			similarVals[index] = Double.parseDouble( value );
			//log.debug("similarVals["+index+"]"+similarVals[index]);
			if( similarVals[index] > s_max){ 
				s_max = similarVals[index];
			}
			if( similarVals[index] < s_min){ 
				s_min = similarVals[index];
			}
			index++;
		}
		index=0;
		while(d_itr.hasNext()){
			dissimilarVals[index] = Double.parseDouble( comparisonService.getComparison(d_itr.next()).getValue() );
			//log.debug("dissimilarVals["+index+"]"+dissimilarVals[index]);
			if( dissimilarVals[index] > ds_max){ 
				ds_max = dissimilarVals[index];
			}
			if( dissimilarVals[index] < ds_min){ 
				ds_min = dissimilarVals[index];
			}
			index++;
		}
		
		double max,min;
		max=Math.max(s_max,ds_max);
		min=Math.min(s_min,ds_min);
		
				
		//integration using the gaussian kernels over the distance support 
		//max+=2; //account for sigma in integration
		//min+=-2;
		double perrorComplement = 0;
		double resolution       = 0.10;
		//int range               = (int)((max-min)/resolution);
		//double[] fnSimilar      = new double[ range ];
		//double[] fnDissimilar   = new double[ range ];
		//Arrays.fill(fnSimilar, 0);
		//Arrays.fill(fnDissimilar, 0);
	// For finding optimal h for similar files	
		/*double sd,sfmean=0.0;
		
		for(int i=0;i<similarVals.length;i++){
			
			sfmean=sfmean+similarVals[i];
		}
		sfmean=sfmean/similarVals.length;
		double sfsum=0.0;
		
		
         for(int i=0;i<similarVals.length;i++){
			
			sfsum=sfsum+(similarVals[i]-sfmean)*(similarVals[i]-sfmean);
		}
			sd=Math.sqrt(sfsum/(similarVals.length-1));
			
			Arrays.sort(similarVals);*/
			/*for(int i=0;i<similarVals.length;i++){
				
				log.debug("Sorted similarVals "+i+"="+similarVals[i]);
			}*/
			
		
		   
		    /*double q2=similarVals[(int)similarVals.length/2];
			double q1=similarVals[(int)similarVals.length/4];
			double q3=similarVals[(int)3*similarVals.length/4];
			double R=(q3-q1)/1.34;
			log.debug("q1="+q1+" q2="+q2+" q3="+q3);
			double sf_esig=Math.min(sd,R);
			double sf_hopt=(0.9*sf_esig)/Math.pow((double)similarVals.length,5.0);
			log.debug("sd="+sd+" R="+R+" sf_hopt="+sf_hopt);
			
			// For finding optimal h for similar files	
			double dsfmean=0.0;
			
			for(int i=0;i<dissimilarVals.length;i++){
				
				dsfmean=dsfmean+dissimilarVals[i];
			}
			dsfmean=dsfmean/dissimilarVals.length;
			double dsfsum=0.0;
			
			
	         for(int i=0;i<dissimilarVals.length;i++){
				
				dsfsum=dsfsum+(dissimilarVals[i]-dsfmean)*(dissimilarVals[i]-dsfmean);
			}
				sd=Math.sqrt(dsfsum/(dissimilarVals.length-1));
				
				Arrays.sort(dissimilarVals);*/
				/*for(int i=0;i<dissimilarVals.length;i++){
					
					log.debug("Sorted dissimilarVals "+i+"="+dissimilarVals[i]);
				}*/
				
				/* q2=dissimilarVals[(int)dissimilarVals.length/2];
				 q1=dissimilarVals[(int)dissimilarVals.length/4];
				 q3=dissimilarVals[(int)3*dissimilarVals.length/4];
				 R=(q3-q1)/1.34;
				log.debug("q1="+q1+" q2="+q2+" q3="+q3);
				double dsf_esig=Math.min(sd,R);
				double dsf_hopt=(0.9*dsf_esig)/Math.pow((double)dissimilarVals.length,5.0);
				log.debug("sd="+sd+" R="+R+" dsf_hopt="+dsf_hopt);*/
			
				/*int srange=(int)((s_max-s_min)/sf_hopt);
				int dsrange=(int)((ds_max-ds_min)/dsf_hopt);*/
				
				double hopt=0.1;
				
					//hopt=Math.max(sf_hopt,dsf_hopt);
				
				
				int range=(int)((max-min)/resolution);
					//int range=(int)((max-min)/hopt);
				
				double[] fnSimilar      = new double[ range ];
				double[] fnDissimilar   = new double[ range ];
				Arrays.fill(fnSimilar, 0);
				Arrays.fill(fnDissimilar, 0);
				double x1=min;
			
		for(int i=0;i<range;i++)
		{
			
			for(int s=0; s<similarVals.length; s++){
				//fnSimilar[x] += fnSimilar[x] + Math.exp( -1*Math.pow( (double)x -similarVals[s],2) );
				//log.debug("x="+x+" : similarVals["+s+"]="+similarVals[s]);
				fnSimilar[i] += fnSimilar[i] + Math.exp( -1*Math.pow( (double)x1 -similarVals[s],2)/(2.0*hopt*hopt) );
			}
			for(int d=0; d<dissimilarVals.length; d++){
				//log.debug("x="+x+" : dissimilarVals["+d+"]="+dissimilarVals[d]);
				fnDissimilar[i] += Math.exp( -1*Math.pow( (double)x1 -dissimilarVals[d],2)/(2.0*hopt*hopt));
			}
			fnSimilar[i]=1.0/(hopt*(similarVals.length)*Math.sqrt(2.0*Math.PI))*fnSimilar[i];
			fnDissimilar[i]=1.0/(hopt*(dissimilarVals.length)*Math.sqrt(2.0*Math.PI))*fnDissimilar[i];
			//perrorComplement += Math.max(fnDissimilar[x], fnSimilar[x]);
			perrorComplement += Math.min(fnDissimilar[i], fnSimilar[i])*resolution;
			x1=x1+hopt;
			
			
		}	
		
		
		/*double perrorComplement=0.0;
		int range=(int)((max-min)/resolution);
		double[] fnSimilar      = new double[ range ];
		double[] fnDissimilar   = new double[ range ];
		Arrays.fill(fnSimilar, 0);
		Arrays.fill(fnDissimilar, 0);
		//double x1=min;
		
		fnSimilar=estimatePDensityfunction(similarVals,max,min);
		
	//	for(int i=0;i<range;i++){
	//		log.debug("eDensity fnSimilar["+i+"]="+fnSimilar[i]);
	//	}
	//	for(int i=0;i<range;i++){
	//		log.debug("eDensity fnDissimilar["+i+"]="+fnDissimilar[i]);
	//	}
		fnDissimilar=estimatePDensityfunction(dissimilarVals,max,min);
		for(int i=0;i<range;i++){
			
			perrorComplement += Math.min(fnDissimilar[i], fnSimilar[i])*resolution;
			
		}
		/*for( int x=0; x<range; x++){
			
			for(int s=0; s<similarVals.length; s++){
				//fnSimilar[x] += fnSimilar[x] + Math.exp( -1*Math.pow( (double)x -similarVals[s],2) );
				//log.debug("x="+x+" : similarVals["+s+"]="+similarVals[s]);
				fnSimilar[x] += fnSimilar[x] + Math.exp( -1*Math.pow( (double)x -similarVals[s],2)/(2.0*sf_hopt*sf_hopt) );
			}
			for(int d=0; d<dissimilarVals.length; d++){
				//log.debug("x="+x+" : dissimilarVals["+d+"]="+dissimilarVals[d]);
				fnDissimilar[x] += Math.exp( -1*Math.pow( (double)x -dissimilarVals[d],2)/(2.0*dsf_hopt*dsf_hopt));
			}
			fnSimilar[x]=1.0/(sf_hopt*(similarVals.length)*Math.sqrt(2.0*Math.PI))*fnSimilar[x];
			fnDissimilar[x]=1.0/(dsf_hopt*(dissimilarVals.length)*Math.sqrt(2.0*Math.PI))*fnDissimilar[x];
			//perrorComplement += Math.max(fnDissimilar[x], fnSimilar[x]);
			perrorComplement += Math.min(fnDissimilar[x], fnSimilar[x])*resolution;
			//perrorComplement += Math.min(fnDissimilar[x], fnSimilar[x])*hopt;
			
		}*/
				
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