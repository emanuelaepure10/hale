//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.24 at 11:33:30 AM MEZ 
//

package eu.esdihumboldt.hale.io.project.jaxb.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MappedSchema complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="MappedSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedSchema", propOrder = { "path" })
public class MappedSchema {

	@XmlElement(required = true)
	protected String path;

	/**
	 * Gets the value of the path property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the value of the path property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPath(String value) {
		this.path = value;
	}

}
