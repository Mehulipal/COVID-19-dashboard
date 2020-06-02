package com.karankumar.covid19dashboard.ui.views.global;

import com.karankumar.covid19dashboard.backend.Country;
import com.karankumar.covid19dashboard.backend.api.ApiConst;
import com.karankumar.covid19dashboard.backend.api.ApiStats;
import com.karankumar.covid19dashboard.ui.MainView;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;

@Route(value = "global", layout = MainView.class)
@PageTitle("COVID-19: Global statistics")
public class GlobalView extends VerticalLayout {
    private final Span deathCounter;
    private final Span recoveredCounter;
    private final Span casesCounter;
    private Chart mostCasesChart  = new Chart(ChartType.PIE);;
    private Chart mostDeathsChart = new Chart(ChartType.PIE);

    private enum TotalType {
        DEATHS,
        RECOVERED,
        CASES
    }

    public GlobalView() {
        ApiStats globalStats = new ApiStats();
        Integer totalDeaths = globalStats.getTotalDeaths();
        Integer totalRecovered = globalStats.getTotalRecovered();
        Integer totalCases = globalStats.getTotalCases();

        System.out.println("GlobalView: Total deaths: " + totalDeaths);
        System.out.println("GlobalView: Total recovered: " + totalRecovered);
        System.out.println("GlobalView: Total cases: " + totalCases);

        Board board = new Board();

        String counterDigit = "counterDigit";
        String counterWrapper = "counterWrapper";
        deathCounter = new Span("test1");
        deathCounter.addClassNames(counterDigit, "deathCounterDigit", counterWrapper);
        recoveredCounter = new Span("test2");
        recoveredCounter.addClassNames(counterDigit, "recoveredCounterDigit", counterWrapper);
        casesCounter = new Span("test3");
        casesCounter.addClassNames(counterDigit, "casesCounterDigit", counterWrapper);

        H4 deathsH4 = new H4("Total deaths");
        H4 totalRecoveredH4 = new H4("Total recovered");
        H4 totalCasesH4 = new H4("Total confirmed cases");

        ArrayList<H4> labels = new ArrayList<>(4);
        labels.add(deathsH4);
        labels.add(totalRecoveredH4);
        labels.add(totalCasesH4);
        labels.forEach(h4 -> h4.addClassName(counterWrapper));

        Div totalDeathsDiv = addDivLabel(deathsH4);
        setCounter(totalDeaths, TotalType.DEATHS);
        Div totalRecoveredDiv = addDivLabel(totalRecoveredH4);
        setCounter(totalRecovered, TotalType.RECOVERED);
        Div totalCasesDiv = addDivLabel(totalCasesH4);
        setCounter(totalCases, TotalType.CASES);

        board.addRow(deathCounter, recoveredCounter, casesCounter);
        board.addRow(totalDeathsDiv, totalRecoveredDiv, totalCasesDiv);

        add(board);

        Grid<Country> grid = new Grid<>(Country.class);
        grid.setItems(globalStats.getAllCountriesSummary());
        add(grid);

        add(new HtmlComponent("br"));

        HorizontalLayout charts = new HorizontalLayout(mostCasesChart, mostDeathsChart);
        configureMostCasesChart(globalStats.getMostCases());
        configureMostDeathsChart(globalStats.getMostDeaths());
        charts.setSizeFull();
        add(charts);

        Text lastUpdated = new Text("Last updated on " + globalStats.getDate() + " at " + globalStats.getTime());
        add(lastUpdated);

        add(new Anchor("https://covid19api.com/", "Source: COVID-19 API"));
    }

    private Div addDivLabel(H4 label) {
        Div div = new Div();
        div.add(label);
        return div;
    }

    private void setCounter(Integer total, TotalType type) {
        if (total != null) {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);
            String formattedTotal = numberFormat.format(total);
            switch(type) {
                case DEATHS:
                    deathCounter.setText(formattedTotal);
                    break;
                case RECOVERED:
                    recoveredCounter.setText(formattedTotal);
                    break;
                case CASES:
                    casesCounter.setText(formattedTotal);
                    break;
            }
        }
    }

    private void configureMostCasesChart(TreeMap<Integer, String> mostCases) {
        Configuration conf = mostCasesChart.getConfiguration();
        conf.setTitle("Top " + ApiConst.MOST_CONFIRMED_CASES + " countries with the most confirmed cases");

        conf.setTooltip(new Tooltip());

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        for (Integer i : mostCases.keySet()) {
            series.add(new DataSeriesItem(mostCases.get(i), i));
        }

        conf.setSeries(series);
    }

    private void configureMostDeathsChart(TreeMap<Integer, String> mostCases) {
        Configuration conf = mostDeathsChart.getConfiguration();
        conf.setTitle("Top " + ApiConst.MOST_CONFIRMED_CASES + " countries with the most confirmed deaths");

        conf.setTooltip(new Tooltip());

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        for (Integer i : mostCases.keySet()) {
            series.add(new DataSeriesItem(mostCases.get(i), i));
        }

        conf.setSeries(series);
    }
}
