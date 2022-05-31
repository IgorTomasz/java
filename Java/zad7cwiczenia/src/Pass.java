import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Pass {
    private HashMap userCredentials = new HashMap<String, String>();
    private HashMap userData = new HashMap<String, String[]>();

    Pass(){
        BufferedReader br = null;
        try{
            File file = new File("D:\\Java\\zad7cwiczenia-20220516T190306Z-001\\zad7cwiczenia\\src\\data.txt");
            br = new BufferedReader(new FileReader(file));
            String line = null;

            while((line = br.readLine())!=null){
                String[] parts = line.split("#");
                String type = parts[0].trim();

                if(type.equals("PRACOWNIK")){
                    String userlogin = parts[1].trim();
                    String userPass = parts[2].trim();
                    String userPosition1 = parts[7].trim();
                    String userPosition2 = "";
                    try{
                        userPosition2 = parts[8].trim();
                    }catch (Exception e){

                    }
                    String[] userPosition = new String[2];
                    if(userPosition1.equals("MANAGER")){
                        userPosition[0] = userPosition2;
                        userPosition[1] = userPosition1;
                    }
                    if(userPosition2.equals("MANAGER")) {
                        userPosition[0] = userPosition1;
                        userPosition[1] = userPosition2;
                    }

                    userData.put(userlogin,userPosition);
                    userCredentials.put(userlogin,userPass);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(br != null){
                try{
                    br.close();
                }catch (Exception e){
                }
            }
        }
    }

    public HashMap<String,String> getUserCredentials(){
        return userCredentials;
    }

    public HashMap<String,String[]> getUserData(){
        return userData;
    }

    public String getHashedPass(String pass){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytesOfMessage = digest.digest(pass.getBytes("UTF-8"));

            for (byte b : bytesOfMessage) {
                sb.append(String.format("%02X", b));

            }
        } catch (NoSuchAlgorithmException a) {
            return null;
        } catch (UnsupportedEncodingException a) {
            return null;
        }
        return (sb.toString());
    }

    public void addToHashMap(String login, String pass, String[] pos){
        userCredentials.put(login,pass);
        userData.put(login,pos);
    }

}
