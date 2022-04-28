import java.util.ArrayList;
import java.util.List;

public class Brygadzista extends Kopacz implements IPracownik{
    private String pseudonim;
    private int dlugoscZmiany=5;
    private String brygada;
    private Thread sprawdzanieCzyZdolna;

    Brygadzista(String imie, String nazwisko, String pesel, int nrTelefonu, int waga, String pseudonim) {
        super(imie, nazwisko, pesel, nrTelefonu, waga , Specjalizacja.Brygadzista);
        this.pseudonim=pseudonim;
    }

    public synchronized void sprawdzCzyBrygadaNiezdolnaDoPracy(){

        sprawdzanieCzyZdolna = new Thread(()->{
            List<Boolean> listaSprawnosci = new ArrayList<>();
            boolean komunikat = true;
                for(int i=0; i<dlugoscZmiany;i++) {
                    for (Kopacz el : Brygada.getKopaczZPracowników()) { // przechodzenie po kazdym elemencie typu kopacz
                                                                        // w celu sprawdzenia czy zdolny do pracy
                        if (el.isCzyZdolnyDoPracy()) {
                            listaSprawnosci.add(true);
                        }

                            if(listaSprawnosci.isEmpty()) { // sprawdzenie listy w celu stworzenia stosownego komunikatu
                                komunikat = false;
                            }
                            else {
                                komunikat = true;
                            }
                        listaSprawnosci.clear(); //czyszczenie listy po sprawdzeniu
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
                if(!komunikat){ // sprawdzenie wartosci zmiennej komunikat w celu podjecia odpowiednich dzialan
                    try {
                        throw new brygadaNiezdolnaDoPracyException();
                    } catch (brygadaNiezdolnaDoPracyException e) {}
                }else
                    System.out.println("Brygada zdolna do pracy");
            }
        );
        sprawdzanieCzyZdolna.start();
        }



    @Override
    public int pobierzPensje() {
        return 4350;
    }

    @Override
    public int powiedzIleRazyKopales() {
        return super.powiedzIleRazyKopales();
    }

    @Override
    public String powiedzCoRobisz() {
        if(sprawdzanieCzyZdolna.isAlive()){
            return "Sprawdzam pracownikow";
        }else
        return "Nic nie robie";
    }

    @Override
    public void zakonczDzialanie() {
        this.sprawdzanieCzyZdolna.interrupt();
    }

    @Override
    public void dodajSieDoBrygady(Brygada brygada) {
        brygada.dodajPracownika(this);
    }



}
