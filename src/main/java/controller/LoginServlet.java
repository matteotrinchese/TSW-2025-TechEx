package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utility;
import model.dao.UserDAO;
import model.dto.UserDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        userDAO = new UserDAO(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDTO user = userDAO.findByEmail(email);
            if(user == null || !Utility.checkPassword(password, user.getPasswordHash())){
                request.setAttribute("errorMessage", "Email o Password non validi.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect("home.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
