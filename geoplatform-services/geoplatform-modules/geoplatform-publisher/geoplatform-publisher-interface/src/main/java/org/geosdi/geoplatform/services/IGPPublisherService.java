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
package org.geosdi.geoplatform.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geoplatform.responce.InfoPreview;
import org.geosdi.geoplatform.responce.LayerAttribute;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
public interface IGPPublisherService {

    List<InfoPreview> analyzeZIPEPSG(String sessionID, String userName, File file)
            throws ResourceNotFoundFault;

    List<InfoPreview> processEPSGResult(String userName, List<InfoPreview> previewLayerList)
            throws ResourceNotFoundFault;

    String loadStyle(String layerDatasource, String styleName)
            throws ResourceNotFoundFault;

    List<LayerAttribute> describeFeatureType(String layerName)
            throws ResourceNotFoundFault;

    boolean publishStyle(String styleToPublish)
            throws ResourceNotFoundFault;

    boolean putStyle(String styleToPublish, String styleName)
            throws ResourceNotFoundFault;

    public boolean existsStyle(String styleName);

    InfoPreview analyzeTIFInPreview(String sessionID, File file, boolean overwrite)
            throws ResourceNotFoundFault;

    List<InfoPreview> uploadShapeInPreview(String sessionID, String username,
            File shpFile, File dbfFile, File shxFile, File prjFile, File sldFile)
            throws ResourceNotFoundFault;

    List<InfoPreview> getPreviewDataStores(String userName) throws ResourceNotFoundFault;

    boolean publish(String sessionID, String workspace, String dataStoreName,
            String layerName)
            throws ResourceNotFoundFault, FileNotFoundException;

    boolean publishAll(String sessionID, String workspace, String dataStoreName,
            List<String> layerNames) throws ResourceNotFoundFault, FileNotFoundException;

    boolean publishAllofPreview(String sessionID, String workspace,
            String dataStoreName)
            throws ResourceNotFoundFault, FileNotFoundException;
}
