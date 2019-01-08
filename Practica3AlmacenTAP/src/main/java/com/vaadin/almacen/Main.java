/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.almacen;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.almacen.domain.AlmacenOrder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@StyleSheet("frontend://src/styles.css")
@Route
public class Main extends VerticalLayout implements GlobalConstants {

    AlmacenService service;

    Binder<AlmacenOrder> binder = new BeanValidationBinder<>(AlmacenOrder.class);
    
    
    //TextField name = new TextField("Nombre de producto");
    //TextField email = new TextField("Email");
    //ComboBox<String> shirtSize = new ComboBox("T-shirt size");
    ComboBox<String> name = new ComboBox<String>("Productos");
    ComboBox<String> cantidad = new ComboBox<String>("Cantidad a introducir");
    TextField precio = new TextField("Precio del producto:");
    
    public Main(AlmacenService service) {
        this.service = service;
        if (dispo.size() == 0) {
        	dispo.add("0");
        	
        }
        // Build the layout
        H1 heading = new H1("Práctica 3 de TAP");
        H3 heading2 = new H3("Alejandro Lago Prego y Luis Álvaro Sanz de Ayala Oliver - TAP");
        Button order = new Button("Introducir en Almacén");
        setDefaultHorizontalComponentAlignment(FlexLayout.Alignment.CENTER);
        RouterLink listOrders = new RouterLink("Ver listado", ListadoAlmacen.class);
        add(
                heading,
                heading2,
                name,
                cantidad,
                precio,
                //email,
                //shirtSize,
                order,
                listOrders
        );

        // configure components
        cantidad.setItems(service.getCantidad());
        name.setItems(service.getName());

        
        order.addClickListener(e -> {
        	precio.setValue(precio.getValue()+"€");
            
            Integer cont = 0;
            
            for(int indice = 0;indice<datos.size();indice=indice+3)
            {
            	if (datos.get(indice) == binder.getBean().getName()) {
            		
            		cont++;
            	}
            }
            if (cont == 0){
            	submitOrder();
            	datos.add(binder.getBean().getName());
                datos.add(binder.getBean().getCantidad());
                datos.add(binder.getBean().getPrecio());
                
                String msg = String.format(
                        "%s Agregado correctamente.",
                        binder.getBean().getName(), binder.getBean().getCantidad(), binder.getBean().getPrecio());
                Notification.show(msg, 3000, Notification.Position.MIDDLE);
                init();
            	
            }else{
            	String msg = String.format(
                        "Error - Este producto ya está agregado, si quieres añadir más unidades puedes hacerlo desde el listado.");
                Notification.show(msg, 6000, Notification.Position.MIDDLE);
  
            }
            

            
            
        });

        // Bind fields from this UI class to domain object using naming convetion
        binder.bindInstanceFields(this);
        // enable save button only if the bean is valid
        binder.addStatusChangeListener(e -> order.setEnabled(binder.isValid()));

        init();
    }

    private void submitOrder() {
        service.placeOrder(binder.getBean());
    }

    private void init() {
        binder.setBean(new AlmacenOrder());
    }

    

}
