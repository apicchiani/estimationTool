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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.SaveChoose;
import gui.PrincipalPanel.PrincipalInterfacePanel;

public class ValidationPanel extends JPanel
	implements PrincipalInterfacePanel {

	/** for serialization */
	private static final long serialVersionUID = -7061177071924108242L;
	
	/** Click to set CrossValidation for validation */
//	protected JCheckBox m_CrossValidation;

	/** Label for crossValidation */
	protected JLabel m_CrossValidationLab = new JLabel("Cross Validation ", SwingConstants.RIGHT);
	
	/** Label by where the number of folds for cross validation */
	protected JLabel m_FoldLab = new JLabel("Folds: ", SwingConstants.RIGHT);
	
	/** The field where the number of folds for cross validation are entered */
	protected JTextField m_FoldText = new JTextField("10", 3);
	
	/** the parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session;
	
//	ItemListener m_CheckListener = new ItemListener() {
//		public void itemStateChanged(ItemEvent e) {
//			updateCheckBox();
//		}
//	};
	
	ActionListener m_FoldTestListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_FoldText.getText();
			Integer nFolds = Integer.parseInt(text);
			m_Session.setNFolds(nFolds);
		}
	};
	
	public ValidationPanel() {
//		m_CrossValidation = new JCheckBox("", null, true);
		
//		m_CrossValidation.addItemListener(m_CheckListener);
		
		m_FoldText.addActionListener(m_FoldTestListener);
		
		setBorder(BorderFactory.createTitledBorder("Validation"));
		JPanel checkBoxPanel = new JPanel();
		GridBagLayout gbL = new GridBagLayout();
	    checkBoxPanel.setLayout(gbL);
	    checkBoxPanel.setBorder(BorderFactory.createCompoundBorder(
			 BorderFactory.createTitledBorder("Method for validation"),
			 BorderFactory.createEmptyBorder(0, 5, 5, 5)
			 ));
	    GridBagConstraints gbC = new GridBagConstraints();
//	    gbC.anchor = GridBagConstraints.WEST;
//	    gbC.gridy = 0;     gbC.gridx = 0;
//	    gbL.setConstraints(m_CrossValidation, gbC);
//	    checkBoxPanel.add(m_CrossValidation);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 0;     gbC.gridx = 0;
//	    gbC.insets = new Insets(2, 10, 2, 10);
	    gbL.setConstraints(m_CrossValidationLab, gbC);
	    checkBoxPanel.add(m_CrossValidationLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 0;     gbC.gridx = 1;
//	    gbC.insets = new Insets(2, 10, 2, 10);
	    gbL.setConstraints(m_FoldLab, gbC);
	    checkBoxPanel.add(m_FoldLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 0;     gbC.gridx = 2;
//	    gbC.insets = new Insets(2, 10, 2, 10);
	    gbL.setConstraints(this.m_FoldText, gbC);
	    checkBoxPanel.add(m_FoldText);
	    
	    setLayout(new BorderLayout());
	    add(checkBoxPanel, BorderLayout.NORTH);
	    
	    
	}
	
	
//	private void updateCheckBox() {
//		m_Session.setUsingValidation(1,m_CrossValidation.isSelected());
//		m_FoldLab.setEnabled(m_CrossValidation.isSelected());
//		m_FoldText.setEnabled(m_CrossValidation.isSelected());
//	}

	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Validation";
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
		m_Session = session;
		
	}
	
}
