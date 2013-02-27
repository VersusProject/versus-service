
/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgment if the
 * software is used.
 *
 *
 *  @author  Benjamin Long, blong@nist.gov
 *  @version 1.1
 *    
 */

package gov.nist.itl.ssd.similarity.test;

import java.io.File;

import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.ThreeDimensionalDoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.extract.impl.ArrayFeatureExtractor;
import edu.illinois.ncsa.versus.extract.impl.GrayscaleHistogramExtractor;
import edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import gov.nist.itl.versus.similarity.comparisons.adapter.impl.LabeledImageObjectAdapter;
import gov.nist.itl.versus.similarity.comparisons.descriptor.impl.LabeledThreeDimensionalDoubleArrayFeature;
import gov.nist.itl.versus.similarity.comparisons.exception.ImageCompatibilityException;
import gov.nist.itl.versus.similarity.comparisons.exception.SWIndependenceException;
import gov.nist.itl.versus.similarity.comparisons.exception.SingularityTreatmentException;
import gov.nist.itl.versus.similarity.comparisons.extract.impl.LabeledArrayFeatureExtractor;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.AdjustedRandIndexMeasure;

public class VersusMeasureFx<
		A1 extends ImageObjectAdapter,
		A2 extends ImageObjectAdapter,
		X1 extends Extractor,
		X2 extends Extractor,
		D1 extends Descriptor,
		D2 extends Descriptor,
		M extends Measure,
		MR,
		OR> 
{
	
	protected Exception e;
	protected Boolean raisedException;
	
	public VersusMeasureFx(){ 
			super();
	}
	
	public Exception e(){ return e; }
	public void e(Exception e){ this.e=e; }
	public Boolean raisedException(){ return raisedException;}
	public void raisedException( Boolean v ){ this.raisedException=v; }

	public Double op( Measure measure, String fileName1, String fileName2, A1 a1, A2 a2, X1 x1, X2 x2, D1 d1, D2 d2  )
	{
		File		file1;
		File		file2;
		A1 			adapter1 = a1;
		A2 			adapter2 = a2;
		X1 			extractor1 = x1;
		X2 			extractor2 = x2;
		D1 			descriptor1 = d1;
		D2 			descriptor2 = d2;
		Similarity	measureResult = null;
		Double		opResult = null;		
		
		try 
		{	
			// files
			file1 =  new File(fileName1) ;
				if ( file1 == null  )
					throw new ImageCompatibilityException("failed to create file object for file1");
				
			file2 = new File(fileName2) ;
				if ( file2 == null )
					throw new ImageCompatibilityException("failed to create file object for file2");
		
			// adapters
				//try {
				/////////////////////
					if ( ((A1)adapter1) instanceof ImageObjectAdapter ) {
						adapter1 = (A1)new ImageObjectAdapter();
						((ImageObjectAdapter)adapter1).load(file1);
					}
					else if( ((A1)adapter1) instanceof LabeledImageObjectAdapter ) {
						adapter1 = (A1)new ImageObjectAdapter();
						((LabeledImageObjectAdapter)adapter1).load(file1);
					}
					else {
						adapter1 = null;
						throw new SWIndependenceException("could not complete adapter instantiation and load (adapter1)");
					}
					
					if ( ((A2)adapter2) instanceof ImageObjectAdapter ) {
						adapter2 = (A2)new ImageObjectAdapter();
						((ImageObjectAdapter)adapter2).load(file2);
					}
					else if( ((A2)adapter2) instanceof LabeledImageObjectAdapter ) {
						adapter2 = (A2)new ImageObjectAdapter();
						((LabeledImageObjectAdapter)adapter2).load(file2);
					}
					else {
						adapter2 = null;
						throw new SWIndependenceException("could not complete adapter instantiation and load (adapter2)");
						
					}
					
					if ( adapter1 == null )
						throw new SWIndependenceException("failed to create Adapter adapter for file1");
					
					if ( adapter2 == null )
						throw new SWIndependenceException("failed to create Adapter adapter for file2");
					
		//	}
		//	catch( Exception e0 ) {
		//		e( e0 );
		//		throw new SWIndependenceException("failed to load files via adapters");
		//	}
		
			// extractors
		//	try {	
		//////////////////////////////			
			
					if ( ((X1)extractor1) instanceof RGBHistogramExtractor ) {
						extractor1 =(X1) new RGBHistogramExtractor();
					}
					else if ( ((X1)extractor1) instanceof GrayscaleHistogramExtractor ) {
						extractor1 = (X1)new GrayscaleHistogramExtractor();
					}
					else if ( ((X1)extractor1) instanceof ArrayFeatureExtractor ) {
						extractor1 = (X1)new ArrayFeatureExtractor();
					}
					else if ( ((X1)extractor1) instanceof LabeledArrayFeatureExtractor ) {
						extractor1 = (X1)new LabeledArrayFeatureExtractor();
					}			
					else {
						extractor1 = null;
						throw new SWIndependenceException("could not complete descriptor instantiation and extraction (descriptor1)");
					}
					
					if ( ((X2)extractor2) instanceof RGBHistogramExtractor ) {
						extractor2 =(X2) new RGBHistogramExtractor();
					}
					else if ( ((X2)extractor2) instanceof GrayscaleHistogramExtractor ) {
						extractor2 = (X2)new GrayscaleHistogramExtractor();
					}
					else if ( ((X2)extractor2) instanceof ArrayFeatureExtractor ) {
						extractor2 = (X2)new ArrayFeatureExtractor();
					}
					else if ( ((X2)extractor2) instanceof LabeledArrayFeatureExtractor ) {
						extractor2 = (X2)new LabeledArrayFeatureExtractor();
					}				
					else {
						extractor2 = null;
						throw new SWIndependenceException("could not complete descriptor instantiation and extraction (descriptor2)");
					}
					
					if ( extractor1 == null )
						throw new SWIndependenceException("failed to create Extractor object for extractor1");
			
					if ( extractor2 == null )
						throw new SWIndependenceException("failed to create Extractor object for extractor2");
		
					if ( ((D1)descriptor1) instanceof RGBHistogramDescriptor ) {
						descriptor1 =(D1) new RGBHistogramDescriptor();				
					}
					else if ( ((D1)descriptor1) instanceof GrayscaleHistogramDescriptor ) {
						descriptor1 = (D1)new GrayscaleHistogramDescriptor();
					}
					else if ( ((D1)descriptor1) instanceof ThreeDimensionalDoubleArrayFeature ) {
						descriptor1 = (D1)new ThreeDimensionalDoubleArrayFeature();
					}
					else if ( ((D1)descriptor1) instanceof LabeledThreeDimensionalDoubleArrayFeature ) {
						descriptor1 = (D1)new LabeledThreeDimensionalDoubleArrayFeature();
					}			
					else {
						descriptor1 = null;	
						throw new SWIndependenceException("could not complete descriptor instantiation and extraction (descriptor1)");	
					}
					
					if ( ((D2)descriptor2) instanceof RGBHistogramDescriptor ) {
						descriptor2 =(D2) new RGBHistogramDescriptor();
					}
					else if ( ((D2)descriptor2) instanceof GrayscaleHistogramDescriptor ) {
						descriptor2 = (D2)new GrayscaleHistogramDescriptor();
					}
					else if ( ((D2)descriptor2) instanceof ThreeDimensionalDoubleArrayFeature ) {
						descriptor2 = (D2)new ThreeDimensionalDoubleArrayFeature();
					}
					else if ( ((D2)descriptor2) instanceof LabeledThreeDimensionalDoubleArrayFeature ) {
						descriptor2 = (D2)new LabeledThreeDimensionalDoubleArrayFeature();
					}				
					else {
						descriptor2 = null;
						throw new SWIndependenceException("could not complete descriptor instantiation and extraction (descriptor2)");
					}
					
					if ( descriptor1 == null || extractor1 == null || adapter1 == null )
						throw new SWIndependenceException("could not extract Descriptor1 feature1 via extractor1 and adapter1");
		
					if ( descriptor2 == null || extractor2 == null || adapter2 == null )
						throw new SWIndependenceException("could not extract Descriptor2 feature2 via extractor2 and adapter2");
					
			// descriptors

					descriptor1 = (D1)extractor1.extract( adapter1 );
					descriptor2 = (D2)extractor2.extract( adapter2 );
					
					if ( descriptor1 == null )
						throw new SWIndependenceException("failed to extract Descriptor feature1 via extractor1");

					if ( descriptor2 == null )
						throw new SWIndependenceException("failed to extract Descriptor feature2 via extractor2");
					
			//}
			//catch( Exception e1) {				
			//	e( e1 );
			//	throw new SWIndependenceException("extraction failed : " + e1.getMessage());
			//}
			

			// measure
			//try {			
			///////////////////////////////////	
				if ( measure == null )
					throw new SWIndependenceException("failed to create Measure object for measure");
			
				measureResult = measure.compare( descriptor1, descriptor2 );

				// result
				if ( measureResult == null )
					throw new SingularityTreatmentException("received null comparison result");
				
				if ( measureResult != null )
					opResult = measureResult.getValue();
				
				if ( opResult != null ) {
					//System.out.println( fileName1+ "\n" + fileName2 + "\n" + opResult );
				}		
				
			//}
			//catch( Exception e2 ) {
			//	e( e2 );
			//	throw new SWIndependenceException("failed to successfully perform comparison");
			//}
		
		}
		catch( Exception e ) {
			e( e );			
		}
		
		return opResult;		
	}
	
	public static void main(String[] args ) 
	{		
		VersusMeasureFx<
			ImageObjectAdapter,ImageObjectAdapter,
			ArrayFeatureExtractor,ArrayFeatureExtractor,
			ThreeDimensionalDoubleArrayFeature,ThreeDimensionalDoubleArrayFeature,
			AdjustedRandIndexMeasure,
			Similarity,
			Double> f = new VersusMeasureFx();
			String fileName1 = "test\\data\\metric_recommendation_sys\\Translation Simulation\\image1.tif";
			String fileName2 = "test\\data\\metric_recommendation_sys\\Translation Simulation\\image2_021.tif";				
			f.op( 
					new AdjustedRandIndexMeasure(), 
					fileName1, 
					fileName2,
					new ImageObjectAdapter(),
					new ImageObjectAdapter(),
					new ArrayFeatureExtractor(),
					new ArrayFeatureExtractor(),
					new ThreeDimensionalDoubleArrayFeature(),
					new ThreeDimensionalDoubleArrayFeature()
				);		
	}

}
