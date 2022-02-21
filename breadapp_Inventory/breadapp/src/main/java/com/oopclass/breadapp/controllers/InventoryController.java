package com.oopclass.breadapp.controllers;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Inventory;
import com.oopclass.breadapp.services.impl.InventoryService;
import com.oopclass.breadapp.views.FxmlView;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import java.awt.event.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class InventoryController implements Initializable{

    @FXML
    private Label productId;
    
    @FXML
    private TextField productName;
    
    @FXML
    private TextField stockLocation;
    
    @FXML
    private TextField quantity;
       
    @FXML
    private Button delete;
    
    @FXML
    private Button add;
    
    @FXML
    private Button back;
    
    @FXML
    private Button close;
    
    @FXML
    private Button reset;
    
    @FXML
    private TableView<Inventory> inventoryTable;
    
    @FXML
    private TableColumn<Inventory, Long> colProductId;
    
    @FXML
    private TableColumn<Inventory, String> colProductName;
    
    @FXML
    private TableColumn<Inventory, String> colLocation;
    
    @FXML
    private TableColumn<Inventory, String> colQuantity;
    
    @FXML
    private TableColumn<Inventory, Boolean> colEdit;
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private InventoryService inventoryService;
    
    private ObservableList<Inventory> productList = FXCollections.observableArrayList();
    
    @FXML
    private void backToUser(ActionEvent event){
        stageManager.switchScene(FxmlView.USER);
    }
    
    @FXML
    private void closeApp(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Closing Application");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close this application?");
        Optional<ButtonType> action = alert.showAndWait();
        
        if (action.get() == ButtonType.OK){
            Platform.exit();
        }
    }
    
    @FXML
    void reset(){
        clearFields();
    }
    
    @FXML
    private void addProduct(ActionEvent event) {

        if (validate("Product Name", getProductName(), "([a-zA-Z]{1,30}\\s*)+")
                && validate("Stock Location", getStockLocation(), "([a-zA-Z]{1,30}\\s*)+")
                && emptyValidation("Product Name", productName.getText().isEmpty())
                && emptyValidation("Stock Location", stockLocation.getText().isEmpty())) {

            if ("00".equals(productId.getText())|| "".equals(productId.getText())) {
                if (true) {

                    Inventory product = new Inventory();
                    product.setProductName(getProductName());
                    product.setStockLocation(getStockLocation());
                    product.setQuantity(getQuantity());

                    Inventory newProduct = inventoryService.save(product);
                    saveAlert(newProduct);
                }

            } else {
                Inventory product = inventoryService.find(Long.parseLong(productId.getText()));
                product.setProductName(getProductName());
                product.setStockLocation(getStockLocation());
                product.setQuantity(getQuantity());
                
                Inventory updatedProduct = inventoryService.update(product);
                updateAlert(updatedProduct);
            }

            clearFields();
            loadInventoryTable();
        }

    }
    
    @FXML
    private void deleteProduct(ActionEvent event) {
        List<Inventory> product = inventoryTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            inventoryService.deleteInBatch(product);
        }

        loadInventoryTable();
    }
    
    private void setColumnProperties() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("stockLocation"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Inventory, Boolean>, TableCell<Inventory, Boolean>> cellFactory
            = new Callback<TableColumn<Inventory, Boolean>, TableCell<Inventory, Boolean>>() {
        @Override
        public TableCell<Inventory, Boolean> call(final TableColumn<Inventory, Boolean> param) {
            final TableCell<Inventory, Boolean> cell = new TableCell<Inventory, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit2.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Inventory inventory = getTableView().getItems().get(getIndex());
                            updateProduct(inventory);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateProduct(Inventory inventory) {
                    productId.setText(Long.toString(inventory.getId()));
                    productName.setText(inventory.getProductName());
                    stockLocation.setText(inventory.getStockLocation());
                    quantity.setText(inventory.getQuantity());                   
                }
            };
            return cell;
        }
    };
      
    private void loadInventoryTable(){
        productList.clear();
        productList.addAll(inventoryService.findAll());
        inventoryTable.setItems(productList);
    }
    
    private void clearFields(){
        productId.setText("00");
        productName.clear();
        stockLocation.clear();
        quantity.clear();
    }
    
    public String getProductName(){
        return productName.getText();
    }
    
    public String getStockLocation(){
        return stockLocation.getText();
    }
    
    public String getQuantity(){
        return quantity.getText();
    }
    
    private void saveAlert(Inventory product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product add successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The product " + product.getProductName() + " has been added and \n the id is " + product.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Inventory product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The product " + product.getProductName() +" has been updated.");
        alert.showAndWait();
    }
    
    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        loadInventoryTable();
        setColumnProperties();
        
    } 
}
