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

import weka.classifiers.*;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class Classifiyng {

	private SaveChoose m_Session = null;
	private Classifier scheme = null;
	private Statistic stat = null;
	private Instances ESforEstimation = null;
	private int CE = 0, NCE = 0;
	private int TP = 0, TN = 0, FP = 0, FN = 0;
	private double ER = 0;
	private double ACCES = 0;

	public void classifiyng(Instances instES,int i, Classifier scheme) {

		stat = new Statistic();

		try { 
			this.scheme = scheme;
			instES.setClassIndex(0);

			classifyInstES(instES);
			
//			stat.countTrue(instES);
			
			CE = stat.countTrue(ESforEstimation);
			NCE = ESforEstimation.numInstances() - CE;
			m_Session.setCE(CE);
			m_Session.setNCE(NCE);

			if(CE == ESforEstimation.numInstances()) {
				m_Session.setTpTnFpFn(new int[] {m_Session.getCA(i),0,m_Session.getNCA(i),0});
			} else if(NCE == ESforEstimation.numInstances()) {
				m_Session.setTpTnFpFn(new int[] {0,m_Session.getNCA(i),0,m_Session.getCA(i)});
			} else {
				m_Session.setTpTnFpFn(stat.countTpFpTnFn(instES, ESforEstimation));
			}
			TP = m_Session.getTP();
			TN = m_Session.getTN();
			FP = m_Session.getFP();
			FN = m_Session.getFN();
			
//			AR = stat.calcErR(TP,FP,TN,FN);
			ER = stat.calcErRtot(TP, FP, TN, FN, m_Session.getCS(i));
			//m_Session.addAR(AR); NOT IN USE, use for mean
			m_Session.setErR(ER);
			
			ACCES = stat.calcAccuracyES(TP,TN,FP,FN);
			m_Session.setACCES(ACCES);
			
		} catch  (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}

	}
/** OLD VERSION
	// For classifying
	public void classifyInstES(Instances instES) {
		Instance inst;

		Attribute classIndexAttribute = instES.attribute(0);
		FastVector attInfo = new FastVector();
		attInfo.addElement(classIndexAttribute);
		attInfo.addElement(instES.attribute(1));
		ESforEstimation = new Instances("ESforEstimation",attInfo,0);
		ESforEstimation.setClassIndex(0);

		for(int i=0; i<instES.numInstances(); i++) {
			inst = new Instance(2);
			inst.setValue(instES.instance(i).attribute(1), instES.instance(i).value(1));
			ESforEstimation.add(inst);
		}

		try {
			for(int i1=0; i1<ESforEstimation.numInstances(); i1++) {
				double[] pred;
				double classValue;

				pred = scheme.distributionForInstance(ESforEstimation.instance(i1));

				classValue = Utils.maxIndex(pred);
				ESforEstimation.instance(i1).setClassValue(classValue);

//				for(int j=0; j<pred.length; j++) {
//					System.out.print(" " +pred[j]);
//				}
//				System.out.println();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	*/
	// For classifying 
	public void classifyInstES(Instances instES) {
		Instance inst;

		Attribute attribute;
		Attribute classIndexAttribute = instES.attribute(0);
		FastVector attInfo = new FastVector();
		attInfo.addElement(classIndexAttribute);
		for(int i=1; i<instES.numAttributes(); i++) {
			attribute = instES.attribute(i);
			if(!attribute.isNumeric()) {
				attribute = new Attribute("TruthNumeric");
			}
			attInfo.addElement(attribute);
		}
		
		ESforEstimation = new Instances("ESforEstimation",attInfo,0);
		ESforEstimation.setClassIndex(0);

		for(int i=0; i<instES.numInstances(); i++) {
			inst = new Instance(instES.numAttributes());
			for(int j=1; j<instES.numAttributes(); j++) {
				inst.setValue(instES.instance(i).attribute(j), instES.instance(i).value(j));
			}
			ESforEstimation.add(inst);
		}

		try {
			for(int i1=0; i1<ESforEstimation.numInstances(); i1++) {
				double[] pred;
				double classValue;

				pred = scheme.distributionForInstance(ESforEstimation.instance(i1));

				classValue = Utils.maxIndex(pred);
				ESforEstimation.instance(i1).setClassValue(classValue);

				/***********************************************
				for(int j=0; j<pred.length; j++) {
					System.out.print(" " +pred[j]);
				}
				System.out.println();
				 ****************************************************/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setSession(SaveChoose session) {
		this.m_Session = session;
	}
}
