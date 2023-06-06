package tpo.webapp.webapp.Service;

import tpo.webapp.webapp.Models.FreeVisit;
import tpo.webapp.webapp.Models.Patient;
import tpo.webapp.webapp.Models.PatientDto;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;


public class DbService {

    public String getUser(String loginReq){
        //create();
        String res ="";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";

            try(Connection conn = DriverManager.getConnection(connString);
            Statement statement = conn.createStatement()){

                String select = "Select passw from UserInfo where login="+new String("'"+loginReq+"'");
                ResultSet resultSet = statement.executeQuery(select);
                while (resultSet.next()){
                    res = resultSet.getString("passw");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<FreeVisit> getFreeVisits(String specialization){
        ResultSet res = null;
        List<FreeVisit> freeVisits = new ArrayList<>();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";

            try(Connection conn = DriverManager.getConnection(connString);
                Statement statement = conn.createStatement()){

                String select = "SELECT s.Nazwa, g.Data, L.Imie, L.Nazwisko FROM Grafik g\n" +
                        "INNER JOIN Lek_Spec ls ON g.Lek_Spec_ID_Lek_Spec=ls.ID_Lek_Spec\n" +
                        "INNER JOIN Lekarz L on L.ID_lekarz = ls.Lekarz_ID_lekarz\n" +
                        "INNER JOIN S_SPECJALIZACJA s ON ls.SPECJALIZACJA_ID_SPEC=s.ID_SPEC\n" +
                        "WHERE g.ID_Wizyta IS NULL AND s.Nazwa=? AND g.Data>CURRENT_DATE";

                try{
                    PreparedStatement prep = conn.prepareStatement(select);
                    prep.setString(1,specialization);
                    res = prep.executeQuery();
                }catch (SQLException e){

                }
                while (res.next()){
                    String spec = res.getString(1);
                    Date data = res.getDate(2);
                    String name = res.getString(3);
                    String lastName = res.getString(4);
                    freeVisits.add(new FreeVisit(spec,data,name,lastName));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return freeVisits;
    }

    public List<PatientDto> searchPatient(String name, String lastName, String pesel, String phone){
        List<PatientDto> patients = new ArrayList<>();
        String select = "Select p.imie, p.nazwisko, p.pesel, p.nrtel from pacjent p where ";

        if (!name.equals("")){
            select+="p.imie LIKE \'"+name+"\'";
            if (!lastName.equals("")){
                select+=" AND p.nazwisko LIKE \'"+lastName+"\'";
            }
            if (!pesel.equals("")){
                select+=" AND p.pesel LIKE \'"+pesel+"\'";
            }
            if (!phone.equals("")){
                select+=" AND p.nrtel ="+phone;
            }
        }else if(!lastName.equals("")){
            select+="p.nazwisko LIKE \'"+lastName+"\'";
            if (!pesel.equals("")){
                select+=" AND p.pesel LIKE \'"+pesel+"\'";
            }
            if (!phone.equals("")){
                select+=" AND p.nrtel ="+phone;
            }
        }else if(!pesel.equals("")){
            select+="p.pesel LIKE \'"+pesel+"\'";
            if (!phone.equals("")){
                select+=" AND p.nrtel ="+phone;
            }
        }else if (!phone.equals("")){
            select+="p.nrtel ="+phone;
        }else {
            return new ArrayList<>();
        }


        ResultSet res = null;
        int id = 0;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";

            try(Connection conn = DriverManager.getConnection(connString);
                Statement statement = conn.createStatement()){

                res = statement.executeQuery(select);

                while(res.next()){
                    String patientName = res.getString(1);
                    String patientLastName = res.getString(2);
                    String patientPesel = res.getString(3);
                    String patientPhone = res.getString(4);

                    char[] arr = patientPhone.toCharArray();
                    int i = 1;
                    patientPhone="";
                    for (char el : arr){
                        if (i==4 || i==7){
                            patientPhone+="-";
                        }
                        patientPhone+=el;
                        i++;
                    }
                    patients.add(new PatientDto(patientName,patientLastName,patientPesel,patientPhone));
                }


            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void addPatient(PatientDto patientDto){
        ResultSet res = null;
        int id = 0;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";

            try(Connection conn = DriverManager.getConnection(connString);
                Statement statement = conn.createStatement()){

                String select = "Select max(ID_PACJENT) from PACJENT";
                res = statement.executeQuery(select);

                while(res.next()){
                    id = res.getInt(1);
                }
                id+=1;

                String insert = "Insert into PACJENT values (?,?,?,?,?)";

                try{
                    PreparedStatement prep = conn.prepareStatement(insert);
                    prep.setInt(1,id);
                    prep.setString(2,patientDto.getName());
                    prep.setString(3, patientDto.getLastName());
                    prep.setString(4, patientDto.getPesel());
                    prep.setString(5, patientDto.getPhone());
                    prep.execute();
                }catch (SQLException e){

                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patient> getPatientsByStatusAndMonth(int status, String month){
        ResultSet res = null;
        List<Patient> patients = new ArrayList<>();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";
            String[] splittedMonth = month.split("-");

            try(Connection conn = DriverManager.getConnection(connString);
                Statement statement = conn.createStatement()){

                String select = "Select p.Id_Pacjent, p.imie, p.nazwisko, p.pesel, p.nrtel, g.data from PACJENT p Inner Join WIZYTA w ON p.ID_Pacjent = w.Pacjent_ID_Pacjent\n" +
                        "INNER JOIN Grafik G on G.ID_Grafiku = w.Grafik_ID_Grafiku where w.WIZYTASTATUS_ID_STATUSU=? AND MONTH(g.data)=? AND year(g.data)=?";

                try{
                    PreparedStatement prep = conn.prepareStatement(select);
                    prep.setInt(1,status);
                    prep.setInt(2,Integer.parseInt(splittedMonth[1]));
                    prep.setInt(3, Integer.parseInt(splittedMonth[0]));
                    res = prep.executeQuery();
                }catch (SQLException e){

                }

                while (res.next()){
                    int id = res.getInt(1);
                    String name = res.getString(2);
                    String lastName = res.getString(3);
                    String pesel = res.getString(4);
                    String phone = res.getString(5);
                    Date data = res.getDate(6);
                    patients.add(new Patient(id,name,lastName,pesel,phone,data));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        patients.sort(new Comparator<Patient>() {
            @Override
            public int compare(Patient o1, Patient o2) {
                return o1.getLastName().compareTo(o2.getLastName()) == 0 ? o2.getName().compareTo(o1.getName()) :
                        o1.getLastName().compareTo(o2.getLastName());
            }
        });
        return patients;
    }

    public static void create(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String connString = "jdbc:derby:Przychodnia";
            try(Connection conn = DriverManager.getConnection(connString);
                Statement statement = conn.createStatement()) {
/*                String create = "Create table UserInfo( idUser int Primary Key, login varchar(50), passw varchar(100) )";
                statement.executeUpdate(create);
                String insert = "insert into UserInfo values (1,'Adam','31bfff35c49f5f2b43bdcaf7319d7540a2e6139eb8190212d4a5e3b1705f511b')";
                statement.executeUpdate(insert);
                String insert2 = "insert into UserInfo values (2,'Lek','5aa306e346d2cba0784439aa6ac83f30319387d254076ba375f6071db72d4060')";
                statement.executeUpdate(insert2);*/

                String Pacjent = "CREATE TABLE Pacjent (" +
                        "    ID_Pacjent INT PRIMARY KEY," +
                        "    Imie VARCHAR(15) NOT NULL," +
                        "    Nazwisko VARCHAR(25) NOT NULL," +
                        "    Pesel VARCHAR(11) NOT NULL," +
                        "    NrTel INT NOT NULL" +
                        ")";
                String Lekarz = "CREATE TABLE Lekarz (" +
                        "    ID_lekarz INT PRIMARY KEY," +
                        "    Imie VARCHAR(15) NOT NULL," +
                        "    Nazwisko VARCHAR(25) NOT NULL," +
                        "    Pesel VARCHAR(11) NOT NULL," +
                        "    Data_Zatrudnienia DATE NOT NULL," +
                        "    Data_Zwolnienia DATE," +
                        "    Stawka INT NOT NULL" +
                        ")";
                String Spec = "CREATE TABLE S_Specjalizacja (" +
                        "    ID_spec INT PRIMARY KEY," +
                        "    Nazwa VARCHAR(25) NOT NULL" +
                        ")";
                String WizStat = "CREATE TABLE S_WizytaStatus (" +
                        "    ID_statusu INT PRIMARY KEY," +
                        "    Nazwa VARCHAR(15) NOT NULL" +
                        ")";
                String Lek_Spec = "CREATE TABLE Lek_Spec (" +
                        "    ID_Lek_Spec INT PRIMARY KEY," +
                        "    Lekarz_ID_lekarz INT NOT NULL," +
                        "    Specjalizacja_ID_spec INT NOT NULL," +
                        "    CONSTRAINT Lek_Spec_fk1 FOREIGN KEY (Lekarz_ID_lekarz) REFERENCES Lekarz(ID_lekarz)," +
                        "    CONSTRAINT Lek_Spec_fk2 FOREIGN KEY (Specjalizacja_ID_spec) REFERENCES S_Specjalizacja(ID_spec)" +
                        ")";
                String Grafik = "CREATE TABLE Grafik (" +
                        "    ID_Grafiku INT PRIMARY KEY," +
                        "    Lek_Spec_ID_Lek_Spec INT NOT NULL," +
                        "    Data DATE NOT NULL," +
                        "    ID_wizyta INT," +
                        "    CONSTRAINT Grafik_fk FOREIGN KEY (Lek_Spec_ID_Lek_Spec) REFERENCES Lek_Spec(ID_Lek_Spec)" +
                        ")";
                String Cennik = "CREATE TABLE Cennik_uslug (" +
                        "    ID_cennik INT PRIMARY KEY," +
                        "    Nazwa_uslugi VARCHAR(50) NOT NULL," +
                        "    Cena DECIMAL(10,2) NOT NULL," +
                        "    Czy_aktywny SMALLINT NOT NULL," +
                        "    Specjalizacja_ID_spec INT," +
                        "    CONSTRAINT Cennik_uslug_fk FOREIGN KEY (Specjalizacja_ID_spec) REFERENCES S_Specjalizacja(ID_spec)" +
                        ")";
                String Wizyta = "CREATE TABLE Wizyta (" +
                        "    ID_wizyty INT PRIMARY KEY," +
                        "    Pacjent_ID_Pacjent INT NOT NULL," +
                        "    Grafik_ID_Grafiku INT NOT NULL," +
                        "    WizytaStatus_ID_statusu INT NOT NULL," +
                        "    Numer_faktury INT," +
                        "    CONSTRAINT Wizyta_fk1 FOREIGN KEY (Pacjent_ID_Pacjent) REFERENCES Pacjent(ID_Pacjent)," +
                        "    CONSTRAINT Wizyta_fk2 FOREIGN KEY (Grafik_ID_Grafiku) REFERENCES Grafik(ID_Grafiku)," +
                        "    CONSTRAINT Wizyta_fk3 FOREIGN KEY (WizytaStatus_ID_statusu) REFERENCES S_WizytaStatus(ID_statusu)" +
                        ")";
                String Wiz_Usl = "CREATE TABLE Wizyta_usluga (" +
                        "    ID_Wiz_Usl INT PRIMARY KEY," +
                        "    Wizyta_ID_wizyty INT NOT NULL," +
                        "    Cennik_ID_cennik INT NOT NULL," +
                        "    CONSTRAINT Wizyta_usluga_fk1 FOREIGN KEY (Wizyta_ID_wizyty) REFERENCES Wizyta(ID_wizyty)," +
                        "    CONSTRAINT Wizyta_usluga_fk2 FOREIGN KEY (Cennik_ID_cennik) REFERENCES Cennik_uslug(ID_cennik)" +
                        ")";

/*                statement.executeUpdate(Pacjent);
                statement.executeUpdate(Lekarz);
                statement.executeUpdate(Spec);
                statement.executeUpdate(WizStat);
                statement.executeUpdate(Lek_Spec);
                statement.executeUpdate(Grafik);
                statement.executeUpdate(Cennik);
                statement.executeUpdate(Wizyta);
                statement.executeUpdate(Wiz_Usl);*/


                String insertData =
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (1,'Internista');" +
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (2,'Okulista');" +
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (3,'Diabetolog');" +
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (4,'Radiolog');" +
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (5,'Psycholog');" +
                        "INSERT INTO S_Specjalizacja (ID_spec, Nazwa) VALUES (6,'Stomatolog');" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (1,'Anna', 'Abacka','11111111111','2021-01-01',NULL,100);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (2,'Adam', 'Babacki','22222222222','2001-03-12',NULL,100);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (3,'Maria', 'Cabacka','33333333333','2018-05-14',NULL,120);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (4,'Krzysztof', 'Dabacki','44444444444','2019-01-23','2022-05-31',120);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (5,'Adam', 'Fabacki','55555555555','2015-07-17',NULL,150);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (6,'Marek', 'Gabacki','66666666666','2020-09-15',NULL,130);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (7,'Piotr', 'Habacki','77777777777','2021-03-03',NULL,110);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (8,'Ryszard', 'Ibacki','88888888888','2010-11-05',NULL,160);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (9,'Ewa', 'Jabacka','99999999999','2011-02-09',NULL,120);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (10,'Zofia', 'Kabacka','10101010101','2019-01-23',NULL,100);" +
                        "INSERT inTO LEKARZ (ID_Lekarz, imie, nazwisko, pesel, data_zatrudnienia, data_zwolnienia, stawka) VALUES (11,'Katarzyna', 'Labacka','20202020202','2001-03-12','2020-01-01',130);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 1,1,1);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 19,1,2);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 2,2,1);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 15,2,3);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 20,2,4);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 3,3,1);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 21,3,2);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 4,4,4);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 12,4,5);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 5,5,4);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 13,5,5);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 6,6,1);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 14,6,3);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 22,6,6);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 7,7,2);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 16,7,5);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 8,8,1);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 17,8,6);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 9,9,2);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 18,9,5);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 10,10,2);" +
                        "INSERT inTO LEK_SPEC (ID_Lek_Spec, Lekarz_ID_lekarz, Specjalizacja_ID_spec) VALUES ( 11,11,3);" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (1,1,'2022-02-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (2,2,'2022-02-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (3,3,'2022-02-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (4,4,'2020-05-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (5,5,'2022-02-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (6,6,'2022-02-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (7,7,'2022-02-03');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (8,8,'2022-02-03');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (9,9,'2022-02-03');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (10,10,'2022-02-03');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (11,11,'2021-04-04');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (12,12,'2021-05-06');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (13,13,'2022-02-03');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (14,14,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (15,15,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (16,16,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (17,17,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (18,18,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (19,19,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (20,20,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (21,21,'2022-03-02');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (22,22,'2022-03-05');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (23,8,'2022-03-05');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (24,9,'2022-03-05');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (25,10,'2022-03-06');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (26,11,'2015-10-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (27,12,'2020-07-01');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (28,13,'2022-03-06');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (29,14,'2022-03-06');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (30,15,'2022-04-07');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (31,16,'2022-04-07');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (32,17,'2022-04-07');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (33,18,'2022-04-08');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (34,3,'2022-04-08');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (35,4,'2020-06-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (36,5,'2022-05-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (37,15,'2022-05-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (38,16,'2022-05-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (39,17,'2022-05-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (40,18,'2022-05-11');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (41,19,'2022-05-12');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (42,20,'2022-05-12');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (43,21,'2022-05-12');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (44,22,'2022-05-12');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (45,8,'2022-06-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (46,9,'2022-06-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (47,10,'2022-06-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (48,11,'2021-01-01');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (49,7,'2022-06-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (50,8,'2022-08-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (51,9,'2022-08-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (52,10,'2022-08-14');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (53,11,'2020-07-08');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (54,12,'2020-12-12');" +
                        "INSERT INTO GRAFIK (ID_Grafiku , Lek_Spec_ID_Lek_Spec, Data) VALUES (55,13,'2022-08-14');" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (1,'Adrian','Smith','74080849938',111111111);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (2,'Jowita','Michael','52041377711',222222222);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (3,'Joanna','Roger','99050774614',333333333);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (4,'Kacper','Howard','59022051639',444444444);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (5,'Filip','Stivens','00250662218',555555555);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (6,'Adam','Boldmin','54103116962',666666666);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (7,'Zuzanna','Duda','68103047115',777777777);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (8,'Piotr','Zonecki','58062719136',888888888);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (9,'Zbigniew','Pasiak','71060415293',999999999);" +
                        "INSERT INTO PACJENT (ID_Pacjent,Imie,Nazwisko,Pesel,NrTel) VALUES (10,'Jan','Bogowski','50071463628',123123123);" +
                        "INSERT INTO S_WizytaStatus (ID_Statusu, Nazwa) VALUES (1,'ZAPLANOWANA');" +
                        "INSERT INTO S_WizytaStatus (ID_Statusu, Nazwa) VALUES (2,'ZAKONCZONA');" +
                        "INSERT INTO S_WizytaStatus (ID_Statusu, Nazwa) VALUES (3,'ANULOWANA');" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (1,1,1,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (2,2,2,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (3,3,3,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (4,4,4,3,1000);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (5,5,5,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (6,6,6,3,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (7,7,7,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (8,8,8,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (9,9,9,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (10,10,10,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (11,2,11,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (12,3,12,2,1001)" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (13,4,13,3,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (14,7,14,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (15,8,15,3,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (16,9,16,2,1002);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (17,10,17,2,1003);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (18,2,18,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (19,2,19,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (20,3,20,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (21,4,21,3,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (22,5,22,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (23,6,23,2,1004);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (24,7,24,2,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (25,8,25,3,1005);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (26,9,26,3,1006);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (27,10,49,1,1007);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (28,2,50,1,NULL);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (29,2,51,1,1008);" +
                        "INSERT INTO WIZYTA (ID_wizyty,Pacjent_ID_Pacjent,Grafik_ID_Grafiku,WizytaStatus_ID_statusu,Numer_faktury) VALUES (30,5,52,1,NULL);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (1,'Konsultacja',130,1,2);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (2,'Badanie',150,1,2);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (3,'Teleporada',100,1,2);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (4,'Przeswietlenie',300,1,4);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (5,'Wizyta domowa',150,0,1);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (6,'Konsultacja',130,1,3);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (7,'Badanie',150,1,3);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (8,'Teleporada',100,1,3);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (9,'Przeswietlenie',300,1,6);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (10,'Konsultacja',130,1,6);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (11,'Badanie',150,1,6);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (12,'Konsultacja',130,1,5);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (13,'Badanie',150,1,5);" +
                        "INSERT INTO CENNIK_USLUG (ID_cennik,Nazwa_uslugi,Cena,Czy_aktywny,Specjalizacja_ID_spec) VALUES (14,'Teleporada',100,1,5);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (1,1,1);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (2,2,2);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (3,3,3);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (4,4,4);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (5,5,5);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (6,6,6);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (7,7,7);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (8,8,8);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (9,9,9);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (10,10,10);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (11,11,11);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (12,12,12);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (13,13,13);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (14,14,14);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (15,15,6);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (16,16,7);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (17,17,8);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (18,18,9);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (19,19,2);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (20,20,3);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (21,21,4);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (22,22,5);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (23,23,6);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (24,24,7);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (25,25,13);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (26,26,14);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (27,27,6);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (28,28,7);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (29,29,2);" +
                        "INSERT INTO WIZYTA_USLUGA (ID_Wiz_Usl,Wizyta_ID_wizyty,Cennik_ID_cennik) VALUES (30,30,3);" +
                        "UPDATE GRAFIK SET ID_wizyta = 1 WHERE ID_Grafiku = 1;" +
                        "UPDATE GRAFIK SET ID_wizyta = 2 WHERE ID_Grafiku = 2;" +
                        "UPDATE GRAFIK SET ID_wizyta = 3 WHERE ID_Grafiku = 3;" +
                        "UPDATE GRAFIK SET ID_wizyta = 4 WHERE ID_Grafiku = 4;" +
                        "UPDATE GRAFIK SET ID_wizyta = 5 WHERE ID_Grafiku = 5;" +
                        "UPDATE GRAFIK SET ID_wizyta = 6 WHERE ID_Grafiku = 6;" +
                        "UPDATE GRAFIK SET ID_wizyta = 7 WHERE ID_Grafiku = 7;" +
                        "UPDATE GRAFIK SET ID_wizyta = 8 WHERE ID_Grafiku = 8;" +
                        "UPDATE GRAFIK SET ID_wizyta = 9 WHERE ID_Grafiku = 9;" +
                        "UPDATE GRAFIK SET ID_wizyta = 10 WHERE ID_Grafiku = 10;" +
                        "UPDATE GRAFIK SET ID_wizyta = 11 WHERE ID_Grafiku = 11;" +
                        "UPDATE GRAFIK SET ID_wizyta = 12 WHERE ID_Grafiku = 12;" +
                        "UPDATE GRAFIK SET ID_wizyta = 13 WHERE ID_Grafiku = 13;" +
                        "UPDATE GRAFIK SET ID_wizyta = 14 WHERE ID_Grafiku = 14;" +
                        "UPDATE GRAFIK SET ID_wizyta = 15 WHERE ID_Grafiku = 15;" +
                        "UPDATE GRAFIK SET ID_wizyta = 16 WHERE ID_Grafiku = 16;" +
                        "UPDATE GRAFIK SET ID_wizyta = 17 WHERE ID_Grafiku = 17;" +
                        "UPDATE GRAFIK SET ID_wizyta = 18 WHERE ID_Grafiku = 18;" +
                        "UPDATE GRAFIK SET ID_wizyta = 19 WHERE ID_Grafiku = 19;" +
                        "UPDATE GRAFIK SET ID_wizyta = 20 WHERE ID_Grafiku = 20;" +
                        "UPDATE GRAFIK SET ID_wizyta = 21 WHERE ID_Grafiku = 21;" +
                        "UPDATE GRAFIK SET ID_wizyta = 22 WHERE ID_Grafiku = 22;" +
                        "UPDATE GRAFIK SET ID_wizyta = 23 WHERE ID_Grafiku = 23;" +
                        "UPDATE GRAFIK SET ID_wizyta = 24 WHERE ID_Grafiku = 24;" +
                        "UPDATE GRAFIK SET ID_wizyta = 25 WHERE ID_Grafiku = 25;" +
                        "UPDATE GRAFIK SET ID_wizyta = 26 WHERE ID_Grafiku = 26;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 27;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 28;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 29;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 30;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 31;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 32;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 33;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 34;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 35;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 36;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 37;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 38;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 39;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 40;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 41;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 42;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 43;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 44;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 45;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 46;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 47;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 48;" +
                        "UPDATE GRAFIK SET ID_wizyta = 27 WHERE ID_Grafiku = 49;" +
                        "UPDATE GRAFIK SET ID_wizyta = 28 WHERE ID_Grafiku = 50;" +
                        "UPDATE GRAFIK SET ID_wizyta = 29 WHERE ID_Grafiku = 51;" +
                        "UPDATE GRAFIK SET ID_wizyta = 30 WHERE ID_Grafiku = 52;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 53;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 54;" +
                        "UPDATE GRAFIK SET ID_wizyta = NULL WHERE ID_Grafiku = 55;";
                statement.executeUpdate(insertData);

            }catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
