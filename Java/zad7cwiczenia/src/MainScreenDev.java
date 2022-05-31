import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;

public class MainScreenDev extends JFrame {

    private String login;
    private LogInWindow l;
    private User user;
    private Project project;
    private CommentInProject commentInProject;
    private UserInProject userInProject;
    private ProjectDev projectDev;
    protected SaveToFile saveToFile;


    private JTable commentsPane;

    MainScreenDev(String login, LogInWindow l){
        this.login=login;
        this.l=l;
        this.user = new User(login);
        this.project = new Project(user);
        this.commentInProject= new CommentInProject(user,project);
        this.userInProject = new UserInProject(user,project,commentInProject);
        this.saveToFile = new SaveToFile(commentInProject,user,project);
        this.projectDev = new ProjectDev(user,l,login,this,project,commentInProject,userInProject,saveToFile);

        MainScreenDev();
    }

    public void MainScreenDev(){


        this.setSize(850, 450);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile.addToFile();
            }
        });
        this.setTitle("Ekran główny");

        JPanel jpanelMain = new JPanel();
        jpanelMain.setLayout(new BorderLayout());
        JPanel jpanelLeftMain = new JPanel();
        JPanel jpanelRightMain = new JPanel();
        JPanel jpanelRightUpMain = new JPanel();
        JPanel jpanelRightDownMain = new JPanel();
        JPanel jpanelFootMain = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Projekty");
        JMenu menu3 = new JMenu("Konto");
        JMenuItem itemMenu2 = new JMenuItem("Lista projektow");
        JMenuItem itemMenu5 = new JMenuItem("Moje konto");
        itemMenu5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.setVisible(true);
            }
        });
        JMenuItem itemMenu6 = new JMenuItem("Wyloguj");
        itemMenu6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile.addToFile();
                l.setVisible(true);
                l.loginText.setText("");
                l.passwordText.setText("");
                setVisible(false);
                dispose();
                user.setVisible(false);
                user.dispose();
            }
        });
        menu1.add(itemMenu2);
        menu3.add(itemMenu5);
        menu3.add(itemMenu6);
        menuBar.add(menu1);
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
                user.setVisible(true);
            }
        });

        JList<String> projectsList = new JList<>(project.getProjectByStatusAndUser("AKTYWNY", login));
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

                        projectDev.setProjectName(val.toString());
                        projectDev.setStatusArea(project.getStatus(val.toString()).toString());
                    }
                    projectDev.setVisible(true);
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

                    projectDev.setProject(val.toString());
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


        projectsSP.setViewportView(projectsList);

        projectsList.setLayoutOrientation(JList.VERTICAL);

        jpanelLeftMain.add(projectsLabelPane, BorderLayout.PAGE_START);
        jpanelLeftMain.add(projectsSP, BorderLayout.CENTER);


//        teamSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        commentsSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel jpanelRightUpLabel = new JPanel();
        jpanelRightUpLabel.setLayout(new BorderLayout());

        jpanelRightUpLabel.add(teamLabelMain, BorderLayout.LINE_START);
        //jpanelRightUpLabel.add(timerLabel,BorderLayout.LINE_END);

        jpanelRightUpMain.add(jpanelRightUpLabel, BorderLayout.PAGE_START);
        jpanelRightUpMain.add(teamSP, BorderLayout.CENTER);


        jpanelRightDownMain.add(commentsLabelMain, BorderLayout.PAGE_START);
        jpanelRightDownMain.add(commentsSP, BorderLayout.CENTER);

        jpanelRightMain.add(jpanelRightUpMain);
        jpanelRightMain.add(jpanelRightDownMain);

        jpanelFootMain.add(footer, BorderLayout.EAST);

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

    public void updateCommentsTable(String projectName){
        commentsPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] commentsColumnName = {"Użytkownik","Data","Komentarz"};
        commentsPane.setModel(new DefaultTableModel(commentInProject.getCommentInProjectArray(projectName),commentsColumnName));
    }


}
