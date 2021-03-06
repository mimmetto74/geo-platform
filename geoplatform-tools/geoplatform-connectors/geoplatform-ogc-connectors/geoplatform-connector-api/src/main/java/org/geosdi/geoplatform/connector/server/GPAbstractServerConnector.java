/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2013 geoSDI Group (CNR IMAA - Potenza - ITALY).
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
package org.geosdi.geoplatform.connector.server;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.geosdi.geoplatform.configurator.httpclient.proxy.HttpClientProxyConfiguration;
import org.geosdi.geoplatform.connector.server.security.GPSecurityConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vincenzo Monteverde <vincenzo.monteverde@geosdi.org>
 * @author Giuseppe La Scaleia <giuseppe.lascaleia@geosdi.org>
 */
public abstract class GPAbstractServerConnector implements GPServerConnector {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    protected final URL url;
    protected final GPSecurityConnector securityConnector;
    private final HttpClientProxyConfiguration proxyConfiguration;
    private DefaultHttpClient httpClient;

    protected GPAbstractServerConnector(URL theUrl,
            GPSecurityConnector theSecurityConnector) {
        this(theUrl, theSecurityConnector, null);
    }

    protected GPAbstractServerConnector(URL theUrl,
            GPSecurityConnector theSecurityConnector,
            HttpClientProxyConfiguration theProxyConfiguration) {
        this.url = theUrl;
        this.securityConnector = theSecurityConnector;
        this.proxyConfiguration = theProxyConfiguration;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public GPSecurityConnector getSecurityConnector() {
        return this.securityConnector;
    }

    @Override
    public URI getURI() {
        try {
            return this.url.toURI();
        } catch (URISyntaxException ex) {
            logger.error("URISyntaxException @@@@@@@@@@@@@@@ " + ex);
        }
        return null;
    }

    @Override
    public DefaultHttpClient getClientConnection() {
        return httpClient = (httpClient == null)
                            ? proxyConfiguration != null
                              ? proxyConfiguration.isUseProxy()
                                ? configureProxy() : new DefaultHttpClient()
                              : new DefaultHttpClient()
                            : httpClient;
    }

    @Override
    public void dispose() throws Exception {
        this.httpClient.getConnectionManager().shutdown();
    }

    /**
     * <p> Analyzes the url of the server by eliminating everything that comes
     * after the ? character </p>
     *
     * @param urlServer
     *
     * @return String
     */
    protected static URL analyzesServerURL(String urlServer) {
        try {
            int index = urlServer.indexOf("?");
            if (index != -1) {
                urlServer = urlServer.substring(0, index);
            }
            return new URL(urlServer);
        } catch (MalformedURLException ex) {
            LoggerFactory.getLogger(GPAbstractServerConnector.class).error(
                    "MalformedURLException @@@@@@@@@@@@@@@@@@ : " + ex);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GPAbstractServerConnector other = (GPAbstractServerConnector) obj;
        if (this.url != other.url && (this.url == null || !this.url.equals(
                                      other.url))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "GPAbstractServerConnector{" + "Server Url = " + url
                + ", securityConnector = " + securityConnector + '}';
    }

    private DefaultHttpClient configureProxy() {
        logger.trace("SetUp Proxy Configuratio @@@@@@@@@@@@@@@@ "
                + proxyConfiguration);

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpHost proxy = new HttpHost(proxyConfiguration.getProxyUrl(),
                proxyConfiguration.getProxyPort());
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                proxy);

        return httpclient;
    }
}
