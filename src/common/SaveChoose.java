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
import java.util.Properties;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Utils;

public class SaveChoose {
	
	/** Determines whether to use a balanced dataset */
	private boolean useBalancedDataSet = true;
	
	/** Number of balanced dataset for classification */
	private int numBalancedDataset = 10; 
	
	/** set to true if use all selected attribute for process **/
	private boolean useAllAttributeSelected = false;
	
	/** */
	private Instances m_Instances = null;
	
	/** Array containing the selected measure */
	private int[] arraySelectedAttribute = null;
	
	/** Number of attribute selected */
	private int numSelectedAttribute = 0;
	
	/** Stabilish the number of instances for increment TS */
	private int incTS = 10;
	
	/** Minimal largeness in percentage for TS */
	private int minTS = 10;
	
	/** Maximal largeness in percentage for TS */
	private int maxTS = 70;
	
	/** Number of TS for Random and Mixed approch */
	private int numberRnd = 10;
	private int numberMixed = 10;
	private int numberRipetitionExhaustive = 10;
	
	/** Percentual for random method in mixed for create TS */
	private int percentRnd = 50;
	
	/** Percentual for ranked method in mixed for create TS */
	private int percentRanked = 100 - percentRnd;
	
	/** Selected methods for create TS*/
	private boolean[] method = new boolean[5];

	/** Contain the classname for used classifiers */
	private String[] classifiersClassname = null;
	
	/** It says if the classifier in the date position of classifiersClassname must be used */
	private boolean[] usingClassifiers = null;
	
	/** The array contains the validation in use (1 UseEs 2 CrossValidation 3 splitted) */
	private boolean[] usingValidation = {false,true,false}; //new boolean[3];
	
	/** Number of folds for Cross Validation */
	private int nFolds = 10;

	/** The name of DT */
	private String ID_DT = null;
	
	/** The name of measure in the TS processed */
	private String ID_M = null;
	
	/** The name of classifiers used */
	private String p = null;
	
	/** The classname for classifier in use */
	private String classname = null;
	
	/** Array containing the datasets to process */
	private ArrayList<Instances> balancedDataSetArray = new ArrayList<Instances>(); 
	
	private Instances[] arrayBalancedDataSet;
	
	private int posBalanced = 0;
	
	/** Containing the minimum value for real coverage */
	private double minRealCoverage = 0.1;
	
	private double[] arrayRealCoverage;
	
	private int posRealCoverage = 0;
	
	/** */
	private String[] arrayTimestamp;
	
	private int posTimestamp;
	
	/** Array containing the TS for build model */
	private Instances[] arrayTS;
	
	/** Index for arrayTS */
	private int posTS = 0;
	
	/** Array containing the ES for classification */
	private Instances[] arrayES;
	
	/** Index for arrayES */
	private int posES = 0;
	
	/** Array containing the model of classifier in use */
//	private ArrayList<Classifier> modelsClassifierArray = new ArrayList<Classifier>();
	private Classifier[] arrayModel = null;
	
	/** Index for arrayModel */
	private int posModel = 0;
	
	/** Array containing CA */
	private int[] arrayCA = null;
	
	/** Index for arrayCA */
	private int posCA = 0;
	
	/** Array containing NCA */
	private int[] arrayNCA = null;
	
	/** Array containing CS */
	private int[] arrayCS = null;
	
	/** Index for arrayCS and NCS */
	private int posCS = 0;
	
	/** Array containing NCS */
	private int[] arrayNCS = null;
	
	/** Array containing ErS for calculate the mean */
	private double[] arrayErS = null;
	
	/** Index for arrayErS */
	private int posErS = 0;
	
	/** Array containing ErR for calculate the mean */
	private double[] arrayErR = null;
	
	/** Index for arrayErR */
	private int posErR = 0;
	
	/** Array containing accuracy for calculate the mean */
	private double[] arrayACC = null;
	
	/** Index for arrayACC */
	private int posACC = 0;
	
	/** The number of true instances in the ES */
	private int CE;
	
