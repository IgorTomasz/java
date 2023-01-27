import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserInProject {
    protected User user;
    protected Project project;
    private User addedBy;
    private int status;
    private String startWorkDay;
    private String endWorkDate;
    private User userClass;
    private Project projectClass;
    private  CommentInProject commentInProject;
    protected List<UserInProject> userInProjectList = new ArrayList<>();

    UserInProject(User user, Project project, User addedBy, int status){
        this.user=user;
        this.project=project;
        this.addedBy=addedBy;
        this.status=status;
    }
    UserInProject(User userClass, Project projectClass,CommentInProject commentInProject){
        this.userClass=userClass;
        this.projectClass=projectClass;
        this.commentInProject=commentInProject;
        getUserInProject();

    }
    public void getUserInProject(){
        BufferedReader br = null;
        try{
            File file = new File(".\\src\\data.txt");
            br = new BufferedReader(new FileReader(file));
            String line1 = null;

            while((line1 = br.readLine())!=null){
                String[] parts1 = line1.split("#");

                String type = parts1[0].trim();

                if(type.equals("PROJEKT")){
                    String projectName = parts1[1].trim();
                    String addedBy = parts1[4].trim();

                    try {
                    String allUsers = parts1[5].trim();

                            String[] parts2 = allUsers.split("%");
                            String[] user1AndStatus = parts2[0].split("\\.");
                            String user1 = user1AndStatus[0].trim();
                            int user1Status = Integer.parseInt(user1AndStatus[1].trim());
                            String user2=null;
                            int user2Status=0;
                            try {
                                String[] user2AndStatus = parts2[1].split("\\.");
                                user2= user2AndStatus[0].trim();
                                user2Status=Integer.parseInt(user2AndStatus[1].trim());
                            }catch (Exception e){

                            }



                        this.userInProjectList.add(new UserInProject(userClass.getSpecifiedUser(user1),projectClass.getSpecifiedProject(projectName),userClass.getSpecifiedUser(addedBy),user1Status));
                            if(user2!=null) {
                                this.userInProjectList.add(new UserInProject(userClass.getSpecifiedUser(user2), projectClass.getSpecifiedProject(projectName), userClass.getSpecifiedUser(addedBy),user2Status));
                            }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


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

    public String[][] getUserInProjectArray(String projectName){
        String[][] UserInProject = new String[2][4];
        int i = 0;
        int j = 0;
        for(UserInProject el:userInProjectList){
            if(el.project.getName().equals(projectName)){
                UserInProject[j][i] = el.user.getName();
                i++;
                UserInProject[j][i] = el.user.getLastname();
                i++;
                UserInProject[j][i] = el.user.getPosition();
                i++;
                UserInProject[j][i] = el.user.getStatus();
                j++;
            }
            i=0;
        }

        return UserInProject;
    }
    public String[] getUserInProjectArraySingle(String projectName){
        String[] UserInProject = new String[2];
        int j = 0;
        for(UserInProject el:userInProjectList){
            if(el.project.getName().equals(projectName)) {
                    UserInProject[j] = el.user.getLogin()+ " - " +el.user.getName() + " - " + el.user.getLastname();
                    j++;
            }

        }

        return UserInProject;
    }

    public void deleteUserInProject(String userName, String projectName){
        UserInProject s = null;

        for(UserInProject el:userInProjectList){
            if(!userName.equals(el.addedBy.getLogin())) {
                if (el.project.getName().equals(projectName)) {
                    if (el.user.getLogin().equals(userName)) {
                        s = el;
                    }
                }
            }
        }
        if(s!=null) {
            userInProjectList.remove(s);
            //commentInProject.deleteComment(s.user.getLogin(),s.project); usuwanie komentarza po usunieciu uzytkownika
            s.project.setUsersArrayDelete(userName);
        }else {
            JOptionPane.showMessageDialog(null, "Nie można usunąć twórcy projektu.", "Ostrzeżenie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void addUserToList(User user, Project project, User addedBy){
        userInProjectList.add(new UserInProject(user, project, addedBy,0));

    }

    public UserInProject getUserInProject(String login, Project project){
        UserInProject uip = null;
        for(UserInProject el : userInProjectList){
            if(el.project==project){
                if (el.user.getLogin().equals(login)){
                    uip=el;
                }
            }
        }
        return uip;
    }

    public void setStatus(int status){
        this.status=status;
    }

    public int getStatus(){
        return status;
    }

    public void addUserInProject(String userName, String projectName){
        boolean s = false;
        User ab = null;
        for(UserInProject el:userInProjectList){
            if(el.project.getName().equals(projectName)){
                for(int i = 0; i<el.project.getUsersArray().length;i++) {
                    if (el.project.getUsersArray()[i] == null) {
                        el.project.setUsersArrayAdd(userName);
                        ab=el.addedBy;
                        s=true;
                    }
                }
            }
        }
        if(s) {
            userInProjectList.add(new UserInProject(userClass.getSpecifiedUser(userName), projectClass.getSpecifiedProject(projectName), ab,0));
        }else{
            JOptionPane.showMessageDialog(null, "Nie znaleziono użytkownika lub lista użytkowników jest pełna", "Ostrzeżenie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return "UserInProject{" +
                "user=" + user +
                ", project=" + project.toString() +
                ", addedBy=" + addedBy.toString() +
                ", startWorkDay='" + startWorkDay + '\'' +
                ", endWorkDate='" + endWorkDate + '\'' +
                '}';
    }
}
