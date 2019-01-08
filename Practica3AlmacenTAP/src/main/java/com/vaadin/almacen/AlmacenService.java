package com.vaadin.almacen;

import com.vaadin.almacen.domain.AlmacenOrder;
import com.vaadin.almacen.domain.AlmacenOrderRepository;
import com.vaadin.flow.component.grid.Grid;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator.OrderSourceProvider;

/**
 * A service class for the UI to access backend services.
 */
@Service
class AlmacenService {
    
    @Autowired
    private AlmacenOrderRepository repository;

    List<String> getName() {
        return Arrays.asList("Palo", "Tabla de madera", "Tornillos", "Destornillador");
    }

    String productosFabricados[][] = 
        {
          {"silla", "Palo","Palo","Palo","Palo", "Tabla de madera","Tabla de madera", "Tornillos","Tornillos","Tornillos","Tornillos" },
          {"taburete", "Palo","Tabla de madera","Tornillos"}
        };

    
    List<String> getCantidad() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
    }
    //List<Integer> getCantidad() {
    //    return Arrays.asList(1,2,3,4,5,6,7,8,9);
    //}
    public void placeOrder(AlmacenOrder order) throws IllegalArgumentException {
        repository.save(order);
    }

}
