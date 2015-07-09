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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import common.SaveChoose;

public class ClassifierSelectionPanel extends JPanel {

	/**	for serialization */
	private static final long serialVersionUID = -7588699350099009383L;
	
	/** Classifier type name */
	private String nameTypeClassifier = null;

	/**
	 * A table model that looks at the names of Classifier and maintains
	 * a list of classifier that have been "selected".
	 */
	class ClassifierTableModel extends AbstractTableModel {

		/** for serialization */
		private static final long serialVersionUID = 2839126875038866422L;
		
		/** The name of classifiers */
		protected Vector<String> m_NameClassifiers;
		
		/** The flag for whether the instance will be included */
		protected boolean [] m_Selected;
		
		/** Indicates the beginning of the classifiers for a given type */
		protected int offset;
		
		/** The session to run in progress */
		protected SaveChoose m_Session;

		/** Creates the table model */
		public ClassifierTableModel(Vector<String> nameClassifiers,int offset) {
			this.offset = offset;
			setClassifiers(nameClassifiers);
		}
		
		public void setSession(SaveChoose session) {
			this.m_Session = session;
		}
		
		/**
		 * Sets the table model to look at a new set of instances.
		 *
		 * @param instances the new set of Instances.
		 */
		public void setClassifiers(Vector<String> nameClassifiers) {
			m_NameClassifiers = nameClassifiers;
			m_Selected = new boolean [m_NameClassifiers.size()];
		}

		/**
		 * Gets the number of attributes.
		 *
		 * @return the number of attributes.
		 */
		public int getRowCount() {

			return m_Selected.length;
		}

		/**
		 * Gets the number of columns: 2
		 *
		 * @return 2
		 */
		public int getColumnCount() {

			return 2;
		}

		/**
		 * Gets a table cell
		 *
		 * @param row the row index
		 * @param column the column index
		 * @return the value at row, column
		 */
		public Object getValueAt(int row, int column) {

			switch (column) {
			case 0:
				return new Boolean(m_Selected[row]);
			case 1:
				return m_NameClassifiers.get(row);
			default:
				return null;
			}
		}

		/**
		 * Gets the name for a column.
		 *
		 * @param column the column index.
		 * @return the name of the column.
		 */
		public String getColumnName(int column) {

			switch (column) {
			case 0:
				return new String("");
			case 1:
				return new String("Name of Classifier");
			default:
				return null;
			}
		}

		/**
		 * Sets the value at a cell.
		 *
		 * @param value the new value.
		 * @param row the row index.
		 * @param col the column index.
		 */
		public void setValueAt(Object value, int row, int col) {

			if (col == 0) {
				m_Selected[row] = ((Boolean) value).booleanValue();
				m_Session.setUsingClassifiers(offset+row,m_Selected[row]);
			}
		}

		/**
		 * Gets the class of elements in a column.
		 *
		 * @param col the column index.
		 * @return the class of elements in the column.
		 */
		public Class getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}

		/**
		 * Returns true if the column is the "selected" column.
		 *
		 * @param row ignored
		 * @param col the column index.
		 * @return true if col == 1.
		 */
		public boolean isCellEditable(int row, int col) {

			if (col == 0) { 
				return true;
			}
			return false;
		}

		/**
		 * Gets an array containing the indices of all selected attributes.
		 *
		 * @return the array of selected indices.
		 */
		public int [] getSelectedAttributes() {

			int [] r1 = new int[getRowCount()];
			int selCount = 0;
			for (int i = 0; i < getRowCount(); i++) {
				if (m_Selected[i]) {
					r1[selCount++] = i;
				}
			}
			int [] result = new int[selCount];
			System.arraycopy(r1, 0, result, 0, selCount);
			return result;
		}

		/**
		 * Sets the state of all attributes to selected.
		 */
		public void includeAll() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = true;
				m_Session.setUsingClassifiers(offset+i,m_Selected[i]);
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

		/**
		 * Deselects all attributes.
		 */
		public void removeAll() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = false;
				m_Session.setUsingClassifiers(offset+i,m_Selected[i]);
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

		/**
		 * Inverts the selected status of each attribute.
		 */
		public void invert() {

			for (int i = 0; i < m_Selected.length; i++) {
				m_Selected[i] = !m_Selected[i];
				m_Session.setUsingClassifiers(offset+i,m_Selected[i]);
			}
			fireTableRowsUpdated(0, m_Selected.length);
		}

	}
	
	/** Vector contains the classname of classifiers */
	protected Vector<String> classnameClassifiers = new Vector<String>();
	
	/** Vector contains the name of classfiers*/
	protected Vector<String> nameClassifiers = new Vector<String>();
	
	/** Press to select all attributes */  
	protected JButton m_IncludeAll = new JButton("All");

	/** Press to deselect all attributes */
	protected JButton m_RemoveAll = new JButton("None");

	/** Press to invert the current selection */
	protected JButton m_Invert = new JButton("!");

	/** The table displaying attribute names and selection status */
	protected JTable m_Table = new JTable();

	/** The table model containing attribute names and selection status */
	protected ClassifierTableModel m_Model;
	
	/** Indicates the beginning of the classifiers in use for a given type */
	protected int offsetUseClssifiers;
	
	/** The session to run in progress */
	protected SaveChoose m_Session = null;
	
	public ClassifierSelectionPanel(String typeClassifier) {
		this.nameTypeClassifier = typeClassifier;
		setBorder(BorderFactory.createTitledBorder(nameTypeClassifier));
		m_IncludeAll.setToolTipText("Selects all attributes");
		m_IncludeAll.setEnabled(false);
		m_IncludeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.includeAll();
			}
		});
		m_RemoveAll.setToolTipText("Unselects all attributes");
		m_RemoveAll.setEnabled(false);
		m_RemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.removeAll();
			}
		});
		m_Invert.setToolTipText("Inverts the current attribute selection");
		m_Invert.setEnabled(false);
		m_Invert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Model.invert();
			}
		});
		
		m_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_Table.setColumnSelectionAllowed(false); 
		m_Table.setPreferredScrollableViewportSize(new Dimension(250, 150));
		
		// Set up the layout
		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		p1.setLayout(new GridLayout(1, 3, 5, 5));
		p1.add(m_IncludeAll);
		p1.add(m_RemoveAll);
		p1.add(m_Invert);

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(new JScrollPane(m_Table), BorderLayout.CENTER);
		
	}
	
	/** Set the name of classifiers in the tableModel */
	public void setClassifiers() {

		if (m_Model == null) {
			m_Model = new ClassifierTableModel(nameClassifiers,offsetUseClssifiers);
			m_Table.setModel(m_Model);
			TableColumnModel tcm = m_Table.getColumnModel();
			tcm.getColumn(0).setMaxWidth(tcm.getColumn(1).getMinWidth());
			tcm.getColumn(1).setMinWidth(100);
		} else {
			m_Model.setClassifiers(nameClassifiers);
			m_Table.clearSelection();
		}
		m_IncludeAll.setEnabled(true);
		m_RemoveAll.setEnabled(true);
		m_Invert.setEnabled(true);
		m_Table.sizeColumnsToFit(2);
		m_Table.revalidate();
		m_Table.repaint();
	}
	
	public void addClassifier(String classnameClassifier, String nameClassifier) {
		classnameClassifiers.add(classnameClassifier);
		nameClassifiers.add(nameClassifier);
	}
	
	public void setOffsetUseClassifier(int offset) {
		this.offsetUseClssifiers=offset;
	}
	
	public void setSession(SaveChoose session) {
		m_Session = session;
		m_Model.setSession(session);
	}

}
