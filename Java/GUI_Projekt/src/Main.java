import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        CreatePerson cp = new CreatePerson();

        //tworzenie pracownikow
        Kopacz k1 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Jan", "Kowalski", "11111111111", 111111111, 95);
        Kopacz k2 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Pawel", "Nowak", "11111111112", 111111111, 100);
        Kopacz k3 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Karol", "Iksinski", "11111111112", 111111111, 100);
        Kopacz k4 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Piotr", "Beksinski", "11111111114", 111111111, 100);
        Kopacz k5 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Maciej", "Kowal", "11111111115", 111111111, 100);
        Kopacz k6 = (Kopacz) cp.createOsoba(Specjalizacja.Kopacz, "Franciszek", "Reksinski", "11111111116", 111111111, 100);
        Architekt a1 = (Architekt) cp.createOsoba(Specjalizacja.Architekt, "Pawel", "Pawelski", "11111111117", 111111111, 100);
        Architekt a2 = (Architekt) cp.createOsoba(Specjalizacja.Architekt, "Janusz", "Januszewski", "11111111118", 111111111, 100);
        Architekt a3 = (Architekt) cp.createOsoba(Specjalizacja.Architekt, "Arek", "Arkowski", "11111111119", 111111111, 100);
        Brygadzista b1 = (Brygadzista) cp.createOsoba(Specjalizacja.Brygadzista, "Patryk", "Patrykowski", "11111111110", 111111111, 88, "Kozak");
        Brygadzista b2 = (Brygadzista) cp.createOsoba(Specjalizacja.Brygadzista, "Kornel", "Jasik", "11111111122", 111111111, 88, "Mistrz");

        //tworzenie listy pracownikow w celu dodania do brygady
        List<Osoba> listaPracownikow = new ArrayList<>();

        listaPracownikow.add(k1);
        listaPracownikow.add(k2);
        listaPracownikow.add(k3);
        listaPracownikow.add(k4);
        listaPracownikow.add(a1);
        listaPracownikow.add(a2);
        listaPracownikow.add(a3);
        listaPracownikow.add(b1);
        listaPracownikow.add(b2);

        //tworzenie brygady
        Brygada PierwszaBrygada = new Brygada(15);

        //samozatrudnienie pracownika do brygady
        k5.dodajSieDoBrygady(PierwszaBrygada);

        //dodawanie pracownika do brygady
        PierwszaBrygada.dodajPracownika(k6);

        //dodwanie listy pracownikow do brygady
        PierwszaBrygada.dodajPracownikow(listaPracownikow);

        //ustawianie brygadzisty brygady
        PierwszaBrygada.setBrygadzista(b1);

        //ilu architektow
        System.out.println("Ilosc architektow: "+PierwszaBrygada.ileArchitektow());

        //sprawdzanie czy brygada jest pelna
        System.out.println("Czy brygada jest pełna: "+PierwszaBrygada.czyPelnaBrygada());

        //wyswietlenie pensji kopacza
        System.out.println("Pensja k1: "+k1.pobierzPensje());

            //sprawdzanie czy brygada jest zdolna do pracy przez brygadziste
            b1.sprawdzCzyBrygadaNiezdolnaDoPracy();

            checkifnull(k1);
            checkifnull(k2);
            checkifnull(k3);
            checkifnull(k4);
            checkifnull(k5);
            checkifnull(k6);


            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){}


            //zatrzymywanie dzialania kopacza
            k6.zakonczDzialanie();

            b2.sprawdzCzyBrygadaNiezdolnaDoPracy();

            //wyswietlanie co aktualnie robi brygadzista
            System.out.println(b2.powiedzCoRobisz());

            //wyswietlenie ile razy kopacz wykopal
            System.out.println(k2.imie+" wykopał: "+k2.powiedzIleRazyKopales());






    }

    public static void checkifnull(Kopacz o){
        if(!Objects.isNull(o)){
            o.kop();
        }
    }
}
