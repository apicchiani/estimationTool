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

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import gui.PrincipalPanel;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class PreProcessDT {
	
	private PrincipalPanel comanPanel = null;
	private SaveChoose m_Session = null;
	private Instances m_Instances = null;
	private int[] arraySelectedAttribute = null;
	private int indexAttribute;
	private Attribute attribute = null;
	private double[] attributeValues = null;
	private int maxProgress = 0;
	
	private MethodCreateTS methodCreateTS = null; 
	
	/** For generating seed */
	private GregorianCalendar calendar = null;
	
	/** Contains the name of measure */
	private String measureName = null;

	/** Contains the instances for processing */
	Instances instancesForProcess;
	
	public PreProcessDT(PrincipalPanel comanPanel, SaveChoose m_Session) {
		this.m_Session = m_Session;
		this.comanPanel = comanPanel;
		this.methodCreateTS = new MethodCreateTS(m_Session, comanPanel);
	}

	public void startPreProcess() {

		m_Instances = m_Session.getM_Instances();
		//System.out.println("[PreProcessDT -startPreProcess] Numero di Instanze: " +m_Instances.numInstances());
		m_Instances.delete(m_Instances.numInstances()-1);
		
		arraySelectedAttribute  = m_Session.getArraySelectedAttribute();
		
		// Parte per creare un instances che contiene tutti gli attributi selezionati
		if(m_Session.getUseAllAttributeSelected()) {
			measureName = createInstancesForMultipleAttribute();
			m_Session.setID_M(measureName);
			
			maxProgress = m_Session.numClassifierSelected();
			comanPanel.setNmaxProgressBar(maxProgress);
			
			Instances instancesForProcessCopy = null;
			Instances instsBalanced = null;
			Instance tmpInst;
			int countTrue = 0;
			calendar = new GregorianCalendar();
			long seed = calendar.getTimeInMillis();
			Random rand = new Random(seed);
			
			// Creates balanced set
			if(m_Session.isUseBalancedDataSet()) {

				// Creates attInfo, contains the informations for attributes
				Attribute classIndexAttribute = m_Instances.attribute(0);
				FastVector attInfo = new FastVector();
				attInfo.addElement(classIndexAttribute);
				for(int i=1; i<instancesForProcess.numAttributes(); i++) {
					attribute = m_Instances.attribute(i);
					if(!attribute.isNumeric()) {
						attribute = new Attribute("TruthNumeric");
					}
					attInfo.addElement(attribute);
				}
				
				for(int i1=0; i1<m_Session.getNumBalancedDataset(); i1++) {

					instancesForProcessCopy = new Instances(instancesForProcess);
					instsBalanced = new Instances("Balanced"+i1,attInfo,0);
					
					// Insert the true instance in instsBalanced
					for(int j=0; j<instancesForProcessCopy.numInstances(); j++) {
						tmpInst = instancesForProcessCopy.instance(j); 
						if(tmpInst.value(0) == 1) {
							instsBalanced.add(tmpInst);
							countTrue++;
							instancesForProcessCopy.delete(j);
							if(j>0) { j--; }
						}
					}

					// Insert the same number of true instance for the false instance

					for(int j=0; j<countTrue; j++) {
						int numInsts = instancesForProcessCopy.numInstances();
						int pos = rand.nextInt(numInsts);
						tmpInst = instancesForProcessCopy.instance(pos);
						instsBalanced.add(tmpInst);
						instancesForProcessCopy.delete(pos);
					}

					countTrue = 0;

					instsBalanced.randomize(rand);
					m_Session.addBalancedDataSet(instsBalanced);

				}
			} else {
				m_Session.addBalancedDataSet(instancesForProcess);
			}

			methodCreateTS.startProcess();
		} 
		// Funzionalità vecchia
		else {
			Attribute classIndexAttribute = m_Instances.attribute(0);
			double [] classIndexValue = m_Instances.attributeToDoubleArray(0);
			
			maxProgress = m_Session.getNumSelectedAttribute() * m_Session.numClassifierSelected();
			comanPanel.setNmaxProgressBar(maxProgress);

			for(int i=0; i<arraySelectedAttribute.length; i++) {
				calendar = new GregorianCalendar();
				Instance inst = null;
				Instances instsNew = null;
				Instances instsNewCopy = null;

				long seed = calendar.getTimeInMillis();
				Random rand = new Random(seed);
				int countTrue = 0;
				Instances instsBalanced = null;

				// Creates attInfo, contains the informations for attributes
				FastVector attInfo = new FastVector();
				attInfo.addElement(classIndexAttribute);
				indexAttribute = arraySelectedAttribute[i];
				attribute = m_Instances.attribute(indexAttribute);
				if(!attribute.isNumeric()) {
					attribute = new Attribute("TruthNumeric");
				}
				attributeValues = m_Instances.attributeToDoubleArray(indexAttribute);
				attInfo.addElement(attribute);

				// Sets the attribute ID_M to store in DB
				measureName = attribute.toString();
				measureName = measureName.replace("@attribute ","");
				measureName = measureName.replace("'", "");
				measureName = measureName.replace("' numeric", "");
				m_Session.setID_M(measureName);

				// Creates a new instances contains two only attributes, the truth and a similarity attribute
				instsNew = new Instances("prova",attInfo,0);
				for(int k=0; k<attributeValues.length; k++) {
					inst = new Instance(2);
					inst.setValue(0/*classIndexAttribute*/, classIndexValue[k]);
					inst.setValue(attribute, attributeValues[k]);
					instsNew.add(inst);
				}
				
//				instsNew.sort(1);
//				System.out.println(instsNew);
				
				// Creates balanced set
				if(m_Session.isUseBalancedDataSet()) {

					for(int i1=0; i1<m_Session.getNumBalancedDataset(); i1++) {

						instsNewCopy = new Instances(instsNew);
						instsBalanced = new Instances("Balanced"+i1,attInfo,0);

						// Insert the true instance in instsBalanced
						for(int j=0; j<instsNewCopy.numInstances(); j++) {
							inst = instsNewCopy.instance(j); 
							if(inst.value(0) == 1) {
								instsBalanced.add(inst);
								countTrue++;
								instsNewCopy.delete(j);
								if(j>0) { j--; }
							}
						}

						// Insert the same number of true instance for the false instance

						for(int j=0; j<countTrue; j++) {
							int numInsts = instsNewCopy.numInstances();
							int pos = rand.nextInt(numInsts);
							inst = instsNewCopy.instance(pos);
							instsBalanced.add(inst);
							instsNewCopy.delete(pos);
						}

						countTrue = 0;

						instsBalanced.randomize(rand);
						m_Session.addBalancedDataSet(instsBalanced);

					}
				} else {
					//				System.out.println(instsNew.numInstances());
//					Instances instSorted = new Instances("sorted", attInfo, 0);
//					double max = 0;
//					double valoreInst;
//					int indice = 0;
//					while(0<instsNew.numInstances()) {
//						for(int j=0; j<instsNew.numInstances(); j++) {
//							valoreInst = instsNew.instance(j).value(1); 
//							if(valoreInst>max) {
//								max = valoreInst;
//								indice = j;
//							}
//						}
//						instSorted.add(instsNew.instance(indice));
//						instsNew.delete(indice);
//						max = 0;
//					}
//					System.out.println("num inst: " +instSorted.numInstances());
//					System.out.println(instSorted);
//					System.out.println();
					
//					instsNew.sort(1);
//					System.out.println(instsNew);
					m_Session.addBalancedDataSet(instsNew);
//					m_Session.addBalancedDataSet(instSorted);
				}

				methodCreateTS.startProcess();
			}
		
		}


	}
	
	
	// This method create an instances with selected attribute.
	private String createInstancesForMultipleAttribute() {
		// The return value
		String measureName = new String();
		// Contains the instances for processing
		instancesForProcess = new Instances(m_Instances);
		// Set the class index
		instancesForProcess.setClassIndex(0);
		
		// Create vector to contains the index of attribute for create instancesForProcess
		Vector<Integer> attributesToNoDelete = new Vector<Integer>(); 
		for(int i=0; i<arraySelectedAttribute.length; i++) {
			attributesToNoDelete.add(arraySelectedAttribute[i]);
		}
		
		// Create effective instances
		for(int i=instancesForProcess.numAttributes()-1; i>0; i--) {
			if(!attributesToNoDelete.contains(i)) {
				instancesForProcess.deleteAttributeAt(i);
			}
			
		}
		
		// Create the return string, contains the name of attributes.
		for(int i=1; i<instancesForProcess.numAttributes(); i++) {
			measureName += instancesForProcess.attribute(i).toString() +", ";	
		}
		
		measureName = measureName.replace("@attribute ","");
		measureName = measureName.replace("'", "");
		measureName = measureName.replace("' numeric", "");
		
		return measureName;
		
	}
	
//	// This method create an instances with selected attribute.
//	private void createInstancesForMultipleAttribute(Attribute classIndexAttribute) {
//		// Contains the instances for processing
//		Instances instancesForProces = null;
//		// Temporary instance
//		Instances tempInst = null;
//		// Matrix for attributes values
//		
//		
//		// Creates attInfo, contains the informations for selected attributes
//		FastVector attInfo = new FastVector();
//		// set class index attribute
//		attInfo.addElement(classIndexAttribute);
//		
//		// sets all selected attributes in attInfo
//		for(int i=0; i<arraySelectedAttribute.length; i++) {
//			indexAttribute = arraySelectedAttribute[i];
//			attribute = m_Instances.attribute(indexAttribute);
//			
//			if(!attribute.isNumeric()) {
//				attribute = new Attribute("TruthNumeric");
//			}
//			
//			attInfo.addElement(attribute);
//			
//			// Sets the attribute ID_M to store in DB
//			measureName += attribute.toString();
//			measureName = measureName.replace("@attribute ",""); // Queste cose si dovrebbero poter fare fuori dal ciclo
//			measureName = measureName.replace("'", "");
//			measureName = measureName.replace("' numeric", "");
//		
//		}
//		m_Session.setID_M(measureName);	
//		
//		// For all selected attributes set attribute values in instancesForProces
//		for(int j=1; j<arraySelectedAttribute.length; j++) {
//			tmpInst = null;
//			indexAttribute = arraySelectedAttribute[j];
//			attributeValues = m_Instances.attributeToDoubleArray(indexAttribute);
//			
//			int attributeValuesLength = attributeValues.length;
//			
//			// Creates a new instances contains the selected attributes, the truth and a similarity attributes
//			instancesForProces = new Instances("prova",attInfo,0);
//			// Create new Instances for all attribute Values
//			for(int k=0; k<attributeValuesLength; k++) {
//			tempInst = new Instance(arraySelectedAttribute.length-1);
//							inst.setValue(0/*classIndexAttribute*/, classIndexValue[k]);
//							inst.setValue(attribute, attributeValues[k]);
//							instsNew.add(inst);
//						}
//			}				
//			
//		}
//	}

}
