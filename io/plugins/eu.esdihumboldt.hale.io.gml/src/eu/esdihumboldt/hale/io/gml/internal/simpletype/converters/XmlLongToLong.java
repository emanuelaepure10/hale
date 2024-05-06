/*
 * Copyright (c) 2024 wetransform GmbH
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
 *     wetransform GmbH <http://www.wetransform.to>
 */

package eu.esdihumboldt.hale.io.gml.internal.simpletype.converters;

import org.apache.xmlbeans.XmlLong;
import org.springframework.core.convert.converter.Converter;

/**
 * Convert xs:XmlLong to {@link Long}
 */
public class XmlLongToLong implements Converter<XmlLong, Long> {

	/**
	 * @see Converter#convert(Object)
	 */
	@Override
	public Long convert(XmlLong value) {
		if (value == null) {
			return null;
		}
		return value.getLongValue();
	}

}
