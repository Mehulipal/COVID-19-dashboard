package com.karankumar.covid19dashboard.ui.views.country;

import com.karankumar.covid19dashboard.backend.api.dayone.DayOneTotalStats;
import com.karankumar.covid19dashboard.backend.api.util.CountryName;
import com.karankumar.covid19dashboard.backend.domain.CountryTotal;
import com.karankumar.covid19dashboard.ui.MainView;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "country", layout = MainView.class)
@PageTitle("COVID-19: Country statistics")
public class CountryView extends VerticalLayout {
    private Chart chart = new Chart(ChartType.AREA);
    private static final Logger logger = Logger.getLogger(DayOneTotalStats.class.getName());

    public CountryView() {
        ComboBox<CountryName> country = new ComboBox<>("Country");
        country.setItems(CountryName.values());
        country.setRequired(true);
        country.setPlaceholder("Select a country");
        country.setMinWidth("20%");
        add(country);

        country.addValueChangeListener(event -> {
            if (event != null && event.isFromClient()) {
                createCasesSinceDayOneGraph(event.getValue());
            }
        });

        add(new Anchor("https://covid19api.com/", "Source: COVID-19 API"));
    }

    private void createCasesSinceDayOneGraph(CountryName countryName) {
        DayOneTotalStats dayOneLive = new DayOneTotalStats(countryName);
        ArrayList<CountryTotal> liveCases = dayOneLive.getLiveCases();

        if (liveCases.isEmpty()) {
            logger.log(Level.FINE, "Data was empty");
            Notification notification = new Notification("Error, please try again", 5000);
            notification.open();
            return;
        }

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Number of confirmed cases since the first confirmed case");
        Tooltip tooltip = new Tooltip();
        tooltip.setValueSuffix(" cases");
        conf.setTooltip(tooltip);

        String[] dates = new String[liveCases.size()];
        Number[] cases = new Number[liveCases.size()];

        for (int i = 0; i < liveCases.size(); i++) {
            CountryTotal countryLive = liveCases.get(i);
            dates[i] = countryLive.getDate();
            cases[i] = countryLive.getNumberOfCases();
        }

        XAxis xAxis = conf.getxAxis();
        xAxis.setCategories(dates);

        YAxis yAxis = conf.getyAxis();
        yAxis.setTitle("Number of confirmed cases");

        conf.addSeries(new ListSeries(countryName.toString(), Arrays.asList(cases)));

        add(chart);
    }
}
