import java.util.ArrayList;
import java.util.List;

public class Pracownicy {




    public static List<String> listaKopaczy = new ArrayList<>(); // tworzenie listy przechowujacej pesele

    public boolean czyPeselUnikalny(String pesel){

        boolean Unikat = listaKopaczy.stream().anyMatch(str-> str.equals(pesel));

        /*boolean Unikat = true;
        for (String peselCheck : listaKopaczy) { // przechodzenie po elementach listy i sprawdzenie poprawnosci peselu
            if (peselCheck.equals(pesel)) {
                Unikat = false;
                os=null;
            }
        }*/
        return !Unikat;
    }

    public void deletePerson(Osoba os){
        os=null;
    }
}
