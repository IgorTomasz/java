public class Main {

    public static void main(String[] args) {
	char[] napis = {'a','l','a',' ','m','a',' ','k','o','t','a'};
    char[] szukane = {'a','l','m','k','o','t'};
    int[] wynik = szukanie(napis,szukane);
        for (int i = 0; i < szukane.length; i++) {
            System.out.println(szukane[i] + " - "+wynik[i]);
        }

    }
    public static int[] szukanie(char[] napis, char[] szukane){
        int[] ilosc = new int[szukane.length];
        for (int i = 0; i < szukane.length; i++) {
            char znak = szukane[i];
            for (int j = 0; j < napis.length; j++) {
                if(znak==napis[j]){
                    ilosc[i]++;
                }
            }
        }
        return ilosc;
    }
}
