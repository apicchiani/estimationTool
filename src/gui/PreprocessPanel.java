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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import common.PreProcessDT;
import common.SaveChoose;
import gui.PrincipalPanel.PrincipalInterfacePanel;

import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;

public class PreprocessPanel extends JPanel
implements PrincipalInterfacePanel {

	/** for serialization */
	private static final long serialVersionUID = -4141199617966090199L;

	/** Click to load base instances from a file */
	protected JButton m_OpenFileBut = new JButton("Open file...");

	/** Click to use balanced dataset */
	protected JCheckBox m_UseBalancedDataSet;
	
	/** Click to use all attribute selected to process **/
	protected JCheckBox m_UseAllAttributeSelected;
	
	/** Label by where the number of balanced dataset for classifier are entered */
	protected JLabel m_NumBalancedDatasetLab = new JLabel("Number of balanced dataset: ", SwingConstants.RIGHT);
	
	/** The field where the number of balanced dataset for classifier are entered */
	protected JTextField m_NumBalancedDatasetText = new JTextField("10", 3);

	/** The file chooser for selecting data files */
	protected JFileChooser m_FileChooser 
	= new JFileChooser ("c:\\users\\coman\\documents\\tesi\\");

	/** Panel to let the user choose attributes */
	protected AttributeSelectionPanel m_AttPanel = new AttributeSelectionPanel();

	/** Panel to show basic information for file opened */
	protected FileSummaryPanel m_FileSummPanel = new FileSummaryPanel();

	/** The SaveChoose containing the parameter for create TS */
	protected SaveChoose m_Session = new SaveChoose();

	protected Instances m_Instances;
	
	protected PreProcessDT preProcessDT = null;

	/** The parent frame */
	protected PrincipalPanel m_Principal = null;

	ItemListener m_UseBalancedDataSetListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Integer numBalanced;
			boolean balancedSelected = m_UseBalancedDataSet.isSelected();
			m_Session.setUseBalancedDataSet(balancedSelected);
			m_NumBalancedDatasetLab.setEnabled(balancedSelected);
			m_NumBalancedDatasetText.setEnabled(balancedSelected);
			if(balancedSelected) {
				String text = m_NumBalancedDatasetText.getText();
				numBalanced = Integer.parseInt(text);
			} else {
				numBalanced = 1;
			}
			m_Session.setNumBalancedDataset(numBalanced);
			m_Session.initializeArrayErS();
			m_Session.initializeArrayErR();
			m_Session.initializeArrayRealCoverage();
			m_Session.initializeArrayACC();
			m_Session.initializeArrayTimestamp();
			m_Session.initializeArrayTS();
			m_Session.initializeArrayES();
			m_Session.initializeArrayCANCA();
			m_Session.initializeArrayCSNCS();
			m_Session.initializeArrayModel();
			m_Session.initializeArrayBalanced();
		}
	};
	
	// Set the value for UseAllAttributeSelected in Session
	ItemListener m_UseAllAttributeSelectedListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			boolean useAllAttributeSelected = m_UseAllAttributeSelected.isSelected();
			m_Session.setUseAllAttributeSelected(useAllAttributeSelected);
		}
	};
	
	ActionListener m_NumBalancedDatasetTextListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String text = m_NumBalancedDatasetText.getText();
			Integer numBalancedDT = Integer.parseInt(text);
			m_Session.setNumBalancedDataset(numBalancedDT);
			m_Session.initializeArrayErS();
			m_Session.initializeArrayErR();
			m_Session.initializeArrayRealCoverage();
			m_Session.initializeArrayACC();
			m_Session.initializeArrayTS();
			m_Session.initializeArrayTimestamp();
			m_Session.initializeArrayES();
			m_Session.initializeArrayCANCA();
			m_Session.initializeArrayCSNCS();
			m_Session.initializeArrayModel();
			m_Session.initializeArrayBalanced();
		}
	};
	
	public PreprocessPanel() {

		m_OpenFileBut.setToolTipText("Open a set of instances from a file");

		m_FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"arff files", "arff");
		m_FileChooser.setFileFilter(filter);

		m_OpenFileBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInstancesFromFile();
			}
		});
		
		JPanel balancedPanel = new JPanel();
		balancedPanel.setLayout(new GridLayout(1, 3));
		balancedPanel.setBorder(BorderFactory.createTitledBorder(""));
		m_UseBalancedDataSet = new JCheckBox("Use balanced dataset", null, true);
		m_UseBalancedDataSet.addItemListener(m_UseBalancedDataSetListener);
		m_UseBalancedDataSet.setEnabled(false);
		m_NumBalancedDatasetLab.setEnabled(false);
		m_NumBalancedDatasetText.setEnabled(false);
		m_NumBalancedDatasetText.addActionListener(m_NumBalancedDatasetTextListener);
		m_UseAllAttributeSelected = new JCheckBox("Use all attribute selected for process",null, false);
		m_UseAllAttributeSelected.addItemListener(m_UseAllAttributeSelectedListener);
		m_UseAllAttributeSelected.setEnabled(false);
		balancedPanel.add(m_UseBalancedDataSet);
		balancedPanel.add(m_NumBalancedDatasetLab);
		balancedPanel.add(m_NumBalancedDatasetText);
		balancedPanel.add(m_UseAllAttributeSelected);
		
		// Set up the GUI layout
		JPanel buttons = new JPanel();
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		buttons.setLayout(new GridLayout(1, 1, 5, 5));
		buttons.add(m_OpenFileBut);

		JPanel fileSummPanel = new JPanel();
		fileSummPanel.setBorder(BorderFactory
				.createTitledBorder("File basic Information"));
		fileSummPanel.setLayout(new BorderLayout());
		fileSummPanel.add(buttons, BorderLayout.NORTH);
		fileSummPanel.add(m_FileSummPanel, BorderLayout.CENTER);

		JPanel attStuffHolderPanel = new JPanel();
		attStuffHolderPanel.setBorder(BorderFactory
				.createTitledBorder("Attributes"));
		attStuffHolderPanel.setLayout(new BorderLayout());
		attStuffHolderPanel.add(balancedPanel, BorderLayout.NORTH);
		attStuffHolderPanel.add(m_AttPanel, BorderLayout.CENTER);

