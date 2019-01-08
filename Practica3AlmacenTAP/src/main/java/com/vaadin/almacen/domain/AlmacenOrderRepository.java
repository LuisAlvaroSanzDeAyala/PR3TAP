
package com.vaadin.almacen.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface AlmacenOrderRepository extends JpaRepository<AlmacenOrder, Long> {
    
}
