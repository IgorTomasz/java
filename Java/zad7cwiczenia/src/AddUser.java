import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser extends JFrame {
    private User.Position[] position = new User.Position[2];
    private String[] positionString = new String[2];
    private User.Status status;
    private Pass passH;
    private User user;

    AddUser(User user, Pass pass){
        this.user=user;
        this.passH=pass;
        GetWindow();
    }

    public void GetWindow(){

        this.setSize(300, 300);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Dodaj użytkownika");

        JPanel jpanelMain = new JPanel();
        JPanel jpanelLeft = new JPanel();
        JPanel jpanelRight = new JPanel();
        jpanelLeft.setPreferredSize(new Dimension(120,300));
        jpanelRight.setPreferredSize(new Dimension(170,300));
        jpanelLeft.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        jpanelRight.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));


        jpanelMain.setLayout(new GridBagLayout());
        jpanelLeft.setLayout(new GridBagLayout());
        jpanelRight.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        JLabel loginLabel = new JLabel("Login: ");
        JLabel passLabel = new JLabel("Hasło: ");
        JLabel nameLabel = new JLabel("Imie:");
        JLabel lastNameLabel = new JLabel("Nazwisko:");
        JLabel peselLabel = new JLabel("Pesel:");
        JLabel positionLabel = new JLabel("Stanowisko:");
        JLabel statusLabel = new JLabel("Status:");

        JTextArea loginArea = new JTextArea();
        loginArea.setMaximumSize(new Dimension(100,17));

        JPasswordField passArea = new JPasswordField();

        passArea.setPreferredSize(new Dimension(100,17));
        JTextArea nameArea = new JTextArea();

        nameArea.setPreferredSize(new Dimension(100,17));
        JTextArea lastNameArea = new JTextArea();

        lastNameArea.setPreferredSize(new Dimension(100,17));
        JTextArea peselArea = new JTextArea();

        peselArea.setPreferredSize(new Dimension(100,17));
        JCheckBox developerCheck = new JCheckBox(User.Position.DEVELOPER.toString());
        JCheckBox managerCheck = new JCheckBox(User.Position.MANAGER.toString());
        ButtonGroup radioBG = new ButtonGroup();
        JRadioButton statusArea1 = new JRadioButton("Aktywny");
        JRadioButton statusArea2 = new JRadioButton("Nieaktywny");

        JButton addUser = new JButton("Dodaj użytkownika");
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(developerCheck.isSelected()){
                    position[0]=User.Position.DEVELOPER;
                    positionString[0]="DEVELOPER";
                    positionString[1]="";
                }
                if(managerCheck.isSelected()){
                    position[0]=User.Position.MANAGER;
                    positionString[0]="MANAGER";
                    positionString[1]="";
                }
                if(managerCheck.isSelected()&&developerCheck.isSelected()){
                    position[0]=User.Position.DEVELOPER;
                    positionString[0]="DEVELOPER";
                    position[1]=User.Position.MANAGER;
                    positionString[1]="MANAGER";

                }
                if(statusArea1.isSelected()){
                    status=User.Status.ACTIVE;
                }
                if(statusArea2.isSelected()){
                    status=User.Status.INACTIVE;
                }

                    if(user.addUserToList(new User(nameArea.getText(),loginArea.getText(),passH.getHashedPass(new String(passArea.getPassword())),lastNameArea.getText(),peselArea.getText(),position,status))){
                        setVisible(false);
                        dispose();
                        passH.addToHashMap(loginArea.getText(),passH.getHashedPass(new String(passArea.getPassword())),positionString);
                    }

            }
        });

        radioBG.add(statusArea1);
        radioBG.add(statusArea2);


        Insets i = new Insets(0,0,5,0);

        c.fill=GridBagConstraints.HORIZONTAL;

        c.anchor=GridBagConstraints.LINE_START;
        c.insets=i;
        c.gridheight=1;
        c.gridx=0;
        c.gridy=0;
        jpanelLeft.add(loginLabel,c);
        c.gridwidth=2;
        jpanelRight.add(loginArea,c);

        c.gridwidth=1;
        c.gridy=1;
        jpanelLeft.add(passLabel,c);
        c.gridwidth=2;
        jpanelRight.add(passArea,c);

        c.gridwidth=1;
        c.gridy=2;
        jpanelLeft.add(nameLabel,c);
        c.gridwidth=2;
        jpanelRight.add(nameArea,c);

        c.gridwidth=1;
        c.gridy=3;
        jpanelLeft.add(lastNameLabel,c);
        c.gridwidth=2;
        jpanelRight.add(lastNameArea,c);

        c.gridwidth=1;
        c.gridy=4;
        jpanelLeft.add(peselLabel,c);
        c.gridwidth=2;
        jpanelRight.add(peselArea,c);

        c.gridwidth=1;
        c.gridy=5;
        jpanelLeft.add(positionLabel,c);
        jpanelRight.add(developerCheck,c);
        c.gridx=1;
        jpanelRight.add(managerCheck,c);

        c.gridx=0;
        c.gridy=6;
        jpanelLeft.add(statusLabel,c);
        c.gridy=7;
        jpanelRight.add(statusArea1,c);
        c.gridx=1;
        jpanelRight.add(statusArea2,c);

        c.gridx=0;
        c.gridy=0;
        jpanelMain.add(jpanelLeft,c);
        c.gridx=1;
        jpanelMain.add(jpanelRight,c);
        c.gridwidth=3;
        c.gridx=0;
        c.gridy=1;
        jpanelMain.add(addUser,c);
        this.setLayout(new GridLayout(1,1));
        this.add(jpanelMain);
        this.setVisible(true);

        this.setLocationRelativeTo(null);
    }

}
