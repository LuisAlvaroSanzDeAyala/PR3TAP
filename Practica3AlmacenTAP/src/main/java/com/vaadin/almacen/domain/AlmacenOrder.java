package com.vaadin.almacen.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by mstahv
 */
@Entity
public class AlmacenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //@NotEmpty
    //@Email
    //private String email  = "";
    @NotEmpty
    private String name1;
    @NotEmpty
    private String cantidad1;
    private String precio;
    //private String divisa;
  //private String shirtSize;
    public AlmacenOrder() {
    }

    public AlmacenOrder(String name, String cantidad1, String precio) {
        this.name1 = name;
        //this.email = email;
        this.cantidad1 = cantidad1;
        this.precio = precio;
        //this.divisa = divisa;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name1;
    }

    public void setName(String name) {
        this.name1 = name;
    }

    public String getCantidad() {
        return cantidad1;
    }

    public void setCantidad(String cantidad) {
        this.cantidad1 = cantidad;
    }

	public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

	//public String getDivisa() {
    //    return divisa;
    //}

    //public void setDivisa(String divisa) {
    //    this.divisa = divisa;
    //}
}
