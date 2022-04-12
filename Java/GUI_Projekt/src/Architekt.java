public class Architekt extends Osoba implements IPracownik{
    private String Specjalizacja="Architekt";

    Architekt(String imie, String nazwisko, String pesel, int nrTelefonu, float waga) {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
        super.Stanowisko = this.Specjalizacja;
    }

    @Override
    public int pobierzPensje() {
        return 5600;
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
