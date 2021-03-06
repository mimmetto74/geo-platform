//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.16 at 12:48:05 PM CEST 
//


package org.geosdi.geoplatform.xml.wps.v100;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;


/**
 * <p>Java class for SupportedComplexDataInputType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SupportedComplexDataInputType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/wps/1.0.0}SupportedComplexDataType">
 *       &lt;sequence>
 *         &lt;element name="DefaultValue" type="{http://www.opengis.net/wps/1.0.0}ComplexDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="maximumMegabytes" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupportedComplexDataInputType", propOrder = {
    "defaultValue"
})
public class SupportedComplexDataInputType
    extends SupportedComplexDataType
    implements ToString
{

    @XmlElement(name = "DefaultValue", namespace = "")
    protected ComplexDataType defaultValue;
    @XmlAttribute(name = "maximumMegabytes")
    protected BigInteger maximumMegabytes;

    /**
     * Gets the value of the defaultValue property.
     * 
     * @return
     *     possible object is
     *     {@link ComplexDataType }
     *     
     */
    public ComplexDataType getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the value of the defaultValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexDataType }
     *     
     */
    public void setDefaultValue(ComplexDataType value) {
        this.defaultValue = value;
    }

    public boolean isSetDefaultValue() {
        return (this.defaultValue!= null);
    }

    /**
     * Gets the value of the maximumMegabytes property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaximumMegabytes() {
        return maximumMegabytes;
    }

    /**
     * Sets the value of the maximumMegabytes property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaximumMegabytes(BigInteger value) {
        this.maximumMegabytes = value;
    }

    public boolean isSetMaximumMegabytes() {
        return (this.maximumMegabytes!= null);
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        super.appendFields(locator, buffer, strategy);
        {
            ComplexDataType theDefaultValue;
            theDefaultValue = this.getDefaultValue();
            strategy.appendField(locator, this, "defaultValue", buffer, theDefaultValue);
        }
        {
            BigInteger theMaximumMegabytes;
            theMaximumMegabytes = this.getMaximumMegabytes();
            strategy.appendField(locator, this, "maximumMegabytes", buffer, theMaximumMegabytes);
        }
        return buffer;
    }

}
