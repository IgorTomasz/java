
public abstract class Osoba implements Comparable<Osoba>{
    protected String imie;
    private String nazwisko;
    private String pesel;
    private int nrTelefonu;
    private int waga;
    protected Specjalizacja Stanowisko;
    private Pracownicy pracownicy = new Pracownicy();


    Osoba(String imie, String nazwisko, String pesel, int nrTelefonu, int waga, Specjalizacja stanowisko) {
        this.imie=imie;
        this.nazwisko=nazwisko;
        this.nrTelefonu=nrTelefonu;
        this.waga=waga;
        this.Stanowisko=stanowisko;

        if(pracownicy.czyPeselUnikalny(pesel)){ // odwolanie do metody sprawdzajacej unikalnosc peselu
            this.pesel=pesel;
            pracownicy.listaKopaczy.add(pesel);
        }else {
            try {
                throw new nieUnikalnyPeselException(pesel);
            }catch (nieUnikalnyPeselException e){
                this.pesel = "NieUnikalnyPesel";
                pracownicy.deletePerson(this);
            }
        }
    }

    @Override
    public int compareTo(Osoba o) {
        return this.imie.compareTo(o.imie) == 0 ?
                this.nazwisko.compareTo(o.nazwisko) == 0 ?
                this.waga-o.waga :
                this.nazwisko.compareTo(o.nazwisko):
                this.imie.compareTo(o.imie);
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", pesel='" + pesel + '\'' +
                ", nrTelefonu=" + nrTelefonu +
                ", waga=" + waga +
                ", Stanowisko='" + Stanowisko ;
    }
}
