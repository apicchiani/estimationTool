/**
 * @author Andrea Picchiani
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */

package common;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.StratifiedRemoveFolds;

public class FoldsDT {

	/** Number of fold in which to divide the dataset */
	private int numFoldDT = 0;
	
	/** The array that contains the fold with which to create the TS and the ES */
//	private Instances[] foldDT = new Instances[numFoldDT];
	private ArrayList<Instances> arrayFoldDT = null;
	
	private GregorianCalendar calendar = null;
	private Random random = null;
	private int posRnd = 0;
	private Instance instDT = null;
	private Instances dataset = null;
	private Instances fold = null;
	private Attribute classIndexAttribute = null;
	private FastVector attInfo = null;
	private double seed = 0;
	private int numInstancesInFold = 0;
	
	public FoldsDT(int numFold, Instances dataset) {
		this.numFoldDT = numFold;
		this.dataset = dataset;
		numInstancesInFold = dataset.numInstances()/numFoldDT;
		createEmptyFolds();
	}
	
	private void createEmptyFolds() {
		this.arrayFoldDT = new ArrayList<Instances>();
		
		// Creates attInfo, contains the informations for attributes
		attInfo = new FastVector();
		classIndexAttribute = dataset.attribute(0);
		attInfo = new FastVector();
		attInfo.addElement(classIndexAttribute);
		attInfo.addElement(dataset.attribute(1));
		
		for(int i=0; i<numFoldDT; i++) {
			arrayFoldDT.add(new Instances(dataset.relationName() +"_Fold" +i,attInfo,0));
		}
	}
	
	public void createFolds() {
//		System.out.println("[FoldsDT - createFolds]");
		calendar = new GregorianCalendar();
		seed = calendar.getTimeInMillis() * Math.random();
		random = new Random((long) seed);
		dataset.randomize(random);
		
		int i=0;
		while(0<dataset.numInstances()) {
			posRnd = random.nextInt(dataset.numInstances());
			instDT = dataset.instance(posRnd);
			dataset.delete(posRnd);
			fold = arrayFoldDT.get(i%numFoldDT);
			fold.add(instDT);	
			i++;
		}
	}
	
	
	
	/*
	public void createBalancedFolds() {
		StratifiedRemoveFolds stratified = new StratifiedRemoveFolds();
		dataset.setClassIndex(0);
//		System.out.println(dataset);
		
		try {
			stratified.setInputFormat(dataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < dataset.numInstances(); i++) {
		    stratified.input(dataset.instance(i));
		}
		stratified.batchFinished();
//		
		Instance processed;
		for(int indexFold=0; indexFold<numFoldDT; indexFold++) {
			for (int i = 0; i < dataset.numInstances(); i++) {
			    stratified.input(dataset.instance(i));
			}
			stratified.batchFinished();
			
			fold = this.arrayFoldDT.get(indexFold);
			stratified.setFold(indexFold+1);
//			try {
//				fold = Filter.useFilter(dataset, stratified);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			while ((processed = stratified.output()) != null) {
				fold.add(processed);
			}
			System.out.println(fold);
		}


//		fold = stratified.getOutputFormat();
		
		
	}
	*/
	
	public void createStratifiedFolds() {
//		System.out.println("[FoldsDT - createStratifiedFolds]");
		int numInstanceTrue;
		int numInstanceFalse;
		int ratioTrueFalse = 0;
		int ratioFalseTrue = 0;
		Instances instTrue;
		Attribute attribute;
		
		// Creates attInfo, contains the informations for attributes
		attInfo = new FastVector();
		classIndexAttribute = dataset.attribute(0);
		attInfo.addElement(classIndexAttribute);
		for(int i=1; i<dataset.numAttributes(); i++) {
			attribute = dataset.attribute(i);
			if(!attribute.isNumeric()) {
				attribute = new Attribute("TruthNumeric");
			}
			attInfo.addElement(attribute);
		}
		
		// Creates attInfo, contains the informations for attributes
//		attInfo = new FastVector();
//		classIndexAttribute = dataset.attribute(0);
//		attInfo = new FastVector();
//		attInfo.addElement(classIndexAttribute);
//		attInfo.addElement(dataset.attribute(1));
		
		instTrue = new Instances("instTrue",attInfo,0);
		
		for(int i=0; i<dataset.numInstances(); i++) {
			instDT = dataset.instance(i); 
			if(instDT.value(0) == 1) {
				instTrue.add(instDT);
				dataset.delete(i);
				if(i>0) { i--; }
			}
		}
		
		numInstanceTrue = instTrue.numInstances();
		numInstanceFalse = dataset.numInstances();
		if(numInstanceTrue>numInstanceFalse) {
			ratioTrueFalse = numInstanceTrue/numInstanceFalse;
		} else if(numInstanceTrue<numInstanceFalse){
			ratioFalseTrue = numInstanceFalse/numInstanceTrue;
		} else {
			ratioTrueFalse = ratioFalseTrue = 1;
		}
		
//		System.out.println("Numeri istanze in fold: " +numInstancesInFold);
//		System.out.println(instTrue);
//		System.out.println(dataset);
		for(int indexFold=0; indexFold<numFoldDT; indexFold++) {
			fold = arrayFoldDT.get(indexFold);
			
			if(ratioFalseTrue!=0) {
				createFold(dataset,instTrue, ratioFalseTrue);
			} else {
				createFold(instTrue,dataset, ratioTrueFalse);	
			}
	
		}
		
		fold = arrayFoldDT.get(numFoldDT-1);
		for(int size=fold.numInstances(); (size<numInstancesInFold && dataset.numInstances()!=0); size++) {
			fold.add(dataset.instance(0));
			dataset.delete(0);
		}
		
		for(int i=0; i<dataset.numInstances(); i++) {
			fold = arrayFoldDT.get(i%numFoldDT);
			fold.add(dataset.instance(i));
		}
		
//		for(int indexFold=0; indexFold<numFoldDT; indexFold++) {
//			fold = arrayFoldDT.get(indexFold);
//			System.out.println("num:" +fold.numInstances());
//		
//		}
		
		
	}
	
