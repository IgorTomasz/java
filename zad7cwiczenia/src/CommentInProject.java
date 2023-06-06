import javax.swing.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CommentInProject {
    private User user;
    private Project project;
    private String timeStamp;
    private String comment;
    private User userClass;
    private Project projectClass;
    private List<CommentInProject> commentInProjectList = new ArrayList<>();

    CommentInProject(User user, Project project, String comment, String timeStamp){
        this.user=user;
        this.project=project;
        this.comment=comment;
        this.timeStamp=timeStamp;

    }
    CommentInProject(User user, Project project){
        this.userClass=user;
        this.projectClass=project;
        getCommentsInProject();
    }

    public void getCommentsInProject(){
        BufferedReader br = null;
        try{
            File file = new File(".\\src\\data.txt");
            br = new BufferedReader(new FileReader(file));
            String line1 = null;

            while((line1 = br.readLine())!=null){
                String[] parts1 = line1.split("#");

                String type = parts1[0].trim();
                try{
                if(type.equals("PROJEKT")){
                String allComments=null;
                    try {
                        String projectName = parts1[1].trim();
                        try {
                            allComments = parts1[6].trim();
                        }catch (Exception e){}
                            if(allComments!=null) {
                                String[] parts2 = allComments.split("&");

                                for (int i = 0; i < parts2.length; i++) {
                                    String[] parts3 = parts2[i].split("%");
                                    String userLogin = parts3[0].trim();
                                    String userComment = parts3[1].trim();
                                    String userTimeStamp = parts3[2].trim();
                                    commentInProjectList.add(new CommentInProject(userClass.getSpecifiedUser(userLogin), projectClass.getSpecifiedProject(projectName), userComment, userTimeStamp));
                                }
                            }


                   }catch (Exception e){e.printStackTrace();}
                    }}catch (Exception e){

                    }


                }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Nie znaleziono pliku z danymi","Ostrzeżenie",JOptionPane.INFORMATION_MESSAGE);
        }finally {
            if(br != null){
                try{
                    br.close();
                }catch (Exception e){
                }
            }
        }
    }



    public String[][] getCommentInProjectArray(String projectName){
        int counter =0;
        for(CommentInProject el : commentInProjectList){
            if(el.project.getName().equals(projectName)){
                counter++;
            }
        }


        String[][] CommentInProject = new String[counter][3];
        int i = 0;
        int j = 0;
        for(CommentInProject el:commentInProjectList){
            if(el.project.getName().equals(projectName)){
                CommentInProject[j][i] = el.user.getLogin();
                i++;
                CommentInProject[j][i] = el.timeStamp;
                i++;
                CommentInProject[j][i] = el.comment;
                j++;
            }
            i=0;
        }

        return CommentInProject;
    }

    public String[][] getCommentInProjectArrayToFile(String projectName){
        int counter =0;
        for(CommentInProject el : commentInProjectList){
            if(el.project.getName().equals(projectName)){
                counter++;
            }
        }

        String[][] CommentInProject = new String[counter][3];
        int i = 0;
        int j = 0;
        for(CommentInProject el:commentInProjectList){
            if(el.project.getName().equals(projectName)){
                CommentInProject[j][i] = el.user.getLogin()+"%";
                i++;
                CommentInProject[j][i] = el.comment+"%";
                i++;
                if(j==counter-1) {
                    CommentInProject[j][i] = el.timeStamp;
                }else {
                    CommentInProject[j][i] = el.timeStamp+"&";
                    j++;
                }
            }
            i=0;
        }

        return CommentInProject;
    }

    public void deleteComment(String userLogin, Project project, String comment, String loggedUser){
        CommentInProject c = null;
        int errorIndicator = 0;
        for(CommentInProject el : commentInProjectList){
            if(el.project==project){
                if(el.user.getLogin().equals(userLogin)){
                    if(el.comment.equals(comment)){
                        if(el.user.getLogin().equals(loggedUser)) {
                            c = el;
                        }else {
                            errorIndicator=1;
                        }
                    }
                }
            }
        }
        if(c!=null){
            commentInProjectList.remove(c);
        }
        if(c==null&&errorIndicator==1){
            JOptionPane.showMessageDialog(null, "Nie można usunąć czyjegoś komentarza","Ostrzeżenie",JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void addComment(String userLogin, Project project, String comment, String date){
        if(projectClass.isUserInProject(userLogin,project)) {
            commentInProjectList.add(new CommentInProject(userClass.getSpecifiedUser(userLogin), project, comment, date));
        }
    }
}
