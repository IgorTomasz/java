import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AddComment extends JFrame {
    private String userLogin;
    private CommentInProject commentInProject;
    private Project project;
    private ProjectDev projectDev=null;
    private ProjectManager projectManager=null;
    private JDialog dialog;


    AddComment(String userLogin, CommentInProject commentInProject, Project project, ProjectManager projectSelected){
        this.userLogin=userLogin;
        this.commentInProject=commentInProject;
        this.project=project;
        this.projectManager=projectSelected;
        GetWindow();
    }

    AddComment(String userLogin, CommentInProject commentInProject, Project project, ProjectDev projectDev){
        this.userLogin=userLogin;
        this.commentInProject=commentInProject;
        this.project=project;
        this.projectDev=projectDev;
        GetWindow();
    }

    public void GetWindow(){
        //this.setSize(850, 300);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dialog = new JDialog(this,true);
        dialog.setSize(850,300);
        dialog.setTitle("Dodaj komentarz");

        JPanel jpanelMain = new JPanel();
        //dialog.setLayout(new BoxLayout(dialog,BoxLayout.PAGE_AXIS));
        JPanel jpanelUp = new JPanel();
        jpanelUp.setPreferredSize(new Dimension(850,40));
        JPanel jpanelDown = new JPanel();
        jpanelDown.setPreferredSize(new Dimension(850,220));
        JPanel jpanelFooter = new JPanel();
        jpanelFooter.setPreferredSize(new Dimension(850,40));

        jpanelUp.setBorder(BorderFactory.createEmptyBorder(10,5,10,30));
        jpanelDown.setBorder(BorderFactory.createEmptyBorder(0,10,0,30));
        jpanelFooter.setBorder(BorderFactory.createEmptyBorder(0,10,10,30));

        jpanelMain.setLayout(new BoxLayout(jpanelMain,BoxLayout.PAGE_AXIS));
        jpanelUp.setLayout(new FlowLayout(FlowLayout.LEFT));
        jpanelDown.setLayout(new BorderLayout());
        jpanelDown.setLayout(new BorderLayout());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String date = sdf.format(timestamp);

        JLabel nameLabel = new JLabel("Użytkownik: ");
        JLabel timeStampLabel = new JLabel("Data: ");
        JLabel commentsLabel = new JLabel("Komentarz");

        JTextArea timeStampArea = new JTextArea(date);
        timeStampArea.setPreferredSize(new Dimension(100,20));
        timeStampArea.setEditable(false);
        JTextArea nameArea = new JTextArea();
        nameArea.setPreferredSize(new Dimension(100,20));
        nameArea.setEditable(false);
        nameArea.setText(userLogin);

        JTextArea commentArea = new JTextArea();
        commentArea.setWrapStyleWord(true);

        JScrollPane commentSP = new JScrollPane(commentArea);
        commentSP.setPreferredSize(new Dimension(800,250));
        commentSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        commentSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addCommentButton = new JButton("Dodaj komentarz");
        addCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!commentArea.getText().equals("")){
                    commentInProject.addComment(userLogin,project,commentArea.getText(),date);
                    if(projectDev!=null) {
                        projectDev.updateCommentsTable();
                    }else {
                        projectManager.updateCommentsTable();
                    }
                    setVisible(false);
                    dispose();
                }
            }
        });
        jpanelFooter.add(addCommentButton,BorderLayout.LINE_END);

        jpanelUp.add(nameLabel);
        jpanelUp.add(nameArea);
        jpanelUp.add(timeStampLabel);
        jpanelUp.add(timeStampArea);

        jpanelDown.add(commentsLabel, BorderLayout.PAGE_START);
        jpanelDown.add(commentSP,BorderLayout.CENTER);

        jpanelMain.add(jpanelUp);
        jpanelMain.add(jpanelDown);
        jpanelMain.add(jpanelFooter);
        //this.setLocationRelativeTo(null);
        dialog.add(jpanelMain);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        //this.add(dialog);

    }

    public void setVisability(Boolean b){
        dialog.setVisible(b);
    }

}
