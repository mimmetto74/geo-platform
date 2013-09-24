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
package org.geosdi.geoplatform.gui.client.widget.time;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import java.util.List;
import org.geosdi.geoplatform.gui.client.LayerResources;
import org.geosdi.geoplatform.gui.client.command.layer.basic.GetLayerDimensionRequest;
import org.geosdi.geoplatform.gui.client.command.layer.basic.GetLayerDimensionResponse;
import org.geosdi.geoplatform.gui.client.config.MementoModuleInjector;
import org.geosdi.geoplatform.gui.client.model.memento.save.IMementoSave;
import org.geosdi.geoplatform.gui.client.model.memento.save.storage.AbstractMementoOriginalProperties;
import org.geosdi.geoplatform.gui.client.puregwt.decorator.event.TreeChangeLabelEvent;
import org.geosdi.geoplatform.gui.client.widget.GeoPlatformWindow;
import org.geosdi.geoplatform.gui.client.widget.SearchStatus;
import org.geosdi.geoplatform.gui.client.widget.tree.GPTreePanel;
import org.geosdi.geoplatform.gui.command.api.ClientCommandDispatcher;
import org.geosdi.geoplatform.gui.command.api.GPClientCommand;
import org.geosdi.geoplatform.gui.configuration.message.GeoPlatformMessage;
import org.geosdi.geoplatform.gui.impl.map.event.GPLoginEvent;
import org.geosdi.geoplatform.gui.impl.map.event.TimeFilterLayerMapEvent;
import org.geosdi.geoplatform.gui.impl.view.LayoutManager;
import org.geosdi.geoplatform.gui.model.tree.GPBeanTreeModel;
import org.geosdi.geoplatform.gui.model.tree.GPLayerTreeModel;
import org.geosdi.geoplatform.gui.puregwt.GPHandlerManager;
import org.geosdi.geoplatform.gui.puregwt.layers.decorator.event.GPTreeLabelEvent;
import org.geosdi.geoplatform.gui.puregwt.properties.WidgetPropertiesHandlerManager;
import org.geosdi.geoplatform.gui.utility.GPSessionTimeout;
import org.geosdi.geoplatform.gui.utility.GeoPlatformUtils;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
public class LayerTimeFilterWidget extends GeoPlatformWindow {

    public final static String LAYER_TIME_DELIMITER = " - [";
    private final static short WIDGET_HEIGHT = 250;
    private final static short WIDGET_WIDTH = 400;
    private final static String TIME_FILTER_HEADING = "TIME FILTER EDITOR";
    private final TimeFilterLayerMapEvent timeFilterLayerMapEvent = new TimeFilterLayerMapEvent();
    private final GPTreeLabelEvent labelEvent = new TreeChangeLabelEvent();
    private NumberField startFilterNumberField;
    private NumberField endFilterNumberField;
    private ComboBox<DimensionData> startDimensionComboBox;
    private ComboBox<DimensionData> endDimensionComboBox;
    private ListStore<DimensionData> startStore;
    private ListStore<DimensionData> endStore;
    private GPTreePanel<GPBeanTreeModel> treePanel;
    private Label dimensionSizeLabel;
    private final Radio fixedDimensionRadio = new Radio();
    private final Radio variableDimensionRadio = new Radio();
    private final LayoutContainer fixedDimensionContainer = new LayoutContainer(new FormLayout());
    private final LayoutContainer variableDimensionContainer;
    private RadioGroup dimensionRadioGroup;
    private SelectionListener<ButtonEvent> applyFilterSelectionListener;
    private Label endTimeLabel;
    private Label startTimeLabel;
    private Timer animationTimer;
    private SelectionListener<ButtonEvent> playSelectioListener;
    private ToggleButton playButton = new ToggleButton("Play",
            LayerResources.ICONS.playTime());
    private Slider slider;
    private CheckBox rangeCheckBox;

