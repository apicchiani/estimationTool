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

public class ControlPanel extends JPanel
implements PrincipalInterfacePanel {

	/** For serialization */
	private static final long serialVersionUID = 1637526501352826914L;
	
	/** Checkbox for attivate estimated error control */
	protected JCheckBox m_ErrorCheckBox;
	
	/** Label for minimal value of error */
	protected JLabel m_ErrorLab = new JLabel("The maximum value of estimated error: ", SwingConstants.RIGHT);
	
	/** The field where the number of error are entered */
	protected JTextField m_ErrorText = new JTextField("0.4", 4 );
	
	/** the parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session;
	
	ActionListener m_ErrorTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_ErrorText.getText();
			Double minReliability = Double.parseDouble(text);
			m_Session.setMaxError(minReliability);
		}
	};
	
	ItemListener m_ErrorCheckBoxListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			m_Session.setControlEstimatedError(m_ErrorCheckBox.isSelected());
			m_ErrorLab.setEnabled(m_ErrorCheckBox.isSelected());
			m_ErrorText.setEnabled(m_ErrorCheckBox.isSelected());
			
		}
	};
	
	public ControlPanel() {
		m_ErrorCheckBox = new JCheckBox("", null, false);
		
		m_ErrorCheckBox.addItemListener(m_ErrorCheckBoxListener);
		
		m_ErrorText.addActionListener(m_ErrorTextListener);
		
		m_ErrorLab.setEnabled(false);
		m_ErrorText.setEnabled(false);
		
		setBorder(BorderFactory.createTitledBorder("Estimated error control"));
		
		JPanel errorPanel = new JPanel();
		errorPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		errorPanel.setLayout(new GridLayout(1, 2, 5, 5));
		errorPanel.add(m_ErrorCheckBox);
		errorPanel.add(m_ErrorLab);
		errorPanel.add(m_ErrorText);
		
		add(errorPanel);
	}
	
	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Control";
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
