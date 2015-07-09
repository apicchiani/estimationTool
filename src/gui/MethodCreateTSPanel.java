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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


import common.SaveChoose;
import gui.PrincipalPanel.PrincipalInterfacePanel;

public class MethodCreateTSPanel extends JPanel
	implements PrincipalInterfacePanel {

	/** for serialization */
	private static final long serialVersionUID = 7885894678532508664L;

	/** Click to set Linear mode to create TS */
	protected JCheckBox m_Ranked;
	
	/** Click to set Random mode to create TS */
	protected JCheckBox m_Random;
	
	/** Click to set Mixed mode to create TS */
	protected JCheckBox m_Mixed;
	
	/** Click to set exhaustive mode to create TS */
	protected JCheckBox m_Exhaustive;
	
	/** Click to set balanced folds mode into exhaustive */
	protected JCheckBox m_StratifiedFoldsExhaustive;
	
	/** Click to set Ten-Fold mode*/
	protected JCheckBox m_TenFold;
	
	/** Click to set stratified folds for Ten-Fold */
	protected JCheckBox m_StratifiedFoldsTenFold;
	
	/** Label by where the number of random TS are entered */
	protected JLabel m_numberRndLab = new JLabel("Number of Random TS: ", SwingConstants.RIGHT);
	
	/** The field where the number of random TS are entered */
	protected JTextField m_numberRndText = new JTextField("10", 3);
	
	/** Label by where the number of mixed TS are entered */
	protected JLabel m_numberMixedLab = new JLabel("Number of mixed TS: ", SwingConstants.RIGHT);
	
	/** The field where the number of random TS are entered */
	protected JTextField m_numberMixedText = new JTextField("10", 3);
	
	/** Label by where the number of ripetition for exhaustive are entered */
	protected JLabel m_numberRipetitionExhaustiveLab = new JLabel("Number of ripetition: ", SwingConstants.RIGHT);
	
	/** The field where the number of ripetition for exhaustive are entered */
	protected JTextField m_numberRipetitionExhaustiveText = new JTextField("10", 3);
	
	/** Label by where the number of ripetition for Ten-Fold are entered */
	protected JLabel m_numberRipetitionTenFoldLab = new JLabel("Number of ripetition: ", SwingConstants.RIGHT);
	
	/** The field where the number of ripetition for exhaustive are entered */
	protected JTextField m_numberRipetitionTenFoldText = new JTextField("1", 3);
	
	/** Label by where the percentual of ranked selection in mixed mode for TS are entered */
	protected JLabel m_PercentRankedLab = new JLabel("Percent Ranked: ", SwingConstants.RIGHT);
	
	/** The field where the percentual of ranked selection in mixed mode for TS are entered */
	protected JTextField m_PercentRankedText = new JTextField("50", 3);
	
	/** Label by where the percentual of random selection in mixed mode for TS are entered */
	protected JLabel m_PercentRndLab = new JLabel("Percent Random: ", SwingConstants.RIGHT);
	
	/** The field where the percentual of Random selection in mixed mode for TS are entered */
	protected JTextField m_PercentRndText = new JTextField("50", 3);
	
	ItemListener m_CheckListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			updateCheckBox();
		}
	};
	
	ItemListener m_StratiedFoldsExhaustiveListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			m_Session.setBalancedFolds(m_StratifiedFoldsExhaustive.isSelected());
		}
	};
	
	ItemListener m_StratiedFoldsTenFoldListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			m_Session.setStratifiedFoldsForTenFold(m_StratifiedFoldsTenFold.isSelected());
		}
	};
	
	ActionListener m_PercentMixedListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_PercentRndText.getText();
			Integer percentRnd = Integer.parseInt(text);
			m_Session.setPercentMixed(percentRnd);
			Integer percentRanked = 100 - percentRnd;
			m_PercentRankedText.setText(percentRanked.toString());
		}
	};
	
	ActionListener m_numberRndTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_numberRndText.getText();
			Integer numberRnd = Integer.parseInt(text);
			m_Session.setNumberRnd(numberRnd);
		}
	};
	
	ActionListener m_numberMixedTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_numberMixedText.getText();
			Integer numberMixed = Integer.parseInt(text);
			m_Session.setNumberMixed(numberMixed);
		}
	};
	
	ActionListener m_numberRipetitionExhaustiveTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_numberRipetitionExhaustiveText.getText();
			Integer numberRipetitionExhaustive = Integer.parseInt(text);
			m_Session.setNumberRipetitionExhaustive(numberRipetitionExhaustive);
		}
	};
	
	ActionListener m_numberRipetitionTenFoldTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_numberRipetitionTenFoldText.getText();
			Integer numberRipetitionTenFold = Integer.parseInt(text);
			m_Session.setNumberRipetitionTenFold(numberRipetitionTenFold);
		}
	};
	
	/** The parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session;
	
	public MethodCreateTSPanel() {
		
		m_Ranked = new JCheckBox("Ranked", null, true);
		m_Random = new JCheckBox("Random     ", null, false);
		m_Mixed = new JCheckBox("Mixed", null, false);
		m_Exhaustive = new JCheckBox("Modified N-fold Cross-Validation", null, true);
		m_StratifiedFoldsExhaustive = new JCheckBox("Stratification", null, true);
		m_TenFold = new JCheckBox("Ten-Fold", null, true);
		m_StratifiedFoldsTenFold = new JCheckBox("Stratification", null, true);
		
		m_Ranked.addItemListener(m_CheckListener);
	    m_Random.addItemListener(m_CheckListener);
	    m_Mixed.addItemListener(m_CheckListener);
	    m_Exhaustive.addItemListener(m_CheckListener);
	    m_StratifiedFoldsExhaustive.addItemListener(m_StratiedFoldsExhaustiveListener);
	    m_TenFold.addItemListener(m_CheckListener);
		m_StratifiedFoldsTenFold.addItemListener(m_StratiedFoldsTenFoldListener);
	    
	    m_numberRndLab.setEnabled(false);
	    m_numberMixedLab.setEnabled(false);
	    m_numberRndText.setEnabled(false);
	    m_numberMixedText.setEnabled(false);
	    
	    m_numberRndText.addActionListener(m_numberRndTextListener);
	    m_numberMixedText.addActionListener(m_numberMixedTextListener);
	    m_numberRipetitionExhaustiveText.addActionListener(m_numberRipetitionExhaustiveTextListener);
	    m_numberRipetitionTenFoldText.addActionListener(m_numberRipetitionTenFoldTextListener);
	    
	    m_PercentRndLab.setEnabled(false);
	    m_PercentRankedLab.setEnabled(false);
	    m_PercentRndText.setEnabled(false);
	    m_PercentRankedText.setEnabled(false);
	    
	    m_PercentRndText.addActionListener(m_PercentMixedListener);
	    m_PercentRankedText.setEditable(false);
		
		JPanel checkBoxPanel = new JPanel();
		GridBagLayout gbL = new GridBagLayout();
	    checkBoxPanel.setLayout(gbL);
	    checkBoxPanel.setBorder(BorderFactory.createCompoundBorder(
			 BorderFactory.createTitledBorder("Method for creating Trainig Set"),
			 BorderFactory.createEmptyBorder(0, 5, 5, 5)
			 ));
	    GridBagConstraints gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 0;     gbC.gridx = 0;
	    gbL.setConstraints(m_Ranked, gbC);
	    checkBoxPanel.add(m_Ranked);

	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 1;     gbC.gridx = 0;
	    gbL.setConstraints(m_Random, gbC);
	    checkBoxPanel.add(m_Random);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 1;     gbC.gridx = 1; 
	    gbL.setConstraints(m_numberRndLab, gbC);
	    checkBoxPanel.add(m_numberRndLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 1;     gbC.gridx = 2;
	    gbL.setConstraints(m_numberRndText, gbC);
	    checkBoxPanel.add(m_numberRndText);

	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 2;     gbC.gridx = 0;
	    gbL.setConstraints(m_Mixed, gbC);
	    checkBoxPanel.add(m_Mixed);

	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 2;     gbC.gridx = 1;
//	    gbC.insets = new Insets(2, 10, 2, 10);
	    gbL.setConstraints(m_PercentRndLab, gbC);
	    checkBoxPanel.add(m_PercentRndLab);

	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 2;     gbC.gridx = 2;  gbC.weightx = 100;
	    gbC.ipadx = 20;
	    gbL.setConstraints(m_PercentRndText, gbC);
	    checkBoxPanel.add(m_PercentRndText);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 2;     gbC.gridx = 3;
//	    gbC.insets = new Insets(2, 10, 2, 10);
	    gbL.setConstraints(m_PercentRankedLab, gbC);
	    checkBoxPanel.add(m_PercentRankedLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.gridy = 2;     gbC.gridx = 4;  gbC.weightx = 100;
	    gbC.ipadx = 20;
	    gbL.setConstraints(m_PercentRankedText, gbC);
	    checkBoxPanel.add(m_PercentRankedText);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 2;     gbC.gridx = 5; 
	    gbL.setConstraints(m_numberMixedLab, gbC);
	    checkBoxPanel.add(m_numberMixedLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 2;     gbC.gridx = 6; 
	    gbL.setConstraints(m_numberMixedText, gbC);
	    checkBoxPanel.add(m_numberMixedText);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 3;     gbC.gridx = 0;
	    gbL.setConstraints(m_Exhaustive, gbC);
	    checkBoxPanel.add(m_Exhaustive);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 3;     gbC.gridx = 1;
	    gbL.setConstraints(m_StratifiedFoldsExhaustive, gbC);
	    checkBoxPanel.add(m_StratifiedFoldsExhaustive);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 3;     gbC.gridx = 2; 
	    gbL.setConstraints(m_numberRipetitionExhaustiveLab, gbC);
	    checkBoxPanel.add(m_numberRipetitionExhaustiveLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 3;     gbC.gridx = 3;
	    gbL.setConstraints(m_numberRipetitionExhaustiveText, gbC);
	    checkBoxPanel.add(m_numberRipetitionExhaustiveText);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 4;     gbC.gridx = 0;
	    gbL.setConstraints(m_TenFold, gbC);
	    checkBoxPanel.add(m_TenFold);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 4;     gbC.gridx = 1;
	    gbL.setConstraints(m_StratifiedFoldsTenFold, gbC);
	    checkBoxPanel.add(m_StratifiedFoldsTenFold);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 4;     gbC.gridx = 2; 
	    gbL.setConstraints(m_numberRipetitionTenFoldLab, gbC);
	    checkBoxPanel.add(m_numberRipetitionTenFoldLab);
	    
	    gbC = new GridBagConstraints();
	    gbC.anchor = GridBagConstraints.WEST;
	    gbC.gridy = 4;     gbC.gridx = 3;
	    gbL.setConstraints(m_numberRipetitionTenFoldText, gbC);
	    checkBoxPanel.add(m_numberRipetitionTenFoldText);
	    
	    setLayout(new BorderLayout());
	    add(checkBoxPanel, BorderLayout.CENTER);
	}

	private void updateCheckBox() {
		m_Session.setMethod(0,m_Ranked.isSelected());
		m_Session.setMethod(1,m_Random.isSelected());
		m_Session.setMethod(2,m_Mixed.isSelected());
		m_Session.setMethod(3, m_Exhaustive.isSelected());
		m_Session.setMethod(4, m_TenFold.isSelected());
		m_numberRndLab.setEnabled(m_Random.isSelected());
		m_numberRndText.setEnabled(m_Random.isSelected());
		m_PercentRankedLab.setEnabled(m_Mixed.isSelected());
		m_PercentRankedText.setEnabled(m_Mixed.isSelected());
		m_PercentRndLab.setEnabled(m_Mixed.isSelected());
		m_PercentRndText.setEnabled(m_Mixed.isSelected());
		m_numberMixedLab.setEnabled(m_Mixed.isSelected());
		m_numberMixedText.setEnabled(m_Mixed.isSelected());
		m_StratifiedFoldsExhaustive.setEnabled(m_Exhaustive.isSelected());
		m_numberRipetitionExhaustiveLab.setEnabled(m_Exhaustive.isSelected());
		m_numberRipetitionExhaustiveText.setEnabled(m_Exhaustive.isSelected());
		m_StratifiedFoldsTenFold.setEnabled(m_TenFold.isSelected());
		m_numberRipetitionTenFoldLab.setEnabled(m_TenFold.isSelected());
		m_numberRipetitionTenFoldText.setEnabled(m_TenFold.isSelected());
	}

	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Selection approach";
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
	
	public void setSession(SaveChoose session) {
		m_Session = session;
	}

}
