package com.hirepath.servlet;

import com.hirepath.config.DB;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = clean(req.getParameter("fullName"));
        String email = clean(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");
        String role = clean(req.getParameter("role"));
        if (name.isBlank() || email.isBlank() || password == null || password.length() < 8 ||
            !(role.equals("SEEKER") || role.equals("EMPLOYER"))) {
            resp.sendError(400, "Enter a name, valid email, password of at least 8 characters, and valid role."); return;
        }
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users(full_name,email,password_hash,role) VALUES(?,?,?,?)")) {
            ps.setString(1, name); ps.setString(2, email); ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt(12))); ps.setString(4, role);
            ps.executeUpdate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");
        } catch (SQLIntegrityConstraintViolationException ex) {
            resp.sendError(409, "That email is already registered.");
        } catch (SQLException ex) { throw new ServletException("Could not register user. Check database configuration.", ex); }
    }
    private String clean(String s) { return s == null ? "" : s.trim(); }
}
