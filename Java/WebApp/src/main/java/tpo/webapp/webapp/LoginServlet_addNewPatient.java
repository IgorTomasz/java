package tpo.webapp.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tpo.webapp.webapp.Models.FreeVisit;
import tpo.webapp.webapp.Models.PatientDto;
import tpo.webapp.webapp.Service.DbService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet_visits", value = "/addPatient")
public class LoginServlet_addNewPatient extends HttpServlet {

    private String message;
    private DbService _dbService;


    public void init() {
        this._dbService = new DbService();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String html = "<h3>Dodawanie nowego pacjenta</h3>\n" +
                "<div class=\"MainForm\">" +
                "<div class=\"MainFormLeft\">" +
                "<h3>Imie: </br> Nazwisko: </br> Pesel: </br> Numer telefonu:</h3>" +
                "</div>" +
                "<div class=\"MainFormRight\">" +
                "<form id=\"search_form\" method=\"post\" action=\"addPatient\">\n" +
                "<input type=\"text\" name=\"patient_name\"></br>" +
                "<input type=\"text\" name=\"patient_lastName\"></br>" +
                "<input type=\"text\" name=\"patient_pesel\"></br>" +
                "<input type=\"range\" id=\"inp\"min=\"0\" max=\"999999999\" step=\"1\"name=\"patient_phone\"></br><p><output id=\"value\"></output></p></div>" +
                "</form>\n</br></br></div>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "</br></br></br>";
        request.setAttribute("message",html);
        request.getRequestDispatcher("LoggedIn.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = "<h3>Dodawanie nowego pacjenta</h3>\n" +
                "<div class=\"MainForm\">" +
                "<div class=\"MainFormLeft\">" +
                "<h3>Imie: </br> Nazwisko: </br> Pesel: </br> Numer telefonu:</h3>" +
                "</div>" +
                "<div class=\"MainFormRight\">" +
                "<form id=\"search_form\" method=\"post\" action=\"addPatient\">\n" +
                "<input type=\"text\" name=\"patient_name\"></br>" +
                "<input type=\"text\" name=\"patient_lastName\"></br>" +
                "<input type=\"text\" name=\"patient_pesel\"></br>" +
                "<input type=\"range\" id=\"inp\"min=\"0\" max=\"999999999\" step=\"1\"name=\"patient_phone\"></br><p><output id=\"value\"></output></p></div>" +
                "</form>\n</br></br></div>"+"<button type=\"submit\" class=\"subbut\" form=\"search_form\">Apply</button>\n" +
                "</br></br></br>";

        String name = req.getParameter("patient_name");
        String lastName = req.getParameter("patient_lastName");
        String pesel = req.getParameter("patient_pesel");
        String phone = req.getParameter("patient_phone");

        if (name=="" || lastName=="" || pesel=="" || phone==""){
            html+="<h3>Nie uzupełniono wszystkich danych!</h3>";
            req.setAttribute("message", html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req, resp);
        }else {
            _dbService.addPatient(new PatientDto(name,lastName,pesel,phone));

            char[] arr = phone.toCharArray();
            phone="";
            int i = 1;
            for(char el : arr){
                if (i == 4 || i == 7){
                    phone+="-";
                }
                phone+=el;
                i++;
            }

            html+="<h3>Pacjent został dodany:</h3><div class=\"list-item\"><h3>"+lastName+" "+name+"</h3><h4>Pesel: "+pesel+"</h4><h4>Numer telefonu: "+phone+"</h4></div>";

            req.setAttribute("message", html);
            req.getRequestDispatcher("LoggedIn.jsp").forward(req, resp);
        }


    }

    public void destroy() {
    }

}
