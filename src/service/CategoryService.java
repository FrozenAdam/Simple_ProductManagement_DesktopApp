/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author theFrozenAdam
 */
public class CategoryService {

    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url = "jdbc:sqlserver://SE140022:1433; databaseName=Lab; " + " user=sa; password=123456";

    public Connection openConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }
    
    public ArrayList<Category> getCategories() throws Exception{
        String sql = "Select CategoryID, CategoryName From CategoryTable";
        ArrayList<Category> list = new ArrayList();
        try(Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(sql)){
            ResultSet rs = preStm.executeQuery();
            while(rs.next()){
                String code = rs.getString("CategoryID");
                String name = rs.getString("CategoryName");
                Category a = new Category(code, name);
                list.add(a);
            }
        }
        return list;
    }
    
    public boolean insertCategory(Category category) throws Exception {
        String query = "Insert into CategoryTable Values(?, ?)";
        try (Connection c = openConnection();
                PreparedStatement pst = c.prepareStatement(query)) {
            pst.setString(1, category.getId());
            pst.setString(2, category.getName());
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean updateCategory(String CategoryID, String CategoryName) throws Exception{
        String query = "Update CategoryTable set CategoryName = ? where CategoryID = ?";
        try(Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)){
            preStm.setString(1, CategoryName);
            preStm.setString(2, CategoryID);
            return preStm.executeUpdate() > 0;
        }
    }
    
    public boolean deleteCategory(String CategoryID) throws Exception{
        String query = "Delete CategoryTable where CategoryID = ?";
        try(Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)){
            preStm.setString(1, CategoryID);
            return preStm.executeUpdate() > 0;
        }
    }
    
    public boolean checkProduct(String categoryID) throws Exception{
        String query = "Select * from ProductTable where CategoryID = ?";
        try(Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)){
            preStm.setString(1, categoryID);
            ResultSet rs = preStm.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }
    }
}
