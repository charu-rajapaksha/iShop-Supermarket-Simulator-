/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iShop;

public class InventoryQuery {
    public static String LOAD_ALL_DATA = "Select * from inventory";
    public static String ADD_DATA = "INSERT INTO inventory (productName, quantity, price) VALUES (?, ?, ?)";
    public static String UPDATE_DATA = "UPDATE inventory SET productName = ?, quantity = ?, price = ? WHERE id = ?";
     public static String DELETE_DATA = "DELETE FROM inventory WHERE id = ?";
    
}
