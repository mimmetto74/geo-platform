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
package org.geosdi.geoplatform.gui.client.action.temporary;

import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import javax.inject.Inject;
import org.geosdi.geoplatform.gui.action.menu.MenuAction;
import org.geosdi.geoplatform.gui.action.tree.menu.TreeMenuActionCreator;
import org.geosdi.geoplatform.gui.action.tree.menu.TreeMenuActionRegistar;
import org.geosdi.geoplatform.gui.client.action.menu.AddFolderMenuAction;
import org.geosdi.geoplatform.gui.client.action.menu.CopyLayerAction;
import org.geosdi.geoplatform.gui.client.action.menu.CreateFolderViewportAction;
import org.geosdi.geoplatform.gui.client.action.menu.CreateLayerViewportAction;
import org.geosdi.geoplatform.gui.client.action.menu.DeleteElementsMenuAction;
import org.geosdi.geoplatform.gui.client.action.menu.PasteLayerAction;
import org.geosdi.geoplatform.gui.client.action.menu.ShareProjectMenuAction;
import org.geosdi.geoplatform.gui.client.action.menu.ShowFolderRenameAction;
import org.geosdi.geoplatform.gui.client.action.menu.ShowLayerPropertiesAction;
import org.geosdi.geoplatform.gui.client.action.menu.ZoomToLayerExtentAction;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToCSV;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToGML2;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToGML3_1;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToGML3_2;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToGeoJSON;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToGeoRSS;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToKML;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToPDF;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToShpZip;
import org.geosdi.geoplatform.gui.client.action.menu.export.ExportoToTIFF;
import org.geosdi.geoplatform.gui.client.action.menu.legend.GetLegendGraphicsBox;
import org.geosdi.geoplatform.gui.client.action.menu.time.AddModifyTimeFilterAction;
import org.geosdi.geoplatform.gui.client.action.menu.time.RemoveTimeFilterAction;
import org.geosdi.geoplatform.gui.client.widget.tree.GPTreePanel;
import org.geosdi.geoplatform.gui.impl.tree.menu.config.TreeMenuGinInjector;
import org.geosdi.geoplatform.gui.model.tree.GPBeanTreeModel;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class TreeMenuActionFactory {

    static {
        registar = TreeMenuGinInjector.MainInjector.getInstance().getTreeMenuActionRegistar();
    }
    private static TreeMenuActionRegistar registar;
    //
    @Inject
    static ShareProjectMenuAction shareProjectMenuAction;
    @Inject
    static PasteLayerAction pasteLayerAction;
    @Inject
    static CopyLayerAction copyLayerAction;

    public static void buildTreeMenuActions() {
        buildRootMenuActions();
        buildCompositeMenuActions();
        buildLeafMenuActions();
        buildMultiSelectionMenu();

    }

    private static void buildRootMenuActions() {
        registar.put("ADD_FOLDER", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new AddFolderMenuAction(treePanel);
            }
        });

        registar.put("SHARE_PROJECT", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return shareProjectMenuAction;
            }
        });
    }

    private static void buildCompositeMenuActions() {
        registar.put("PASTE_MENU", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return pasteLayerAction;
            }
        });

        registar.put("RENAME_FOLDER", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ShowFolderRenameAction(treePanel);
            }
        });

        registar.put("CREATE_FOLDER_VIEWPORT", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new CreateFolderViewportAction(treePanel);
            }
        });
    }

    private static void buildLeafMenuActions() {
        registar.put("ZOOM_TO_EXTEND", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ZoomToLayerExtentAction(
                        treePanel);
            }
        });

        registar.put("GET_LEGEND", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new GetLegendGraphicsBox(
                        treePanel);
            }
        });

        registar.put("EXPORT_TO_KML", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToKML(treePanel);
            }
        });

        registar.put("EXPORT_TO_PDF", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToPDF(treePanel);
            }
        });

        registar.put("EXPORT_TO_TIFF", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToTIFF(treePanel);
            }
        });

        registar.put("EXPORT_TO_SHP_ZIP", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToShpZip(treePanel);
            }
        });

        registar.put("EXPORT_GML_2", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToGML2(treePanel);
            }
        });

        registar.put("EXPORT_GML_3_1", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToGML3_1(treePanel);
            }
        });

        registar.put("EXPORT_GML_3_2", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToGML3_2(treePanel);
            }
        });

        registar.put("EXPORT_TO_CSV", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToCSV(treePanel);
            }
        });

        registar.put("EXPORT_TO_GEOJSON", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToGeoJSON(treePanel);
            }
        });

        registar.put("EXPORT_TO_RSS", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ExportoToGeoRSS(treePanel);
            }
        });

//        registar.put("ADD_MODIFY_CQL_FILTER", new TreeMenuActionCreator() {
//
//            @Override
//            public MenuAction createAction(TreePanel treePanel) {
//                return new AddModifyCQLFilterAction(
//                        (GPTreePanel<GPBeanTreeModel>) treePanel);
//            }
//
//        });
//
//        registar.put("REMOVE_CQL_FILTER", new TreeMenuActionCreator() {
//
//            @Override
//            public MenuAction createAction(TreePanel treePanel) {
//                return new RemoveCQLFilterAction(
//                        (GPTreePanel<GPBeanTreeModel>) treePanel);
//            }
//
//        });

        registar.put("ADD_MODIFY_TIME_FILTER", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new AddModifyTimeFilterAction(
                        (GPTreePanel<GPBeanTreeModel>) treePanel);
            }
        });

        registar.put("REMOVE_TIME_FILTER", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new RemoveTimeFilterAction(
                        (GPTreePanel<GPBeanTreeModel>) treePanel);
            }
        });

        registar.put("CREATE_LAYER_VIEWPORT", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new CreateLayerViewportAction(treePanel);
            }
        });

        registar.put("COPY_LAYER", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return copyLayerAction;
            }
        });

        /**
         * @Todo Add for WFS-T and Paste Layer Action
         *
         * registar.put("EDIT_FEATURE", new TreeMenuActionCreator() {
         *
         * @Override public MenuAction createAction(TreePanel treePanel) { throw
         * new UnsupportedOperationException("Not supported yet."); }
         *
         * });
         */
        registar.put("LAYER_PROPERTIES", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new ShowLayerPropertiesAction(treePanel);
            }
        });
    }

    private static void buildMultiSelectionMenu() {
        buildAllElementsActions();
        buildLeafsMultiSelectionActions();
    }

    private static void buildAllElementsActions() {
        registar.put("DELETE_TREE_ELEMENTS", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return new DeleteElementsMenuAction(
                        treePanel);
            }
        });
    }

    private static void buildLeafsMultiSelectionActions() {
        registar.put("COPY_LAYERS", new TreeMenuActionCreator() {
            @Override
            public MenuAction createAction(TreePanel treePanel) {
                return copyLayerAction;
            }
        });
    }
}
