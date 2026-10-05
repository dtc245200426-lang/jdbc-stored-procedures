package com.codegym.dao;
import com.codegym.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO implements IUserDAO {
 private static String config(String key,String fallback){String value=System.getenv(key);return value==null?fallback:value;}
 private Connection getConnection() throws SQLException {
  try {Class.forName("com.mysql.cj.jdbc.Driver");}catch(ClassNotFoundException ex){throw new SQLException("MySQL JDBC driver missing",ex);}
  return DriverManager.getConnection(config("DB_URL","jdbc:mysql://localhost:3306/demo?serverTimezone=UTC&characterEncoding=UTF-8"),config("DB_USER","root"),config("DB_PASSWORD",""));
 }
 @Override public User getUserById(int id){
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL get_user_by_id(?)}")){
   s.setInt(1,id);try(ResultSet r=s.executeQuery()){return r.next()?new User(id,r.getString("name"),r.getString("email"),r.getString("country")):null;}
  }catch(SQLException ex){throw new IllegalStateException("Cannot load user",ex);}
 }
 @Override public void insertUserStore(User user) throws SQLException {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL insert_user(?, ?, ?)}")){
   s.setString(1,user.getName());s.setString(2,user.getEmail());s.setString(3,user.getCountry());s.executeUpdate();
  }
 }
 @Override public User selectUser(int id){return getUserById(id);}
 @Override public void insertUser(User user) throws SQLException{insertUserStore(user);}
 @Override public List<User> selectAllUsers(){
  try(Connection c=getConnection();PreparedStatement s=c.prepareStatement("SELECT id,name,email,country FROM users ORDER BY id");ResultSet r=s.executeQuery()){
   List<User> result=new ArrayList<>();while(r.next())result.add(new User(r.getInt("id"),r.getString("name"),r.getString("email"),r.getString("country")));return result;
  }catch(SQLException ex){throw new IllegalStateException("Cannot list users",ex);}
 }
 @Override public boolean deleteUser(int id) throws SQLException{
  try(Connection c=getConnection();PreparedStatement s=c.prepareStatement("DELETE FROM users WHERE id=?")){s.setInt(1,id);return s.executeUpdate()>0;}
 }
 @Override public boolean updateUser(User u) throws SQLException{
  try(Connection c=getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET name=?,email=?,country=? WHERE id=?")){s.setString(1,u.getName());s.setString(2,u.getEmail());s.setString(3,u.getCountry());s.setInt(4,u.getId());return s.executeUpdate()>0;}
 }
}
