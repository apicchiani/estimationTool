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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import weka.core.Instances;

public class FileSummaryPanel extends JPanel {
	
	/** for serialization */
	private static final long serialVersionUID = 640313121702452379L;

	/** Message shown when no instances have been loaded */
	protected static final String NO_SOURCE = "None";

	/** Displays the name of the relation */
	protected JLabel m_FileNameLab = new JLabel(NO_SOURCE);

	/** Displays the number of instances */
	protected JLabel m_NumInstancesLab = new JLabel(NO_SOURCE);

	/** Displays the number of attributes */
	protected JLabel m_NumAttributesLab = new JLabel(NO_SOURCE);

	/** The instances we're playing with */
	protected Instances m_Instances;

	/**
	 * Creates the instances panel with no initial instances.
	 */
	public FileSummaryPanel() {

		GridBagLayout gbLayout = new GridBagLayout();
		setLayout(gbLayout);
		JLabel lab = new JLabel("Relation:", SwingConstants.RIGHT);
		lab.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		GridBagConstraints gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.EAST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 0;     gbConstraints.gridx = 0;
		gbLayout.setConstraints(lab, gbConstraints);
		add(lab);
		gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.WEST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 0;     gbConstraints.gridx = 1;
		gbConstraints.weightx = 100; gbConstraints.gridwidth = 3;
		gbLayout.setConstraints(m_FileNameLab, gbConstraints);
		add(m_FileNameLab);
		m_FileNameLab.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));

		lab = new JLabel("Instances:", SwingConstants.RIGHT);
		lab.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.EAST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 1;     gbConstraints.gridx = 0;
		gbLayout.setConstraints(lab, gbConstraints);
		add(lab);
		gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.WEST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 1;     gbConstraints.gridx = 1;
		gbConstraints.weightx = 100;
		gbLayout.setConstraints(m_NumInstancesLab, gbConstraints);
		add(m_NumInstancesLab);
		m_NumInstancesLab.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));

		lab = new JLabel("Attributes:", SwingConstants.RIGHT);
		lab.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.EAST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 1;     gbConstraints.gridx = 2;
		gbLayout.setConstraints(lab, gbConstraints);
		add(lab);
		gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.WEST;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.gridy = 1;     gbConstraints.gridx = 3;
		gbConstraints.weightx = 100;
		gbLayout.setConstraints(m_NumAttributesLab, gbConstraints);
		add(m_NumAttributesLab);
		m_NumAttributesLab.setBorder(BorderFactory.createEmptyBorder(0, 5,
				0, 10));
	}

	/**
	 * Tells the panel to use a new file name
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		m_FileNameLab.setText(fileName);
	}
	
	/**
	 * Tells the panel to use a new set of instances.
	 *
	 * @param inst a set of Instances
	 */
	public void setInstances(Instances inst) {

		m_Instances = inst;
		m_NumInstancesLab.setText("" + m_Instances.numInstances());
		m_NumAttributesLab.setText("" + m_Instances.numAttributes());
	}

}