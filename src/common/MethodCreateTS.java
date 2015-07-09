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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import gui.PrincipalPanel;

import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class MethodCreateTS {
	
	private PrincipalPanel m_Coman = null;

	private SaveChoose m_Session = null;
	private CoreMethodCreateTS coreMethodTS = new CoreMethodCreateTS();
	private Classifiyng classifyng = new Classifiyng();
	private Statistic stat = new Statistic();
	private DButil db = null;
	private GregorianCalendar calendar = null;
	String[] classnameArray = null;
	String classname;
	
	private int progress = 0;
	private String completed = null;

	private int nRip = 1;
	
	// Parameters for insert in the DB
	private String ID_DT = null;
	private String ID_M = null;
	private String P = null;
	private String S = null;
	private int nDataset = 1, nRipetition = 1, nRun = 0;
	private String timestamp = null;
	private boolean isBalanced = true;
	private boolean isStratifiedFold = true;
	private int CS = 0, NCS = 0;
	private int CA = 0, NCA = 0;
	private int CE = 0, NCE = 0;
	private double ErS,ErR,COVTS,COVES;
	private double summRealCoverage;
	private double ACC,ACCES;
	private int TP,TN,FP,FN;
	
	private String day = null;
	private String month = null;
	private String year = null;
	private String hour = null;
	private String minute = null;
	private String second = null;
	
	private Instances inst;
	private int maxInstanceTS;
	private int minInstanceTS;
	private Instances instTS;
	private Instances instES;
	private Attribute classIndexAttribute = null;
	private FastVector attInfo = null;

	public MethodCreateTS(SaveChoose session, PrincipalPanel parent) {
		this.m_Session = session;
		this.m_Coman = parent;
		this.db = new DButil(m_Session,parent);
		classifyng.setSession(session);
	}

	public void ranked(int minTS,int maxTS,int step) {
		calcMaxMin(minTS,maxTS);
		Instances instESsave;
//		System.out.println("Min: "+minInstanceTS +" Max: " +maxInstanceTS);
		
		for(int i=this.minInstanceTS; i<this.maxInstanceTS; i+=step) {
			
			this.summRealCoverage = 0;
			nRip = 0;
			for(int j=0; j<m_Session.sizeBalancedDataSetArray(); j++) {
				inst = m_Session.getBalancedDataSet(j);

//				inst.sort(1);

				classIndexAttribute = inst.attribute(0);
				attInfo = new FastVector();
				attInfo.addElement(classIndexAttribute);
				attInfo.addElement(inst.attribute(1));

				instTS = new Instances("train",attInfo,0);
				instES = new Instances(inst);
				instES.setRelationName("test");	

				coreMethodTS.rankedCore(instTS, instES, i);
				
				instTS = new Instances(instTS);
//				instTS.randomize(new Random());
//				System.out.println(instTS);
				
				CS = stat.countTrue(instTS);
				NCS = instTS.numInstances()-CS;
				m_Session.addCSNCS(CS,NCS);
				CA = stat.countTrue(instES);
				NCA = instES.numInstances() - CA;
				m_Session.addCANCA(CA,NCA);
				COVTS = stat.calcCOVTS(CS, CA);
				this.summRealCoverage += COVTS;
				m_Session.addRealCoverage(COVTS);
				
				m_Session.addNumDataset(j+1);
				
//				instESsave = new Instances(instES);
//				instESsave.randomize(new Random());
//				System.out.println(instESsave);

//				System.out.println(instTS.classIndex());
//				System.out.println(instES.classIndex());
				instTS.setClassIndex(0);
				instTS.setClassIndex(0);
				
//				System.out.println(instES);
				m_Session.addTS(instTS);
				m_Session.addES(new Instances(instES));
//				m_Session.addES(instESsave);

				//		Insert to DB
				nRip = j+1;
			}
			// Create the model for classifying
//			System.out.println(this.summRealCoverage +" " +nRip +" " +summRealCoverage/nRip);
			if((summRealCoverage/nRip > m_Session.getRealCoverage()) || !m_Session.isControlRealCoverage()) {
				
				createModel();			
				classifyWithModels();
			}
		}
		
	}	

	public void random(int minTS, int maxTS, int step) {
		calcMaxMin(minTS,maxTS);
		nDataset = 1;
		for(int i=this.minInstanceTS; i<this.maxInstanceTS; i+=step) {
			nRip = 0;
			this.summRealCoverage = 0;
			for(int j=0; j<m_Session.sizeBalancedDataSetArray(); j++) {

				inst = m_Session.getBalancedDataSet(j);

//				inst.sort(1);
				
				classIndexAttribute = inst.attribute(0);
				attInfo = new FastVector();
				attInfo.addElement(classIndexAttribute);
				attInfo.addElement(inst.attribute(1));

				for(int i1=0; i1<m_Session.getNumberRnd(); i1++) {

					instTS = new Instances("train",attInfo,0);
					instES = new Instances(inst);
					instES.setRelationName("test");

					coreMethodTS.randomCore(instTS,instES,i);

					CS = stat.countTrue(instTS);
					NCS = instTS.numInstances()-CS;
					m_Session.addCSNCS(CS,NCS);
					CA = stat.countTrue(instES);
					NCA = instES.numInstances() - CA;
					m_Session.addCANCA(CA,NCA);
					COVTS = stat.calcCOVTS(CS, CA);
					this.summRealCoverage += COVTS;
					m_Session.addRealCoverage(COVTS);

					m_Session.addNumDataset(j+1);
					m_Session.addNumRipetition(i1+1);
					
					m_Session.addTS(instTS);
					m_Session.addES(new Instances(instES));

				}
				nRip = j+1;
			}
			// Create the model for classifying
			if((summRealCoverage/(nRip*m_Session.getNumberRnd()) > m_Session.getRealCoverage()) || !m_Session.isControlRealCoverage()) {
				
				createModel();			
				classifyWithModels();
			}
		}
	}

	public void mixed(int minTS, int maxTS, int step, int percentRnd) {
		int incLin;
		Instances instsTS, instsES;

		calcMaxMin(minTS,maxTS);
		nDataset = 1;

		incLin = (int) step/2;

		for(int i=minInstanceTS; i<this.maxInstanceTS; i+=step) {
			this.summRealCoverage = 0;
			nRip = 0;
			for(int j=0; j<m_Session.sizeBalancedDataSetArray(); j++) {

				inst = m_Session.getBalancedDataSet(j);

				attInfo = new FastVector();
				classIndexAttribute = inst.attribute(0);
				attInfo.addElement(classIndexAttribute);
				attInfo.addElement(inst.attribute(1));
				
				for(int i1=0; i1<m_Session.getNumberMixed(); i1++) {

					instsTS = new Instances("train",attInfo,0);
					instsES = new Instances(inst);
					instsES.setRelationName("test" +nRip);

					coreMethodTS.randomCore(instsTS,instsES,i-incLin);

					coreMethodTS.rankedCore(instsTS,instsES,i);

					instTS = new Instances(instsTS);
					instES = new Instances(instsES);

					CS = stat.countTrue(instTS);
					NCS = instTS.numInstances()-CS;
					m_Session.addCSNCS(CS,NCS);
					CA = stat.countTrue(instES);
					NCA = instsES.numInstances() - CA;
					m_Session.addCANCA(CA,NCA);
					COVTS = stat.calcCOVTS(CS, CA);
					this.summRealCoverage += COVTS;
					m_Session.addRealCoverage(COVTS);
					
					m_Session.addNumDataset(j+1);
					m_Session.addNumRipetition(i1+1);

					m_Session.addTS(instTS);
					m_Session.addES(new Instances(instsES));
				}
				
				nRip = j+1;
			}
			
			// Create the model for classifying
			if((summRealCoverage/(nRip*m_Session.getNumberMixed()) > m_Session.getRealCoverage()) || !m_Session.isControlRealCoverage()) {
				
				createModel();			
				classifyWithModels();
			}
		}
	}
	
	private void exhaustive() {
		FoldsDT foldsDT = null;
//		ExhaustiveData exhaustiveData = null;
		int[] numFold = {10,5,4,3,2,3,4,5,10};
		int[] numRuns = {10,5,4,3,2,3,4,5,10};
		int[] numFoldsES = {9,4,3,2,1,1,1,1,1};
		String[] percentFolds = {"10%","20%","25%","33.33%","50%","66.66%","75%","80%","90%"};
		int numFoldES;
		int foldToStartES;
		int foldToStartTS;
		int numFoldInES;
		int numFoldInTS;
		int posFold;
		int numRun;
		int numRipetition;

		for(int i=0; i<9; i++) {
			
			numFoldES = numFoldsES[i];
			numRun = numRuns[i];
			S = "exhaustive_" +percentFolds[i];
			
			//exhaustiveData = new ExhaustiveData(numRun[i]);
			m_Session.initializeExhaustive(numRun);
			m_Session.setNumRun(numRun);
			
			summRealCoverage = 0;
			numRipetition = m_Session.getNumberRipetitionExhaustive();
			for(int ripetition=0; ripetition<numRipetition; ripetition++) {
			
				for(int j=0; j<m_Session.sizeBalancedDataSetArray(); j++) {
	
					inst = new Instances(m_Session.getBalancedDataSet(j));
	//				System.out.println("Num instances in balanced " +inst.numInstances());
	
					foldsDT = new FoldsDT(numFold[i],new Instances(this.inst));
					if(m_Session.isBalancedFolds()) {
						if(m_Session.isUseBalancedDataSet()) {
//							System.out.println("[MethodCreateTS - exhaustive] Creo folds bilanciati");
							foldsDT.createBalancedFolds();
						} else {
							foldsDT.createStratifiedFolds();
//							System.out.println("[MethodCreateTS - exhaustive] Creo folds stratified");
						}
					} else {
						foldsDT.createFolds();
					}
//					foldsDT.printFold();
					
					for(int run=0; run<numRun; run++) {
						if(i<5) {
							instTS = new Instances(foldsDT.getFold(run));
							foldToStartES = (run+1)%(numFoldES+1);
							//					System.out.println("foldToStartES: " +foldToStartES);
							instES = new Instances(foldsDT.getFold(foldToStartES));
							foldToStartES++;
							//					System.out.println(foldToStartES);
							numFoldInES = foldToStartES + numFoldES-1;
							//					System.out.println(numFoldInES);
							for(int fold=foldToStartES; fold<numFoldInES; fold++) {
								posFold = fold%(numFoldES+1);
								instES = this.mergeInstances(instES,new Instances(foldsDT.getFold(posFold)));
								//						System.out.println("posFold: " +posFold +" numInstES: " +instES.numInstances());
							}
						} else {
							instES = new Instances(foldsDT.getFold(run));
							foldToStartTS = (run+1)%(numRun);
							//					System.out.println("foldToStartES: " +foldToStartES);
							instTS = new Instances(foldsDT.getFold(foldToStartTS));
							foldToStartTS++;
							//					System.out.println(foldToStartES);
							numFoldInTS = foldToStartTS + numRun-2;
							//					System.out.println(numFoldInES);
							for(int fold=foldToStartTS; fold<numFoldInTS; fold++) {
								posFold = fold%(numRun);
								instTS = this.mergeInstances(instTS,new Instances(foldsDT.getFold(posFold)));
								//						System.out.println("posFold: " +posFold +" numInstES: " +instES.numInstances());
							}
						}
						
	//					System.out.println(instTS.numInstances() +" " +instES.numInstances());
	//					System.out.println(instTS.relationName());
	//					System.out.println(instES.relationName());
						
						CS = stat.countTrue(instTS);
						NCS = instTS.numInstances()-CS;
						m_Session.addCSNCS(CS, NCS);
						CA = stat.countTrue(instES);
						NCA = instES.numInstances() - CA;
						m_Session.addCANCA(CA, NCA);
						COVTS = stat.calcCOVTS(CS, CA);
						m_Session.addRealCoverage(COVTS);
						
						m_Session.addTS(instTS);
						m_Session.addES(instES);
						m_Session.addNumRun(run+1);
						m_Session.addNumRipetition(ripetition+1);
						m_Session.addNumDataset(j+1);
	//					System.out.println(" Coverage: " +COVTS);
	
	//					exhaustiveData.setExhaustiveData(CS, NCS, CA, NCA, COVTS, instTS, instES);
						summRealCoverage += COVTS;
	
					}
	//				System.out.println();
					
				}
			}
			
			if((summRealCoverage/(m_Session.getNumBalancedDataset()*numRun*numRipetition) > m_Session.getRealCoverage()) || !m_Session.isControlRealCoverage()) {
				createModel();
				classifyWithModels();
			}
			
		}

	}
	
	/** Ten-Fold  */
	private void tenfold() {
		FoldsDT foldsDT = null;
		int numFoldES;
		int foldToStartTS;
		int numFoldInTS;
		int posFold;
		int numRun;
		int numRipetition;


		numFoldES = 1;
		numRun = 10;
		S = "TenFold";

		m_Session.initializeTenFold(numRun);
		m_Session.setNumRun(numRun);

		summRealCoverage = 0;


		numRipetition = m_Session.getNumberRipetitionTenFold();

		for(int ripetition=0; ripetition<numRipetition; ripetition++) {

			for(int j=0; j<m_Session.sizeBalancedDataSetArray(); j++) {

				inst = new Instances(m_Session.getBalancedDataSet(j));
//				System.out.println("[MethodCreateTS - tenFold] Num instances in balanced " +inst.numInstances());

				foldsDT = new FoldsDT(10,new Instances(this.inst));
				if(m_Session.getStratifiedFoldsForTenFold()) {
					if(m_Session.isUseBalancedDataSet()) {
//						System.out.println("[MethodCreateTS - tenfold] Creo folds bilanciati");
						foldsDT.createBalancedFolds();
					} else {
//						System.out.println("[MethodCreateTS - tenfold] Creo folds stratified");
						foldsDT.createStratifiedFolds();
					}
				} else {
					foldsDT.createFolds();
				}
				//				foldsDT.printFold();

				for(int run=0; run<numRun; run++) {


					instES = new Instances(foldsDT.getFold(run));
					foldToStartTS = (run+1)%(numRun);
					//					System.out.println("foldToStartES: " +foldToStartES);
					instTS = new Instances(foldsDT.getFold(foldToStartTS));
					foldToStartTS++;
					//					System.out.println(foldToStartES);
					numFoldInTS = foldToStartTS + numRun-2;
					//					System.out.println(numFoldInES);
					for(int fold=foldToStartTS; fold<numFoldInTS; fold++) {
						posFold = fold%(numRun);
						instTS = this.mergeInstances(instTS,new Instances(foldsDT.getFold(posFold)));
						//						System.out.println("posFold: " +posFold +" numInstES: " +instES.numInstances());
					}

					//				System.out.println(instTS.numInstances() +" " +instES.numInstances());
					//				System.out.println(instTS.relationName());
					//				System.out.println(instES.relationName());

					CS = stat.countTrue(instTS);
					NCS = instTS.numInstances()-CS;
					m_Session.addCSNCS(CS, NCS);
					CA = stat.countTrue(instES);
					NCA = instES.numInstances() - CA;
					m_Session.addCANCA(CA, NCA);
					COVTS = stat.calcCOVTS(CS, CA);
					m_Session.addRealCoverage(COVTS);

					m_Session.addTS(instTS);
					m_Session.addES(instES);
					m_Session.addNumRun(run+1);
					m_Session.addNumRipetition(ripetition+1);
					m_Session.addNumDataset(j+1);
					//					System.out.println(" Coverage: " +COVTS);

					summRealCoverage += COVTS;

				}
				//				System.out.println();

			}
		}

		if((summRealCoverage/(m_Session.getNumBalancedDataset()*numRun*numRipetition) > m_Session.getRealCoverage()) || !m_Session.isControlRealCoverage()) {
			createModel();
			classifyWithModels();
		}


	}
	
	/** Make the merge instToMerge in instances */
	private Instances mergeInstances(Instances instances, Instances instToMerge) {
		Instances instMerged = instances;
		for(int i=0; i<instToMerge.numInstances(); i++) {
			instMerged.add(instToMerge.instance(i));
		}
		return instMerged;
	}

	private void calcMaxMin(int minTS,int maxTS) {
		int numInstance = m_Session.getBalancedDataSet(0).numInstances();
		minInstanceTS = (numInstance*minTS)/100;
		maxInstanceTS = (numInstance*maxTS)/100;
//		System.out.println(+numInstance +" " +maxTS +" " +maxInstanceTS);
	}
	
	private Validating val;
	private Classifier scheme;
	private CostMatrix costMatrix;
	
	private void createModel() {
		int numTrueTS;
		int numberRipetition = m_Session.getNumBalancedDataset();
		val = new Validating();
		val.setSession(m_Session);
		
		if(S == "random") {
			numberRipetition *= m_Session.getNumberRnd();
		} else if (S.contains("mixed")) {
			numberRipetition *= m_Session.getNumberMixed();
		} else if (S.contains("exhaustive")) {
			numberRipetition *= m_Session.getNumRun() * m_Session.getNumberRipetitionExhaustive();
		} else if (S.contains("TenFold")) {
			numberRipetition *= m_Session.getNumRun() * m_Session.getNumberRipetitionTenFold();
//			System.out.println("[MethodCreateTS -  createModel] Numero Ripetizioni: " +numberRipetition);

		}

		for(int i=0; i<numberRipetition; i++) {
			calendar = new GregorianCalendar();
			day = ((Integer) calendar.get(Calendar.DAY_OF_MONTH)).toString();
			month = ((Integer) (calendar.get(Calendar.MONTH)+1)).toString();
			year = ((Integer) calendar.get(Calendar.YEAR)).toString();
			hour = ((Integer) calendar.get(Calendar.HOUR_OF_DAY)).toString();
			minute = ((Integer) calendar.get(Calendar.MINUTE)).toString();
			second = ((Integer) calendar.get(Calendar.SECOND)).toString();
			m_Session.addTimestamp(day +"." +month +"." +year +" " +hour +":" +minute +":" +second);
			
			instTS = new Instances(m_Session.getTS(i));
//			System.out.println(instTS);

			try { 
				scheme = (Classifier) Class.forName(classname).newInstance();
				instTS.setClassIndex(0);
				/**/
				numTrueTS = stat.countTrue(instTS);

				Evaluation evaluation = new Evaluation(instTS,costMatrix);

				val.crossValidation(instTS, scheme, evaluation);

				//			m_Session.addModelClassifierArray(scheme);
				m_Session.addModel(scheme);

				/** Store the model of classifier */
				//			ObjectOutputStream oos = new ObjectOutputStream(
				//			new FileOutputStream(classnameArray[classnameArray.length-1]+instES.relationName() +".model"));
				//			oos.writeObject(scheme);
				//			oos.flush();
				//			oos.close();

				// Get the confusion matrix
				double[][] cmMatrix = evaluation.confusionMatrix();

//				System.out.println(cmMatrix[0][0] +" " +cmMatrix[0][1]);
//				System.out.println(cmMatrix[1][0] +" " +cmMatrix[1][1]);
//
//				System.out.println(numTrueTS);
				ErS = stat.calcErS(cmMatrix,numTrueTS);
//				System.out.println(ErS);
				m_Session.addErS(ErS);

				ACC = stat.calcAccuracyTS(cmMatrix);
				m_Session.addACC(ACC);

			} catch  (Exception ex) {
				ex.printStackTrace();
				System.err.println(ex.getMessage());
			}

		}

	}
	
	private void classifyWithModels() {
		int numberRipetition = m_Session.getNumBalancedDataset();
		
		if(S == "random") {
			numberRipetition *= m_Session.getNumberRnd();
		} else if (S.contains("mixed")) {
			numberRipetition *= m_Session.getNumberMixed();
		}  else if (S.contains("exhaustive")) {
			numberRipetition *= m_Session.getNumRun() * m_Session.getNumberRipetitionExhaustive();
		} else if (S.contains("TenFold")) {
			numberRipetition *= m_Session.getNumRun() * m_Session.getNumberRipetitionTenFold();
//			System.out.println("[MethodCreateTS -  classifyWithModels] Numero Ripetizioni: " +numberRipetition);
			
		}
		
		if((stat.meanErS(m_Session.getArrayErS()) < m_Session.getMaxError()) || !m_Session.isControlEstimatedError()) {
			for(int i=0; i<numberRipetition; i++) {
				instES = m_Session.getES(i);
//				System.out.println(instES);
				scheme = m_Session.getModel(i);
				classifyng.classifiyng(instES,i,scheme);	
				ErS = m_Session.getErS(i);
				ErR = m_Session.getErR();
				CS = m_Session.getCS(i);
				CE = m_Session.getCE();
				COVES = stat.calcCOVES(CS,CE);
				m_Session.setCOV(COVES);
				insertDB(i);
			}
		}
	}
	
	private void insertDB(int i) {
		// Set the parameters for store to DB
		
		if(S.contains("exhaustive")) {
			nRipetition = m_Session.getNumRipetition(i);
			nRun = m_Session.getNumRun(i);
		} else if (S.contains("TenFold")) {
			nRip = m_Session.getNumberRipetitionTenFold();
			nRun = m_Session.getNumRun(i);
			isStratifiedFold = m_Session.getStratifiedFoldsForTenFold();
		} else if(S == "ranked") {
			nRipetition = 1;
			nRun = 0;
			isStratifiedFold = false;
		} else {
			nRipetition = m_Session.getNumRipetition(i);
			nRun = 0;
			isStratifiedFold = false;
		}
		nDataset = m_Session.getNumDataset(i);
		
		P = m_Session.getP();
		timestamp = m_Session.getTimestamp(i);
		
		/** NOT IN USE
		ASS = statistic.meanAS(m_Session.getArrayAS());
		AR = statistic.meanAR(m_Session.getArrayAR());
		EA = statistic.calcEA(ASS, AR);
		*/
		
		CS = m_Session.getCS(i);
		NCS = m_Session.getNCS(i);
		CA = m_Session.getCA(i); //statistic.countTrue(instES);
		NCA = m_Session.getNCA(i); //instES.numInstances() - CA;
		CE = m_Session.getCE();
		NCE = m_Session.getNCE();
		
		ErS = m_Session.getErS(i);
		ErR = m_Session.getErR();
		COVTS = m_Session.getRealCoverage(i);
		COVES = m_Session.getCOV();
		
		ACC = m_Session.getACC(i);
		ACCES = m_Session.getACCES();
		
		TP = m_Session.getTP();
		TN = m_Session.getTN();
		FP = m_Session.getFP();
		FN = m_Session.getFN();
		
		// Execute query
		db.createConnectionDB();
		db.insertTuplaDB(ID_DT, ID_M, P, S, isBalanced, isStratifiedFold, nDataset, nRipetition, nRun, timestamp, CS, NCS, CA, NCA, CE, NCE, ErS, ACC, COVTS, ErR, ACCES, COVES, TP, TN, FP, FN);
		db.closeConnectionDB();
		
	}
	
	
	
	public void startProcess() {
		// Set the parameters for store to DB
		ID_DT = m_Session.getID_DT();
		ID_M = m_Session.getID_M();
		isBalanced = m_Session.isUseBalancedDataSet();
		isStratifiedFold = m_Session.isBalancedFolds();
		
		// Select and use classifier
		for(int i=0; i<m_Session.classifiersLength(); i++ ) {

			if(m_Session.getUsingClassifier(i)==true) {
				classname = m_Session.getClassifier(i);

				// Sets P for store in DB
				classnameArray = classname.split("\\.");
				P = classnameArray[classnameArray.length-1];
				
				/** */
				m_Session.setP(P);
				m_Session.setClassname(classname);

				boolean[] methodUse = m_Session.getMethod();

				if(methodUse[0]==true) {
					// Set the parameters for store to DB
					nDataset = 1;
					S = "ranked";
					m_Session.initzializeArrayRanked();

					ranked(m_Session.getMinTS(), m_Session.getMaxTS(), m_Session.getIncTS());
					m_Coman.printError("Completed " +S +" ");
				}
				if(methodUse[1]==true) {
					// Set the parameters for store to DB
					S = "random";
					m_Session.initializeArrayRandom();

					random(m_Session.getMinTS(), m_Session.getMaxTS(), m_Session.getIncTS());
					m_Coman.printError("Completed " +S +" ");
				}
				if(methodUse[2]== true) {
					// Set the parameters for store to DB
					S = "mixed_" + m_Session.getPercentRnd();
					m_Session.initializeArrayMixed();
					
					mixed(m_Session.getMinTS(), m_Session.getMaxTS(), m_Session.getIncTS(), m_Session.getPercentRnd());		
					m_Coman.printError("Completed " +S +" ");
				}
				if(methodUse[3]== true) {
					// Set the parameters for store to DB
					nDataset = 1;
					S = "exhaustive";
//					m_Session.initializeArrayMixed();
					
					exhaustive();
					m_Coman.printError("Completed modified N-fold cross-validation");
				}
				if(methodUse[4] == true) {
					nDataset = 1;
					S = "TenFold";
					
					tenfold();
					m_Coman.printError("Completed Ten-Fold");
				}
				
				m_Coman.printError("\n");
				progress++; 
				progress = Math.min(progress, 100);
				completed = "Completed " +P +" on " +ID_M +"\n";
				m_Coman.propertyChange(progress, completed);

			}
		}
		m_Session.initializeArrayBalancedDataset();
	}

}
