package com.hirepath.servlet;

import com.hirepath.config.DB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/jobs")
public class JobsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Map<String,Object>> jobs = new ArrayList<>();
        String sql = "SELECT j.id,j.title,j.company,j.location,j.job_type,j.description,j.required_skills,j.created_at,u.full_name AS employer FROM jobs j JOIN users u ON j.employer_id=u.id WHERE j.status='OPEN' AND (? IS NULL OR j.title LIKE ? OR j.company LIKE ? OR j.location LIKE ?) ORDER BY j.created_at DESC";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            String term = (q == null || q.isBlank()) ? null : "%" + q.trim() + "%";
            ps.setString(1, term); ps.setString(2, term); ps.setString(3, term); ps.setString(4, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> j = new HashMap<>();
                    for (String k : List.of("id","title","company","location","job_type","description","required_skills","created_at","employer")) j.put(k, rs.getObject(k));
                    jobs.add(j);
                }
            }
        } catch (SQLException ex) { throw new ServletException("Could not load jobs. Have you run database/schema.sql?", ex); }
        req.setAttribute("jobs", jobs); req.getRequestDispatcher("/jobs.jsp").forward(req, resp);
    }
}
