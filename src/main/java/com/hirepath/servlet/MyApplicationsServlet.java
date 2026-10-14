package com.hirepath.servlet;

import com.hirepath.config.DB;
import com.hirepath.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/my-applications")
public class MyApplicationsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        if(session==null || !(session.getAttribute("user") instanceof User)) { resp.sendRedirect(req.getContextPath()+"/login.jsp"); return; }
        User user=(User)session.getAttribute("user");
        List<Map<String,Object>> apps=new ArrayList<>();
        String sql="SELECT a.id,a.status,a.applied_at,j.title,j.company,j.location FROM applications a JOIN jobs j ON a.job_id=j.id WHERE a.seeker_id=? ORDER BY a.applied_at DESC";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setLong(1,user.id()); try(ResultSet rs=ps.executeQuery()) {
                while(rs.next()){ Map<String,Object> a=new HashMap<>(); for(String k:List.of("id","status","applied_at","title","company","location"))a.put(k,rs.getObject(k)); apps.add(a); }
            }
        } catch(SQLException ex){ throw new ServletException("Could not load applications.",ex); }
        req.setAttribute("applications",apps); req.getRequestDispatcher("/my-applications.jsp").forward(req,resp);
    }
}