//		JPanel buttonsSouth = new JPanel();
//		buttonsSouth.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
//		buttonsSouth.setLayout(new GridLayout(1, 1, 5, 5));
//		buttonsSouth.add(m_StartClassifier);

		setLayout(new BorderLayout());
		add(fileSummPanel, BorderLayout.NORTH);
		add(attStuffHolderPanel, BorderLayout.CENTER);
//		add(buttonsSouth, BorderLayout.SOUTH);
	}

	/**
	 * Queries the user for a file to load instances from, then loads the
	 * instances in a background process. This is done in the IO
	 * thread, and an error message is popped up if the IO thread is busy.
	 */
	public void setInstancesFromFile() {

		String fileName;

		int returnVal = m_FileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {

			final AbstractFileLoader loader = new ArffLoader();

			try {
				loader.setSource(m_FileChooser.getSelectedFile());
				fileName = m_FileChooser.getSelectedFile().getName();
				m_Session.setID_DT(fileName);
				m_Instances = loader.getDataSet();
				m_AttPanel.setInstances(m_Instances);
				m_FileSummPanel.setFileName(fileName);
				m_FileSummPanel.setInstances(m_Instances);
				m_UseBalancedDataSet.setEnabled(true);
				this.m_NumBalancedDatasetLab.setEnabled(true);
				this.m_NumBalancedDatasetText.setEnabled(true);
				this.m_UseAllAttributeSelected.setEnabled(true);
				m_AttPanel.includeAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.m_Principal.setStartClassifierEnabled(true);

		}

	}

	public void startClassifier() {
		
		int [] arraySelectedAttribute = m_AttPanel.getSelectedAttributes();
		
		m_Session.setM_Instances(m_Instances);
		m_Session.setArraySelectedAttribute(arraySelectedAttribute);
		m_Session.setNumSelectedAttribute(arraySelectedAttribute.length);
		
		
			preProcessDT = new PreProcessDT(m_Principal,m_Session);
			preProcessDT.startPreProcess();

			m_Principal.m_StartClassifier.setEnabled(true);
		}
		

	/**
	 * Sets the Explorer to use as parent frame (used for sending notifications
	 * about changes in the data)
	 * 
	 * @param parent	the parent frame
	 */
	public void setPrincipal(PrincipalPanel parent) {
		m_Principal = parent;
	}

	/**
	 * returns the parent Explorer frame
	 * 
	 * @return		the parent
	 */
	public PrincipalPanel getPrincipal() {
		return m_Principal;
	}
	
	public void setSession(SaveChoose session) {
		m_Session = session;
	}

	/**
	 * Returns the title for the tab in the Explorer
	 * 
	 * @return 		the title of this tab
	 */
	public String getTabTitle() {
		return "Preprocess";
	}

	/**
	 * Returns the tooltip for the tab in the Explorer
	 * 
	 * @return 		the tooltip of this tab
	 */
	public String getTabTitleToolTip() {
		return "Open/Edit/Save instances";
	}

	public static void main(String [] args) {

		try {
			final JFrame jf = new JFrame("");
			jf.getContentPane().setLayout(new BorderLayout());
			final PreprocessPanel sp = new PreprocessPanel();
			jf.getContentPane().add(sp, BorderLayout.CENTER);
			jf.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					jf.dispose();
					System.exit(0);
				}
			});
			jf.pack();
			jf.setSize(800, 600);
			jf.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}

}
