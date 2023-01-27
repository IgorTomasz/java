import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame {
    private JList<String> projectsList;
    private JTable commentsPane;

    private String login;
    private LogInWindow l;
    private User user;
    private Project project;
    private CommentInProject commentInProject;
    private UserInProject userInProject;
    private ProjectManager projectManager;
    private SaveToFile saveToFile;
    private Pass pass;
    protected static JLabel timerLabel = new JLabel("Czas sesji: 60");
    //private AddUser addUser;

    MainScreen(String login, LogInWindow l, Pass pass){
        this.login=login;
        this.l=l;
        this.user = new User(login);
        this.project = new Project(user);
        this.commentInProject= new CommentInProject(user,project);
        this.userInProject = new UserInProject(user,project,commentInProject);
        this.saveToFile = new SaveToFile(commentInProject,user,project,userInProject);
        this.pass=pass;
        this.projectManager = new ProjectManager(user,l,login,this,project,commentInProject,userInProject,saveToFile,pass);


        MainScreenWindow();
    }

    public void MainScreenWindow(){

        this.setSize(850, 450);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile.addToFile();
            }
        });
        this.setTitle("Ekran główny");
        Timer.resetCounter();

        JPanel jpanelMain = new JPanel();
        jpanelMain.setLayout(new BorderLayout());
        JPanel jpanelLeftMain = new JPanel();
        JPanel jpanelRightMain = new JPanel();
        JPanel jpanelRightUpMain = new JPanel();
        JPanel jpanelRightDownMain = new JPanel();
        JPanel jpanelFootMain = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Projekty");
        JMenu menu2 = new JMenu("Uzytkownicy");
        JMenu menu3 = new JMenu("Konto");
        JMenuItem itemMenu1 = new JMenuItem("Dodaj projekt");
        itemMenu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProject();
            }
        });
        JMenuItem itemMenu2 = new JMenuItem("Lista projektow");
        JMenuItem itemMenu3 = new JMenuItem("Dodaj uzytkownika");
        itemMenu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               addUser();
            }
        });
        JMenuItem itemMenu5 = new JMenuItem("Moje konto");
        itemMenu5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.GetWindow();
            }
        });
        JMenuItem itemMenu6 = new JMenuItem("Wyloguj");
        itemMenu6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile.addToFile();
                Timer.stopThread();
                l.setVisible(true);
                l.loginText.setText("");
                l.passwordText.setText("");
                setVisible(false);
                dispose();
            }
        });
        menu1.add(itemMenu1);
        menu1.add(itemMenu2);
        menu2.add(itemMenu3);
        menu3.add(itemMenu5);
        menu3.add(itemMenu6);
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);

        JPanel projectsLabelPane = new JPanel();
        JPanel teamLabelPane = new JPanel();
        JPanel commentsLabelPane = new JPanel();


        jpanelMain.setSize(850,450);
        jpanelLeftMain.setLayout(new BorderLayout());
        jpanelLeftMain.setSize(200,440);
        jpanelRightUpMain.setLayout(new BorderLayout());
        jpanelRightUpMain.setSize(600,100);
        jpanelRightDownMain.setLayout(new BorderLayout());
        jpanelRightDownMain.setSize(600,300);
        jpanelRightMain.setLayout(new GridLayout(2,1));
        jpanelRightMain.setSize(600,410);
        jpanelFootMain.setLayout(new BorderLayout());


        jpanelLeftMain.setBorder(BorderFactory.createEmptyBorder(20,10,10,20));
        jpanelRightUpMain.setBorder(BorderFactory.createEmptyBorder(30,0,10,20));
        jpanelRightDownMain.setBorder(BorderFactory.createEmptyBorder(0,0,10,20));


        JLabel projectsLabelMain = new JLabel("Aktywne projekty");
        JLabel teamLabelMain = new JLabel("Zespół");
        JLabel commentsLabelMain = new JLabel("Komentarze");
        JButton footer = new JButton(login);

        footer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setEnabled(false);
                user.GetWindow();
            }
        });

        projectsList = new JList<>(project.getProjectByStatus("AKTYWNY"));
        projectsList.setPreferredSize(new Dimension(200,450));
        projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        projectsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    setVisible(false);
                    JList list = (JList) e.getSource();
                    int selection[] = list.getSelectedIndices();
                    Object values[] = list.getSelectedValues();
                    for(int i=0,n=selection.length;i<n; i++) {
                        Object val = values[i];

                        projectManager.setProjectName(val.toString());
                        projectManager.setStatusArea(project.getStatus(val.toString()).toString());
                    }
                    Timer.resetCounter();
                    projectManager.setVisible(true);
                }
            }
        });

        projectsLabelPane.add(projectsLabelMain);
        teamLabelPane.add(teamLabelMain);
        commentsLabelPane.add(commentsLabelMain);


        JTable teamPane = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        commentsPane = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        projectsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                    JList list = (JList) e.getSource();
                    int selection[] = list.getSelectedIndices();
                    Object values[] = list.getSelectedValues();
                String[] columnNameTeam = {"Imie", "Nazwisko", "Stanowisko", "Status"};
                String[] columnNameComment = {"Użytkownik","Data","Komentarz"};
                    for(int i=0,n=selection.length;i<n; i++) {
                        Object val = values[i];

                        projectManager.setProject(val.toString());
                        teamPane.setModel(new DefaultTableModel(userInProject.getUserInProjectArray(val.toString()),columnNameTeam));
                        commentsPane.setModel(new DefaultTableModel(commentInProject.getCommentInProjectArray(val.toString()),columnNameComment));
                    }
            }
        });


        teamPane.setPreferredSize(new Dimension(600,300));
        JScrollPane teamSP = new JScrollPane(teamPane);
        teamSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        teamSP.setVisible(true);
        jpanelRightUpMain.add(teamSP, BorderLayout.CENTER);


        commentsPane.setPreferredSize(new Dimension(600,300));
        commentsPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        commentsPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = commentsPane.getSelectedRow();
                    int col = 2;
                    TableModel model = commentsPane.getModel();
                    String rowValue = model.getValueAt(row, col).toString();

                    JOptionPane.showMessageDialog(null,rowValue);
                }
            }
        });


        JScrollPane projectsSP = new JScrollPane();
        JScrollPane commentsSP = new JScrollPane(commentsPane);

        String[] filters = {"Wszystkie","ZAPLANOWANY","AKTYWNY","ZAKONCZONY"};
        JComboBox<String> filter = new JComboBox<>(filters);
        filter.setSelectedIndex(2);

        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProjectsList(filter.getSelectedItem().toString());
            }
        });

        MouseListeners listeners = new MouseListeners();

        addMouseListener(listeners);
        commentsPane.addMouseListener(listeners);
        projectsList.addMouseListener(listeners);
        teamPane.addMouseListener(listeners);

        projectsSP.setViewportView(projectsList);

        projectsList.setLayoutOrientation(JList.VERTICAL);

        jpanelLeftMain.add(filter,BorderLayout.PAGE_START);
        jpanelLeftMain.add(projectsSP, BorderLayout.CENTER);


        commentsSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jpanelRightUpMain.add(teamLabelMain, BorderLayout.PAGE_START);


        jpanelRightDownMain.add(commentsLabelMain, BorderLayout.PAGE_START);
        jpanelRightDownMain.add(commentsSP, BorderLayout.CENTER);

        jpanelRightMain.add(jpanelRightUpMain);
        jpanelRightMain.add(jpanelRightDownMain);

        jpanelFootMain.add(footer, BorderLayout.EAST);
        jpanelFootMain.add(timerLabel, BorderLayout.WEST);

        Timer.TimerThread(saveToFile,l,user);

        jpanelMain.add(jpanelLeftMain, BorderLayout.LINE_START);
        jpanelMain.add(jpanelRightMain, BorderLayout.CENTER);
        jpanelMain.add(jpanelFootMain, BorderLayout.PAGE_END);



