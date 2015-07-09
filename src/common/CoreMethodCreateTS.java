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

import java.util.GregorianCalendar;
import java.util.Random;

import weka.core.Instances;

public class CoreMethodCreateTS {

	
	public void rankedCore(Instances instsTS, Instances instsES, int sizeTS) {
		for(int i=instsTS.numInstances(); i<sizeTS; i++) {
			instsTS.add(instsES.lastInstance());
			instsES.delete(instsES.numInstances()-1);
//			instsTS.add(instsES.firstInstance());
//			instsES.delete(0);
		}
	}
	
	
	
	public void randomCore(Instances instsTS, Instances instsES, int sizeTS) {
		GregorianCalendar calendar;
		double seed; 
		Random rand;
//		double randDouble;
		int rnd;

		for(int j=0; j<sizeTS; j++) {
			calendar = new GregorianCalendar();
			seed = calendar.getTimeInMillis() * Math.random();
			rand = new Random((long) seed);
//			randDouble = rand.nextDouble();
			rnd = rand.nextInt(instsES.numInstances());
//			System.out.println(+seed + " " +" " +rnd);
			instsTS.add(instsES.instance(rnd));
			instsES.delete(rnd);
		}
	}
}
