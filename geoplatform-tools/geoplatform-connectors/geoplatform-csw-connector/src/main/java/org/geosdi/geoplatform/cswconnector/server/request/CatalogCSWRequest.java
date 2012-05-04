/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2012 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see http://www.gnu.org/licenses/ 
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is 
 * making a combined work based on this library. Thus, the terms and 
 * conditions of the GNU General Public License cover the whole combination. 
 * 
 * As a special exception, the copyright holders of this library give you permission 
 * to link this library with independent modules to produce an executable, regardless 
 * of the license terms of these independent modules, and to copy and distribute 
 * the resulting executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the license of 
 * that module. An independent module is a module which is not derived from or 
 * based on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obligated to do so. If you do not 
 * wish to do so, delete this exception statement from your version. 
 *
 */
package org.geosdi.geoplatform.cswconnector.server.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.geosdi.geoplatform.connector.jaxb.GPConnectorJAXBContext;
import org.geosdi.geoplatform.connector.jaxb.provider.GeoPlatformJAXBContextRepository;
import org.geosdi.geoplatform.connector.protocol.GeoPlatformHTTP;
import org.geosdi.geoplatform.connector.server.GPServerConnector;
import org.geosdi.geoplatform.connector.server.request.GPPostConnectorRequest;
import org.geosdi.geoplatform.cswconnector.jaxb.CSWConnectorJAXBContext;
import org.geosdi.geoplatform.exception.ServerInternalFault;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 * @author Vincenzo Monteverde <vincenzo.monteverde@geosdi.org>
 */
public abstract class CatalogCSWRequest<T> extends GPPostConnectorRequest<T> {

    static {
        cswContext = GeoPlatformJAXBContextRepository.getProvider(
                CSWConnectorJAXBContext.CSW_CONTEXT_KEY);
    }
    //
    protected static final GPConnectorJAXBContext cswContext;

    public CatalogCSWRequest(GPServerConnector server) {
        super(server);
    }

    @Override
    protected HttpEntity preparePostEntity()
            throws JAXBException, UnsupportedEncodingException {

        Marshaller marshaller = cswContext.acquireMarshaller();

        Object request = this.createRequest();
        StringWriter writer = new StringWriter();
        marshaller.marshal(request, writer);

        return new StringEntity(writer.toString(),
                GeoPlatformHTTP.CONTENT_TYPE_XML, HTTP.UTF_8);
    }

    protected abstract Object createRequest();

    @Override
    public T getResponse() throws ServerInternalFault, IOException {
        T request = null;

        try {
            HttpResponse response = super.clientConnection.execute(
                    super.getPostMethod());
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                InputStream is = responseEntity.getContent();

                Unmarshaller unmarshaller = cswContext.acquireUnmarshaller();
                Object content = unmarshaller.unmarshal(is);
                if (!(content instanceof JAXBElement)) { // ExceptionReport
                    logger.error("\n#############\n{}\n#############", content);
                    throw new ServerInternalFault(
                            "CSW Catalog Server Error: incorrect responce");
                }

                JAXBElement<T> elementType = (JAXBElement<T>) content;
                request = elementType.getValue();

                EntityUtils.consume(responseEntity);
            }

        } catch (JAXBException ex) {
            logger.error("\n@@@@@@@@@@@@@@@@@@ JAXBException *** {} ***",
                    ex.getMessage());
            throw new ServerInternalFault("*** JAXBException ***");
        } catch (ClientProtocolException ex) {
            logger.error(
                    "\n@@@@@@@@@@@@@@@@@@ ClientProtocolException *** {} ***",
                    ex.getMessage());
            throw new ServerInternalFault("*** ClientProtocolException ***");
        } finally {
            super.clientConnection.getConnectionManager().shutdown();
        }

        return request;
    }
}