//        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        this.setLayout(new GridLayout(1,1));
        this.add(jpanelMain);

        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JList<String> teamList = new JList<>();

    public JDialog addProject(){
        JDialog main = new JDialog(this, "Dodaj projekt", Dialog.ModalityType.TOOLKIT_MODAL);
        main.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        main.setSize(680,300);

        JPanel jpanelMain = new JPanel();
        JPanel jpanelLeft = new JPanel();
        JPanel jpanelRight = new JPanel();
        JPanel jpanelRightDown = new JPanel();
        jpanelLeft.setPreferredSize(new Dimension(200,300));
        jpanelRight.setPreferredSize(new Dimension(300,280));
        jpanelRightDown.setPreferredSize(new Dimension(300,20));
        jpanelLeft.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        jpanelRight.setBorder(BorderFactory.createEmptyBorder(10,0,0,10));


        jpanelMain.setLayout(new BorderLayout());
        jpanelLeft.setLayout(new GridBagLayout());
        jpanelRight.setLayout(new BorderLayout());
        jpanelRightDown.setLayout(new GridBagLayout());



        JLabel nameLabel = new JLabel("Nazwa projektu: ");
        JLabel dateLabel = new JLabel("Data dodania: ");
        JLabel usersLabel = new JLabel("Użytkownicy:");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String date = sdf.format(timestamp);

        JTextArea nameArea = new JTextArea();
        nameArea.setSize(new Dimension(100,17));

        JTextArea dateArea = new JTextArea();
        dateArea.setEditable(false);
        dateArea.setText(date);
        dateArea.setSize(new Dimension(100,17));

        JComboBox<String> addUserList = new JComboBox<>(user.getUsersInArraySingle());

        JButton addUserButton = new JButton("Dodaj do zespołu");
        addUserButton.setSize(new Dimension(170,20));
        JButton deleteUserButton = new JButton("Usuń z zespołu");
        deleteUserButton.setSize(new Dimension(170,20));
        JButton addProject = new JButton("Dodaj projekt");
        addProject.setSize(new Dimension(170,20));


        DefaultListModel model = new DefaultListModel();
        List<String> users = new ArrayList<>();

        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    Project.statusEnum status = Project.statusEnum.ZAPLANOWANY;
                    String[] userArray = new String[2];

                    int i=0;
                    for(String el : users){
                        userArray[i]=el;
                        System.out.println(el);
                        i++;
                    }

                    project.addProjectToList(new Project(nameArea.getText(), status, date, user.getSpecifiedUser(login),userArray));
                    userInProject.addUserToList(user.getSpecifiedUser(userArray[0]),project.getSpecifiedProject(nameArea.getText()),user.getSpecifiedUser(login));
                    if(userArray[1]!=null){
                        userInProject.addUserToList(user.getSpecifiedUser(userArray[1]),project.getSpecifiedProject(nameArea.getText()),user.getSpecifiedUser(login));
                    }

                updateProjectsList("AKTYWNY");
                main.dispose();
            }
        });

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(users.size()<2) {
                    model.addElement(addUserList.getSelectedItem());
                    String data = addUserList.getSelectedItem().toString();
                    String[] dataArray = data.split("-");
                    users.add(dataArray[0].trim());
                    teamList.setModel(model);
                }else {
                    JOptionPane.showMessageDialog(main,"Za dużo dodanych użytkowników", "Ostrzeżenie",JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        teamList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteUserButton.addActionListener(new ActionListener() {
                    JList list = (JList) e.getSource();
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int selection[] = list.getSelectedIndices();
                        Object values[] = list.getSelectedValues();
                        for(int i=0,n=selection.length;i<n; i++) {
                            Object val = values[i];

                            String[] data = val.toString().split("-");
                            model.removeElement(val);
                            users.remove(data[0].trim());
                        }
                    }
                });
            }
        });

        JScrollPane teamListSP = new JScrollPane(teamList);
        teamListSP.setPreferredSize(new Dimension(300,300));
        teamListSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        GridBagConstraints c = new GridBagConstraints();
        Insets i = new Insets(0,0,5,0);

        c.fill=GridBagConstraints.HORIZONTAL;
        c.insets=i;
        c.gridx=0;
        c.gridy=0;
        jpanelLeft.add(nameLabel,c);
        c.gridy=1;
        c.gridwidth=2;
        jpanelLeft.add(nameArea,c);

        c.gridwidth=1;
        c.gridx=0;
        c.gridy=2;
        jpanelLeft.add(dateLabel,c);
        c.gridwidth=2;
        c.gridy=3;
        jpanelLeft.add(dateArea,c);

        jpanelRight.add(usersLabel,BorderLayout.PAGE_START);
        jpanelRight.add(teamListSP,BorderLayout.CENTER);

        c.gridx=0;
        c.gridy=0;
        jpanelRightDown.add(addUserList,c);
        c.gridx=3;
        jpanelRightDown.add(addUserButton,c);
        c.gridx=5;
        jpanelRightDown.add(deleteUserButton,c);

        jpanelRight.add(jpanelRightDown,BorderLayout.PAGE_END);

        jpanelMain.add(jpanelLeft,BorderLayout.LINE_START);
        jpanelMain.add(jpanelRight,BorderLayout.CENTER);
        jpanelMain.add(addProject, BorderLayout.PAGE_END);
        main.add(jpanelMain);

        main.setLocationRelativeTo(null);
        main.setVisible(true);
        return main;
    }

    private User.Position[] position = new User.Position[2];
    private String[] positionString = new String[2];
    private User.Status status;

    public JDialog addUser(){

        JDialog main = new JDialog(this, "Dodaj użytkownika", Dialog.ModalityType.TOOLKIT_MODAL);
        main.setSize(new Dimension(300,300));
        main.setLocationRelativeTo(null);

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
        JLabel phoneNumberLabel = new JLabel("Numer telefonu:");
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

        JTextArea phoneNumberArea = new JTextArea();
        phoneNumberArea.setPreferredSize(new Dimension(100,17));

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
                if(!loginArea.getText().equals("")&&!passArea.getPassword().equals("")&&!nameArea.getText().equals("")&&!lastNameArea.getText().equals("")
                        &&!phoneNumberArea.getText().equals("")&&!peselArea.getText().equals("")&&developerCheck.isSelected()
                        ||managerCheck.isSelected()&&statusArea1.isSelected()||statusArea2.isSelected()) {

                    if (developerCheck.isSelected()) {
                        position[0] = User.Position.DEVELOPER;
                        positionString[0] = "DEVELOPER";
                        positionString[1] = "";
                    }
                    if (managerCheck.isSelected()) {
                        position[0] = User.Position.MANAGER;
                        positionString[1] = "MANAGER";
                        positionString[0] = "";
                    }
                    if (managerCheck.isSelected() && developerCheck.isSelected()) {
                        position[0] = User.Position.DEVELOPER;
                        positionString[0] = "DEVELOPER";
                        position[1] = User.Position.MANAGER;
                        positionString[1] = "MANAGER";

                    }
                    if (statusArea1.isSelected()) {
                        status = User.Status.ACTIVE;
                    }
                    if (statusArea2.isSelected()) {
                        status = User.Status.INACTIVE;
                    }

                    if (user.addUserToList(new User(nameArea.getText(), loginArea.getText(), pass.getHashedPass(new String(passArea.getPassword())), lastNameArea.getText(), slicePhoneNumber(phoneNumberArea.getText()), peselArea.getText(), position, status), main)) {
                        pass.addToHashMap(loginArea.getText(), pass.getHashedPass(new String(passArea.getPassword())), positionString);
                        main.setVisible(false);
                    }
                }else {
                    JOptionPane.showMessageDialog(main,"Należy wypełnić wszystkie pola","Ostrzeżenie",JOptionPane.INFORMATION_MESSAGE);
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

        c.gridy=4;
        jpanelLeft.add(phoneNumberLabel,c);
        c.gridwidth=2;
        jpanelRight.add(phoneNumberArea,c);

        c.gridwidth=1;
        c.gridy=5;
        jpanelLeft.add(peselLabel,c);
        c.gridwidth=2;
        jpanelRight.add(peselArea,c);

        c.gridwidth=1;
        c.gridy=6;
        jpanelLeft.add(positionLabel,c);
        jpanelRight.add(developerCheck,c);
        c.gridx=1;
        jpanelRight.add(managerCheck,c);

        c.gridx=0;
        c.gridy=7;
        jpanelLeft.add(statusLabel,c);
        c.gridy=8;
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
        main.add(jpanelMain);

        main.setVisible(true);

        return main;
        //this.setVisible(true);


    }

    public String slicePhoneNumber(String pn){
        String newPhone = "";
        newPhone+=pn.charAt(0);
        if(!pn.contains("-")) {
            for (int i = 1; i < pn.length(); i++) {
                if (i % 3 == 0) {
                    newPhone += '-';
                    newPhone += pn.charAt(i);
                }
                if (i % 3 != 0) {
                    newPhone += pn.charAt(i);
                }
            }
        }else {
            newPhone=pn;
        }
        return newPhone;
    }


   public void updateProjectsList(String selectedStatus){
        DefaultListModel model = new DefaultListModel();
        String[] projectsList = project.getProjectByStatus(selectedStatus);
        for(int i = 0; i<projectsList.length;i++){
            model.addElement(projectsList[i]);
        }
        this.projectsList.setModel(model);
    }


    public void updateCommentsTable(String projectName){
        commentsPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] commentsColumnName = {"Użytkownik","Data","Komentarz"};
        commentsPane.setModel(new DefaultTableModel(commentInProject.getCommentInProjectArray(projectName),commentsColumnName));
        commentsPane.updateUI();
    }


}
class MouseListeners extends MouseAdapter{
    @Override
    public void mouseClicked(MouseEvent e) {
        Timer.resetCounter();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Timer.resetCounter();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Timer.resetCounter();
    }
}
