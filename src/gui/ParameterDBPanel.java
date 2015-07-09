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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.SaveChoose;
import gui.PrincipalPanel.PrincipalInterfacePanel;

public class ParameterDBPanel extends JPanel
implements PrincipalInterfacePanel {
	
	/** For serialization */
	private static final long serialVersionUID = -2919458362374811040L;

	/** The parent frame */
	protected PrincipalPanel m_Coman = null;
	
	/** The session to run in progress */
	protected SaveChoose m_Session;
	
	/** Label by where the name of Database are entered */
	protected JLabel m_NameDBLab = new JLabel("Database Name: ", SwingConstants.LEFT);

	/** The field where the name of Database are entered */
	protected JTextField m_NameDBText = new JTextField("postgres", 10);
	
	/** Label by where the name of user for Database are entered */
	protected JLabel m_UserNameDBLab = new JLabel("User: ", SwingConstants.LEFT);

	/** The field where the name of user for Database are entered */
	protected JTextField m_UserNameDBText = new JTextField("postgres", 10);
	
	/** Label by where the password of user for Database are entered */
	protected JLabel m_UserPassDBLab = new JLabel("Password: ", SwingConstants.LEFT);

	/** The field where the password of user for Database are entered */
	protected JTextField m_UserPassDBText = new JTextField("CANTONE", 10);
	
	/** Label by where the name of table are entered */
	protected JLabel m_TableDBLab = new JLabel("Nome tabella: ", SwingConstants.LEFT);

	/** The field where the name of table are entered */
	protected JTextField m_TableDBText = new JTextField("risultati");

	ActionListener m_NameDBTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_NameDBText.getText();
			m_Session.setNameDB(text);
		}
	};
	
	ActionListener m_UserNameDBTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_UserNameDBText.getText();
			m_Session.setUserNameDB(text);
		}
	};
	
	ActionListener m_UserPassDBTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_UserPassDBText.getText();
			m_Session.setUserPassDB(text);
		}
	};
	
	ActionListener m_TableDBTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_TableDBText.getText();
			m_Session.setTableDB(text);
		}
	};
	
	public ParameterDBPanel() {
		setBorder(BorderFactory.createTitledBorder("General parameters for Database connection"));
		
		JPanel labelPanel = new JPanel();
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		labelPanel.setLayout(new GridLayout(4, 2, 5, 5));
		labelPanel.add(this.m_NameDBLab);
		labelPanel.add(this.m_NameDBText);
		labelPanel.add(this.m_UserNameDBLab);
		labelPanel.add(this.m_UserNameDBText);
		labelPanel.add(this.m_UserPassDBLab);
		labelPanel.add(this.m_UserPassDBText);
		labelPanel.add(this.m_TableDBLab);
		labelPanel.add(this.m_TableDBText);

		m_NameDBText.addActionListener(m_NameDBTextListener);
		m_UserNameDBText.addActionListener(m_UserNameDBTextListener);
		m_UserPassDBText.addActionListener(m_UserPassDBTextListener);
		m_TableDBText.addActionListener(m_TableDBTextListener);		
		
		setLayout(new BorderLayout());
	    add(labelPanel, BorderLayout.CENTER);
		
	}
	
	@Override
	public PrincipalPanel getPrincipal() {
		// TODO Auto-generated method stub
		return m_Coman;
	}

	@Override
	public String getTabTitle() {
		// TODO Auto-generated method stub
		return "Parameters for DB";
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
