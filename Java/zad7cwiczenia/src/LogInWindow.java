import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LogInWindow extends JFrame {

    Pass pass = new Pass();
    JTextField loginText = new JTextField("Adam");
    JPasswordField passwordText = new JPasswordField("Adam1");

    LogInWindow(){
        this.setSize(350,350);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel firstPanel = new JPanel();
        JPanel secondPanel = new JPanel();
        JPanel thirdPanel = new JPanel();


        JLabel login = new JLabel("Login");
        JLabel password = new JLabel("Haslo");



        KeyListener k = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    LogIn(loginText,passwordText);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        loginText.addKeyListener(k);
        passwordText.addKeyListener(k);


        loginText.setColumns(10);
        passwordText.setColumns(10);

        loginText.setSize(25, 10);

        JButton loguj = new JButton("Zaloguj");

        Thread thread = new Thread(()->{
           loguj.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   LogIn(loginText,passwordText);
               }
           });
        });
        thread.start();

        firstPanel.add(login);
        firstPanel.add(loginText);


        secondPanel.add(password);
        secondPanel.add(passwordText);

        thirdPanel.add(loguj);



        this.setLayout(new GridLayout(3,1));
        this.add(firstPanel);
        this.add(secondPanel);
        this.add(thirdPanel);

        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void LogIn(JTextField loginText, JPasswordField passwordText){
        if(pass.getUserCredentials().containsKey(loginText.getText())){
            if(pass.getUserCredentials().get(loginText.getText()).equals(pass.getHashedPass(new String(passwordText.getPassword())))){
                setVisible(false);
                dispose();
                    if(pass.getUserData().get(loginText.getText())[1]!=null) {
                        if (pass.getUserData().get(loginText.getText())[1].equals("MANAGER")) {
                            MainScreen ms = new MainScreen(loginText.getText(), this, pass);
                        }
                    }else {
                        MainScreenDev msd = new MainScreenDev(loginText.getText(), this);
                    }




            }else {
                JOptionPane.showMessageDialog(null, "Haslo niepoprawne");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Nie ma takiego loginu");
        }
    }
}
