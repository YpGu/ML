/**
	SparseMatrix.java: store the data in a sparse matrix
**/

import java.util.*;
import java.io.*;

public class SparseMatrix
{
	private Map<String, Map<String, Double>> mat;
	private Set<String> xDict;
	private Set<String> yDict;
	private Map<String, Set<String>> outNeighborSet;
	private Map<String, Set<String>> inNeighborSet;
	private Map<String, Set<String>> outNeighborComplementSet;		// will not contain x itself 
	private Map<String, Set<String>> inNeighborComplementSet;
	private Map<String, Integer> labelSet;

	public SparseMatrix() {
		mat = new HashMap<String, Map<String, Double>>();
		xDict = new HashSet<String>();
		yDict = new HashSet<String>();
		outNeighborSet = new HashMap<String, Set<String>>();
		inNeighborSet = new HashMap<String, Set<String>>();
		outNeighborComplementSet = new HashMap<String, Set<String>>();
		inNeighborComplementSet = new HashMap<String, Set<String>>();
		labelSet = new HashMap<String, Integer>();
	}

	public Map<String, Map<String, Double>>
	getMat() {
		return mat;
	}

	public Set<String>
	getDict() {
		return xDict;
	}

	public double 
	getElement(String row, String col) {
		try {
			double res = mat.get(row).get(col);
			return res;
		}
		catch (java.lang.NullPointerException e) {
			return 0;
		}
	}

	public void 
	set(String row, String col, double val) {
		if (!mat.containsKey(row)) {
			Map<String, Double> m = new HashMap<String, Double>();
			mat.put(row, m);
		}
		mat.get(row).put(col, val);
		return;
	}

	public Map<String, Integer>
	getLabelSet() {
		return labelSet;
	}

	public void
	setLabel(String doc, int nClass) {
		labelSet.put(doc, nClass);
		return;
	}

	public Set<String> 
	getRow(String row) {
		return outNeighborSet.get(row);
	}

	public Set<String> 
	getColumn(String col) {
		return inNeighborSet.get(col);
	}

	public Set<String> 
	getRowComplement(String row) {
		return outNeighborComplementSet.get(row);
	}

	public Set<String> 
	getColumnComplement(String col) {
		return inNeighborComplementSet.get(col);
	}

	public int
	getSize() {
		int size = 0;
		for (Map.Entry<String, Map<String, Double>> e: mat.entrySet()) {
			size += e.getValue().size();
		}
		return size;
	}

	public int
	getDictSize() {
		return xDict.size();
	}

	public void 
	update(Set<String> wordDict) {
		// init dict set
		for (Map.Entry<String, Map<String, Double>> e: mat.entrySet()) {
			String x = e.getKey();
			xDict.add(x);
		}
		yDict = wordDict;

		// init neighbor set
		for (String s: xDict) {
			outNeighborSet.put(s, new HashSet<String>());
			outNeighborComplementSet.put(s, new HashSet<String>());
		}

		// update neighbor set
		for (Map.Entry<String, Map<String, Double>> e: mat.entrySet()) {
			String x = e.getKey();
			Map<String, Double> m = e.getValue();
			for (Map.Entry<String, Double> f: m.entrySet()) {
				String y = f.getKey();
				double v = f.getValue();

				if (wordDict.contains(y)) {
					outNeighborSet.get(x).add(y);
//					Set<String> ys = outNeighborSet.get(x);
//					ys.add(y);
//					outNeighborSet.put(x, ys);
				}
			}
		}

/*		// update non-neighbor set
		Set<String> p = new HashSet<String>();
		for (String s: xDict) {
			Set<String> outNS = outNeighborSet.get(s);
			p = outNS;

			yDict.removeAll(outNS);
			System.out.println("SS SUM = " + (yDict.size() + outNS.size()));

			outNeighborComplementSet.put(s, yDict);
			System.out.println("A = " + outNeighborComplementSet.get(s).size());

			yDict.addAll(outNS);
			System.out.println("B = " + outNeighborComplementSet.get(s).size());

			int v = outNeighborComplementSet.get(s).size() + outNeighborSet.get(s).size();
			Scanner sc = new Scanner(System.in);
			int gu = sc.nextInt();
			if (v != yDict.size()) {
				for (String ss: outNS) {
					if (!yDict.contains(ss)) {
						System.out.println(ss + " does not in yDict!");
						int gu = sc.nextInt();
					}
				}
				System.out.println("sum = " + v + " voc size = " + yDict.size());
				int gu = sc.nextInt();
			}
		}
*/
	}
}

