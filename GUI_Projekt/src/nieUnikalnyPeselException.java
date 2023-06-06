public class nieUnikalnyPeselException extends Exception{
    nieUnikalnyPeselException(String pesel){
        System.out.println("Pesel " + pesel +
                " istnieje na liście pracowników - nie jest unikalny");
    }
}
