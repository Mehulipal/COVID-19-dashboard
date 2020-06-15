package com.karankumar.covid19dashboard.ui;

import com.karankumar.covid19dashboard.ui.utils.ViewsConst;
import com.karankumar.covid19dashboard.ui.views.country.CountryView;
import com.karankumar.covid19dashboard.ui.views.global.GlobalView;
import com.karankumar.covid19dashboard.ui.views.help.Feedback;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
public class MainView extends AppLayout {

    public MainView() {
        Tabs tabs = new Tabs();

        Tab globalTab = createTab(VaadinIcon.GLOBE, GlobalView.class, ViewsConst.GLOBAL);
        Tab countryTab = createTab(VaadinIcon.FLAG_O, CountryView.class, ViewsConst.COUNTRY);
        Tab feedbackTab = createTab(VaadinIcon.QUESTION_CIRCLE_O, Feedback.class, ViewsConst.FEEDBACK);
        tabs.add(globalTab, countryTab, feedbackTab);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        FlexLayout centreTabs = new FlexLayout();
        centreTabs.setSizeFull();
        centreTabs.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centreTabs.setAlignItems(FlexComponent.Alignment.CENTER);
        centreTabs.add(tabs);

        addToNavbar(true, centreTabs);
    }

    private static Tab createTab(VaadinIcon icon, Class<? extends Component> viewClass, String title) {
        return createTab(addLink(new RouterLink(null, viewClass), icon, title));
    }

    private static <T extends HasComponents> T addLink(T a, VaadinIcon icon, String title) {
        a.add(icon.create());
        a.add(title);
        return a;
    }

    private static Tab createTab (Component component) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(component);
        return tab;
    }
}
