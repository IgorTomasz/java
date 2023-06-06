package tpo.webapp.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bouncycastle.util.encoders.Hex;
import tpo.webapp.webapp.Service.DbService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private String message;
    private DbService _dbService;


    public void init() {
        message = "Zalogowano!";
        this._dbService = new DbService();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String passw = req.getParameter("password");
        String log = req.getParameter("username");

        if (checkHash(passw).equals(_dbService.getUser(log))){
            req.setAttribute("message","<h4>Witaj "+log+"!</h4>");
            req.getRequestDispatcher("LoggedIn.jsp").forward(req,resp);
        }else {
            req.getRequestDispatcher("index.jsp").forward(req,resp);
        }


    }

    public void destroy() {
    }

    public final String checkHash(String passw){
        String res = "";
        try {
            MessageDigest dig = MessageDigest.getInstance("SHA-256");
            byte[] hash = dig.digest(passw.getBytes(StandardCharsets.UTF_8));
            res = new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            return "Error";
        }
        return res;
    }
}
