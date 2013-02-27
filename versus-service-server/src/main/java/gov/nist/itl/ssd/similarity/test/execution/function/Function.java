package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;

import fj.data.Array;
import gov.nist.itl.versus.similarity.comparisons.MathOpsE;
import static gov.nist.itl.ssd.similarity.test.config.Config.mops;

public class Function<A>
{
	protected A result;
	protected String type;
	protected String name;
	protected Boolean raisedException;
	protected FaultTracker tracker;
	
	// auxiliary information
	protected TA<String> 	measureCategories; 
	protected String 		measureOpImpl;
	protected TA<String> 	measureOpEquivalents;
	protected TA<String> 	dependentOperations;
	protected TA<String> 	properties;
	protected TA<String> 	supportedFaultCategories;	
	protected TA unitTests;
	protected TA failureModeTests;
	
	public Function(){ super(); initialize(); }
	public Function(String type){ super(); type(type); initialize(); }
	public Function(String name, String type){ super(); name(name); type(type); initialize(); }
		
	public void initialize() {
		tracker = new FaultTracker(name);
		raisedException(false);
		e(null);		
		measureCategories = new TA<String>();
		measureOpImpl="";
		measureOpEquivalents  = new TA<String>();
		dependentOperations   = new TA<String>();
		properties   		  = new TA<String>();
		supportedFaultCategories = new TA<String>();
		unitTests = new TA();
		failureModeTests = new TA();
		raisedException = false;
		 
		initFunction();
	}
	
	public TA unitTests(){ return unitTests;}
	public void unitTests(TA v){ this.unitTests=v;}
	public void addUnitTest( Object v ){ unitTests.add(v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }
	public TA failureModeTests(){ return failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=v;}
	public void addFailureModeTest( Object v ){ failureModeTests.add(v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	public A result(){return result;}
	public void result(A a){this.result=a;}
	protected Exception e;
	public Exception e(){return e;}
	public void e(Exception e){this.e=e;}
	public void type(String v){this.type=type;}
	public String type(){ return type; }	
	public void name(String v){this.name=name;}
	public String name(){ return name; }		
	
	public TA<String> measureCategories(){ return measureCategories;}
	public void measureCategories( TA<String> v ){this.measureCategories=v;}
	public String measureOpImpl(){return measureOpImpl;}
	public void measureOpImpl(String v){this.measureOpImpl=v;}
	public TA<String> measureOpEquivalents(){return measureOpEquivalents;}
	public void measureOpEquivalents(TA<String> v){this.measureOpEquivalents=v;}
	public TA<String> dependentOperations(){return dependentOperations;}
	public void dependentOperations(TA<String> v){this.dependentOperations=v;}
	public TA<String> supportedFaultCategories(){ return supportedFaultCategories;}
	public void supportedFaultCategories(TA<String> v){this.supportedFaultCategories=v;}


	public void addMeasureCategory( String v ){ measureCategories.add(v); }
	public void addMeasureCategories( Array<String> v ){ measureCategories.add(v); }	
	public void addMeasureOpEquivalent( String v ) { measureOpEquivalents.add(v); }
	public void addMeasureOpEquivalents( Array<String> v) { measureOpEquivalents.add(v); }
	public void addDependentOp( String v ){ dependentOperations.add(v); }
	public void addDependentOps( Array<String> v ) {dependentOperations.add(v); }
	public void addProperty( String v ) { properties.add(v); }
	public void addProperties( Array<String> v ) { properties.add(v); }
	public void addFaultCategory( String v ) { supportedFaultCategories.add(v); }
	public void addFaultCategories( Array<String> v ) { supportedFaultCategories.add(v); }
	
	public Boolean isMeasureCategory( String v) { return (measureCategories.exists(v)) ? true : false;}
	public Boolean isMeasureOpEquivalent(String v) { return (measureOpEquivalents.exists(v)) ? true : false;}
	public Boolean isDependentOp(String v) { return (dependentOperations.exists(v)) ? true : false;}
	public Boolean isSupportedProperty(String v) { return (properties.exists(v)) ? true : false;}
	public Boolean isMeasureOpImpl(String v) { return (v.compareTo(measureOpImpl)==0) ? true : false;}
	public Boolean isSupportedFaultCategory(String v) { return (supportedFaultCategories.exists(v)) ? true : false;}
		
	public void tracker(FaultTracker v){this.tracker=tracker;}
	public FaultTracker tracker(){ return tracker;}	
	public Boolean raisedException(){return raisedException;}
	public void raisedException(Boolean v){this.raisedException=v;}	
	public MathOpsE m(){ return mops; }
	public A getResult(){ return result(); }
	public Exception getException() { return e(); }

	public void handleException(Exception e) {
		e(e);
		tracker.track(e);
		raisedException(true);
	}
	
	public void reset(){
		initialize();
	}
	
	public void initFunction() {
		// fill in dynamically at instantiation
	}	
	
	public String toString() {
		String s = "";
		String SEP = "\t";
		s += "name = " + ((name()!=null) ? name() : "") + SEP;
		s += "type = " + ((type()!=null) ? type() : "") + SEP;
		return s;
	}
	
	public static void main( String[] args ) {
		System.out.println("Function");
		Function<Double> f = new Function<Double>();		
	}
}

