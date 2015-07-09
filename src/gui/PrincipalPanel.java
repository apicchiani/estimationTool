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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import common.SaveChoose;

/** The main panel of application */
public class PrincipalPanel extends JPanel {

	/** for serialization */
	private static final long serialVersionUID = 7595460028798989080L;

	/**
	 * A common interface for panels to be displayed in the ComanPanel
	 * 
	 */
	public static interface PrincipalInterfacePanel {

		/**
		 * Sets the PrincipalPanel to use as parent frame (used for sending notifications
		 * about changes in the data)
		 * 
		 * @param parent	the parent frame
		 */
		public void setPrincipal(PrincipalPanel parent);

		/**
		 * returns the parent PrincipalPanel frame
		 * 
		 * @return		the parent
		 */
		public PrincipalPanel getPrincipal();

		public void setSession(SaveChoose session);
		
		/**
		 * Returns the title for the tab in the PrincipalPanel
		 * 
		 * @return the title of this tab
		 */
		public String getTabTitle();

		/**
		 * Returns the tooltip for the tab in the PrincipalPanel
		 * 
		 * @return the tooltip of this tab
		 */
		public String getTabTitleToolTip();
	}

	/** The panel for pre-processing instances */
	protected PreprocessPanel m_PreprocessPanel = new PreprocessPanel();

	/** Contains all the additional panels apart from the pre-processing panel */
	protected Vector<PrincipalInterfacePanel> m_Panels = new Vector<PrincipalInterfacePanel>();

	/** The tabbed pane that controls which sub-pane we are working with */
	protected JTabbedPane m_TabbedPane = new JTabbedPane();
	
	/** A thread that classification runs in */
	protected Thread m_RunThread;
	
	/** Click to start Classifier */
	protected JButton m_StartClassifier = new JButton("Start Classifier...");
	
	private JProgressBar m_ProgressBar = null;
    private JTextArea m_TaskOutput = null;
	
	/** The session to run in progress */
	protected SaveChoose session = new SaveChoose();
	
	public PrincipalPanel() {
		session.setClassifier();
		m_PreprocessPanel.setSession(session);
		m_PreprocessPanel.setPrincipal(this);
		m_TabbedPane.addTab(
				m_PreprocessPanel.getTabTitle(),
				null,
				m_PreprocessPanel,
				m_PreprocessPanel.getToolTipText());

		// initialize additional panels
		String[] tabs = PrincipalDefaults.getTabs();

		Hashtable<String, HashSet> tabOptions = new Hashtable<String, HashSet>();
		for (int i = 0; i < tabs.length; i++) {
			try {
				// determine classname
				String classname = tabs[i];

				// setup panel
				PrincipalInterfacePanel panel = (PrincipalInterfacePanel) Class.forName(classname).newInstance();
				panel.setPrincipal(this);
				panel.setSession(session);
				m_Panels.add(panel);
				m_TabbedPane.addTab(
						panel.getTabTitle(), null, (JPanel) panel, panel.getTabTitleToolTip());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// setup tabbed pane
	    m_TabbedPane.setSelectedIndex(0);
	    
	    m_StartClassifier.setToolTipText("Start classifier for selected attributes");
		m_StartClassifier.setEnabled(false);
		
		m_StartClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_ProgressBar.setValue(0);
				 
				if (m_RunThread == null) {
					synchronized (this) {
						
					}
					m_RunThread = new Thread() {
						public void run() {
							m_PreprocessPanel.startClassifier();
						}
					};
					m_RunThread.setPriority(Thread.MIN_PRIORITY);
					m_RunThread.start();
				}
				m_RunThread = null;
				m_StartClassifier.setEnabled(false);
				
//				m_PreprocessPanel.startClassifier();
//				m_ProgressBar.setValue(0);
			}
		});
		
        m_ProgressBar = new JProgressBar();
        m_ProgressBar.setValue(0);
        m_ProgressBar.setStringPainted(true);

        m_TaskOutput = new JTextArea(5, 20);
        m_TaskOutput.setMargin(new Insets(5,5,5,5));
        m_TaskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(m_StartClassifier);
        panel.add(m_ProgressBar);
        panel.setVisible(true);

		// add components to layout
		setLayout(new BorderLayout());
		add(m_TabbedPane, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(new JScrollPane(m_TaskOutput), BorderLayout.SOUTH);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
	}
	
	
	public void setStartClassifierEnabled(boolean bool) {
		this.m_StartClassifier.setEnabled(bool);
	}
	
	public void setNmaxProgressBar(int n) {
		m_ProgressBar.setMaximum(n);
	}

	public void propertyChange(int progress, String completed) {
		m_ProgressBar.setValue(progress);
		m_TaskOutput.append(completed);
	}
	
	public void printError(String string) {
		m_TaskOutput.append(string);
	}
	
	private static PrincipalPanel m_PrincipalPanel;

	public static void main(String [] args) {

		try {

			m_PrincipalPanel = new PrincipalPanel();
			final JFrame jf = new JFrame("Principal panel");
			jf.getContentPane().setLayout(new BorderLayout());
			jf.getContentPane().add(m_PrincipalPanel, BorderLayout.CENTER);
			jf.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					jf.dispose();
					System.exit(0);
				}
			});
			jf.pack();
			jf.setSize(1024, 768);
			jf.setVisible(true);
		
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}
	
	
}

