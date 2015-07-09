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

package gui;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import common.SaveChoose;

import gui.PrincipalPanel.PrincipalInterfacePanel;

public class ClassifierPanel extends JPanel
	implements PrincipalInterfacePanel {

	/** for serialization */
	private static final long serialVersionUID = -7553288061011602914L;
	
	/** Contains the panel for each type of classifier */
	private Vector<ClassifierSelectionPanel> classifiersPanel = new Vector<ClassifierSelectionPanel>();
	
	/** Contains the name of each classifier */
	private String[] classifierName = null;

	/** The parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session = null;
	
	/** Constructor for the classifier Panel */ 
	public ClassifierPanel() {
		Vector<String> typeClassifiers = new Vector<String>();
		String[] classifier;
		String type;
		
		/** Get the classifier in use */
		classifierName = PrincipalDefaults.getClassifiers();
		
		setBorder(BorderFactory.createTitledBorder("Classifier"));
		setLayout(new GridLayout(2,4));
		
		/** Creates and set the classifierSelection Panel */
		for(int i=0; i<classifierName.length; i++) {
			classifier = classifierName[i].split("\\.");
			type = classifier[2];
			if(!typeClassifiers.contains(type)) {
				typeClassifiers.add(type);
				classifiersPanel.add(new ClassifierSelectionPanel(type));
				classifiersPanel.lastElement().setOffsetUseClassifier(i);
			}
			
			if(typeClassifiers.contains(type)) {
				classifiersPanel.lastElement().addClassifier(classifierName[i],classifier[classifier.length-1]);
			}
		}
		
		for(ClassifierSelectionPanel classifierSelectionPanel : classifiersPanel) {
			classifierSelectionPanel.setClassifiers();
			add(classifierSelectionPanel);
		}
	}
	
	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Classifier";
	}

	@Override
	public String getTabTitleToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrincipal(PrincipalPanel parent) {
		// TODO Auto-generated method stub
		m_Coman = parent;
	}

	@Override
	public void setSession(SaveChoose session) {
		// TODO Auto-generated method stub
		this.m_Session = session;
		for(ClassifierSelectionPanel classifierSelectionPanel : classifiersPanel) {
			classifierSelectionPanel.setSession(m_Session);
		}
	}

}
