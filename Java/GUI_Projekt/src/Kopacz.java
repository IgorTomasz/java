public class Kopacz extends Osoba implements IPracownik{
    private int iloscMachniecLopata;
    private boolean czyZdolnyDoPracy = true;

    Kopacz(String imie, String nazwisko, String pesel, int nrTelefonu, float waga) {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
    }

    public void kop(){
        int los = (int)(Math.random()*95+5);

        for (int i = 0; i < los; i++) {
            int czas = (int)(Math.random()*1000);
            Thread kopanie = new Thread(() -> {System.out.println("Kopacz "+this.imie+"machnal lopata");});
            try {
                Thread.sleep(czas);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public int pobierzPensje() {
        return 2500;
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
