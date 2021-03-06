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
package org.geosdi.geoplatform.gui.client.form;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geosdi.geoplatform.gui.client.BasicWidgetResources;
import org.geosdi.geoplatform.gui.client.PrintResources;
import org.geosdi.geoplatform.gui.client.form.binding.GPComboBoxFieldBinding;
import org.geosdi.geoplatform.gui.client.form.binding.MapCommentFieldBinding;
import org.geosdi.geoplatform.gui.client.form.binding.MapTitleFieldBinding;
import org.geosdi.geoplatform.gui.client.form.binding.PrintTitleFieldBinding;
import org.geosdi.geoplatform.gui.client.i18n.PrintModuleConstants;
import org.geosdi.geoplatform.gui.client.i18n.PrintTemplateConstants;
import org.geosdi.geoplatform.gui.client.i18n.buttons.ButtonsConstants;
import org.geosdi.geoplatform.gui.client.model.DPI;
import org.geosdi.geoplatform.gui.client.model.GPPrintBean;
import org.geosdi.geoplatform.gui.client.model.GPPrintBean.GPPrintEnumBean;
import org.geosdi.geoplatform.gui.client.model.PrintTemplate;
import org.geosdi.geoplatform.gui.client.model.Scale;
import org.geosdi.geoplatform.gui.client.utility.LayerComparable;
import org.geosdi.geoplatform.gui.client.utility.PrintUtility;
import org.geosdi.geoplatform.gui.client.widget.form.binding.GPDynamicFormBinding;
import org.geosdi.geoplatform.gui.factory.map.GPApplicationMap;
import org.geosdi.geoplatform.gui.model.GPLayerBean;
import org.geosdi.geoplatform.gui.model.server.GPRasterLayerGrid;
import org.geosdi.geoplatform.gui.model.tree.AbstractFolderTreeNode;
import org.geosdi.geoplatform.gui.model.tree.AbstractRootTreeNode;
import org.geosdi.geoplatform.gui.model.tree.GPBeanTreeModel;
import org.geosdi.geoplatform.gui.model.tree.GPLayerTreeModel;
import org.geosdi.geoplatform.gui.shared.GPLayerType;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;

/**
 *
 * @author Francesco Izzi - CNR IMAA geoSDI Group
 * @mail francesco.izzi@geosdi.org
 */
public class GPPrintWidget extends GPDynamicFormBinding<GPPrintBean> {

    private ListStore<DPI> storeDPI;
    private ListStore<PrintTemplate> storeTemplate;
    private ListStore<Scale> storeScale;
    private ComboBox<DPI> comboDPI;
    private ComboBox<PrintTemplate> comboTemplate;
    private ComboBox<Scale> comboScale;
    private ComboBox<Scale> scaleCombo;
    private CheckBox checkPrintBaseMap;
    private TextField<String> title;
    private TextField<String> mapTitle;
    private TextArea comments;
    private Button print;
    private Button cancel;
    private TreePanel tree;
    private List<GPLayerBean> layerList;
    private double sizeFactor = .5;
    private boolean rotation = true;
    private DragFeature dragPrintArea;

    public GPPrintWidget() {
        super();
        this.entity = new GPPrintBean();
    }

    public GPPrintWidget(TreePanel theTree) {
        this.entity = new GPPrintBean();
        this.tree = theTree;
    }

    @Override
    public void addComponentToForm() {
        addEditPrintSettings();
        addComboDPI();
        addComboTemplate();
        addScaleCombo();
        addCheckPrintBaseMap();
        addButtons();
    }

    @Override
    public void initSize() {
        super.setHeadingHtml(PrintModuleConstants.INSTANCE.GPPrintWidget_headingText());
        super.setPosition(RootPanel.get().getOffsetWidth() - 400 - 6, 55);
        setSize(400, 650);
    }

    @Override
    public void initSizeFormPanel() {
        formPanel.setHeaderVisible(false);
        formPanel.setSize(400, 560);
        super.setModal(false);
    }

