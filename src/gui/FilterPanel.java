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
import java.awt.GridLayout;
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

public class FilterPanel extends JPanel
implements PrincipalInterfacePanel {

	/** For serialization */
	private static final long serialVersionUID = 7104298413784543620L;

	/** The parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session;
	
	/** Label by where the size of increment are entered */
	protected JLabel m_StepLab = new JLabel("Step: ", SwingConstants.RIGHT);

	/** The field where the step are entered */
	protected JTextField m_StepText = new JTextField("10", 3);
	
	/** Label by where the minimum size for TS are entered */
	protected JLabel m_MinTSLab = new JLabel("Minimum size: ", SwingConstants.RIGHT);

	/** The field where the minimum size for TS are entered */
	protected JTextField m_MinTSText = new JTextField("10", 3);
	
	/** Label by where the maximum size for TS are entered */
	protected JLabel m_MaxTSLab = new JLabel("Maximum size: ", SwingConstants.RIGHT);

	/** The field where the maximum size for TS are entered */
	protected JTextField m_MaxTSText = new JTextField("70", 3);
	
	/** The panel where insert the minimal reliability */
	protected ControlPanel controlPanel = new ControlPanel();
	
	/** Checkbox for attivate coverage control */
	protected JCheckBox m_CoverageCheckBox;
	
	/** Label for minimum value of real coverage */
	protected JLabel m_RealCoverageLab = new JLabel("The minimum value of real coverage: ", SwingConstants.RIGHT);
	
	/** The field where the number of real coverage are entered */
	protected JTextField m_RealCoverageText = new JTextField("0.1", 4 );
	
	ActionListener m_StepTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_StepText.getText();
			Integer inc = Integer.parseInt(text);
			m_Session.setIncTS(inc);
		}
	};
	
	ActionListener m_MinTSListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_MinTSText.getText();
			Integer minTS = Integer.parseInt(text);
			m_Session.setMinTS(minTS);
		}
	};
	
	ActionListener m_MaxTSListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_MaxTSText.getText();
			Integer maxTS = Integer.parseInt(text);
			m_Session.setMaxTS(maxTS);
		}
	};
	
	ActionListener m_RealCoverageTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_RealCoverageText.getText();
			Double realCoverage = Double.parseDouble(text);
			m_Session.setRealCoverage(realCoverage);
		}
	};
	
	ItemListener m_CoverageCheckBoxListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			m_Session.setControlRealCoverage(m_CoverageCheckBox.isSelected());
			m_RealCoverageLab.setEnabled(m_CoverageCheckBox.isSelected());
			m_RealCoverageText.setEnabled(m_CoverageCheckBox.isSelected());
			
		}
	};
	
	public FilterPanel() {
		
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(1, 3));
		labelPanel.setBorder(BorderFactory.createTitledBorder("Control general parameter for Training Set"));
		labelPanel.add(m_StepLab);
		labelPanel.add(m_StepText);
		labelPanel.add(m_MinTSLab);
		labelPanel.add(m_MinTSText);
		labelPanel.add(m_MaxTSLab);
		labelPanel.add(m_MaxTSText);
		
		m_StepText.addActionListener(m_StepTextListener);
		m_MinTSText.addActionListener(m_MinTSListener);
		m_MaxTSText.addActionListener(m_MaxTSListener);
		
		m_CoverageCheckBox = new JCheckBox("", null, false);
		
		m_CoverageCheckBox.addItemListener(m_CoverageCheckBoxListener);
		
		m_RealCoverageLab.setEnabled(false);
		m_RealCoverageText.setEnabled(false);
		
		JPanel coveragePanel = new JPanel();
		coveragePanel.setLayout(new GridLayout(1, 3));
		coveragePanel.setBorder(BorderFactory.createTitledBorder("Real coverage control"));
		coveragePanel.add(m_CoverageCheckBox);
		coveragePanel.add(m_RealCoverageLab);
		coveragePanel.add(m_RealCoverageText);
		
		m_RealCoverageText.addActionListener(m_RealCoverageTextListener);
		
		setLayout(new BorderLayout());
	    add(labelPanel, BorderLayout.NORTH);
	    add(controlPanel, BorderLayout.CENTER);
	    add(coveragePanel, BorderLayout.SOUTH);
	}
	
	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Filter";
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
		controlPanel.setSession(m_Session);
	}

}
