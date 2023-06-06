package tpo.webapp.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tpo.webapp.webapp.Models.Patient;
import tpo.webapp.webapp.Models.PatientDto;
import tpo.webapp.webapp.Service.DbService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet_searchPatient", value = "/searchPatient")
public class LoginServlet_searchPatient extends HttpServlet {

    private String message;
    private DbService _dbService;

    public void init() {
        this._dbService=new DbService();
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String html = "<h3>Wyszukiwanie pacjenta</h3>\n" +
                "<div class=\"MainForm\">" +
                "<div class=\"MainFormLeft\">" +
                "<h3>Imie: </br> Nazwisko: </br> Pesel: </br> Numer telefonu:</h3>" +
                "</div>" +
                "<div class=\"MainFormRight\">" +
                "<form id=\"search_form\" method=\"post\" action=\"searchPatient\">\n" +
                "<input type=\"text\" name=\"patient_name\"></br>" +
                "<input type=\"text\" name=\"patient_lastName\"></br>" +
                "<input type=\"text\" name=\"patient_pesel\"></br>" +
                "<input type=\"text\" name=\"patient_phone\"></div>" +
                "</form>\n</br></br></div>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "</br></br></br>";
            request.setAttribute("message",html);
            request.getRequestDispatcher("LoggedIn.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = "<h3>Wyszukiwanie pacjenta</h3>\n" +
                "<div class=\"MainForm\">" +
                "<div class=\"MainFormLeft\">" +
                "<h3>Imie: </br> Nazwisko: </br> Pesel: </br> Numer telefonu:</h3>" +
                "</div>" +
                "<div class=\"MainFormRight\">" +
                "<form id=\"search_form\" method=\"post\" action=\"searchPatient\">\n" +
                "<input type=\"text\" name=\"patient_name\"></br>" +
                "<input type=\"text\" name=\"patient_lastName\"></br>" +
                "<input type=\"text\" name=\"patient_pesel\"></br>" +
                "<input type=\"text\" name=\"patient_phone\"></div>" +
                "</form>\n</br></br></div>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "</br></br></br><div class=\"scrollable-list\">";

        String name = req.getParameter("patient_name");
        String lastName = req.getParameter("patient_lastName");
        String pesel = req.getParameter("patient_pesel");
        String phone = req.getParameter("patient_phone");


            List<PatientDto> list = _dbService.searchPatient(name,lastName,pesel,phone);

            if (list.isEmpty()){
                html+="<h3>Brak wyników lub brak wprowadzonych danych!</h3></div>";
            }else {
                for (PatientDto el : list) {
                    html += "<div class=\"list-item\"><h3>" + el.getLastName() + " " + el.getName() +"</h3>\n<h4>Pesel: " + el.getPesel() + "</h4>\n<h4>Numer telefonu: " + el.getPhone() + "</h4></div>";
                }
                html += "</div>";
            }
            req.setAttribute("message", html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req, resp);

    }

    public void destroy() {
    }

}
