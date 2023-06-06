package tpo.webapp.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tpo.webapp.webapp.Models.FreeVisit;
import tpo.webapp.webapp.Service.DbService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet_visit", value = "/visit")
public class LoginServlet_visit extends HttpServlet {

    private String message;
    private DbService _dbService;


    public void init() {
        this._dbService = new DbService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String html = "<h3>Wyszukiwanie wolnych terminów u specjalizacji</h3>\n" +
                "<form id=\"search_form\" method=\"post\" action=\"visit\">\n" +
                "<h3>Status wizyty: <select name=\"status\">" +
                "<option value=\"Internista\">Internista</option>" +
                "<option value=\"Okulista\">Okulista</option>" +
                "<option value=\"Diabetolog\">Diabetolog</option>" +
                "<option value=\"Radiolog\">Radiolog</option>" +
                "<option value=\"Psycholog\">Psycholog</option>" +
                "<option value=\"Stomatolog\">Stomatolog</option></h3></select>\n" +
                "</form>\n</br></br>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "<h2>Wyniki wyszukiwania</h2></br></br></br><div class=\"scrollable-list\">";
        request.setAttribute("message",html);
        request.getRequestDispatcher("LoggedIn.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = "<h3>Wyszukiwanie wolnych terminów u specjalizacji</h3>\n" +
                "<form id=\"search_form\" method=\"post\" action=\"visit\">\n" +
                "<h3>Status wizyty: <select name=\"status\">" +
                "<option value=\"Internista\">Internista</option>" +
                "<option value=\"Okulista\">Okulista</option>" +
                "<option value=\"Diabetolog\">Diabetolog</option>" +
                "<option value=\"Radiolog\">Radiolog</option>" +
                "<option value=\"Psycholog\">Psycholog</option>" +
                "<option value=\"Stomatolog\">Stomatolog</option></h3></select>\n" +
                "</form>\n</br></br>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "<h2>Wyniki wyszukiwania</h2></br></br></br><div class=\"scrollable-list\">";

        String status = req.getParameter("status");

            List<FreeVisit> list = _dbService.getFreeVisits(status);


            for (FreeVisit el : list) {
                html += "<div class=\"list-item\"><h3>" + el.getSpecialization() +"</h3>\n<h4>"+el.getLastName()+ " " + el.getName() + "</h4>\n<h4>Data: " + el.getData() + "</h4></div>";
            }
            html += "</div>";

            req.setAttribute("message", html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req, resp);
    }

    public void destroy() {
    }

}