	/** The number of false instances in the ES */
	private int NCE;
	
	/** The final values of AS, AR to store in DB */
	private double ASS,ErR;
	
	/** The value of accuracy for validation */
	private double ACC,ACCES;
	
	/** */
	private double COV;
	
	/** The values of TP, TN, FP, FN to store in DB */
	private int TP,TN,FP,FN;
	
	/** The values of maximum estimated error */
	private double maxError = 0.4;
	
	/** Indicates whether the active control on the estimated error */
	private boolean controlEstimatedError = false;
	
	/** Indicates whether the active control on the real coverage (COVTS) */
	private boolean controlRealCoverage = false;
	
	private int numRun;
	
	private int[] arrayNumDataset;
	private int[] arrayNumRipetition;
	private int[] arrayNumRun;
	private int posNumDataset = 0;
	private int posNumRipetition = 0;
	private int posNumRun = 0;
	
	/** Indicates whether use balanced folds or stratified folds */
	private boolean balancedFolds = true;
	
	/** Parameters for connecting to DB */
	private String nameDB = "postgres";
	private String userNameDB = "postgres";
	private String userPassDB = "CANTONE";
	private String tableDB = "risultati";

	// Indica se usare Fold stratificati nella ten fold
	private boolean stratifiedFoldsForTenFold = true;

	// Quante volte effettuare la TenFold, di default è impostato ad 1
	private Integer numberRipetitionTenFold = 1;
	
	/** The name of the properties file. */
	public final static String PROPERTY_FILE = "gui/classifier.props";

	/** Properties associated with the explorer options. */
	protected static Properties PROPERTIES;
	
	/** Set defaults */
	{
		// set the four method
//		for(int i=0; i<method.length; i++) {
//			method[i] = true;
//		}
		method[0] = true;
		method[1] = false;
		method[2] = false;
		method[3] = true;
		method[4] = false;
		
		// set the three usingValidation to false
		for(int i=0; i<usingValidation.length; i++) {
			usingValidation[i] = false;
		}
		
		/** Initialize the arrayAS, arrayAR and arrayACC at the default length */
		this.initializeArrayErS();
		this.initializeArrayErR();
		this.initializeArrayRealCoverage();
		this.initializeArrayACC();
		this.initializeArrayTimestamp();
		this.initializeArrayTS();
		this.initializeArrayES();
		this.initializeArrayCANCA();
		this.initializeArrayCSNCS();
		this.initializeArrayModel();
		this.initializeArrayBalanced();
		
		setClassifier();
	}

	public boolean isUseBalancedDataSet() {
		return useBalancedDataSet;
	}

	public void setUseBalancedDataSet(boolean useBalancedDataSet) {
		this.useBalancedDataSet = useBalancedDataSet;
	}

	public void setNumBalancedDataset(int numBalancedDataset) {
		this.numBalancedDataset = numBalancedDataset;
	}

	public int getNumBalancedDataset() {
		return numBalancedDataset;
	}
	
	public void setM_Instances(Instances m_Instances) {
		this.m_Instances = m_Instances;
	}

	public Instances getM_Instances() {
		return m_Instances;
	}

	public void setArraySelectedAttribute(int[] array) {
		this.arraySelectedAttribute = array;
	}
	
	public int[] getArraySelectedAttribute() {
		return arraySelectedAttribute;
	}

	public void setNumSelectedAttribute(int numSelectedAttribute) {
		this.numSelectedAttribute = numSelectedAttribute;
	}

	public int getNumSelectedAttribute() {
		return numSelectedAttribute;
	}

	public void setIncTS(int incTS) {
		this.incTS = incTS;
	}

	public int getIncTS() {
		return incTS;
	}

	public void setMinTS(int minTS) {
		this.minTS = minTS;
	}

	public int getMinTS() {
		return minTS;
	}

	public void setMaxTS(int maxTS) {
		this.maxTS = maxTS;
	}

	public int getMaxTS() {
		return maxTS;
	}
	
