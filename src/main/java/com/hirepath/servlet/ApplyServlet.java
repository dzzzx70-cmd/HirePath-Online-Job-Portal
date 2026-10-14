package com.hirepath.servlet;

import com.hirepath.config.DB;
import com.hirepath.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/apply")
public class ApplyServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !(session.getAttribute("user") instanceof User)) { resp.sendRedirect(req.getContextPath()+"/login.jsp"); return; }
        User user = (User) session.getAttribute("user");
        if (!"SEEKER".equals(user.role())) { resp.sendError(403, "Only job seekers can apply."); return; }
        long jobId;
        try { jobId = Long.parseLong(req.getParameter("jobId")); } catch(Exception e) { resp.sendError(400,"Invalid job."); return; }
        String letter = req.getParameter("coverLetter");
        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement check = c.prepareStatement("SELECT id FROM jobs WHERE id=? AND status='OPEN'")) {
                check.setLong(1, jobId); try(ResultSet rs=check.executeQuery()) { if(!rs.next()){ c.rollback(); resp.sendError(404,"Job not found or closed."); return; } }
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO applications(job_id,seeker_id,cover_letter) VALUES(?,?,?)")) {
                ps.setLong(1,jobId); ps.setLong(2,user.id()); ps.setString(3,letter); ps.executeUpdate();
            }
            c.commit(); resp.sendRedirect(req.getContextPath()+"/my-applications");
        } catch(SQLIntegrityConstraintViolationException ex) { resp.sendError(409,"You have already applied for this job."); }
          catch(SQLException ex) { throw new ServletException("Application failed.",ex); }
    }
}
