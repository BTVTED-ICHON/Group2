package com.oopclass.breadapp.services.impl;

import com.oopclass.breadapp.models.Inventory;
import com.oopclass.breadapp.repository.InventoryRepository;
import com.oopclass.breadapp.services.IInventoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService implements IInventoryService{

    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Override
    public Inventory save(Inventory entity) {
        return inventoryRepository.save(entity);
    }

    @Override
    public Inventory update(Inventory entity) {
        return inventoryRepository.save(entity);
    }

    @Override
    public void delete(Inventory entity) {
       inventoryRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Inventory> product) {
        inventoryRepository.deleteInBatch(product);
    }

    @Override
    public Inventory find(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
    
}