	public void setNumberRnd(int numberRnd) {
		this.numberRnd = numberRnd;
	}

	public int getNumberRnd() {
		return numberRnd;
	}

	public void setNumberMixed(int numberMixed) {
		this.numberMixed = numberMixed;
	}

	public int getNumberMixed() {
		return numberMixed;
	}

	public void setNumberRipetitionExhaustive(int numberRipetitionExhaustive) {
		this.numberRipetitionExhaustive = numberRipetitionExhaustive;
	}

	public int getNumberRipetitionExhaustive() {
		return numberRipetitionExhaustive;
	}

	public void setPercentMixed(int percentRnd) {
		this.percentRnd = percentRnd;
		this.percentRanked = 100 - percentRnd;
	}
	
	public int getPercentRnd() {
		return percentRnd;
	}
	
	public int getPercentRanked() {
		return percentRanked;
	}

	public void setMethod(int pos, boolean value) {
		this.method[pos] = value;
	}

	public boolean[] getMethod() {
		return method;
	}
	
	public int classifiersLength() {
		return classifiersClassname.length;
	}
	
	public String getClassifier(int pos) {
		return classifiersClassname[pos];
	}
	
	public void setClassifier(int pos, String val) {
		classifiersClassname[pos] = val;
	}
	
	public void setUsingClassifiers(int pos, boolean value) {
		usingClassifiers[pos] = value;
	}
	
	public boolean getUsingClassifier(int pos) {
		return usingClassifiers[pos];
	}
	
	public int numClassifierSelected() {
		int ret = 0;
		for(int i=0; i<usingClassifiers.length; i++) {
			if(usingClassifiers[i]==true) {
				ret++;
			}
		}
		return ret;
	}
	
	public void setUsingValidation(int pos, boolean value) {
		usingValidation[pos] = value;
	}
	
	public boolean getUsingValidation(int pos) {
		return usingValidation[pos];
	}
	
	public void setNFolds(int nFolds) {
		this.nFolds = nFolds;
	}
	
	public int getNFolds() {
		return nFolds;
	}

	public String getID_DT() {
		return ID_DT;
	}

	public void setID_DT(String iDDT) {
		ID_DT = iDDT;
	}

	public String getID_M() {
		return ID_M;
	}

