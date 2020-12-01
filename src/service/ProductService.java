/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author theFrozenAdam
 */
public class ProductService {

    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url = "jdbc:sqlserver://SE140022:1433; databaseName=Lab; " + " user=sa; password=123456";

    public Connection openConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }

    public Vector<Product> getProduct(String CategoryID) throws Exception {
        Vector<Product> list = new Vector();
        String sql = "Select ProductID, ProductName, Price, Quantity from ProductTable where CategoryID = ?";
        try (Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(sql)) {
            preStm.setString(1, CategoryID);
            ResultSet rs = preStm.executeQuery();
            while (rs.next()) {
                String code = rs.getString("ProductID");
                String name = rs.getString("ProductName");
                String price = rs.getString("Price");
                int quantity = rs.getInt("Quantity");
                Product emp = new Product(code, name, price, quantity);
                list.add(emp);
            }
        }
        return list;
    }

    public Product getProductByCode(String code) throws Exception {
        String query = "Select * From ProductTable Where ProductID = ?";
        try (Connection c = openConnection();
                PreparedStatement pst = c.prepareStatement(query)) {
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Product(rs.getString("ProductID"), rs.getString("ProductName"), rs.getString("Price"), rs.getInt("Quantity"));
            }
        }
        return null;
    }

    public int insertProduct(Product add) throws Exception {
        String query = "Insert into ProductTable values (?, ?, ?, ?, ?)";
        try (Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)) {
            preStm.setString(1, add.getProductID());
            preStm.setString(2, add.getProductName());
            preStm.setString(3, add.getPrice());
            preStm.setInt(4, add.getQuantity());
            preStm.setString(5, add.getCategoryID());
            return preStm.executeUpdate();
        }
    }

    public int updateProduct(Product pro) throws Exception {
        String query = "Update ProductTable Set ProductName = ?, Price = ?, Quantity =  ? Where ProductID = ?";
        try (Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)) {
            preStm.setString(1, pro.getProductName());
            preStm.setString(2, pro.getPrice());
            preStm.setInt(3, pro.getQuantity());
            preStm.setString(4, pro.getProductID());
            return preStm.executeUpdate();
        }
    }
    
    public int deleteProduct(String id) throws Exception{
        String query = "Delete ProductTable where ProductID = ?";
        try(Connection c = openConnection();
                PreparedStatement preStm = c.prepareStatement(query)){
            preStm.setString(1, id);
            return preStm.executeUpdate();
        }
    }
}
