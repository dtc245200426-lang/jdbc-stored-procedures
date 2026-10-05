package com.codegym.controller;
import com.codegym.dao.*;
import com.codegym.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet("/users")
public class UserServlet extends HttpServlet {
 private final IUserDAO userDAO=new UserDAO();
 @Override protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
  try{
   String action=req.getParameter("action");
   if("new".equals(action)){req.getRequestDispatcher("/WEB-INF/user/form.jsp").forward(req,res);}
   else if("edit".equals(action)){User user=userDAO.getUserById(id(req));if(user==null){res.sendError(404);return;}req.setAttribute("user",user);req.getRequestDispatcher("/WEB-INF/user/form.jsp").forward(req,res);}
   else {req.setAttribute("users",userDAO.selectAllUsersStore());req.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(req,res);}
  }catch(IllegalArgumentException ex){res.sendError(400,"Invalid user ID");}catch(SQLException|IllegalStateException ex){log("Database error",ex);res.sendError(500,"Database unavailable. Check connection and sql/setup.sql.");}
 }
 @Override protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
  req.setCharacterEncoding("UTF-8");
  try{
   String action=req.getParameter("action");
   if("delete".equals(action)){if(!userDAO.deleteUserStore(id(req))){res.sendError(404);return;}}
   else {
    String name=field(req,"name"),email=field(req,"email"),country=field(req,"country");
    if(!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))throw new IllegalArgumentException();
    if("update".equals(action)){if(!userDAO.updateUserStore(new User(id(req),name,email,country))){res.sendError(404);return;}}
    else if("insert".equals(action)){userDAO.insertUserStore(new User(name,email,country));}
    else throw new IllegalArgumentException();
   }
   res.sendRedirect(req.getContextPath()+"/users");
  }catch(IllegalArgumentException ex){res.sendError(400,"Enter valid fields (1-50 characters) and email.");}catch(SQLException|IllegalStateException ex){log("Database error",ex);res.sendError(500,"Database operation failed.");}
 }
 private int id(HttpServletRequest req){int value=Integer.parseInt(req.getParameter("id"));if(value<=0)throw new IllegalArgumentException();return value;}
 private String field(HttpServletRequest req,String name){String value=req.getParameter(name);if(value==null||value.isBlank()||value.trim().length()>50)throw new IllegalArgumentException();return value.trim();}
}
