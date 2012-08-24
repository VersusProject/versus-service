package gov.nist.itl.ssd.similarity.test.execution.function;

import fj.data.Array;
import gov.nist.itl.versus.similarity.comparisons.MathOpsE;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import static gov.nist.itl.ssd.similarity.test.config.Config.mops;

public class Component<A> 
{
	protected A component;
	
	protected String type;
	protected String name;
	
	// auxiliary information
	protected TA<String> 	measureCategories; 
	protected String 		measureOpImpl;
	protected TA<String> 	measureOpEquivalents;
	protected TA<String> 	dependentOperations;
	protected TA<String> 	properties;
	protected TA<String> 	supportedFaultCategories;

	protected Boolean 		raisedException = false;
	protected FaultTracker 	tracker;
	
	protected TA<Component> compatibilities = new TA();
	
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
	
	public Component(){ super(); name(""); type(""); initialize(); }
	public Component(String type){ super(); type(type); initialize(); }
	public Component(String type, A c){ super(); type(type); component(c); initialize(); }
	public Component(String name, String type){ super(); name(name); type(type); initialize(); }
	
	public void initialize() {
		tracker = new FaultTracker(name);
		raisedException(false);
		initComponent();
	}
	
	public Boolean isCompatibleWith( Component c ) { return (compatibilities.exists(c)) ? true : false;}
	
	public Boolean isCompatibleWith( String name ) {
		int len = compatibilities.length();
		Component c = null;
		String nm = "";
		String tp = "";
		for (int i=0; i < len; i++) {
			c = (Component)compatibilities.get(i);
			nm = (String)c.name();
			tp = (String)c.type();
			if ( tp == null || name==null ) continue;
			if ( tp.compareTo(name) == 0 ) return true;			
		}
		return false;
	}
	
	public void component(A v){this.component=v;}
	public A component(){ return this.component;}
	
	public void initComponent() {
		// fill in dynamically at instantiation
	}
	
	public void addCompatibility( Component c ) {
		compatibilities.add(c);
	}
	
	public void addCompatibilities( Array<Component> components ) {
		compatibilities.add(components);
	}
	
	public void type(String v){this.type=v;}
	public String type(){ return type; }	
	public void name(String v){this.name=v;}
	public String name(){ return name; }
	
	public void tracker(FaultTracker v){this.tracker=tracker;}
	public FaultTracker tracker(){ return tracker;}
	
	public Boolean raisedException(){return raisedException;}
	public void raisedException(Boolean v){this.raisedException=v;}
	
	public MathOpsE m(){ return mops; }
	
	public String toString() {
		String s = "";
		String SEP = "\t";
		s += "name = " + ((name()!=null) ? name() : "") + SEP;
		s += "type = " + ((type()!=null) ? type() : "") + SEP;
		return s;
	}	

	public void reset(){
		initialize();
	}
}

