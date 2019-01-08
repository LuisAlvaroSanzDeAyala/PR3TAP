package com.vaadin.almacen;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.safety.Cleaner;

import com.vaadin.almacen.domain.AlmacenOrder;
import com.vaadin.almacen.domain.AlmacenOrderRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import aj.org.objectweb.asm.Label;

@StyleSheet("frontend://src/styles.css")
@Route
public class Ingresar extends VerticalLayout implements GlobalConstants{
	AlmacenService service;
	String importe = "0";
     H3 heading2 = new H3("Ingreso de dinero");
     RouterLink listOrders = new RouterLink("Ver listado", ListadoAlmacen.class);
     String acumulado;
     Integer cambioInt;
     Integer cambioInt2;
     String cambioString;
     String cambioString2;
     
	TextField ingreso = new TextField("Ingreso en Euros:");

	 public Ingresar(AlmacenOrderRepository repo) {
	        this.service = service;
	        ingreso.setValue(importe);

	        setDefaultHorizontalComponentAlignment(FlexLayout.Alignment.CENTER);
	        RouterLink listOrders = new RouterLink("Ver listado", ListadoAlmacen.class);
	        RouterLink main = new RouterLink("Agregar nuevo producto", Main.class);
	        Button order = new Button("Agregar dinero");
	        add(heading2, ingreso, listOrders,main, order);
	        
	        order.addClickListener(e -> {
	        	dispo.add(ingreso.getValue());
	        	if (dispo.size() == 2) {
	        		cambioString = dispo.get(0);
	        		cambioString2 = dispo.get(1);
	        		dispo.remove(0);
	        		cambioInt = Integer.parseInt(cambioString);
	        		cambioInt2 = Integer.parseInt(cambioString2);
	        		cambioInt = cambioInt+cambioInt2;
	        		acumulado = Integer.toString(cambioInt);
	        		dispo.set(0, acumulado);

	        	}else {
	        		dispo.add(ingreso.getValue());
	        	}
	        	String msg = String.format(
                        "Ingresado correctamente.");
	        	
	        });
	 }
}