	private void createFold(Instances instMaggiore, Instances instMinore, int ratio) {
//		System.out.println("[FoldsDT - createFold] ");
		//		System.out.println(instMaggiore.numInstances());
//		System.out.println(instMinore.numInstances());
//		System.out.println(ratio);
//		System.out.println(instMaggiore);
		int i=0;
		int numInstancesMinore = 1;
		int numInstancesMaggiore = 1;
		
		calendar = new GregorianCalendar();
		seed = calendar.getTimeInMillis() * Math.random();
		random = new Random((long) seed);
		
		while(i<numInstancesInFold) {
			for(int j=0; j<ratio; j++) {
				numInstancesMaggiore = instMaggiore.numInstances();
				posRnd = random.nextInt(numInstancesMaggiore);
				instDT = instMaggiore.instance(posRnd);
//				System.out.println("istanza maggiore: " +instDT);
				fold.add(instDT);
				instMaggiore.delete(posRnd);
				i++;
				if(i>=numInstancesInFold) {
					break;
				}
			}
			if(i>=numInstancesInFold) {
				break;
			}
			
			
			numInstancesMinore = instMinore.numInstances();
			if(numInstancesMinore!=0) {
				posRnd = random.nextInt(numInstancesMinore);
				instDT = instMinore.instance(posRnd);
//				System.out.println("istanza minore: " +instDT);
				fold.add(instDT);
				instMinore.delete(posRnd);
				i++;
			} else {
				break;
			}

		}
		
//		while(i<numInstancesInFold) {
//			numInstancesMaggiore = instMaggiore.numInstances();
//			System.out.println("maggiore: " +numInstancesMaggiore);
//			if(numInstancesMaggiore!=0) {
//				for(int j=0; j<ratio; j++) {
//					posRnd = random.nextInt(numInstancesMaggiore);
//					instDT = instMaggiore.instance(posRnd);
//									System.out.println("istanza maggiore: " +instDT);
//					fold.add(instDT);
//					instMaggiore.delete(posRnd);
//					i++;
//					if(i>=numInstancesInFold) {
//						break;
//					}
//					
//				}
//				
//				if(i>=numInstancesInFold) {
//					break;
//				}
//			}
//			
//			numInstancesMinore = instMinore.numInstances();
//			System.out.println("minore: " +numInstancesMinore);
//			if(numInstancesMinore!=0) {
//				posRnd = random.nextInt(numInstancesMinore);
//				instDT = instMinore.instance(posRnd);
//				System.out.println("istanza minore: " +instDT);
//				fold.add(instDT);
//				instMinore.delete(posRnd);
//				i++;
//			}
//			
//		}
	}
	
	public void createBalancedFolds() {
//		System.out.println("[FoldsDT - createBalancedFolds]");
		Instances instTrue;
		Attribute attribute;
		
		// Creates attInfo, contains the informations for attributes
		attInfo = new FastVector();
		classIndexAttribute = dataset.attribute(0);
		attInfo.addElement(classIndexAttribute);
		for(int i=1; i<dataset.numAttributes(); i++) {
			attribute = dataset.attribute(i);
			if(!attribute.isNumeric()) {
				attribute = new Attribute("TruthNumeric");
			}
			attInfo.addElement(attribute);
		}

		// For print attInfro vector of attribute
//		for(int i=0; i<attInfo.size(); i++) {
//			System.out.println("[FoldsDT - createBalancedFolds] Attribute " +attInfo.elementAt(i).toString());
//		}
		
		// Creates attInfo, contains the informations for attributes
//		attInfo = new FastVector();
//		classIndexAttribute = dataset.attribute(0);
//		attInfo = new FastVector();
//		attInfo.addElement(classIndexAttribute);
//		attInfo.addElement(dataset.attribute(1));
		
		instTrue = new Instances("instTrue",attInfo,0);
		
		calendar = new GregorianCalendar();
		seed = calendar.getTimeInMillis() * Math.random();
		random = new Random((long) seed);
		
		for(int i=0; i<dataset.numInstances(); i++) {
			instDT = dataset.instance(i); 
			if(instDT.value(0) == 1) {
				instTrue.add(instDT);
				dataset.delete(i);
				if(i>0) { i--; }
			}
		}
		
		int i=0;
		while(0<instTrue.numInstances()) {
			fold = arrayFoldDT.get(i%numFoldDT);
			
			posRnd = random.nextInt(instTrue.numInstances());
			instDT = instTrue.instance(posRnd);
			instTrue.delete(posRnd);
			fold.add(instDT);
			
			posRnd = random.nextInt(dataset.numInstances());
			instDT = dataset.instance(posRnd);
			dataset.delete(posRnd);
			fold.add(instDT);
			
			i++;
		}
		
	}
	
	public Instances getFold(int pos) {
		return this.arrayFoldDT.get(pos);
	}
	
	public void printFold() {
		int numInstances;
		int sumNumInstances = 0;
		for(Instances fold : this.arrayFoldDT) {
			numInstances = fold.numInstances();
//			System.out.println(numInstances);
			sumNumInstances += numInstances;
//			System.out.println(fold.toString());
		}
//		System.out.println(sumNumInstances);
	}
}
