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
package org.geosdi.geoplatform.gui.featureinfo.widget;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import java.util.List;
import org.geosdi.geoplatform.gui.client.i18n.FeatureInfoModuleConstants;
import org.geosdi.geoplatform.gui.client.widget.GeoPlatformWindow;
import org.geosdi.geoplatform.gui.configuration.map.puregwt.MapHandlerManager;
import org.geosdi.geoplatform.gui.configuration.message.GeoPlatformMessage;
import org.geosdi.geoplatform.gui.factory.map.GPApplicationMap;
import org.geosdi.geoplatform.gui.featureinfo.cache.FeatureInfoFlyWeight;
import org.geosdi.geoplatform.gui.featureinfo.cache.IGPFeatureInfoElement;
import org.geosdi.geoplatform.gui.impl.map.GeoPlatformMap;
import org.geosdi.geoplatform.gui.puregwt.featureinfo.GPFeatureInfoHandler;
import org.geosdi.geoplatform.gui.utility.GeoPlatformUtils;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class GPFeatureInfoWidget extends GeoPlatformWindow implements GPFeatureInfoHandler {

    private ContentPanel mainPanel;
    private GPFeatureInfoCaller featureCaller;
    private GeoPlatformMap mapWidget;

    public GPFeatureInfoWidget(GeoPlatformMap theMapWidget) {
        super(false);
        this.mapWidget = theMapWidget;
        this.featureCaller = new GPFeatureInfoCaller(this.mapWidget.getMap());
        MapHandlerManager.addHandler(GPFeatureInfoHandler.TYPE, this);
    }

    @Override
    public void addComponent() {
        this.mainPanel = new ContentPanel();
        this.mainPanel.setHeaderVisible(false);
        add(this.mainPanel);
    }

    @Override
    public void initSize() {
        super.setHeight(200);
        super.setWidth(400);
    }

    @Override
    public void setWindowProperties() {
        super.setClosable(true);
        super.setScrollMode(Scroll.AUTO);
        super.setResizable(true);
        super.setPlain(true);
        super.setModal(true);
        super.setHeadingHtml(FeatureInfoModuleConstants.INSTANCE.GPFeatureInfoWidget_headingText());
    }

    @Override
    public void removeLayer(Layer layer) {
//        System.out.println("On remove layer: " + layer);
        if (FeatureInfoFlyWeight.getInstance().contains(layer)) {
            Map map = GPApplicationMap.getInstance().getApplicationMap().getMap();
            IGPFeatureInfoElement featureInfoElement = FeatureInfoFlyWeight.getInstance().get(layer);
            featureInfoElement.getElementControl().deactivate();
            map.removeControl(featureInfoElement.getElementControl());
            FeatureInfoFlyWeight.getInstance().remove(layer);
//            System.out.println("layer removed");
        }
    }

    @Override
    public void addModifyLayer(Layer layer) {
//        System.out.println("On addModifyLayer: " + layer);
        this.removeLayer(layer);
        if (layer.isVisible()) {
            IGPFeatureInfoElement element = FeatureInfoFlyWeight.getInstance().get(layer);
            Map map = GPApplicationMap.getInstance().getApplicationMap().getMap();
            map.addControl(element.getElementControl());
            element.getElementControl().activate();
//            System.out.println("Added layer");
        }
    }

    @Override
    public void activateHandler(List<Layer> layerList) {
//        System.out.println("On activate handler");
        this.featureCaller.load(layerList);
    }

    @Override
    public void deactivateHandler() {
        this.featureCaller.deactivateFeatureInfoControl();
    }

    @Override
    public void reset() {
        this.mainPanel.removeAll();
    }

    @Override
    public void showInfoWidget() {
//        System.out.println("Showing the info widget");
        for (IGPFeatureInfoElement featureInfoElement : GeoPlatformUtils.safeCollection(
                FeatureInfoFlyWeight.getInstance().getCollection())) {
            if (featureInfoElement.isActive()) {
                this.mainPanel.add(featureInfoElement.getElementPanel());
            }
        }
        this.mainPanel.layout();
        if (this.mainPanel.getItemCount() > 0) {
            super.show();
        } else {
            GeoPlatformMessage.alertMessage(FeatureInfoModuleConstants.INSTANCE.
                    GPFeatureInfoWidget_alertNoLayerTitleText(),
                    FeatureInfoModuleConstants.INSTANCE.GPFeatureInfoWidget_alertNoLayerBodyText());
        }
    }
}
