/*
 * Copyright (c) 2016 Fraunhofer IGD
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Fraunhofer IGD <http://www.igd.fraunhofer.de/>
 */
package de.fhg.igd.mapviewer.server.wms.capabilities;

/**
 * WMSCapabilitiesException
 *
 * @author <a href="mailto:simon.templer@igd.fhg.de">Simon Templer</a>
 *
 * @version $Id$
 */
public class WMSCapabilitiesException extends Exception {

	private static final long serialVersionUID = -9159220304576993113L;

	/**
	 * @see Exception#Exception()
	 */
	public WMSCapabilitiesException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public WMSCapabilitiesException(String arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public WMSCapabilitiesException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public WMSCapabilitiesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
