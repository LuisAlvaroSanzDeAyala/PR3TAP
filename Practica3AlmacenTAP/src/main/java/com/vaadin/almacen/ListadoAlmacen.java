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
public class ListadoAlmacen extends VerticalLayout implements GlobalConstants{
    
    private final AlmacenOrderRepository repo;
    final Grid<AlmacenOrder> orders = new Grid<>(AlmacenOrder.class);
    
    
    Integer cambioInt;
    String cambioString;
    Float cambioDecimal;
    Integer euro = 1;
    String ncadena;
    //Integer totalInt;
    Binder<AlmacenOrder> binder = new BeanValidationBinder<>(AlmacenOrder.class); 
    
    public ListadoAlmacen(AlmacenOrderRepository repo) {
        this.repo = repo;
        System.out.print(datos);
        // Build the layout
        H1 heading = new H1("Listado de Almacén");
        Button update = new Button(VaadinIcon.REFRESH.create());

        TextField total = new TextField("Recuento total");
        total.setEnabled(false);
        TextField disponible = new TextField("Dinero disponible");
        disponible.setEnabled(false);
        
        actualizarTotal(total);
        actualizarDisponible(disponible);

    	
        update.addClickListener(e -> {
        	actualizarTotal(total);
	
        }); 

        RouterLink orderView = new RouterLink("Agregar nuevo producto", Main.class);
        RouterLink ingreso = new RouterLink("Ingresar dinero", Ingresar.class);
        RouterLink crafting = new RouterLink("Craftear", Crafteo.class);
        add(heading,total, disponible, orders, orderView,ingreso, crafting);
        
        
        orders.setColumns("name", "cantidad", "precio");

        
        orders.addComponentColumn(order -> {
            Button dolarBtn = new Button(VaadinIcon.EXCHANGE.create());           
            cambioDivisa(dolarBtn, order, total);
            return dolarBtn;
        });
        
        orders.addComponentColumn(order -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create());
            modificar( editBtn,  order,  total);     
            return editBtn;
        });
        
        
        orders.addComponentColumn(order -> {
            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            eliminar( deleteBtn,  order,  total);
            return deleteBtn;
        });
        
        orders.addComponentColumn(order -> {
            Button plusBtn = new Button(VaadinIcon.PLUS_CIRCLE.create());
            plus( plusBtn,  order,  total);
            return plusBtn;
        });
        
        orders.addComponentColumn(order -> {
            Button minusBtn = new Button(VaadinIcon.MINUS_CIRCLE.create());
            minus( minusBtn,  order,  total);
            return minusBtn;
        });
        

        listOrders();
        
        update.addClickListener(e -> listOrders());
        
    }
    private void plus(Button plusBtn, AlmacenOrder order, TextField total) {
    	 plusBtn.addClickListener(e -> {
         	
         	cambioString = order.getCantidad();
         	cambioInt = Integer.parseInt(cambioString);
         	cambioInt++;
         	cambioString = Integer.toString(cambioInt);
         	order.setCantidad(cambioString);
         	
         	actualizarArray(order,1);
         	//System.out.print(order.getCantidad());
         	repo.save(order);
             listOrders();
             actualizarTotal(total);
         });
    }
    private void minus(Button minusBtn, AlmacenOrder order, TextField total) {
    	minusBtn.addClickListener(e -> {
        	cambioString = order.getCantidad();
        	cambioInt = Integer.parseInt(cambioString);
        	if (cambioInt > 1) {
        		cambioInt--;
            	cambioString = Integer.toString(cambioInt);
            	order.setCantidad(cambioString);
            	repo.save(order);
        	}else{
        		repo.delete(order);
        		borrarArray(order);
                
        	}
        	listOrders();
        	actualizarArray(order,1);
        	actualizarTotal(total);
        });
    }
    
    private void cambioDivisa(Button dolarBtn, AlmacenOrder order, TextField total) {
    	dolarBtn.addClickListener(e -> {
        	cambioString = order.getPrecio();   
        	String verify=cambioString.substring(cambioString.length()-1,cambioString.length());
        	if(verify.equals("€")) {
        		String ncadena=cambioString.substring(0,cambioString.length()-1);
            	cambioDecimal = Float.parseFloat(ncadena);
            	cambioDecimal=cambioDecimal*12/10;
            	BigDecimal roundfinalPrice = new BigDecimal(cambioDecimal.floatValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
                cambioString=roundfinalPrice.toPlainString();
            	order.setPrecio(cambioString+"$");
        	}else if (verify.equals("$")) {
        		String ncadena=cambioString.substring(0,cambioString.length()-1);
            	cambioDecimal = Float.parseFloat(ncadena);
            	cambioDecimal=cambioDecimal/12*10;
            	BigDecimal roundfinalPrice = new BigDecimal(cambioDecimal.floatValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
                cambioString=roundfinalPrice.toPlainString();
            	order.setPrecio(cambioString+"€");
        	}
        	listOrders();
            actualizarArray(order,2);
            actualizarTotal(total);
            repo.save(order); 
    		listOrders();               
        });
		
	}
      
    private void modificar(Button editBtn, AlmacenOrder order, TextField total) {
    	editBtn.addClickListener(e -> {
        	
        	
        	H3 heading2 = new H3("Modificación de Producto");
        	TextField nombre = new TextField("Modificar Producto");
        	TextField cantidad1 = new TextField("Modificar Cantidad");
        	setDefaultHorizontalComponentAlignment(FlexLayout.Alignment.BASELINE);
        	Button guardar = new Button(VaadinIcon.CHECK_CIRCLE.create());
        	Button cancelar = new Button(VaadinIcon.CLOSE_CIRCLE.create());
        	add(heading2, nombre, cantidad1, guardar, cancelar);
        	
        	nombre.clear();
    		cantidad1.clear();
        	nombre.setValue(order.getName());
        	cantidad1.setValue(order.getCantidad());
        	
        	guardar.addClickListener(p -> {
        		String nombre2 = nombre.getValue();
        		String cantidad2 = cantidad1.getValue();
        		order.setName(nombre2);
        		order.setCantidad(cantidad2);
        		cambioString = order.getCantidad();	    		
        		repo.save(order); 
        		listOrders();
        		actualizarArray(order,1);
        		actualizarTotal(total);
        		remove(heading2, nombre, cantidad1, guardar, cancelar);	
        		String msg = String.format("Modificado correctamente."); 
        		Notification.show(msg, 3000, Notification.Position.MIDDLE);
        		
        	});
        	
        	cancelar.addClickListener(p -> {
        	remove(heading2, nombre, cantidad1, guardar, cancelar);	  
        	});        	

        });
    }
    private void eliminar(Button deleteBtn, AlmacenOrder order, TextField total) {
    	deleteBtn.addClickListener(e -> {
        	order.setCantidad("0");
            repo.delete(order);
            listOrders();
            borrarArray(order);
            actualizarArray(order,1);
            actualizarTotal(total);
            
        });
    }
    
    
	private void actualizarArray(AlmacenOrder order, Integer cambio) {
    	for(int indice = 0;indice<datos.size();indice=indice+3)
        {
        	if (datos.get(indice) == order.getName()) {
        		if (cambio == 1){
        			datos.set(indice+1, cambioString);
            		System.out.print(datos);
        		}else if (cambio == 2) {
        			datos.set(indice+2, cambioString);
            		System.out.print(datos);
        		}
        		
        	}
        }	
	}

    private void borrarArray(AlmacenOrder order) {
    	for(int indice = 0;indice<datos.size();indice=indice+3)
        {
        	if (datos.get(indice) == order.getName()) {
        		datos.remove(indice);
        		datos.remove(indice);
        		datos.remove(indice);

        		System.out.print(datos);
        	}
        }
		
	}

	public void actualizarTotal(TextField total) {
    	Integer sumatorio = 0;
    	if (datos.size() == 0) {
    		sumatorio = 0;
    	}else {
    		for(int indice = 0;indice<datos.size();indice=indice+3)
            {
            	
            	cambioString = datos.get(indice+1);
            	cambioInt = Integer.parseInt(cambioString);
            	sumatorio = sumatorio + cambioInt;
           
            }
    	 }
    	
        cambioString = Integer.toString(sumatorio);
    	total.setValue(cambioString);	
      
    }
	public void actualizarDisponible(TextField disponible) {
		cambioString = dispo.get(0);
		disponible.setValue(cambioString);	
      
    }
    
	private Collection<AlmacenOrder> createItems() {
		// TODO Auto-generated method stub
		return null;
	}


	private Collection<AlmacenOrder> getItems() {
		// TODO Auto-generated method stub
		return null;
	}
	public void listOrders() {
        orders.setItems(repo.findAll());
    }
	
    
}
