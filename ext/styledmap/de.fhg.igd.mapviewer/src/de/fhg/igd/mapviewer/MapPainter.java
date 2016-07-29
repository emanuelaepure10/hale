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
package de.fhg.igd.mapviewer;

import java.awt.Point;

import org.jdesktop.swingx.mapviewer.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

/**
 * Map painter interface.
 * 
 * @author Simon Templer
 */
public interface MapPainter extends Painter<JXMapViewer> {

	/**
	 * Sets the map kit that provides the context of the painter
	 * 
	 * @param mapKit the map kit
	 */
	public void setMapKit(BasicMapKit mapKit);

	/**
	 * @param point the point of interest (usually the cursor position)
	 * @return the text string. Can be null or empty
	 */
	public String getTipText(Point point);

	/**
	 * Perform clean-up
	 */
	public void dispose();

}
