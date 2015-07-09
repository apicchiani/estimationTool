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
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Validating {

	private SaveChoose m_Session;

	public void crossValidation(Instances data, Classifier cls, Evaluation eval) {

		try {
			Instances inst = new Instances(data);
//			System.out.println("Class index: " +data.classIndex());
			int folds = m_Session.getNFolds();

			for (int n = 0; n < folds; n++) {
				
				Instances train = inst.trainCV(folds, n);
				Instances test = inst.testCV(folds, n);
				
//				System.out.println(train.numInstances());
//				System.out.println(train);
//				System.out.println("class index train: " +train.classIndex());
//				System.out.println(test.numInstances());
//				System.out.println(test);
//				System.out.println("class index test: " +test.classIndex());

				// build and evaluate classifier
//								Classifier clsCopy = Classifier.makeCopy(cls);
//								cls = Classifier.makeCopy(cls);
				cls.buildClassifier(train);
				eval.evaluateModel(cls, test);

			}

		} catch (Exception e) {
			System.out.println(e);
		}


	}

	public void setSession(SaveChoose session) {
		m_Session = session;
	}
}
