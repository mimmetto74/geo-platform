//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 10:27:36 PM CEST 
//


package org.geosdi.geoplatform.xml.gml.v311;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class TopologyStylePropertyElement
    extends JAXBElement<TopologyStylePropertyType>
{

    protected final static QName NAME = new QName("http://www.opengis.net/gml", "topologyStyle");

    public TopologyStylePropertyElement(TopologyStylePropertyType value) {
        super(NAME, ((Class) TopologyStylePropertyType.class), null, value);
    }

    public TopologyStylePropertyElement() {
        super(NAME, ((Class) TopologyStylePropertyType.class), null, null);
    }

}