	public void setID_M(String iDM) {
		ID_M = iDM;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getClassname() {
		return classname;
	}

	public int sizeBalancedDataSetArray() {
		return balancedDataSetArray.size();
	}
	
	public void addBalancedDataSet(Instances inst) {
		balancedDataSetArray.add(inst);
	}
	
	public Instances getBalancedDataSet(int pos) {
		return balancedDataSetArray.get(pos);
	}
	
	public void initializeArrayBalanced() {
		arrayBalancedDataSet = new Instances[numBalancedDataset];
	}
	
	public void addBalanced(Instances Balanced) {
		arrayBalancedDataSet[posBalanced] = Balanced;
		posBalanced++;
		if(posBalanced == arrayBalancedDataSet.length) {
			posBalanced = 0;
		}
	}
	
	public Instances getBalanced(int i) {
		return arrayBalancedDataSet[i];
	}
	
	public void initializeArrayBalancedDataset() {
		this.balancedDataSetArray = new ArrayList<Instances>();
	}
	
	public void setRealCoverage(double realCoverage) {
		this.minRealCoverage = realCoverage;
	}

	public double getRealCoverage() {
		return minRealCoverage;
	}
	
	public void initializeArrayRealCoverage() {
		arrayRealCoverage = new double[numBalancedDataset];
	}
	
	public void addRealCoverage(double realCoverage) {
		arrayRealCoverage[posRealCoverage] = realCoverage;
		posRealCoverage++;
		if(posRealCoverage == arrayRealCoverage.length) {
			posRealCoverage = 0;
		}
	}
	
	public double[] getArrayRealCoverage() {
		return arrayRealCoverage;
	}
	
	public double getRealCoverage(int i) {
		return arrayRealCoverage[i];
	}

	public void initializeArrayTimestamp() {
		arrayTimestamp = new String[numBalancedDataset];
	}
	
	public void addTimestamp(String timestamp) {
		arrayTimestamp[posTimestamp] = timestamp;
		posTimestamp++;
		if(posTimestamp == arrayTimestamp.length) {
			posTimestamp = 0;
		}
	}
	
	public String getTimestamp(int i) {
		return arrayTimestamp[i];
	}
	
	public void initializeArrayTS() {
		arrayTS = new Instances[numBalancedDataset];
	}
	
	public void addTS(Instances TS) {
		arrayTS[posTS] = TS;
		posTS++;
		if(posTS == arrayTS.length) {
			posTS = 0;
		}
	}
	
	public Instances getTS(int i) {
		return arrayTS[i];
	}
	
	public void initializeArrayES() {
		arrayES = new Instances[numBalancedDataset];
	}
	
	public void addES(Instances ES) {
		arrayES[posES] = ES;
		posES++;
		if(posES == arrayES.length) {
			posES = 0;
		}
	}
	
	public Instances getES(int i) {
		return arrayES[i];
	}
	
	public void initializeArrayModel() {
		arrayModel = new Classifier[numBalancedDataset];
	}
	
	public void addModel(Classifier model) {
		arrayModel[posModel] = model;
		posModel++;
		if(posModel == arrayModel.length) {
			posModel = 0;
		}
	}
	
	public Classifier getModel(int i) {
		return arrayModel[i];
	}
	
	public Classifier[] getArrayModel() {
		return arrayModel;
	}
	
//	public void addModelClassifierArray(Classifier model) {
//		modelsClassifierArray.add(model);
//	}
//	
//	public Classifier removeModel() {
//		return modelsClassifierArray.remove(0);
//	}
//	
//	public Classifier getModel(int i) {
//		return modelsClassifierArray.get(i);
//	}
	
	public void initializeArrayCANCA() {
		arrayCA = new int[numBalancedDataset];
		arrayNCA = new int[numBalancedDataset];
	}
	
	public void addCANCA(int CA, int NCA) {
		arrayCA[posCA] = CA;
		arrayNCA[posCA] = NCA;
		posCA++;
		if(posCA == arrayCA.length) {
			posCA = 0;
		}
	}
	
	public int getCA(int i) {
		return arrayCA[i];
	}
	
	public int getNCA(int i) {
		return arrayNCA[i];
	}
	
	public int[] getArrayCA() {
		return arrayCA;
	}
	
	public void initializeArrayCSNCS() {
		arrayCS = new int[numBalancedDataset];
		arrayNCS = new int[numBalancedDataset];
	}
	
	public void addCSNCS(int CS, int NCS) {
		arrayCS[posCS] = CS;
		arrayNCS[posCS] = NCS;
		posCS++;
		if(posCS == arrayCS.length) {
			posCS = 0;
		}
	}
	
	public int getCS(int i) {
		return arrayCS[i];
	}
	
	public int getNCS(int i) {
		return arrayNCS[i];
	}
	
	public void initializeArrayErS() {
		arrayErS = new double[numBalancedDataset];
	}
	
	public void addErS(double ErS) {
		arrayErS[posErS] = ErS;
		posErS++;
		if(posErS == arrayErS.length) {
			posErS = 0;
		}
	}
	
	public double getErS(int i) {
		return arrayErS[i];
	}
	
	public double[] getArrayErS() {
		return arrayErS;
	}
	
	public void initializeArrayErR() {
		arrayErR = new double[numBalancedDataset];
	}
	
	public void addErR(double ErR) {
		arrayErR[posErR] = ErR;
		posErR++;
		if(posErR == arrayErR.length) {
			posErR = 0;
		}
	}
	
	public double[] getArrayErR() {
		return arrayErR;
	}
	
	public void initializeArrayNumDataset() {
		arrayNumDataset = new int[numBalancedDataset];
	}
	
	public void addNumDataset(int numDataset) {
		arrayNumDataset[posNumDataset] = numDataset;
		posNumDataset++;
		if(posNumDataset == arrayNumDataset.length) {
			posNumDataset = 0;
		}
	}
	
	public int getNumDataset(int i) {
		return arrayNumDataset[i];
	}
	
	public void initializeArrayNumRipetition() {
		arrayNumRipetition = new int[numBalancedDataset];
	}
	
	public void addNumRipetition(int numRipetition) {
		arrayNumRipetition[posNumRipetition] = numRipetition;
		posNumRipetition++;
		if(posNumRipetition == arrayNumRipetition.length) {
			posNumRipetition = 0;
		}
	}
	
	public int getNumRipetition(int i) {
		return arrayNumRipetition[i];
	}
	
	public void initializeArrayNumRun() {
		arrayNumRun = new int[numBalancedDataset];
	}
	
	public void addNumRun(int numRun) {
		arrayNumRun[posNumRun] = numRun;
		posNumRun++;
		if(posNumRun == arrayNumRun.length) {
			posNumRun = 0;
		}
	}
	
	public int getNumRun(int i) {
		return arrayNumRun[i];
	}
	
	public void initializeArrayACC() {
		arrayACC = new double[numBalancedDataset];
	}
	
	public void addACC(double ACC) {
		arrayACC[posACC] = ACC;
		posACC++;
		if(posACC == arrayACC.length) {
			posACC = 0;
		}
	}
	
	public double getACC(int i) {
		return arrayACC[i];
	}
	
	public double[] getArrayACC() {
		return arrayACC;
	}
	
	public double getASS() {
		return ASS;
	}

	public void setASS(double aSS) {
		ASS = aSS;
	}

	public void setACC(double aCC) {
		ACC = aCC;
	}

	public double getACC() {
		return ACC;
	}

	public double getErR() {
		return ErR;
	}

	public void setErR(double ErR) {
		this.ErR = ErR;
	}

	public void setACCES(double aCCES) {
		ACCES = aCCES;
	}

	public double getACCES() {
		return ACCES;
	}

	public void setCOV(double cOV) {
		COV = cOV;
	}

	public double getCOV() {
		return COV;
	}

	public void setCE(int cE) {
		CE = cE;
	}

	public int getCE() {
		return CE;
	}

	public void setNCE(int nCE) {
		NCE = nCE;
	}

	public int getNCE() {
		return NCE;
	}

	public void setTpTnFpFn(int[] tptnfpfn ) {
		TP = tptnfpfn[0];
		TN = tptnfpfn[1];
		FP = tptnfpfn[2];
		FN = tptnfpfn[3];
	}
	
	public int getTP() {
		return TP;
	}

	public int getTN() {
		return TN;
	}

	public int getFP() {
		return FP;
	}

	public int getFN() {
		return FN;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setControlEstimatedError(boolean controlEstimatedError) {
		this.controlEstimatedError = controlEstimatedError;
	}

	public boolean isControlEstimatedError() {
		return controlEstimatedError;
	}

	public void setControlRealCoverage(boolean controlRealCoverage) {
		this.controlRealCoverage = controlRealCoverage;
	}

	public boolean isControlRealCoverage() {
		return controlRealCoverage;
	}

	public void setBalancedFolds(boolean balancedFolds) {
		this.balancedFolds = balancedFolds;
	}

	public boolean isBalancedFolds() {
		return balancedFolds;
	}

	public void setNameDB(String nameDB) {
		this.nameDB = nameDB;
	}

	public String getNameDB() {
		return nameDB;
	}

	public void setUserNameDB(String userNameDB) {
		this.userNameDB = userNameDB;
	}

	public String getUserNameDB() {
		return userNameDB;
	}

	public void setUserPassDB(String userPassDB) {
		this.userPassDB = userPassDB;
	}

	public String getUserPassDB() {
		return userPassDB;
	}

	public void setTableDB(String tableDB) {
		this.tableDB = tableDB;
	}

	public String getTableDB() {
		return tableDB;
	}
	
	public void initzializeArrayRanked() {
		this.initializeArrayErS();
		this.initializeArrayErR();
		this.initializeArrayRealCoverage();
		this.initializeArrayACC();
		this.initializeArrayTimestamp();
		this.initializeArrayTS();
		this.initializeArrayES();
		this.initializeArrayCANCA();
		this.initializeArrayCSNCS();
		this.initializeArrayModel();
		this.initializeArrayNumDataset();
		this.initializeArrayNumRipetition();
		this.initializeArrayNumRun();
	}
	
	public void initializeArrayRandom() {
		int numRipBck = this.numBalancedDataset;
		this.numBalancedDataset = numRipBck * this.numberRnd;
		this.initializeArray();
		numBalancedDataset = numRipBck;
	}
	
	public void initializeArrayMixed() {
		int numRipBck = this.numBalancedDataset;
		this.numBalancedDataset = numRipBck * this.numberMixed;
		this.initializeArray();
		numBalancedDataset = numRipBck;
	}
	
	public void initializeExhaustive(int numRun) {
		int numRipBck = this.numBalancedDataset;
		this.numBalancedDataset = numRipBck * numRun * numberRipetitionExhaustive;
		this.initializeArray();			
		System.out.println("[SaveChoose - InitializeExhaustive] Lunghezza arrayModel: " +arrayModel.length);
		numBalancedDataset = numRipBck;
	}
	
	// Inizializza gli array per la Ten-Fold
		public void initializeTenFold(int numRun) {
			int numRipBck = this.numBalancedDataset;
			this.numBalancedDataset = numRipBck * numRun * numberRipetitionTenFold;
			this.initializeArray();
//			System.out.println("[SaveChoose - InitializeTenFold] Lunghezza arrayModel: " +arrayModel.length);
			numBalancedDataset = numRipBck;
		}
	
	private void initializeArray() {
		this.initializeArrayErS();
		this.initializeArrayErR();
		this.initializeArrayRealCoverage();
		this.initializeArrayACC();
		this.initializeArrayTimestamp();
		this.initializeArrayTS();
		this.initializeArrayES();
		this.initializeArrayCANCA();
		this.initializeArrayCSNCS();
		this.initializeArrayModel();
		this.initializeArrayNumDataset();
		this.initializeArrayNumRipetition();
		this.initializeArrayNumRun();
	}

	public void setClassifier() {

		String	classifiers;

		try {
			PROPERTIES = Utils.readProperties(PROPERTY_FILE);
		}
		catch (Exception e) {
			System.err.println("Problem reading properties. Fix before continuing.");
			e.printStackTrace();
			PROPERTIES = new Properties();
		}

		// read and split on comma
		classifiers   = PROPERTIES.getProperty("Classifier");
		classifiersClassname = classifiers.split(",");
		usingClassifiers = new boolean[classifiersClassname.length];

	}

	public void stampClassifier() {
		for(int i=0; i<classifiersClassname.length; i++) {
			System.out.println(classifiersClassname[i]);
		}
	}

	public void setNumRun(int numRun) {
		this.numRun = numRun;
	}

	public int getNumRun() {
		return numRun;
	}

	public void setUseAllAttributeSelected(boolean useAllAttributeSelected) {
		this.useAllAttributeSelected = useAllAttributeSelected;
	}
	
	public boolean getUseAllAttributeSelected() {
		return this.useAllAttributeSelected;
	}

	public void setStratifiedFoldsForTenFold(boolean selected) {
		this.stratifiedFoldsForTenFold = selected;
	}
	
	public boolean getStratifiedFoldsForTenFold() {
		return this.stratifiedFoldsForTenFold;
	}

	public void setNumberRipetitionTenFold(Integer numberRipetitionTenFold) {
		this.numberRipetitionTenFold = numberRipetitionTenFold;
	}
	
	public int getNumberRipetitionTenFold() {
		return this.numberRipetitionTenFold;
	}
	
}
