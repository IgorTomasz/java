import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Brygada {
    private Brygadzista brygadzista;
    private static List<Osoba> pracownicy = new ArrayList<>();
    protected int maksymalnaIloscPracownikow;
    private static int iloscMachniecLopataBrygady;

    Brygada(int maxIlosc){
        this.maksymalnaIloscPracownikow=maxIlosc;
    }

    public int ileArchitektow(){
        int ileArch =0;
        for(Osoba el : pracownicy){
            if(el.Stanowisko.equals(Specjalizacja.Architekt)) { // sprawdzenie czy obiekt nie jest nullem
                ileArch++;
            }
        }
        return ileArch;
    }

    public boolean czyPelnaBrygada(){
        return maksymalnaIloscPracownikow==pracownicy.size();
    }

    public void dodajPracownika(Osoba pracownikDoDodania){
        if(!Objects.isNull(pracownikDoDodania)) {
            pracownicy.add(pracownikDoDodania);
        }
    }

    public void dodajPracownikow(List<Osoba> pracownicyDoDodania){
        for(Osoba el : pracownicyDoDodania) {
            if(!Objects.isNull(el)) {
                pracownicy.add(el);
            }
        }
    }
    public void setBrygadzista(Brygadzista brygadzista){
        this.brygadzista = brygadzista;
    }

    public List<Osoba> getPracownicy(){
        return pracownicy;
    }

    public static List<Kopacz> getKopaczZPracowników(){ // metoda potrzebna do sprawnego sprawdzania zdolnych do pracy kopaczy
        List<Kopacz> curenntKopacze = new ArrayList<>();
        for(Osoba el : pracownicy){
            if(el.Stanowisko.equals(Specjalizacja.Kopacz))
                curenntKopacze.add((Kopacz) el);
        }
        return curenntKopacze;
    }

    public synchronized void setIloscMachniecLopataBrygady() {
        iloscMachniecLopataBrygady++;
    }

    public int getIloscMachniecLopataBrygady() {
        return iloscMachniecLopataBrygady;
    }


}
