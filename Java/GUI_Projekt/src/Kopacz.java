import javax.swing.plaf.IconUIResource;

public class Kopacz extends Osoba implements IPracownik{
    private int iloscMachniecLopata;
    private static boolean czyZdolnyDoPracy = true;
    private Thread kopanie;
    private Brygada brygada = new Brygada(0);

    Kopacz(String imie, String nazwisko, String pesel, int nrTelefonu, int waga) {
        super(imie, nazwisko, pesel, nrTelefonu, waga, Specjalizacja.Kopacz);

    }

    Kopacz(String imie, String nazwisko, String pesel, int nrTelefonu, int waga, Specjalizacja stanowisko) {
        super(imie, nazwisko, pesel, nrTelefonu, waga, stanowisko);
    }

    public synchronized void kop(){
        int los = (int)(Math.random()*95+5); // losowanie ilosci machniec
        int jedenProcent = (int)(Math.random()*99+1); // losowanie liczby odpowiadajacej za 1% na zlamanie lopaty podczas kopania

        //tworzenie wątku odpowiadającego za kopanie
        kopanie = new Thread(()-> {
                for (int i = 0; i < los ; i++) {
                    int czas = (int) (Math.random() * 1000); // losowanie czasu miedzy machnieciami
                    System.out.println(" Kopacz " + this.imie + " machnął lopata");
                    if (jedenProcent == 1) { //
                        try {
                            throw new zlamanaLopataException("Łopata była wadliwa i " +
                                    "złamała się niespodziewanie w trakcie użytkowania");
                        } catch (zlamanaLopataException e) {
                            czyZdolnyDoPracy = false;
                            break;
                        }
                    }
                    brygada.setIloscMachniecLopataBrygady(); // dodawanie machniecia do wszyskich machniec brygady
                    ++this.iloscMachniecLopata; // dodawanie machniecia do indywidualnego licznika kazdego kopacza
                    if (this.iloscMachniecLopata >= 15) { // sprawdzenie czy kopacz machnal juz 15 razy
                        try {
                            throw new zlamanaLopataException("Łopata zużyła się i pękła");

                        } catch (zlamanaLopataException e) {
                            czyZdolnyDoPracy = false;
                            break;
                        }
                    }
                    try {
                        Thread.sleep(czas);
                    } catch (InterruptedException e) {
                    }
                }

        });
        kopanie.start();
    }

    @Override
    public int pobierzPensje() {
        return 2500;
    }

    @Override
    public int powiedzIleRazyKopales() {
        return iloscMachniecLopata;
    }

    @Override
    public String powiedzCoRobisz() {
        if(kopanie.isAlive()){
            return "Kopie";
        }else
        return "Nic nie robie";
    }

    @Override
    public void zakonczDzialanie() {
        this.kopanie.interrupt();
        System.out.println("Przerwano kopanie");
    }

    @Override
    public void dodajSieDoBrygady(Brygada brygada) {
        brygada.dodajPracownika(this);
    }

    public String getImie(){
        return super.imie;
    }

    public boolean isCzyZdolnyDoPracy(){
        return czyZdolnyDoPracy;
    }

}
