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
public class Crafteo extends VerticalLayout implements GlobalConstants{
	AlmacenService service;
	String importe = "0";
     H3 heading2 = new H3("Ingreso de dinero");
     RouterLink listOrders = new RouterLink("Ver listado", ListadoAlmacen.class);
     String acumulado;
     Integer cambioInt;
     Integer cambioInt2;
     String cambioString;
     String cambioString2;
     String seleccionado;   
     
     final Grid<AlmacenOrder> orders = new Grid<>(AlmacenOrder.class);
     Binder<AlmacenOrder> binder = new BeanValidationBinder<>(AlmacenOrder.class); 
     
	ComboBox<String> nuevoProducto = new ComboBox<String>("Productos");

	 public Crafteo(AlmacenService service) {
	        this.service = service;
	        listaCraft.clear();
			 //ingreso.setValue(importe);
	        //System.out.print(service.productosFabricados.length);
	        //System.out.print(service.productosFabricados[0][0]);
	        //System.out.print(service.productosFabricados[1][0]);
	        for(int indice = 0;indice<service.productosFabricados.length;indice++) {
	        	//System.out.print(indice);
	        	listaCraft.add(service.productosFabricados[indice][0]);
            };
            nuevoProducto.setItems(listaCraft);

	        setDefaultHorizontalComponentAlignment(FlexLayout.Alignment.CENTER);
	        RouterLink listOrders = new RouterLink("Ir a la lista de productos básicos", ListadoAlmacen.class);
	        RouterLink main = new RouterLink("Agregar nuevo producto", Main.class);
	        Button order = new Button("Aceptar");
	        add(heading2,nuevoProducto, listOrders,main, order);
	        order.addClickListener(e -> {
	        	crafter(order);
		
	        }); 
	 }
	    private void crafter(Button order) {
	    	order.setEnabled(false);
	    	TextField aux = new TextField("Producto necesario número ");
	    	add(aux);
	    	remove(aux);
	    	
	    	
        	//add(guardar, cancelar);
      	
        	
	    	seleccionado = nuevoProducto.getValue();

	    	for(int indice = 0;indice<service.productosFabricados.length;indice++) {
	    		List<String> craftList = new ArrayList<String>();
	    		List<String> volcado = new ArrayList<String>();
	    		List<String> craftListAux = new ArrayList<String>();

	        	if(seleccionado == service.productosFabricados[indice][0]) {
	        		H3 heading3 = new H3("Necesitas el siguiente listado de materiales");
	        		add(heading3);

	        		for(int m = 1;m<service.productosFabricados[indice].length;m++) {

	        			aux = new TextField("Producto necesario número "+m);
	        			
	        			craftList.add(service.productosFabricados[indice][m]);
	        			aux.setEnabled(false);
	        			aux.setValue(service.productosFabricados[indice][m]);
	        			add(aux);
	        		};

	        		for (int i = 0 ; i<craftList.size();i++){
	        			craftListAux.add(craftList.get(i)) ;
	        			System.out.print("craftListAux");
	        			System.out.print(craftListAux);
	        		}
	        		for (int i = 0 ; i<datos.size();i++){
	        			volcado.add(datos.get(i)) ;
	        			System.out.print("volcado");
	        			System.out.print(volcado);
	        		}
	        		for(int a = 0;a<volcado.size();a=a+3) {
	        			for(int b = 0;b<craftListAux.size();b++) {
	        				if(volcado.get(a).equals(craftListAux.get(b))) {
	        					cambioString = volcado.get(a+1);
	        					cambioInt = Integer.parseInt(cambioString);
	        		         	if (cambioInt >1) {
	        		         		cambioInt--;
	        		         		cambioString = Integer.toString(cambioInt);
		        		         	volcado.set(a+1, cambioString);
		        		         	craftListAux.remove(b);
	        		         	}else if (cambioInt == 1){
	        		         		b--;
	        		         		volcado.remove(a);
	        		         		volcado.remove(a);
	        		         		volcado.remove(a);
	        		         		craftListAux.remove(b);
	        		         	}   
	        		         	
	        				}       				
	        			}
	        		};
	        		if (craftListAux.size()==0) {
	        			H3 heading4 = new H3("Enhorabuena, tienes materiales suficientes! para crear este objeto");
	        			H3 heading5 = new H3("¿Quieres fabricarlo?, se eliminarán los materiales de coste");
	        			Button guardar = new Button(VaadinIcon.CHECK_CIRCLE.create());
	                	RouterLink cancelar = new RouterLink("Cancelar Operación", Main.class);
    	        		add(heading4,heading5,guardar,cancelar);
    	        		
    	        		
    	        		guardar.addClickListener(e -> {
    	        			String msg = String.format("Objeto fabricado correctamente.");
    	        			 Notification.show(msg, 3000, Notification.Position.MIDDLE);
    	        			 for(int a = 0;a<datos.size();a=a+3) {
    		        			for(int b = 0;b<craftList.size();b++) {
    		        				if(datos.get(a).equals(craftList.get(b))) {
    		        					cambioString = datos.get(a+1);
    		        					cambioInt = Integer.parseInt(cambioString);
    		        		         	if (cambioInt >1) {
    		        		         		cambioInt--;
    		        		         		cambioString = Integer.toString(cambioInt);
    		        		         		datos.set(a+1, cambioString);
    		        		         		craftList.remove(b);
    		        		         	}else if (cambioInt == 1){
    		        		         		b--;
    		        		         		datos.remove(a);
    		        		         		datos.remove(a);
    		        		         		datos.remove(a);
    		        		         		craftList.remove(b);
    		        		         	}   
    		        		         	
    		        				}       				
    		        			}
    		        		};
    	        	
    	                }); 

        			}else {
        				H3 heading4 = new H3("No tienes materiales suficientes!");
    	        		add(heading4);
        			}
	        		
	        		
	            	//
	            	//TextField cantidad1 = new TextField("Modificar Cantidad");
	        	}
	        	volcado.clear();
	        	craftListAux.clear();
            };
            remove(aux);
	    }
}