    public LayerTimeFilterWidget(boolean lazy, GPTreePanel<GPBeanTreeModel> treePanel) {
        super(lazy);
        this.variableDimensionContainer = new LayoutContainer(new FormLayout()) {

            @Override
            protected void onAttach() {
                super.onAttach();
                playButton.enable();
            }

            @Override
            protected void onHide() {
                super.onHide();
                stopPlayer();
                playButton.disable();
            }

            @Override
            protected void onShow() {
                super.onShow();
                playButton.enable();
            }

            @Override
            protected void onDetach() {
                super.onDetach();
                stopPlayer();
                playButton.disable();
            }
        };
        this.treePanel = treePanel;
    }

    @Override
    public void addComponent() {
        this.dimensionRadioGroup = new RadioGroup();
        this.dimensionRadioGroup.setFieldLabel("Dimension strategy");
        fixedDimensionRadio.setBoxLabel("Fixed Dimension");
        fixedDimensionRadio.setHideLabel(Boolean.TRUE);
        this.dimensionRadioGroup.add(fixedDimensionRadio);
        variableDimensionRadio.setBoxLabel("Variable Dimension");
        variableDimensionRadio.setHideLabel(Boolean.TRUE);
        this.dimensionRadioGroup.add(variableDimensionRadio);
        this.fixedDimensionContainer.setVisible(Boolean.FALSE);
        this.variableDimensionContainer.setVisible(Boolean.FALSE);
        this.fixedDimensionRadio.addListener(Events.Change, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent fe) {
                if (fixedDimensionRadio.getValue()) {
                    fixedDimensionContainer.setVisible(Boolean.TRUE);
                    variableDimensionContainer.setVisible(Boolean.FALSE);
                }
            }
        });
        this.variableDimensionRadio.addListener(Events.Change, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent fe) {
                if (variableDimensionRadio.getValue()) {
                    variableDimensionContainer.setVisible(Boolean.TRUE);
                    fixedDimensionContainer.setVisible(Boolean.FALSE);
                }
            }
        });

        this.startStore = new ListStore<DimensionData>();
        this.startDimensionComboBox = new ComboBox<DimensionData>();
        this.startDimensionComboBox.setFieldLabel("Start Fixed dimension");
        this.startDimensionComboBox.setStore(this.startStore);
        this.startDimensionComboBox.setDisplayField(DimensionData.DIMENSION_KEY);
        this.startDimensionComboBox.setEditable(Boolean.FALSE);
        this.startDimensionComboBox.addSelectionChangedListener(new SelectionChangedListener<DimensionData>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<DimensionData> se) {
                if (se.getSelectedItem() != null) {
                    endStore.removeAll();
                    int indexStart = startStore.getModels().indexOf(se.getSelectedItem()) + 1;
//                    System.out.println("Sublist start: " + indexStart);
//                    System.out.println("Sublist end: " + (startStore.getModels().size() - 1));
                    if (indexStart < startStore.getModels().size()) {
                        endDimensionComboBox.enable();
                        endStore.add(startStore.getModels().subList(
                                startStore.indexOf(se.getSelectedItem()) + 1,
                                startStore.getModels().size()));
                    } else {
                        endDimensionComboBox.disable();
                    }
                }
            }
        });
        this.endStore = new ListStore<DimensionData>();
        this.endDimensionComboBox = new ComboBox<DimensionData>();
        this.endDimensionComboBox.setFieldLabel("End Fixed dimension");
        this.endDimensionComboBox.setStore(this.endStore);
        this.endDimensionComboBox.setDisplayField(DimensionData.DIMENSION_KEY);
        this.endDimensionComboBox.disable();
        this.endDimensionComboBox.setEditable(Boolean.FALSE);
        dimensionSizeLabel = new Label();
        dimensionSizeLabel.setStyleAttribute("font-size", "12px");
        this.startFilterNumberField = new NumberField();
        this.startFilterNumberField.setFireChangeEventOnSetValue(Boolean.TRUE);
        this.startFilterNumberField.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (checkStartFieldValue()) {
                    int fromFilter = startFilterNumberField.getValue().intValue();
                    int position = startStore.getModels().size() - fromFilter - 1;
                    String tooltip = (String) startStore.getModels().get(
                            position).get(
                            DimensionData.DIMENSION_KEY);
//                            startStore.getModels().size() - fromFilter - 1).get(
                    startFilterNumberField.setToolTip(tooltip);
                    startTimeLabel.setText(tooltip);
                    slider.setValue(position);
                } else {
                    startTimeLabel.setText("");
                    startFilterNumberField.setToolTip("");
                }
            }
        });
        startTimeLabel = new Label();
        startTimeLabel.setStyleAttribute("font-size", "1.3em");
        this.startFilterNumberField.setSize(50, 30);
        this.endFilterNumberField = new NumberField();
        this.endFilterNumberField.setFireChangeEventOnSetValue(Boolean.TRUE);
        endTimeLabel = new Label();
        endTimeLabel.setStyleAttribute("font-size", "1.3em");
        this.endFilterNumberField.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (checkEndFieldValue()) {
                    int endFilter = endFilterNumberField.getValue().intValue();
                    String tooltip = (String) startStore.getModels().get(
                            endFilter).
                            get(DimensionData.DIMENSION_KEY);
//                            startStore.getModels().size() - endFilter - 1).
                    endFilterNumberField.setToolTip(tooltip);
                    endTimeLabel.setText(tooltip);
                } else {
                    endTimeLabel.setText("");
                    endFilterNumberField.setToolTip("");
                }
            }
        });
        this.endFilterNumberField.setSize(50, 30);
        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(Boolean.FALSE);
        panel.setFrame(Boolean.TRUE);
        panel.setBorders(Boolean.FALSE);
        panel.setHeight(WIDGET_HEIGHT - 67);
        panel.add(this.dimensionRadioGroup);
        panel.setStyleAttribute("background-color", "white");
        this.variableDimensionContainer.add(dimensionSizeLabel, new FormData("100%"));

        Portal portal = new Portal(3);
        portal.setBorders(false);
        portal.setColumnWidth(0, .20);
        portal.setColumnWidth(1, .60);
        portal.setColumnWidth(2, .20);

        Portlet portlet = new Portlet();
        portlet.setLayout(new FitLayout());
        portlet.setHeaderVisible(false);
        portlet.setSize(50, 30);
        portlet.add(this.startFilterNumberField);
        portal.add(portlet, 0);
        portlet = new Portlet(new CenterLayout());
        portlet.setHeaderVisible(false);
        portlet.setSize(100, 30);
        this.rangeCheckBox = new CheckBox();
        rangeCheckBox.setValue(true);
        rangeCheckBox.setBoxLabel("range");
        portlet.add(rangeCheckBox);
        portlet.setBorders(false);
        portal.add(portlet, 1);
        portlet = new Portlet();
        portlet.setHeaderVisible(false);
        portlet.setSize(50, 30);
        portlet.add(this.endFilterNumberField);
        portal.add(portlet, 2);

        this.variableDimensionContainer.add(portal, new FormData("99%"));
        //
        this.slider = new Slider() {

            @Override
            public void setValue(int value) {
                super.setValue(value);
                super.setMessage("" + (startStore.getModels().size() - value));
            }
        };
        slider.setIncrement(1);
        this.variableDimensionContainer.add(slider, new FormData("98%"));

        this.fixedDimensionContainer.add(this.startDimensionComboBox);
        this.fixedDimensionContainer.add(this.endDimensionComboBox);
        panel.add(this.fixedDimensionContainer, new FormData("100%"));
        panel.add(this.variableDimensionContainer, new FormData("100%"));
        super.add(panel);
        this.playSelectioListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                System.out.println("Play button status: ");
                if (!playButton.isPressed()) {
                    playButton.setText("Play");
                    playButton.setIcon(LayerResources.ICONS.playTime());
                    animationTimer.cancel();
                } else {
                    Number startValueNumber = startFilterNumberField.getValue();
                    if (startValueNumber == null) {
                        GeoPlatformMessage.alertMessage("Time Filter Warning",
                                "Impossible to show time sequence, the T1 time must be set");
                    } else {
                        Number endValueNumber = endFilterNumberField.getValue();
                        int endValue = -1;
                        if (endValueNumber != null) {
                            endValue = endValueNumber.intValue();
                        }
                        slider.setValue(startStore.getModels().size() - startValueNumber.intValue() - 1);
                        playButton.setIcon(LayerResources.ICONS.pauseTime());
                        playButton.setText("Pause");
                        playTimeFilter(endValue);
                    }
                }
            }
        };
        playButton.addSelectionListener(playSelectioListener);
        playButton.setHeight(30);
        super.addButton(playButton);

        Button apply = new Button("Apply");
        this.applyFilterSelectionListener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                GPLayerTreeModel layerSelected = (GPLayerTreeModel) treePanel.getSelectionModel().getSelectedItem();
                IMementoSave mementoSave = MementoModuleInjector.MainInjector.getInstance().getMementoSave();
                AbstractMementoOriginalProperties memento = mementoSave.copyOriginalProperties(layerSelected);
                String layerName;
                if (layerSelected.getAlias() != null
                        && layerSelected.getAlias().indexOf(LAYER_TIME_DELIMITER) != -1) {
                    layerName = layerSelected.getAlias().substring(0,
                            layerSelected.getAlias().indexOf(LAYER_TIME_DELIMITER));
                } else {
                    layerName = layerSelected.getLabel();
                }
                if (fixedDimensionRadio.getValue()) {
                    String timeFilter = (String) startDimensionComboBox.getValue().get(DimensionData.DIMENSION_KEY);
                    if (endDimensionComboBox.getValue() != null) {
                        timeFilter += "/" + (String) endDimensionComboBox.getValue().get(DimensionData.DIMENSION_KEY);
                    }
                    layerSelected.setTimeFilter(timeFilter);
                    layerSelected.setVariableTimeFilter(null);
                    layerSelected.setAlias(layerName + LAYER_TIME_DELIMITER + layerSelected.getTimeFilter() + "]");
                    WidgetPropertiesHandlerManager.fireEvent(labelEvent);
                } else {
                    if ((rangeCheckBox.getValue() && !checkEndFieldValue())
                            || !checkStartFieldValue()) {
                        GeoPlatformMessage.errorMessage("Time Filter Error", "Incorrect Position time, you must specify a valid position in size range");
                        return;
                    } else {
                        int fromFilter = slider.getValue();
                        String timeFilter = "" + (startStore.getModels().size() - fromFilter - 1);
                        String variableTimeFilter = (String) startStore.getModels().get(
                                fromFilter).get(DimensionData.DIMENSION_KEY);
                        if (rangeCheckBox.getValue() && playButton.isPressed()) {
                            fromFilter = startStore.getModels().size() - startFilterNumberField.getValue().intValue() - 1;
                            timeFilter = "" + startFilterNumberField.getValue().intValue();
                            variableTimeFilter = (String) startStore.getModels().get(
                                    fromFilter).get(DimensionData.DIMENSION_KEY);
                            //
                            int toFilter = slider.getValue();
                            timeFilter += "/" + (startStore.getModels().size() - slider.getValue() - 1);
                            variableTimeFilter += "/" + (String) startStore.getModels().get(
                                    toFilter).get(DimensionData.DIMENSION_KEY);
                        } //This is usefull when press Apply button to show all the elements in range
                        else if (rangeCheckBox.getValue() && endFilterNumberField.getValue() != null) {
                            fromFilter = startStore.getModels().size() - startFilterNumberField.getValue().intValue() - 1;
                            timeFilter = "" + startFilterNumberField.getValue().intValue();
                            variableTimeFilter = (String) startStore.getModels().get(
                                    fromFilter).get(DimensionData.DIMENSION_KEY);
                            //
                            int toFilter = startStore.getModels().size() - endFilterNumberField.getValue().intValue() - 1;
                            timeFilter += "/" + endFilterNumberField.getValue().intValue();
                            variableTimeFilter += "/" + (String) startStore.getModels().get(
                                    toFilter).get(DimensionData.DIMENSION_KEY);
                        }
                        layerSelected.setTimeFilter(timeFilter);
                        layerSelected.setVariableTimeFilter(variableTimeFilter);
                        layerSelected.setAlias(layerName + LAYER_TIME_DELIMITER + layerSelected.getVariableTimeFilter() + "]");
                        WidgetPropertiesHandlerManager.fireEvent(labelEvent);
                        slider.setMessage(variableTimeFilter);
                        GeoPlatformMessage.infoMessage("Time Filter Message",
                                "Showing the layer status at: " + timeFilter);
                    }
                }
                mementoSave.putOriginalPropertiesInCache(memento);
                timeFilterLayerMapEvent.setLayerBean(layerSelected);
                GPHandlerManager.fireEvent(timeFilterLayerMapEvent);
                treePanel.refresh(layerSelected);
            }
        };

        apply.addSelectionListener(applyFilterSelectionListener);

        super.addButton(apply);
        Button close = new Button("Close",
                new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                startFilterNumberField.clear();
                startTimeLabel.setText("");
                endTimeLabel.setText("");
                endFilterNumberField.clear();
                hide();
            }
        });

        super.addButton(close);
    }

    private void playTimeFilter(final int t2Time) {
        animationTimer = new Timer() {

            @Override
            public void run() {
//                    int startValue = getFielValueDecrement(startFilterNumberField);
                int startValue = slider.getValue();
                if (startValue != startStore.getModels().size() - 1
                        && (endFilterNumberField.getValue() != null
                        && (startStore.getModels().size() - startValue - 1)
                        != (endFilterNumberField.getValue().intValue() - 1))) {
                    slider.setValue(++startValue);
                    //This call produce the update in map
                    applyFilterSelectionListener.componentSelected(null);
                } else {
                    stopPlayer();
                }
            }
        };
        animationTimer.scheduleRepeating(2000);
    }

    private void stopPlayer() {
        playButton.toggle(Boolean.FALSE);
        playSelectioListener.componentSelected(null);
        //This call produce the update in map
        applyFilterSelectionListener.componentSelected(null);
    }

    private boolean checkStartFieldValue() {
        boolean condition = false;
//        System.out.println("Start store size: " + startStore.getModels().size());
        if (startFilterNumberField.getValue() != null) {
            int fromFilter = startFilterNumberField.getValue().intValue();
//            System.out.println("From filter: " + fromFilter);
            if (fromFilter >= 0 && fromFilter <= startStore.getModels().size() - 1) {
                condition = true;
            }
        }
        return condition;
    }

    private boolean checkEndFieldValue() {
        boolean condition = false;
        if (this.checkStartFieldValue() && endFilterNumberField.getValue() != null) {
            int fromFilter = startFilterNumberField.getValue().intValue();
            int toFilter = endFilterNumberField.getValue().intValue();
//            System.out.println("To filter: " + toFilter);
            if (toFilter < fromFilter && toFilter >= 0) {
                condition = true;
            }
        }
        return condition;
    }

    private int getFielValueDecrement(NumberField numberField) {
        int valueToReturn = -1;
        Number timeNumber = numberField.getValue();
        if (timeNumber != null) {
            valueToReturn = timeNumber.intValue();
            if (valueToReturn == 0) {
                valueToReturn = startStore.getModels().size() - 1;
            } else {
                --valueToReturn;
            }
        }
        return valueToReturn;
    }

    private int getFielValueIncrement(NumberField numberField) {
        int valueToReturn = -1;
        Number timeNumber = numberField.getValue();
        if (timeNumber != null) {
            valueToReturn = timeNumber.intValue();
            if (valueToReturn == startStore.getModels().size() - 1) {
                valueToReturn = 0;
            } else {
                ++valueToReturn;
            }
        }
        return valueToReturn;
    }

    private void loadDataToDisplay() {
        final GetLayerDimensionRequest getLayerDimensionRequest = GWT.
                <GetLayerDimensionRequest>create(GetLayerDimensionRequest.class);

        final GPLayerTreeModel layerSelected = (GPLayerTreeModel) treePanel.getSelectionModel().getSelectedItem();
        getLayerDimensionRequest.setLayerName(layerSelected.getName());

        ClientCommandDispatcher.getInstance().execute(
                new GPClientCommand<GetLayerDimensionResponse>() {

            private static final long serialVersionUID = 4372276287420606744L;

            {
                super.setCommandRequest(getLayerDimensionRequest);
            }

            @Override
            public void onCommandSuccess(GetLayerDimensionResponse response) {
                List<String> dimensionList = Lists.<String>newArrayList(response.getResult().split(","));
                dimensionSizeLabel.setText("Dimension Size: " + dimensionList.size());
                dimensionSizeLabel.setStyleAttribute("font-size", "1.3em");
                dimensionSizeLabel.setStyleAttribute("text-align", "right");
                startStore.removeAll();
                endStore.removeAll();
                for (String dimension : GeoPlatformUtils.safeList(dimensionList)) {
                    startStore.add(new DimensionData(dimension));
                }
                if (!dimensionList.isEmpty()) {
                    slider.setMaxValue(dimensionList.size());
                    slider.setMinValue(0);
                    endFilterNumberField.setValue(0);
                    if (layerSelected.getTimeFilter() != null) {
                        String[] timeFilterSplitted = layerSelected.getTimeFilter().split("/");
                        int startDimensionPosition = Integer.parseInt(timeFilterSplitted[0]);
                        slider.setValue(startStore.getModels().size() - startDimensionPosition - 1);
                        startFilterNumberField.setValue(startDimensionPosition);
                        if (timeFilterSplitted.length > 1) {
                            int endDimensionPosition = Integer.parseInt(timeFilterSplitted[1]);
                            endFilterNumberField.setValue(endDimensionPosition);
                        }
                    } else {
                        startFilterNumberField.setValue(dimensionList.size() - 1);
                    }
                }
            }

            @Override
            public void onCommandFailure(Throwable exception) {
                if (exception.getCause() instanceof GPSessionTimeout) {
                    GPHandlerManager.fireEvent(new GPLoginEvent(null));
                } else {
                    GeoPlatformMessage.errorMessage("Error Loading Time Filter",
                            "An error occurred while making the requested connection.\n"
                            + "Verify network connections and try again."
                            + "\nIf the problem persists contact your system administrator.");
                    LayoutManager.getInstance().getStatusMap().setStatus(
                            "Error Loading Time Filter",
                            SearchStatus.EnumSearchStatus.STATUS_NO_SEARCH.toString());
                    System.out.println("Error Loading Time Filter: " + exception.toString()
                            + " data: " + exception.getMessage());
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        this.loadDataToDisplay();
        GPLayerTreeModel layerElement = (GPLayerTreeModel) treePanel.getSelectionModel().getSelectedItem();
        String timeFilter = layerElement.getTimeFilter();
        String variableTimeFilter = layerElement.getVariableTimeFilter();
        if (variableTimeFilter != null) {
            try {
                String[] variableTimeFilterSplitted = timeFilter.split("/");
                int integerTimeFilter = Integer.parseInt(variableTimeFilterSplitted[0]);
                this.startFilterNumberField.setValue(integerTimeFilter);
                this.dimensionRadioGroup.setValue(this.variableDimensionRadio);
                if (variableTimeFilterSplitted.length > 1) {
                    integerTimeFilter = Integer.parseInt(variableTimeFilterSplitted[1]);
                    this.endFilterNumberField.setValue(integerTimeFilter);
                }
            } catch (NumberFormatException nfe) {
            }
        } else if (timeFilter != null) {
            String[] timeFilterSplitted = timeFilter.split("/");
            this.startDimensionComboBox.setValue(new DimensionData(timeFilterSplitted[0]));
            if (timeFilterSplitted.length > 1) {
                this.endDimensionComboBox.enable();
                this.endDimensionComboBox.setValue(new DimensionData(timeFilterSplitted[1]));
            }
            this.dimensionRadioGroup.setValue(this.fixedDimensionRadio);
        }
    }

    @Override
    public void initSize() {
        super.setSize(WIDGET_WIDTH, WIDGET_HEIGHT);
    }

    @Override
    public void setWindowProperties() {
        super.setHeading(TIME_FILTER_HEADING);
        super.setLayout(new FormLayout());
        super.setModal(Boolean.TRUE);
    }
}
