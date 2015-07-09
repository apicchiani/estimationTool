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

import weka.core.Instance;
import weka.core.Instances;

public class Statistic {
	
	private int numTrue = 0;
	
	// Parameters for count tp fp tn fn of ES actual and estimed
	private Instance instActual = null;
	private Instance instEstimed = null;
	private int classValueActual, classValueEstimed;
	private int tp=0,fp=0,tn=0,fn=0;
	private int[] tptnfpfn = new int[4];
	
	/** Count the number of true instances in the set */
	public int countTrue(Instances inst) {
		numTrue = 0;
		
		for(int i=0; i<inst.numInstances(); i++) {
			if(inst.instance(i).value(0) == 1 ) {
				numTrue++;
			}
		}
		
		return numTrue;
	}
	
	public double calcAccuracyTS(double[][] cmMatrix) {
		double tptn=cmMatrix[0][0]+cmMatrix[1][1];
		double fpfn=cmMatrix[1][0]+cmMatrix[0][1];
		return tptn/(tptn+fpfn);
	}
	
	public double calcAccuracyES(int TP, int TN, int FP, int FN) {
		return (double) (TP+TN) /(double) (TP+TN+FP+FN);
	}

	public double calcErS(double[][] cmMatrix,int numTrue) {
		double ErS;
		double fptp =  cmMatrix[0][1] + cmMatrix[1][1];
//		System.out.println("numtrue: " +numTrue +" fptp: " +fptp);
		ErS = Math.abs(numTrue-fptp)/numTrue;
		return ErS;
	}
	
	public double calcErR(int tp, int fp, int tn, int fn) {
//		return 1 - ((double) Math.abs(numTrue-(fp+tp))) / (double) (numTrue);
		return ((double) Math.abs(fn-fp)) / (double) (tp+fn);
	}
	
	public double calcErRtot(int tp, int fp, int tn, int fn, int CS) {
		return ((double) Math.abs(fn-fp)) / (double) (CS+tp+fn);
	}
	
	public double calcCOVTS(int CS, int CA) {
		return  (double) CS/(double) (CS+CA);
	}
	
	public double calcCOVES(int CS,int CE) {
		return (double) CS/(double)(CS+CE);
	}
	
	// Count tp fp tn fn
	public int[] countTpFpTnFn(Instances instES, Instances ESforEstimation) {
		tp = 0;
		fp = 0;
		tn = 0;
		fn = 0;
		
		for(int k=0; k<instES.numInstances(); k++) {
			instActual = instES.instance(k);
			instEstimed = ESforEstimation.instance(k);
			
			classValueActual = (int) instActual.value(0);
			classValueEstimed = (int) instEstimed.value(0);
			
			if(classValueEstimed == 1) {
				if(classValueActual == 1) {
					tp++;
				} else {
					fp++;
				}
			} else {
				if(classValueActual == 1) {
					fn++;
				} else {
					tn++;
				}
			}
			
//			if(classValueActual == 1 && classValueEstimed == 1) {
//				tp++;
//			}
//			
//			if(classValueActual == 1 && classValueEstimed != 1) {
//				fn++;
//			}
//			
//			if(classValueActual == 0 && classValueEstimed == 0) {
//				tn++;
//			}
//			if(classValueActual == 0 && classValueEstimed != 1) {
//				fp++;
//			}
			
			
		}
		
		tptnfpfn[0] = tp;
		tptnfpfn[1] = tn;
		tptnfpfn[2] = fp;
		tptnfpfn[3] = fn;
		return tptnfpfn;
	}
	
	public double meanErS(double[] arrayErS) {
		double sumErS = 0;
		
		for(int i=0; i<arrayErS.length; i++) {
			sumErS += arrayErS[i];
		}
		
		return sumErS/arrayErS.length;
	}
	
	public double meanErR(double[] arrayErR) {
		double sumErR = 0;
		
		for(int i=0; i<arrayErR.length; i++) {
			sumErR += arrayErR[i];
		}
		
		return sumErR/arrayErR.length;
	}
	
	
}
