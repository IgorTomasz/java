import java.util.ArrayList;
import java.util.List;

public class Brygada {
    private Brygadzista brygadzista;
    private static List<Osoba> pracownicy = new ArrayList<>();
    private int maksymalnaIloscPracownikow;
    private int iloscMachniecLopataBrygady;

    public int ileArchitektow(){
        int ileArch =0;
        for(Osoba el : pracownicy){
            if(el.Stanowisko.equals("Architekt"));
                ileArch++;
        }
        return ileArch;
    }
    public boolean czyPelnaBrygada(){
        return maksymalnaIloscPracownikow==pracownicy.size();
    }
    public void dodajPracownika(Osoba pracownikDoDodania){
        pracownicy.add(pracownikDoDodania);
    }
    public void dodajPracownikow(List<Osoba> pracownicyDoDodania){
        pracownicy.addAll(pracownicyDoDodania);
    }
    public void setBrygadzista(Brygadzista brygadzista){
        this.brygadzista = brygadzista;
    }

    public static List<Osoba> getPracownicy(){
        return pracownicy;
    }

}
