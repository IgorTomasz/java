import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Kopacz k1 = new Kopacz("Jan", "Kowalski", "11111111111", 123123123, 95);
        Kopacz k2 = new Kopacz("Pawel", "Nowak", "11111111111", 123123123, 95);
        Kopacz k3 = new Kopacz("Dominik", "Mimala", "22222222222", 123123123, 95);
        Kopacz k4 = new Kopacz("Konrad", "Mimala", "22222222222", 123123123, 95);
        Brygadzista b1 = new Brygadzista("Pawel", "Nowak", "12312312311", 111222333, 100);
        Architekt a1 = new Architekt("Konrad", "Piechlo", "99999999999", 123456123, 88);

        List<Osoba> lista = new ArrayList<>();
        lista.add(k1);
        lista.add(k2);
        lista.add(k3);
        lista.add(k4);
        lista.add(b1);
        lista.add(a1);



        Brygada PierwszaBrygada = new Brygada();
        PierwszaBrygada.dodajPracownikow(lista);


        b1.sprawdzCzyBrygadaNiezdolnaDoPracy();
        k1.kop();
        k2.kop();
        k3.kop();

        for(Osoba el : lista){
            System.out.println(el);
        }


    }
}
