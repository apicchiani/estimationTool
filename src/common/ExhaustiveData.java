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

import weka.classifiers.Classifier;
import weka.core.Instances;

public class ExhaustiveData {

	/** Array containing the TS for build model */
	private Instances[] arrayTS;
	
	/** Array containing the ES for classification */
	private Instances[] arrayES;
	
	/** Array containing the model of classifier in use */
	private Classifier[] arrayModel = null;
	
	/** Array containing CA */
	private int[] arrayCA = null;
	
	/** Array containing NCA */
	private int[] arrayNCA = null;
	
	/** Array containing CS */
	private int[] arrayCS = null;
	
	/** Array containing NCS */
	private int[] arrayNCS = null;
	
	/** Array containing ErS for calculate the mean */
	private double[] arrayErS = null;
	
	/** Array containing ErR for calculate the mean */
	private double[] arrayErR = null;
	
	/** Array containing accuracy for calculate the mean */
	private double[] arrayACC = null;
	
	/** Array containing the real coverage COVTS */
	private double[] arrayRealCoverage;
	
	int pos = 0;
	
	public ExhaustiveData(int numRun) {
		arrayCS = new int[numRun];
		arrayNCS = new int[numRun];
		arrayCA = new int[numRun];
		arrayNCA = new int[numRun];
		arrayRealCoverage = new double[numRun];
		arrayTS = new Instances[numRun];
		arrayES = new Instances[numRun];
	}
	
	public void setExhaustiveData(int CS,int NCS,int CA, int NCA,double COVTS,Instances TS, Instances ES) {
		arrayCS[pos] = CS;
		arrayNCS[pos] = NCS;
		arrayCA[pos] = CA;
		arrayNCA[pos] = CA;
		arrayRealCoverage[pos] = COVTS;
		arrayTS[pos] = TS;
		arrayES[pos] = ES;
		pos++;
		if(pos==arrayCS.length) {
			pos = 0;
		}
	}
}
