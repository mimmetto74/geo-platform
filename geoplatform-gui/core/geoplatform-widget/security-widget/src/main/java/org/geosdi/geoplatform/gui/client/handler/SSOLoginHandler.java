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
package org.geosdi.geoplatform.gui.client.handler;

import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.geosdi.geoplatform.gui.client.config.BasicGinInjector;
import org.geosdi.geoplatform.gui.client.config.SecurityGinInjector;
import org.geosdi.geoplatform.gui.client.widget.security.ILoginHandler;
import org.geosdi.geoplatform.gui.global.security.IGPAccountDetail;
import org.geosdi.geoplatform.gui.server.gwt.SecurityRemoteImpl;
import org.geosdi.geoplatform.gui.view.event.GeoPlatformEvents;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
public class SSOLoginHandler extends ILoginHandler {

    @Override
    public void doLogin() {
        SecurityRemoteImpl.Util.getInstance().ssoLogin(new AsyncCallback<IGPAccountDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Error login on IVUser: " + caught.getMessage());
            }

            @Override
            public void onSuccess(IGPAccountDetail resultDetails) {
                if (resultDetails != null) {
                    SecurityGinInjector.MainInjector.getInstance().getPostLoginOperations().
                            executeLoginOperations(resultDetails);
                    //loginXMPPClient(ivUser, password.getValue(), result.getHostXmppServer());
                    BasicGinInjector.MainInjector.getInstance().
                            getLoginAccessManager().hideProgressBar(Boolean.TRUE);
                } else if (SSOLoginHandler.super.nextHandler != null) {
                    SSOLoginHandler.super.nextHandler.doLogin();
                } else {
                    BasicGinInjector.MainInjector.getInstance().
                            getLoginAccessManager().hideProgressBar(Boolean.FALSE);
                    Dispatcher.forwardEvent(GeoPlatformEvents.APPLICATION_FIRST_LOGIN);
                }
            }
        });
    }
}
