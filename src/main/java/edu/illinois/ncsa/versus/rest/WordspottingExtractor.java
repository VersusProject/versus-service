/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import kgm.image.ImageUtility;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.HasPixels;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.ncsa.handwriting.WordSpotting;

/**
 * Word spot on the cell.
 * 
 * @author Luigi Marini
 * 
 */
public class WordspottingExtractor implements Extractor {

	public Descriptor extract(Adapter adapter) throws Exception {
		// TODO Auto-generated method stub
		if (adapter instanceof BufferedImageAdapter) {
			BufferedImageAdapter biAdapter = (BufferedImageAdapter)adapter;
			BufferedImage image = biAdapter.getImage();
			// load image
			int[][] subImage = ImageUtility.image2argb(image);
			double[] grayscaleSubImage = ImageUtility.argb2g(subImage);
			double[] signature = WordSpotting.getSignature_Rath04(
					grayscaleSubImage, subImage[0].length, subImage.length);
			DoubleArrayFeature arrayOfDoubles = new DoubleArrayFeature(signature);
			return arrayOfDoubles;
		} else {
			throw(new Exception("Need bufferedimageadapter."));
		}
	}

	@Deprecated
	public Adapter newAdapter() {
		return new BufferedImageAdapter();
	}

	public String getName() {
		return "Wordspotting";
	}

	public Set<Class<? extends Adapter>> supportedAdapters() {
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		adapters.add(HasPixels.class);
		return adapters;
	}

	public Class<? extends Descriptor> getFeatureType() {
		return DoubleArrayFeature.class;
	}

	public boolean hasPreview() {
		// TODO Auto-generated method stub
		return false;
	}

	public String previewName() {
		// TODO Auto-generated method stub
		return null;
	}

}
