public class CreatePerson {
    Pracownicy pracownicy = new Pracownicy();


    public Osoba createOsoba(Specjalizacja stanowisko, String imie, String nazwisko, String pesel, int nrTelefonu, int waga){
        if(pracownicy.czyPeselUnikalny(pesel)){

            switch (stanowisko) {
                case Kopacz -> {
                    return new Kopacz(imie, nazwisko, pesel, nrTelefonu, waga);
                }
                case Architekt -> {
                    return new Architekt(imie, nazwisko, pesel, nrTelefonu, waga);
                }
            }
        }else{
            try{
                throw new nieUnikalnyPeselException(pesel);
            }catch (nieUnikalnyPeselException e){

            }
        }
        return null;
    }

    public Osoba createOsoba(Specjalizacja stanowisko, String imie, String nazwisko, String pesel, int nrTelefonu, int waga, String pseudonim){
        if(pracownicy.czyPeselUnikalny(pesel)){

            switch (stanowisko) {
                case Brygadzista -> {
                    return new Brygadzista(imie, nazwisko, pesel, nrTelefonu, waga, pseudonim);
                }
            }
        }
        return null;
    }
}
