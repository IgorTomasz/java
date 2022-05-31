import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.List;

public class User extends JFrame {
    private String name;
    private String lastname;
    private String pesel;
    private Position[] position = new Position[2];
    private Status status;
    private String login;
    private String pass;
    private List<User> users = new ArrayList<>();
    private Project project = new Project(this);

    enum Position{
        DEVELOPER, MANAGER
    }
    enum Status{
        ACTIVE, INACTIVE
    }
    User(String name, String login, String pass, String lastname, String pesel, Position[] position, Status status){
        this.name=name;
        this.login=login;
        this.pass=pass;
        this.lastname=lastname;
        this.pesel=pesel;
        this.position=position;
        this.status=status;
    }

/*    User(String name, String login, String pass, String lastname, String pesel, Position position, Status status){
        this.name=name;
        this.login=login;
        this.pass=pass;
        this.lastname=lastname;
        this.pesel=pesel;
        this.position[0]=position;
        this.status=status;
    }*/
    User(String MainScreenLogin){
        GetWindow(MainScreenLogin);
    }

    public void GetWindow(String MainScreenLogin){
        getUsersToList(MainScreenLogin);

        this.setSize(850, 250);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Użytkownik");

        JPanel jpanelMain = new JPanel();
        JPanel jpanelLeft = new JPanel();
        jpanelLeft.setPreferredSize(new Dimension(300,250));
        JPanel jpanelRight = new JPanel();
        jpanelRight.setPreferredSize(new Dimension(500,250));

        jpanelLeft.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        jpanelRight.setBorder(BorderFactory.createEmptyBorder(20,0,20,30));

        jpanelMain.setLayout(new BorderLayout());
        jpanelLeft.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        jpanelRight.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Imię:");
        JLabel lastNameLabel = new JLabel("Nazwisko:");
        JLabel peselLabel = new JLabel("Pesel:");
        JLabel positionLabel = new JLabel("Stanowisko:");
        JLabel statusLabel = new JLabel("Status:");
        JLabel projectsLabel = new JLabel("Projekty");

        JTextArea nameArea = new JTextArea();
        nameArea.setText(name);
        nameArea.setPreferredSize(new Dimension(100,20));
        JTextArea lastNameArea = new JTextArea(lastname);
        lastNameArea.setPreferredSize(new Dimension(100,20));
        JTextArea peselArea = new JTextArea(pesel);
        peselArea.setEditable(false);
        peselArea.setPreferredSize(new Dimension(100,20));
        JComboBox positoionArea = new JComboBox(position);
        ButtonGroup radioBG = new ButtonGroup();
        JRadioButton statusArea1 = new JRadioButton("Aktywny");
        JRadioButton statusArea2 = new JRadioButton("Nieaktywny");
        statusArea1.setEnabled(false);
        statusArea2.setEnabled(false);
        if(status == Status.ACTIVE){
            statusArea1.setSelected(true);
        }else {
            statusArea2.setSelected(true);
        }
        radioBG.add(statusArea1);
        radioBG.add(statusArea2);

        String[] columnNameProjects = {"Nazwa","Status"};

        JTable projectsTable = new JTable(project.getProjectsInArrayMulti(MainScreenLogin),columnNameProjects){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        projectsTable.setPreferredSize(new Dimension(500,300));
        Insets i = new Insets(0,0,5,0);

        JScrollPane projTableScroll = new JScrollPane(projectsTable);
        projTableScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        projTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        c.insets = i;
        c.gridx=0;
        c.gridy=0;
        jpanelLeft.add(nameLabel,c);
        c.gridx=1;
        jpanelLeft.add(nameArea,c);
        c.gridx=0;
        c.gridy=1;
        jpanelLeft.add(lastNameLabel,c);
        c.gridx=1;
        jpanelLeft.add(lastNameArea,c);
        c.gridx=0;
        c.gridy=2;
        jpanelLeft.add(peselLabel,c);
        c.gridx=1;
        jpanelLeft.add(peselArea,c);
        c.gridx=0;
        c.gridy=3;
        jpanelLeft.add(positionLabel,c);
        c.gridx=1;
        jpanelLeft.add(positoionArea,c);
        c.gridx=0;
        c.gridy=4;
        jpanelLeft.add(statusLabel,c);
        c.gridx=1;
        jpanelLeft.add(statusArea1,c);
        c.gridx=2;
        jpanelLeft.add(statusArea2,c);

        jpanelRight.add(projectsLabel, BorderLayout.PAGE_START);
        jpanelRight.add(projTableScroll,BorderLayout.CENTER);

        jpanelMain.add(jpanelLeft, BorderLayout.LINE_START);
        jpanelMain.add(jpanelRight,BorderLayout.CENTER);
        this.add(jpanelMain);


        this.setLocationRelativeTo(null);
    }

    public void getUsersToList(String MainScreenLogin){
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
                String userName = parts[3].trim();
                String userLastName = parts[4].trim();
                String userPesel = parts[5].trim();
                Status userStatus = Status.valueOf(parts[6].trim());
                Position[] userPosition = new Position[2];
                userPosition[0]=Position.valueOf(parts[7].trim());
                        try{
                            if(parts[7].trim()!=null){
                                userPosition[1]=Position.valueOf(parts[8].trim());
                            }
                        }catch (Exception e){

                        }
                        if(userlogin.equals(MainScreenLogin)){
                            this.login=userlogin;
                            this.pass=userPass;
                            this.name=userName;
                            this.lastname=userLastName;
                            this.pesel=userPesel;
                            this.position=userPosition;
                            this.status=userStatus;
                            users.add(this);
                        }else {
                            users.add(new User(userName, userlogin, userPass, userLastName, userPesel, userPosition, userStatus));
                        }

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

    public String[][] getUsersInArray(){
        String[][] usersArray = new String[users.size()][4];

        int i=0;
        for(User el:users){
            usersArray[i][0]=el.name;
            usersArray[i][1]=el.lastname;
            String userPosition = el.position[0].toString();
            if(el.position[1]!=null){
                userPosition+="-"+el.position[1].toString();
            }
            usersArray[i][2]=userPosition;
            usersArray[i][3]=el.status.toString();
            i++;
        }

        return usersArray;
    }

    public String[] getUsersInArraySingle(){
        String[] usersArray = new String[users.size()];

        int i=0;
        for(User el:users){
            usersArray[i]=el.login+" - "+el.name+" - "+el.lastname;
            i++;
        }

        return usersArray;
    }

    public List<User> getList(){
        return users;
    }
    public String getLogin(){
        return login;
    }

    public String getPass(){
        return pass;
    }

    public String getPesel(){
        return pesel;
    }

    public User getSpecifiedUser(String login){
        User u1 = null;
        for(User el : users) {
            if (el.login.equals(login)) {
                u1 = el;
            }
        }
        return u1;
    }

    public String getName(){
        return this.name;
    }

    public String getLastname(){
        return lastname;
    }

    public String getPosition(){
        String positionString="";
        for(int i =0; i<position.length;i++){
            if(position[i]!=null) {
                positionString += "-" + position[i];
            }
        }
        return positionString;
    }

    public String getPositionClear(){
        String positionString="";
        for(int i =0; i<position.length;i++){
            if(position[i]!=null) {
                positionString += "#"+position[i];
            }
        }
        return positionString;
    }

    public String getStatus(){
        return status.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
    public boolean addUserToList(User user){
        boolean isAdded = false;
        boolean isNew = true;
        for (User el : users){
            if(el.login.equals(user.login)) {
                isNew = false;
            }
        }

        if(isNew){
            users.add(user);
            isAdded=true;
        }else {
            JOptionPane.showMessageDialog(null,"Istnieje już użytkownik z takim loginem");
        }
        return isAdded;
    }

}