    @Override
    public void execute() {
        if (formPanel.isValid()) {

            // Center on correct ViewPort
            Vector printExtent = PrintUtility.createRectangle(
                    GPApplicationMap.getInstance().
                    getApplicationMap().getMap().getCenter(), getCurrentScale(),
                    GPApplicationMap.getInstance().
                    getApplicationMap().getMap(), sizeFactor,
                    rotation);

            LonLat center = printExtent.getDataExtent().getCenterLonLat();
//            if (GPApplicationMap.getInstance().getApplicationMap().getMap().
//                    getProjection().equals(
//                    GPCoordinateReferenceSystem.GOOGLE_MERCATOR.getCode())) {
//                center.transform(GPCoordinateReferenceSystem.EPSG_GOOGLE.
//                        getCode(),
//                        GPCoordinateReferenceSystem.WGS_84.getCode());
//            }

            String specJson = "{\"layout\":\"" + comboTemplate.getValue().getTemplate() + "\""
                    + ",\"srs\":\"EPSG:900913\",\"units\": \"m\",\"geodetic\":true,\"outputFilename\":\"gp-map\", \"outputFormat\":\"pdf\",";


            String layers = "{\"title\":\"" + title.getValue() + "\",\"pages\":[{\"center\":["
                    + center.lon() + ","
                    + center.lat()
                    + "],\"scale\":" + getCurrentScale()
                    + ",\"rotation\":0,\"mapTitle\":\"" + mapTitle.getValue()
                    + "\",\"comment\":\"" + comments.getValue() + "\"}],\"layers\":[";




            GPLayerBean baseMap = new GPRasterLayerGrid();

            baseMap.setName("Mappa_di_Base");
            baseMap.setDataSource("http://dpc.geosdi.org/geoserver/wms");
            baseMap.setLayerType(GPLayerType.WMS);

            specJson = specJson.concat(buildBaseLayerJson());

            String pagesJson = "\"pages\": ["
                    + "{"
                    + "\"center\": [" + center.lon() + "," + center.lat() + "],"
                    + "\"scale\": " + getCurrentScale() + ","
                    + "\"dpi\": " + comboDPI.getValue().getDpi() + ","
                    + "\"mapTitle\": \"" + mapTitle.getValue() + "\","
                    + "\"title\": \"" + title.getValue() + "\","
                    + "\"comment\": \"" + comments.getValue() + "\""
                    + "}"
                    + "],\n";


            String legendJson = "\"legends\": [";
            String legendLayers = buildLegendLayerJson();
            String legendEnd = "]}";


            specJson = specJson.concat(
                    pagesJson + legendJson + legendLayers + legendEnd);


            String url = GWT.getHostPageBaseURL() + GWT.getModuleName() + "/pdf/create.json";


            RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);

            String jsonData = "spec=" + specJson;



            builder.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            try {
                Info.display(PrintModuleConstants.INSTANCE.printText(),
                        PrintModuleConstants.INSTANCE.GPPrintWidget_infoStartPringBodyText());
                Request response = builder.sendRequest(jsonData,
                        new RequestCallback() {
                    @Override
                    public void onError(Request request, Throwable exception) {
                        Window.alert(exception.getLocalizedMessage());
                    }

                    @Override
                    public void onResponseReceived(Request request,
                            Response response) {
                        Info.display(PrintModuleConstants.INSTANCE.printText(),
                                PrintModuleConstants.INSTANCE.GPPrintWidget_infoFinishPrintBodyText());
                        String downloadURL = response.getText().substring(11,
                                response.getText().indexOf("printout") + 8);

                        Window.open(downloadURL, "_blank", "");

                    }
                });
            } catch (RequestException ex) {
                Logger.getLogger(GPPrintWidget.class.getName()).log(Level.SEVERE,
                        null, ex);
            }

            this.hide();
        }

    }

    public String buildLegendLayerJson() {
        String jsonLegendLayer = "";

        layerList = buildLayerList();

        Collections.sort(layerList, new LayerComparable());


        String firstLegend = "{\n"
                + "\"name\": \"" + layerList.get(0).getName() + "\",\n"
                + "\"classes\": ["
                + "{"
                + "\"name\": \"" + "\",\n"
                + "\"icons\": ["
                + "\"" + getLegendUrl(layerList.get(0)) + "\""
                //+ "\"" + "http%3A%2F%2Fdpc.geosdi.org%2Fgeoserver%2Fwms%3FREQUEST%3DGetLegendGraphic%26VERSION%3D1.0.0%26FORMAT%3Dimage%2Fpng%26LAYER%3DPiano_Calabria%3Apga%26scale%3D5000%26service%3DWMS" + "\""
                + "]\n"
                + "}"
                + "]\n"
                + "}";


        String legendListJson = "";
        for (int i = 1; i < layerList.size(); i++) {
            if (layerList.get(i) instanceof GPLayerBean) {
                GPLayerBean layer = (GPLayerBean) layerList.get(i);
                legendListJson = legendListJson.concat(buildLegendOrderList(
                        layer));
            }
        }

        return jsonLegendLayer.concat(firstLegend + legendListJson);

    }

    private String buildLegendOrderList(GPLayerBean layer) {
        String legend = ",{\n"
                + "            \"name\": \"" + layer.getName() + "\",\n"
                + "            \"classes\": ["
                + "{"
                + "\"name\": \"" + "\",\n"
                + "            \"icons\": ["
                + "\"" + getLegendUrl(layer) + "\""
                //+ "\"" + "http%3A%2F%2Fdpc.geosdi.org%2Fgeoserver%2Fwms%3FREQUEST%3DGetLegendGraphic%26VERSION%3D1.0.0%26FORMAT%3Dimage%2Fpng%26LAYER%3DPiano_Calabria%3Ascenario_crolli%26scale%3D5000%26service%3DWMS" + "\""
                + "]\n"
                + "}"
                + "]\n"
                + "}";
        return legend;

    }

    private String getLegendUrl(GPLayerBean layer) {
        String dataSource = layer.getDataSource();

        if (dataSource.contains("gwc/service/wms")) {
            dataSource = dataSource.replaceAll("gwc/service/wms", "wms");
        } else if (!(dataSource.startsWith("http://ows"))
                && (dataSource.contains("/ows"))) {
            dataSource = dataSource.replaceAll("/ows", "/wms");
        } else {
            dataSource = dataSource.replaceAll("/wfs", "/wms");
        }

        String dataSourceT = dataSource;

        String imageURL = URL.encodeComponent(
                dataSourceT + "?REQUEST=GetLegendGraphic"
                + "&VERSION=1.0.0&FORMAT=image/png&LAYER=" + layer.getName() + "&scale=5000&service=WMS");

        return imageURL;

    }

    public String buildBaseLayerJson() {
        String json = "";
        String start = "\"layers\":[{";
        String baseURL = "\"baseURL\": \"http://tile.openstreetmap.org/\",";
        String opacity = "\"opacity\": 1,";
        String type = "\"type\":\"Osm\",";
        String maxExtent = "\"maxExtent\": ["
                + "-20037508.3392,"
                + "-20037508.3392,"
                + "20037508.3392,"
                + "20037508.3392"
                + "],";
        String tileSize = "\"tileSize\": ["
                + "256,"
                + "256"
                + "],";

        String resolutions = "\"resolutions\": ["
                + "156543.0339,78271.51695,39135.758475,19567.8792375,9783.93961875,4891.969809375,2445.9849046875,1222.99245234375,611.496226171875,305.7481130859375,152.87405654296876,76.43702827148438,38.21851413574219,19.109257067871095,9.554628533935547,4.777314266967774,2.388657133483887,1.1943285667419434,0.5971642833709717"
                + "],";
        String extentions = "\"extension\": \"png\"}";


        layerList = buildLayerList();

        Collections.sort(layerList, new LayerComparable());

        String layerListJson = "";
        for (int i = 0; i < layerList.size(); i++) {
            if (layerList.get(i) instanceof GPLayerBean) {
                GPLayerBean layer = (GPLayerBean) layerList.get(i);
                layerListJson = layerListJson.concat(buildLayersOrderList(layer));
            }
        }

        String end = "],";



        return json.concat(
                start + baseURL + opacity + type + maxExtent + tileSize + resolutions + extentions + layerListJson + end);
    }

    public String buildLayersOrderList(GPLayerBean layer) {

        String layerJson = ",{\n"
                + "            \"baseURL\": \"" + layer.getDataSource() + "\",\n"
                + "            \"opacity\": 1,\n"
                + "            \"singleTile\": false,\n"
                + "            \"type\": \"WMS\",\n"
                + "            \"layers\": [\n"
                + "                \"" + layer.getName() + "\"\n"
                + "            ],\n"
                + "            \"format\": \"image/png\",\n"
                + "            \"styles\": [\n"
                + "                \"\"\n"
                + "            ],\n"
                + "            \"customParams\": {\n"
                + "                \"TRANSPARENT\": \"TRUE\"\n"
                + "            }\n"
                + "        }";

        return layerJson;
    }

    @Override
    public void addFieldsBinding() {
        this.formBinding.addFieldBinding(
                new PrintTitleFieldBinding(title,
                GPPrintEnumBean.GPPRINT_TITLE.toString()));
        this.formBinding.addFieldBinding(
                new MapTitleFieldBinding(mapTitle,
                GPPrintEnumBean.GPPRINT_MAP_TITLE.toString()));
        this.formBinding.addFieldBinding(
                new MapCommentFieldBinding(comments,
                GPPrintEnumBean.GPPRINT_COMMENTS.toString()));
        this.formBinding.addFieldBinding(
                new GPComboBoxFieldBinding(comboDPI,
                GPPrintEnumBean.GPPRINT_DPI.toString()));
    }

    @Override
    public void reset() {
        this.entity.reset();
        this.formBinding.unbind();
        this.title.reset();
        this.mapTitle.reset();
        this.comments.reset();
        this.comboDPI.reset();
        this.comboDPI.clearSelections();
        this.comboScale.reset();
        this.comboScale.setValue(new Scale("1:4.000.000"));

        if (GPApplicationMap.getInstance().getApplicationMap().getMap().
                getLayerByName("VectorPrintExtent") != null) {
            GPApplicationMap.getInstance().getApplicationMap().getMap().
                    removeControl(dragPrintArea);
            //dragPrintArea.deactivate();
            GPApplicationMap.getInstance().getApplicationMap().getMap().
                    removeLayer(GPApplicationMap.getInstance().
                    getApplicationMap().getMap().getLayerByName(
                    "VectorPrintExtent"));
            //GPApplicationMap.getInstance().getApplicationMap().getMap().getLayerByName("VectorPrintExtent").destroy(true);
        }

    }

    private void addEditPrintSettings() {
        fieldSet = new FieldSet();
        fieldSet.setHeadingHtml(PrintModuleConstants.INSTANCE.GPPrintWidget_editFieldSetHeadingText());
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(100);
        layout.setLabelPad(5);
        fieldSet.setLayout(layout);

        title = new TextField<String>();
        title.setAllowBlank(false);
        title.setName(GPPrintEnumBean.GPPRINT_TITLE.toString());
        title.setFieldLabel(PrintModuleConstants.INSTANCE.GPPrintWidget_titleLabelText());

        fieldSet.add(title);

        mapTitle = new TextField<String>();
        mapTitle.setName(GPPrintEnumBean.GPPRINT_MAP_TITLE.toString());
        mapTitle.setFieldLabel(PrintModuleConstants.INSTANCE.GPPrintWidget_mapTitleLabelText());

        fieldSet.add(mapTitle);

        comments = new TextArea();
        comments.setName(GPPrintEnumBean.GPPRINT_COMMENTS.toString());
        comments.setPreventScrollbars(true);
        comments.setFieldLabel(PrintModuleConstants.INSTANCE.GPPrintWidget_commentsLabelText());

        comments.setSize(150, 150);
        fieldSet.add(comments);

        this.formPanel.add(fieldSet);
    }

    private void addComboDPI() {
        fieldSet = new FieldSet();
        fieldSet.setHeadingHtml(PrintModuleConstants.INSTANCE.GPPrintWidget_DPIFieldSetHeadingText());
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(100);
        layout.setLabelPad(5);
        fieldSet.setLayout(layout);

        this.storeDPI = new ListStore<DPI>();
        this.storeDPI.add(PrintUtility.getDPI());

        this.comboDPI = new ComboBox<DPI>();
        this.comboDPI.setFieldLabel(PrintModuleConstants.INSTANCE.GPPrintWidget_comboDPIFieldLabelText());
        this.comboDPI.setEmptyText(PrintModuleConstants.INSTANCE.GPPrintWidget_comboDPIEmptyText());
        this.comboDPI.setDisplayField(DPI.EnumDPI.DPI.getValue());
        this.comboDPI.setEditable(false);
        this.comboDPI.setAllowBlank(false);
        this.comboDPI.setForceSelection(true);
        this.comboDPI.setTypeAhead(true);
        this.comboDPI.setTriggerAction(TriggerAction.ALL);

        this.comboDPI.setStore(this.storeDPI);

        fieldSet.add(this.comboDPI);

        super.formPanel.add(fieldSet);
    }

    private void addComboTemplate() {
        fieldSet = new FieldSet();
        fieldSet.setHeadingHtml(PrintModuleConstants.INSTANCE.GPPrintWidget_templateFieldSetHeadingText());
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(100);
        layout.setLabelPad(5);
        fieldSet.setLayout(layout);

        this.storeTemplate = new ListStore<PrintTemplate>();
        this.storeTemplate.add(PrintUtility.getTemplate());

        this.comboTemplate = new ComboBox<PrintTemplate>();
        this.comboTemplate.setFieldLabel(
                PrintModuleConstants.INSTANCE.GPPrintWidget_comboTemplateFieldLabelText());
//        this.comboTemplate.setEmptyText("Choose Template....");
        this.comboTemplate.setValue(new PrintTemplate(
                PrintTemplateConstants.INSTANCE.A4_Portrait()));

        this.comboTemplate.setDisplayField(
                PrintTemplate.PrintEnumTemplate.TEMPLATE.getValue());
        this.comboTemplate.setEditable(false);
        this.comboTemplate.setAllowBlank(false);
        this.comboTemplate.setForceSelection(true);
        this.comboTemplate.setTypeAhead(true);
        this.comboTemplate.setTriggerAction(TriggerAction.ALL);

        this.comboTemplate.setStore(this.storeTemplate);

        fieldSet.add(this.comboTemplate);

        super.formPanel.add(fieldSet);

        comboTemplate.addSelectionChangedListener(
                new SelectionChangedListener<PrintTemplate>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<PrintTemplate> se) {
                if (se != null) {
                    if (GPApplicationMap.getInstance().getApplicationMap().
                            getMap().getLayerByName("VectorPrintExtent") != null) {
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().removeControl(dragPrintArea);
                        dragPrintArea.deactivate();
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().removeLayer(GPApplicationMap.
                                getInstance().getApplicationMap().getMap().
                                getLayerByName("VectorPrintExtent"));

                        String scaleString = comboScale.getValue().getScale();
                        String scaleStringRight = scaleString.substring(
                                scaleString.indexOf(":") + 1);
                        String scaleStringWithoutDot = scaleStringRight.
                                replaceAll("\\.", "");
                        float scale = Float.parseFloat(scaleStringWithoutDot);
                        updateRotationAndSizeForPrint(se.getSelectedItem().
                                getTemplate());
                        Vector printExtent = PrintUtility.createRectangle(
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap().getCenter(), scale,
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap(), sizeFactor,
                                rotation);
                        dragPrintArea = PrintUtility.enableDragPrintArea(
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap(), printExtent);
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().addLayer(printExtent);
                    }

                }
            }
        });
    }

    private void addScaleCombo() {
        fieldSet = new FieldSet();
        fieldSet.setHeadingHtml(PrintModuleConstants.INSTANCE.GPPrintWidget_scaleFieldSetHeadingText());
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(100);
        layout.setLabelPad(5);
        fieldSet.setLayout(layout);

        this.storeScale = new ListStore<Scale>();
        this.storeScale.add(PrintUtility.getScale());

        this.comboScale = new ComboBox<Scale>();
        this.comboScale.setFieldLabel(PrintModuleConstants.INSTANCE.
                GPPrintWidget_comboScaleFieldLabelText());
//        this.comboScale.setEmptyText("Choose Scale....");
        this.comboScale.setValue(new Scale("1:4.000.000"));
        this.comboScale.setDisplayField(
                Scale.ScaleEnum.SCALE.getValue());
        this.comboScale.setEditable(false);
        this.comboScale.setAllowBlank(false);
        this.comboScale.setForceSelection(true);
        this.comboScale.setTypeAhead(true);
        this.comboScale.setTriggerAction(TriggerAction.ALL);

        this.comboScale.setStore(this.storeScale);

        fieldSet.add(this.comboScale);

        super.formPanel.add(fieldSet);

        comboScale.addSelectionChangedListener(
                new SelectionChangedListener<Scale>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<Scale> se) {
                if (se != null) {
                    if (GPApplicationMap.getInstance().getApplicationMap().
                            getMap().getLayerByName("VectorPrintExtent") != null) {
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().removeControl(dragPrintArea);
                        dragPrintArea.deactivate();
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().removeLayer(GPApplicationMap.
                                getInstance().getApplicationMap().getMap().
                                getLayerByName("VectorPrintExtent"));

                        String scaleString = se.getSelectedItem().getScale();
                        String scaleStringRight = scaleString.substring(
                                scaleString.indexOf(":") + 1);
                        String scaleStringWithoutDot = scaleStringRight.
                                replaceAll("\\.", "");
                        float scale = Float.parseFloat(scaleStringWithoutDot);
                        Vector printExtent = PrintUtility.createRectangle(
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap().getCenter(), scale,
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap(), sizeFactor,
                                rotation);
                        dragPrintArea = PrintUtility.enableDragPrintArea(
                                GPApplicationMap.getInstance().
                                getApplicationMap().getMap(), printExtent);
                        GPApplicationMap.getInstance().getApplicationMap().
                                getMap().addLayer(printExtent);
                    }

                }
            }
        });


    }

    private void addCheckPrintBaseMap() {
        fieldSet = new FieldSet();
        fieldSet.setHeadingHtml(PrintModuleConstants.INSTANCE.
                GPPrintWidget_checkPrintFieldSetHeadingText());
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(100);
        layout.setLabelPad(5);
        fieldSet.setLayout(layout);
        this.checkPrintBaseMap = new CheckBox();
        this.checkPrintBaseMap.setFieldLabel(PrintModuleConstants.INSTANCE.
                GPPrintWidget_checkBoxPrintBaseMapFieldLabelText());
        this.checkPrintBaseMap.setToolTip(PrintModuleConstants.INSTANCE.
                GPPrintWidget_checkBoxPrintBaseMapTooltipText());
        fieldSet.add(this.checkPrintBaseMap);
        super.formPanel.add(fieldSet);
    }

    private void addButtons() {
        formPanel.setButtonAlign(HorizontalAlignment.RIGHT);

        print = new Button(ButtonsConstants.INSTANCE.printText(),
                PrintResources.ICONS.print(),
                new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                execute();
            }
        });

        this.formPanel.addButton(print);

        this.cancel = new Button(ButtonsConstants.INSTANCE.cancelText(),
                BasicWidgetResources.ICONS.cancel(),
                new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (GPApplicationMap.getInstance().getApplicationMap().getMap().
                        getLayerByName("VectorPrintExtent") != null) {
                    GPApplicationMap.getInstance().getApplicationMap().getMap().
                            removeControl(dragPrintArea);
                    dragPrintArea.deactivate();
                    GPApplicationMap.getInstance().getApplicationMap().getMap().
                            removeLayer(GPApplicationMap.getInstance().
                            getApplicationMap().getMap().getLayerByName(
                            "VectorPrintExtent"));
                    //GPApplicationMap.getInstance().getApplicationMap().getMap().getLayerByName("VectorPrintExtent").destroy(true);
                }
                hide();
            }
        });

        this.formPanel.addButton(cancel);
    }

    private List<GPLayerBean> getVisibleLayersOnTree(List<ModelData> layers,
            List<GPLayerBean> visibleLayers) {
        for (Iterator<ModelData> it = layers.iterator(); it.hasNext();) {
            GPBeanTreeModel element = (GPBeanTreeModel) it.next();
            if (element instanceof AbstractFolderTreeNode && element.isChecked()
                    && element.getChildCount() != 0) {
                this.
                        getVisibleLayersOnTree(element.getChildren(),
                        visibleLayers);
            } else if (element.isChecked() && element instanceof GPLayerTreeModel) {
                visibleLayers.add((GPLayerBean) element);
            }
        }
        return visibleLayers;
    }

    public List<GPLayerBean> buildLayerList() {
        layerList = Lists.<GPLayerBean>newArrayList();
        AbstractRootTreeNode root = (AbstractRootTreeNode) this.tree.getStore().
                getRootItems().get(
                0);
        assert (root != null) : "VisitorDisplayHide on getVisibleLayers(): Impossible to retrieve root element";
        return this.getVisibleLayersOnTree(root.getChildren(), layerList);
    }

    @Override
    public void showForm() {
        super.showForm();
        this.comboScale.setValue(new Scale("1:4.000.000"));

        updateRotationAndSizeForPrint(comboTemplate.getValue().getTemplate());
        Vector printExtent = PrintUtility.createRectangle(GPApplicationMap.
                getInstance().getApplicationMap().getMap().getCenter(), 4000000,
                GPApplicationMap.getInstance().getApplicationMap().getMap(),
                sizeFactor, rotation);

        GPApplicationMap.getInstance().getApplicationMap().getMap().addLayer(
                printExtent);
        dragPrintArea = PrintUtility.enableDragPrintArea(GPApplicationMap.
                getInstance().getApplicationMap().getMap(), printExtent);

    }

    private void updateRotationAndSizeForPrint(String template) {

        if (template.contains(PrintTemplateConstants.INSTANCE.A4_Landscape())) {
            sizeFactor = .5;
            rotation = false;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A4_Portrait())) {
            sizeFactor = .5;
            rotation = true;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A3_Landscape())) {
            sizeFactor = 1;
            rotation = false;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A3_Portrait())) {
            sizeFactor = 1;
            rotation = true;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A2_Landscape())) {
            sizeFactor = 2;
            rotation = false;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A2_Portrait())) {
            sizeFactor = 2;
            rotation = true;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A1_Landscape())) {
            sizeFactor = 3;
            rotation = false;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A1_Portrait())) {
            sizeFactor = 3;
            rotation = true;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A0_Landscape())) {
            sizeFactor = 4;
            rotation = false;
        } else if (template.contains(PrintTemplateConstants.INSTANCE.A0_Portrait())) {
            sizeFactor = 4;
            rotation = true;
        }
    }

    private float getCurrentScale() {
        String scaleString = comboScale.getValue().getScale();
        String scaleStringRight = scaleString.substring(
                scaleString.indexOf(":") + 1);
        String scaleStringWithoutDot = scaleStringRight.
                replaceAll("\\.", "");
        return Float.parseFloat(scaleStringWithoutDot);
    }
}