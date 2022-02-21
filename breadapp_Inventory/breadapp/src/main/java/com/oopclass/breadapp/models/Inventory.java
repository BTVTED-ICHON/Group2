package com.oopclass.breadapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    
    private long id;
	
    private String productName;
	
    private String stockLocation;
		
    private String quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductName(){
        return productName;
    }
    
    public void setProductName(String pructName){
        this.productName = pructName;
    }
    
    public String getStockLocation(){
        return stockLocation;
    }
    
    public void setStockLocation(String stockLocation){
        this.stockLocation = stockLocation;
    }
    
    public String getQuantity(){
        return quantity;
    }
    
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    
    @Override
	public String toString() {
            return "Product [id=" + id + ", productName=" + productName + ", stockLocation=" + stockLocation + ", quantity = "+ quantity +"]";
	}
}
