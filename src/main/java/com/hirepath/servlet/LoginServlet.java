package com.hirepath.servlet;

import com.hirepath.config.DB;
import com.hirepath.model.User;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
        String password = req.getParameter("password");
        String sql = "SELECT id,full_name,email,password_hash,role,skills FROM users WHERE email=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && password != null && BCrypt.checkpw(password, rs.getString("password_hash"))) {
                    HttpSession old = req.getSession(false); if (old != null) old.invalidate();
                    HttpSession session = req.getSession(true);
                    session.setAttribute("user", new User(rs.getLong("id"), rs.getString("full_name"), rs.getString("email"), rs.getString("role"), rs.getString("skills")));
                    session.setMaxInactiveInterval(30 * 60);
                    resp.sendRedirect(req.getContextPath() + "/jobs"); return;
                }
            }
            resp.sendError(401, "Invalid email or password.");
        } catch (SQLException ex) { throw new ServletException("Login failed. Check database configuration.", ex); }
    }
}
