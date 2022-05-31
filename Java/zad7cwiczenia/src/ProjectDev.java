import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;

public class ProjectDev extends JFrame {
    private JTextArea statusArea = new JTextArea();
    private JTextArea projectNameArea = new JTextArea();
    private String projectName;
    private JList<String> teamList = new JList<>();
    private  JTable commentsTable = new JTable(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };;

    private UserInProject userInProject;
    private CommentInProject commentInProject;
    private Project project;
    private LogInWindow l;



    ProjectDev(User user, LogInWindow l, String login, MainScreenDev mainScreen, Project project,CommentInProject commentInProject, UserInProject userInProject, SaveToFile saveToFile){

        this.userInProject=userInProject;
        this.commentInProject=commentInProject;
        this.project=project;
        this.l=l;


        this.setSize(850, 450);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile.addToFile();
            }
        });
        this.setTitle("Project Developer");

        JPanel jpanelMain = new JPanel();
        JPanel jpanelLeft = new JPanel();
        jpanelLeft.setPreferredSize(new Dimension(200,400));
        JPanel jpanelRight = new JPanel();
        jpanelRight.setPreferredSize(new Dimension(500,450));
        JPanel jpanelRightUp = new JPanel();
        jpanelRightUp.setPreferredSize(new Dimension(500,22));
        jpanelRightUp.setLayout(new GridBagLayout());
        JPanel jpanelFootMain = new JPanel();
        jpanelFootMain.setLayout(new BorderLayout());


        jpanelLeft.setBorder(BorderFactory.createEmptyBorder(20,10,20,20));
        jpanelRight.setBorder(BorderFactory.createEmptyBorder(20,0,20,30));

        jpanelMain.setLayout(new BorderLayout());
        jpanelLeft.setLayout(new BoxLayout(jpanelLeft,BoxLayout.Y_AXIS));
        jpanelRight.setLayout(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Projekty");
        JMenu menu3 = new JMenu("Konto");
        JMenuItem itemMenu2 = new JMenuItem("Lista projektow");
        itemMenu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                mainScreen.setVisible(true);
            }
        });
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

        JLabel projectsLabelMain = new JLabel("Nazwa projektu: ");
        JLabel statusLabelMain = new JLabel("Status: ");
        JLabel teamLabelMain = new JLabel("Zespół: ");
        JLabel commentsLabelMain = new JLabel("Komentarze: ");

        JButton addComment = new JButton("Dodaj komentarz");
        ProjectDev s = this;
        addComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddComment addComment = new AddComment(login, commentInProject, project.getSpecifiedProject(projectNameArea.getText()),s);
                addComment.setVisability(true);
                mainScreen.updateCommentsTable(projectNameArea.getText());
            }
        });
        JButton deleteComment = new JButton("Usuń komentarz");
        deleteComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = commentsTable.getSelectedRow();
                String selectedUser = commentsTable.getValueAt(i,0).toString();
                String selectedComment = commentsTable.getValueAt(i,2).toString();
                commentInProject.deleteComment(selectedUser,project.getSpecifiedProject(projectNameArea.getText()),selectedComment, login);
                updateCommentsTable();
                mainScreen.updateCommentsTable(projectNameArea.getText());
            }
        });
        JButton footer = new JButton(login);



        footer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setEnabled(false);
                user.setVisible(true);
            }
        });




        commentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = commentsTable.getSelectedRow();
                    int col = 2;
                    TableModel model = commentsTable.getModel();
                    String rowValue = model.getValueAt(row, col).toString();

                    JOptionPane.showMessageDialog(null,rowValue);
                }
            }
        });

        SwingUtilities.updateComponentTreeUI(commentsTable);

        projectNameArea.setEditable(false);
        statusArea.setEditable(false);

        JScrollPane teamListSP = new JScrollPane(teamList);
        teamListSP.setPreferredSize(new Dimension(200,150));
        teamListSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane commentsTableSP = new JScrollPane(commentsTable);
        commentsTableSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Insets i = new Insets(0,0,5,0);

        c.insets = i;

        jpanelLeft.add(projectsLabelMain,BorderLayout.LINE_START);

        jpanelLeft.setAlignmentX(LEFT_ALIGNMENT);
        jpanelLeft.add(projectNameArea,BorderLayout.CENTER);


        jpanelLeft.add(statusLabelMain,BorderLayout.LINE_START);
        jpanelLeft.add(statusArea,BorderLayout.CENTER);

        jpanelLeft.add(teamLabelMain,BorderLayout.LINE_START);
        jpanelLeft.add(teamListSP,BorderLayout.CENTER);


        c.gridx=0;
        c.gridy=0;
        c.weightx=2;
        jpanelRightUp.add(commentsLabelMain, c);
        c.weightx=0;
        c.gridx=2;
        jpanelRightUp.add(addComment,c);
        c.gridx=3;
        jpanelRightUp.add(deleteComment,c);
        c.gridx=4;
        //jpanelRightUp.add(timerLabel,c);


        jpanelRight.add(jpanelRightUp,BorderLayout.PAGE_START);
        jpanelRight.add(commentsTableSP, BorderLayout.CENTER);
        jpanelFootMain.add(footer, BorderLayout.EAST);
        jpanelMain.add(jpanelLeft, BorderLayout.LINE_START);
        jpanelMain.add(jpanelRight, BorderLayout.CENTER);
        jpanelMain.add(jpanelFootMain,BorderLayout.PAGE_END);

        this.setLayout(new GridLayout(1,1));
        this.add(jpanelMain);

        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);

    }

    public void setProjectName(String projectName){
        this.projectNameArea.setText(projectName);
        this.projectNameArea.setPreferredSize(new Dimension(50,10));
    }

    public void setStatusArea(String statusArea){
        this.statusArea.setText(statusArea);
        this.statusArea.setPreferredSize(new Dimension(50,10));
    }

    public void setProject(String projectName){
        this.projectName=projectName;
        updateUserList();
        System.out.println();
        updateCommentsTable();
    }

    public void updateUserList(){
        DefaultListModel model = new DefaultListModel();
        String[] usersInProject = userInProject.getUserInProjectArraySingle(projectName);
        model.addElement(usersInProject[0]);
        model.addElement(usersInProject[1]);
        teamList.setModel(model);
        teamList.setPreferredSize(new Dimension(100,200));
        teamList.updateUI();
    }

    public void updateCommentsTable(){
        commentsTable.setPreferredSize(new Dimension(500,400));
        commentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] commentsColumnName = {"Użytkownik","Data","Komentarz"};
        commentsTable.setModel(new DefaultTableModel(commentInProject.getCommentInProjectArray(projectName),commentsColumnName));
        commentsTable.updateUI();
    }

}
