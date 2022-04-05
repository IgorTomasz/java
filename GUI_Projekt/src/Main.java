public class Main {

    public static void main(String[] args) {
	Architekt architekt = new Architekt("Jan", "Kowalski", "11111111111", 123123123, 96.5f);
    Brygadzista brygadzista = new Brygadzista("Piotr", "Nowak", "11111111111", 111222333, 115f);
    Brygada Brygada1 = new Brygada();
    Brygada1.dodajPracownika(architekt);
        System.out.println(brygadzista.pobierzPensje());
    }
}
