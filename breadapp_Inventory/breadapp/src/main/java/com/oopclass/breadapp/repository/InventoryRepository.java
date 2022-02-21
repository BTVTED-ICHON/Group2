package com.oopclass.breadapp.repository;

import com.oopclass.breadapp.models.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    
}
