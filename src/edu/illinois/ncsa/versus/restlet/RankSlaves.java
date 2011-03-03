/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Rank slaves ordered by
 * 
 * @author lmarini
 * 
 */
public class RankSlaves {

	public static List<Slave> rank(Collection<Slave> list) {
		ArrayList<Slave> arrayList = new ArrayList<Slave>(list);
		return arrayList;
	}
}
