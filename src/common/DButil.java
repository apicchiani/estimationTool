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

package common;

import java.net.SocketException;
import java.sql.*;

import gui.PrincipalPanel;

public class DButil {
	
	PrincipalPanel coman = null;
	SaveChoose m_Session = null;
	
	private Connection conn = null; 
	
	private String url = "jdbc:postgresql://localhost/";
	private String urlConn = null;
	private String username = null;
	private String password = null;
	
	/** Parameter for insert query */
	private String tableName = null;
	
	private static int ID;
	
	/** 
	private String ID_DT = null; // ID del dataset di partenza
	private String ID_M = null; // Nome della misura di similarità processata
	private String P = null; // Nome del classificatore utilizzato
	private String S = null; // Metodo per creare il TS utilizzato
	private int nRip = 1;
	private boolean isBalanced = true; // Indicates if the dataset is balanced
	private int CS = 0;
	private int NCS = 0;
	private int CA = 0;
	private int NCA = 0;
	private int CE = 0;
	private int NCE = 0;
	private double ASS = 0;
	private double ACC = 0;
	private double AR = 0;
	private double ACCES = 0;
	private double COV = 0;
	private int TP = 0;
	private int TN = 0;
	private int FP = 0;
	private int FN = 0;
	
	*/
	
	public DButil(SaveChoose m_Session,PrincipalPanel parent) {
		
		this.m_Session = m_Session;
		/** Loading postgreSQL driver */
		setComanPanel(parent);
		try {
			Class.forName("org.postgresql.Driver");
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
		
		createConnectionDB();
		countRow();
		ID++;
		closeConnectionDB();
		
	}
	
	public void createConnectionDB() {
		
		try {
			this.urlConn = url + m_Session.getNameDB();
			this.username = m_Session.getUserNameDB();
			this.password = m_Session.getUserPassDB();
			this.tableName = m_Session.getTableDB();
			
			conn = DriverManager.getConnection(urlConn, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//coman.printError(e.toString());
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			createConnectionDB();
		}
	}
	
	public void closeConnectionDB() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertTuplaDB(
			String ID_DT,
			String ID_M,
			String P,
			String S,
			boolean isBalanced,
			boolean isStratifiedFold,
			int nDataset,
			int nRipetition,
			int nRun,
			String timestamp,
			int CS,
			int NCS,
			int CA,
			int NCA,
			int CE,
			int NCE, 
			double ErS,
			double ACC,
			double COVTS,
			double ErR,
			double ACCES,
			double COVES,
			int TP,
			int TN,
			int FP,
			int FN ) {
		
		
		try {
			PreparedStatement st = conn.prepareStatement("INSERT INTO " +tableName +" VALUES ( '" 
					+ID +"', '" +ID_DT +"','" +ID_M +"','" +P +"','" +S +"','"+isBalanced +"','" +isStratifiedFold +"','"
					+nDataset +"','" +nRipetition +"','" +nRun +"','" +timestamp +"','" +CS +"','" +NCS +"','" 
					+CA +"','" +NCA +"','" +CE +"','" +NCE +"','" +ErS +"','" +ACC +"','" +COVTS +"','" 
					+ErR +"','" +ACCES +"','" +COVES +"','" +TP +"','" +TN +"','"+FP +"','" +FN
					+"')");
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		ID++;
	}
	
	public void countRow() {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(ID) FROM " +tableName);
			while(rs.next()) {
				ID = rs.getInt(1);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setComanPanel(PrincipalPanel parent) {
		this.coman = parent;
	}
	
}
