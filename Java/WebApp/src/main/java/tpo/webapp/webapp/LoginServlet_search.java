package tpo.webapp.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tpo.webapp.webapp.Models.Patient;
import tpo.webapp.webapp.Service.DbService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet_search", value = "/search")
public class LoginServlet_search extends HttpServlet {

    private String message;
    private DbService _dbService;

    public void init() {
        this._dbService=new DbService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String html = "<h3>Wyszukiwanie pacjentów w danym miesiącu na podstawie statusu wizyty</h3>\n" +
                "<form id=\"search_form\" method=\"post\" action=\"search\">\n" +
                "    <h3>Status wizyty: <select name=\"status\">" +
                "<option value=\"Anulowana\">Anulowana</option>" +
                "<option value=\"Zaplanowana\">Zaplanowana</option>" +
                "<option value=\"Zakonczona\">Zakończona</option></h3></select>\n" +
                "    <h3>Miesiąc: <input type=\"month\" name=\"month_i\"></h3><br>\n" +
                "</form>\n"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "<h2>Wyniki wyszukiwania</h2></br></br></br><div class=\"scrollable-list\"></div>";
            request.setAttribute("message",html);
            request.getRequestDispatcher("LoggedIn.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = "<h3>Wyszukiwanie pacjentów w danym miesiącu na podstawie statusu wizyty</h3>\n" +
                "<form id=\"search_form\" method=\"post\" action=\"search\">\n" +
                "<h3>Status wizyty: <select name=\"status\">" +
                "<option value=\"Anulowana\">Anulowana</option> "+
                "<option value=\"Zaplanowana\">Zaplanowana</option>" +
                "<option value=\"Zakonczona\">Zakończona</option></h3></select>\n" +
                "<h3>Miesiąc: <input type=\"month\" name=\"month_i\"></h3><br> \n" +
                "</form>\n" +
                "<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>" +
                "<h2>Wyniki wyszukiwania</h2></br></br></br><div class=\"scrollable-list\">";

        String status = req.getParameter("status");
        String month = req.getParameter("month_i");
        if (month==""){
            html+="</div><h2>Brak miesiąca do wyszukania!</h2>";
            req.setAttribute("message",html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req,resp);
        }else {
            int statusNumber = 0;

            switch (status) {
                case "Anulowana":
                    statusNumber = 3;
                    break;
                case "Zakonczona":
                    statusNumber = 2;
                    break;
                case "Zaplanowana":
                    statusNumber = 1;
                    break;
                default:
                    statusNumber = 2;
            }


            List<Patient> list = _dbService.getPatientsByStatusAndMonth(statusNumber, req.getParameter("month_i"));


            for (Patient el : list) {
                html += "<div class=\"list-item\"><h2>Data wizyty: "+el.getData()+"</h2><h3>" + el.getLastName() + " " + el.getName() + "</h3>\n<h4>Pesel: " + el.getPesel() + "</h4>\n<h4>Numer telefonu: " + el.getPhone() + "</h4></div>";
            }
            html += "</div>";

            req.setAttribute("message", html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req, resp);
        }
    }

    public void destroy() {
    }

}
