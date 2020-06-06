package com.karankumar.covid19dashboard.ui.views.country;

import com.karankumar.covid19dashboard.backend.api.dayone.CaseType;
import com.karankumar.covid19dashboard.backend.api.dayone.DayOneTotalStats;
import com.karankumar.covid19dashboard.backend.api.util.CountryName;
import com.karankumar.covid19dashboard.backend.domain.dayone.CountryDeathsTotal;
import com.karankumar.covid19dashboard.backend.domain.dayone.CountryTotal;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeathComparisonView<T extends CountryTotal> extends BaseCaseView<T> {
    private ArrayList<CountryDeathsTotal> totalDeaths;
    private Chart deathChart = new Chart(ChartType.AREA);
    private static final Logger logger = Logger.getLogger(CountryView.class.getName());
    private String[] deathDates;
    private Number[] deaths;

    public DeathComparisonView() {
        ComboBox<CountryName> selectCountry = configureCountryNameComboBox();
        Button clear = new Button("Reset", e -> removeExistingChart());

        HorizontalLayout horizontalLayout = new HorizontalLayout(selectCountry, clear);
        horizontalLayout.setAlignItems(Alignment.END);
        horizontalLayout.setSizeFull();

        add(horizontalLayout);
    }

    @Override
    protected void setCountryValueChangeListener(ComboBox<CountryName> country) {
        country.addValueChangeListener(event -> {
            if (event != null && event.isFromClient()) {
                createDeathsGraph(event.getValue());
            }
        });
    }

    private void createDeathsGraph(CountryName countryName) {
        DayOneTotalStats totalDeathsSinceDayOne = new DayOneTotalStats(countryName, CaseType.DEATHS);
        totalDeaths = totalDeathsSinceDayOne.getTotalDeaths();

        if (isTotalEmpty((ArrayList<T>) totalDeaths)) {
            logger.log(Level.FINE, "Data was empty");
            return;
        }

        setDeaths();

        String chartTitle = "Number of deaths since the first death";
        String yAxisName = "Number of deaths";
        setChartConfig(chartTitle, yAxisName, countryName.toString(), deaths);
        add(deathChart);
    }

    protected void removeExistingChart() {
        if (deathChart.isVisible()) {
            remove(deathChart);
            deathChart = new Chart(ChartType.AREA);
        }
    }

    @Override
    protected void createGraph(CountryName value) {

    }

//    private boolean isTotalEmpty(ArrayList<T> deathTotal) {
//        if (deathTotal.isEmpty()) {
//            Notification notification = new Notification(
//                    EMPTY_ERROR_MESSAGE, 5000);
//            notification.open();
//            return true;
//        }
//        return false;
//    }

    private void setDeaths() {
        deathDates = new String[totalDeaths.size()];
        deaths = new Number[totalDeaths.size()];

        for (int i = 0; i < totalDeaths.size(); i++) {
            CountryDeathsTotal countryLive = totalDeaths.get(i);
            deathDates[i] = countryLive.getDate();
            deaths[i] = countryLive.getNumberOfDeaths();
        }
    }

    protected void setChartConfig(String chartTitle, String yAxisName, String seriesName, Number[] total) {
        Configuration conf = deathChart.getConfiguration();
        conf.setTitle(chartTitle);
        Tooltip tooltip = new Tooltip();
        tooltip.setValueSuffix(" deaths");
        conf.setTooltip(tooltip);

        XAxis xAxis = conf.getxAxis();
        xAxis.setCategories(deathDates);

        YAxis yAxis = conf.getyAxis();
        yAxis.setTitle(yAxisName);

        conf.addSeries(new ListSeries(seriesName, Arrays.asList(total)));
    }
}
