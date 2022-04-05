public class Brygadzista extends Kopacz implements IPracownik{
    private String pseudonim;
    private int dlugoscZmiany;
    private String brygada;

    Brygadzista(String imie, String nazwisko, String pesel, int nrTelefonu, float waga) {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
    }

    public void sprawdzCzyBrygadaNiezdolnaDoPracy(){

    }

    @Override
    public int pobierzPensje() {
        return 4350;
    }

    @Override
    public int powiedzIleRazyKopales() {
        return 0;
    }

    @Override
    public String powiedzCoRobisz() {
        return null;
    }

    @Override
    public void zakonczDzialanie() {

    }

    @Override
    public void dodajSieDoBrygady(Brygada brygada) {
        brygada.dodajPracownika(this);
    }

}
