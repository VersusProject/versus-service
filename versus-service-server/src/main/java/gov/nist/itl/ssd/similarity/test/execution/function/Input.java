package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.versus.similarity.comparisons.MathOpsE;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;

public class Input<A> 
{
	
	protected String type;
	protected String name;

	public Input(){ super(); init(); }
	public Input(String type){ super(); type(type); init(); }
	public Input(String name, String type){ super(); name(name); type(type); init(); }
	
	public void init() {
	
	}
	
	public void type(String v){this.type=type;}
	public String type(){ return type; }
	
	public void name(String v){this.name=name;}
	public String name(){ return name; }

	public void reset(){
		init();
	}
}

