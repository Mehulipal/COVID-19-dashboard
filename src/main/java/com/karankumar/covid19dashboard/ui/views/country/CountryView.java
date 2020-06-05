package com.karankumar.covid19dashboard.ui.views.country;

import com.karankumar.covid19dashboard.ui.MainView;
import com.karankumar.covid19dashboard.ui.component.DashboardFooter;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "country", layout = MainView.class)
@PageTitle("COVID-19: Country statistics")
public class CountryView extends VerticalLayout {

    public CountryView() {
        Accordion accordion = new Accordion();
        accordion.setWidthFull();

        VerticalLayout casesAndDeathsView = new CasesAndDeathsView();
        accordion.add("Cases and deaths for one country", casesAndDeathsView);

        // TODO: change VerticalLayout to a vertical layout containing the combobox and graph
        VerticalLayout caseComparison = new CaseComparisonView();
        accordion.add("Compare the number of cases between two countries", caseComparison);

        // TODO: change VerticalLayout to a vertical layout containing the combobox and graph
        VerticalLayout deathComparison = new DeathComparisonView();
        accordion.add("Compare the number of deaths between two countries", deathComparison);

        add(accordion);
        add(new DashboardFooter());
    }
}
