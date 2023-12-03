package iShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class InventoryService {
    public InventoryService(){
        
    }
    
    public ArrayList<Inventory> getInventoryData(){
        ArrayList<Inventory> inventoryList = new ArrayList();
        ResultSet resultSet = null;
        try {
            Connection conn = DBConnection.getDBConnection();
            resultSet = conn.createStatement().executeQuery(InventoryQuery.LOAD_ALL_DATA);
            while (resultSet.next()) {
                inventoryList.add(new Inventory(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getDouble(4)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return inventoryList;
    }
    
    public boolean addItem(Inventory inventory){
        PreparedStatement ps = null;
        try{
            ps = DBConnection.getDBConnection().prepareStatement(InventoryQuery.ADD_DATA);
            ps.setString(1, inventory.getProductName());
            ps.setInt(2, inventory.getQuantity());
            ps.setDouble(3, inventory.getPrice());
            
            ps.execute();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        finally{
            try{
                ps.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    public boolean updateItem(Inventory inventory){
        PreparedStatement ps = null;
        try{
            ps = DBConnection.getDBConnection().prepareStatement(InventoryQuery.UPDATE_DATA);
            ps.setString(1, inventory.getProductName());
            ps.setInt(2, inventory.getQuantity());
            ps.setDouble(3, inventory.getPrice());
            ps.setInt(4, inventory.getId());
            
            ps.execute();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        finally{
            try{
                ps.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    public boolean deleteItem(Integer id){
        PreparedStatement ps = null;
        try{
            ps = DBConnection.getDBConnection().prepareStatement(InventoryQuery.DELETE_DATA);
            ps.setInt(1, id);
           
            ps.executeUpdate();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        finally{
            try{
                ps.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }
}
