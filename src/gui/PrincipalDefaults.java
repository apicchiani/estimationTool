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

import java.io.Serializable;
import java.util.Properties;

import weka.core.Utils;


/** The class of utils for get the classifier for use in the program */
public class PrincipalDefaults
	implements Serializable {

	/** for serialization */
	private static final long serialVersionUID = 473332029139480068L;

	/** The name of the properties file. */
	public final static String PROPERTY_FILE = "gui/coman.props";

	/** Properties associated with the explorer options. */
	protected static Properties PROPERTIES;
	
	static {
		try {
			PROPERTIES = Utils.readProperties(PROPERTY_FILE);
		}
		catch (Exception e) {
			System.err.println("Problem reading properties. Fix before continuing.");
			e.printStackTrace();
			PROPERTIES = new Properties();
		}
	}
	
	/**
	   * returns the value for the specified property, if non-existent then the
	   * default value.
	   *
	   * @param property      the property to retrieve the value for
	   * @param defaultValue  the default value for the property
	   * @return              the value of the specified property
	   */
	  public static String get(String property, String defaultValue) {
	    return PROPERTIES.getProperty(property, defaultValue);
	  }
	
	/**
	   * returns an array with the classnames of all the additional panels to 
	   * display as tabs in the Explorer.
	   * 
	   * @return		the classnames
	   */
	  public static String[] getTabs() {
	    String[]	result;
	    String	tabs;
	    
	    // read and split on comma
	    tabs   = get("Tabs", "gui.MethodCreateTSPanel,gui.ValidationPanel,gui.ClassifierPanel");
	    result = tabs.split(",");
	    
	    return result;
	  }

	  /**
	   * returns an array with the classname of all classifier.
	   * 
	   * @return		the classname of classifiers
	   */
	  public static String[] getClassifiers() {
		  String[]	result;
		  String	classifiers;

		  // read and split on comma
		  classifiers   = get("Classifiers", null);
		  result = classifiers.split(",");

		  return result;
	  }

}
