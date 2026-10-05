package com.codegym.dao;
import com.codegym.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO implements IUserDAO {
 private static String config(String key,String fallback){String value=System.getenv(key);return value==null?fallback:value;}
 protected Connection getConnection() throws SQLException {
  try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(ClassNotFoundException ex){throw new SQLException("MySQL JDBC driver missing",ex);}
  return DriverManager.getConnection(config("DB_URL","jdbc:mysql://localhost:3306/demo?serverTimezone=UTC&characterEncoding=UTF-8"),config("DB_USER","root"),config("DB_PASSWORD",""));
 }
 @Override public List<User> selectAllUsersStore() throws SQLException {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL select_all_users()}")) {
   try(ResultSet r=s.executeQuery()) {
    List<User> users=new ArrayList<>();
    while(r.next()) users.add(new User(r.getInt("id"),r.getString("name"),r.getString("email"),r.getString("country")));
    return users;
   }
  }
 }
 @Override public boolean updateUserStore(User user) throws SQLException {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL update_user(?, ?, ?, ?, ?)}")) {
   s.setInt(1,user.getId());s.setString(2,user.getName());s.setString(3,user.getEmail());s.setString(4,user.getCountry());
   s.registerOutParameter(5,Types.INTEGER);s.execute();return s.getInt(5)>0;
  }
 }
 @Override public boolean deleteUserStore(int id) throws SQLException {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL delete_user(?, ?)}")) {
   s.setInt(1,id);s.registerOutParameter(2,Types.INTEGER);s.execute();return s.getInt(2)>0;
  }
 }
 @Override public User getUserById(int id) {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL get_user_by_id(?)}")) {
   s.setInt(1,id);try(ResultSet r=s.executeQuery()){return r.next()?new User(id,r.getString("name"),r.getString("email"),r.getString("country")):null;}
  }catch(SQLException ex){throw new IllegalStateException("Cannot load user",ex);}
 }
 @Override public void insertUserStore(User user) throws SQLException {
  try(Connection c=getConnection();CallableStatement s=c.prepareCall("{CALL insert_user(?, ?, ?)}")) {
   s.setString(1,user.getName());s.setString(2,user.getEmail());s.setString(3,user.getCountry());s.executeUpdate();
  }
 }
 @Override public User selectUser(int id){return getUserById(id);}
 @Override public void insertUser(User user) throws SQLException{insertUserStore(user);}
 @Override public List<User> selectAllUsers(){try{return selectAllUsersStore();}catch(SQLException ex){throw new IllegalStateException("Cannot list users",ex);}}
 @Override public boolean deleteUser(int id) throws SQLException{return deleteUserStore(id);}
 @Override public boolean updateUser(User user) throws SQLException{return updateUserStore(user);}
}