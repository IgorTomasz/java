public abstract class Osoba{
    protected String imie;
    private String nazwisko;
    private String pesel;
    private int nrTelefonu;
    private float waga;
    protected String Stanowisko;

    Osoba(String imie, String nazwisko, String pesel, int nrTelefonu, float waga) throws nieUnikalnyPeselException {
        this.imie=imie;
        this.nazwisko=nazwisko;
        if(czyPeselUnikalny(pesel)) {
            this.pesel = pesel;
        }
        else {
            throw new nieUnikalnyPeselException();
        }
        this.nrTelefonu=nrTelefonu;
        this.waga=waga;
    }

    public boolean czyPeselUnikalny(String pesel){
        boolean Unikat = true;
        for(Osoba el: Brygada.getPracownicy()){
            if(pesel.equals(el.pesel)){
                Unikat= false;
            }
        }
        return Unikat;
    }

}
