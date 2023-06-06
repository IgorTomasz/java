import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private statusEnum status;
    private String date;
    private String[] usersArray;
    private User createdBy;
    private List<Project> projectList = new ArrayList<>();
    private User user;

    enum statusEnum{
        ZAPLANOWANY,AKTYWNY,ZAKONCZONY
    }
    Project(String name, statusEnum status, String date, User createdBy, String[] usersArray){
        this.name=name;
        this.status=status;
        this.date=date;
        this.createdBy=createdBy;
        this.usersArray=usersArray;
    }
    Project(User user){
        this.user=user;
        getProjects();
    }

    public void getProjects(){
        BufferedReader br = null;
        try{
            File file = new File(".\\src\\data.txt");
            br = new BufferedReader(new FileReader(file));
            String line = null;

            while((line = br.readLine())!=null){
                String[] parts = line.split("#");

                String type = parts[0].trim();

                if(type.equals("PROJEKT")){
                String projectName = parts[1].trim();
                statusEnum projectStatus = statusEnum.valueOf(parts[2].trim());
                String projectDate = parts[3].trim();
                User projectCreatedBy = findCreator(parts[4].trim());
                String usersToArray = parts[5].trim();
                String[] usersInArray = usersToArray.split("%");
                String[] userArray = new String[2];
                for(int i =0; i<usersInArray.length; i++){
                    userArray[i]=usersInArray[i].split("\\.")[0];
                }

                projectList.add(new Project(projectName,projectStatus,projectDate,projectCreatedBy,userArray));

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
    public User findCreator(String CreatorsLogin){
        User u1 = null;
        for(User el: user.getList()){
            if(el.getLogin().equals(CreatorsLogin)){
                u1=el;
            }
        }
        return u1;
    }
    public String[] getProjectsInArray(){
        String[] projectsArray = new String[projectList.size()];
        int i =0;
        for(Project el: projectList){
            projectsArray[i]=el.name;
            i++;
        }
        return projectsArray;
    }

    public List<Project> getProjectList(){
        return projectList;
    }

    public statusEnum getStatus(String projectName){
        statusEnum statusEnum= null;
        for(Project el:projectList){
            if(el.name.equals(projectName)){
                statusEnum=el.status;
            }
        }
        return statusEnum;
    }

    public String getStatus(){
        return status.toString();
    }


    public String[] getUsersArray(){
        return usersArray;
    }

    public String[][] getProjectsInArrayMulti(String login){
        int counter=0;
        int x=0;
        for(Project el : projectList){
            if(el.usersArray[x]!=null) {
                for (int k = 0; k < el.usersArray.length; k++) {
                    if (el.usersArray[k] != null) {
                        if (el.usersArray[k].equals(login)) {
                            counter++;
                        }
                    }
                }
                x++;
                }
            x=0;
        }

        String[][] projectsArray = new String[counter][2];
        int i =0;
        int j=0;
        for(Project el: projectList){
            for(int k = 0 ; k<el.usersArray.length; k++) {
                if (el.usersArray[k] != null)
                    if (el.usersArray[k].equals(login)) {
                        projectsArray[i][j] = el.name;
                        j++;
                        projectsArray[i][j] = el.status.toString();
                        i++;
                        j = 0;
                    }
                }
            }

        return projectsArray;
    }

    public Project getSpecifiedProject(String name){
        Project p1 = null;
        for(Project el : projectList){
            if(el.name.equals(name)){
                p1=el;
            }
        }
        return p1;

    }

    public void setUsersArrayDelete(String userName){
        for(int i = 0; i<usersArray.length;i++){
            if(usersArray[i].equals(userName)){
                usersArray[i]=null;
            }
        }

    }

    public void setUsersArrayAdd(String userName){
        for(int i = 0; i<usersArray.length;i++){
            if(usersArray[i]==null){
                usersArray[i]=userName;
            }
        }

    }

    public String getName(){
        return this.name;
    }

    public String[] getUsersOfProject(String project){
        int counter=0;
        for(Project el : projectList) {
            if (el.name.equals(project)) {
                counter+=el.usersArray.length;
            }
        }

        String[] projectUsers = new String[counter];
        for(Project el: projectList){
            if(el.name.equals(project)){
                for(int i = 0; i<el.usersArray.length;i++){
                    projectUsers[i]=usersArray[i];
                }
            }
        }
        return projectUsers;
    }

    public String getDate(){
        return date;
    }
    public String getOwner(){
        return createdBy.getLogin();
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                "userInProj1='" + usersArray[0] + '\'' +
                "userInProj2='" + usersArray[1] + '\'' +
                '}';
    }

    public String[] getProjectByStatus(String status){
        String[] projectArrayByStatus = null;
        if(status.equals("Wszystkie")){

            int counter = 0;
            for (Project el : projectList) {
                counter++;

            }
            projectArrayByStatus = new String[counter];
            int i = 0;
            for (Project el : projectList) {
                    projectArrayByStatus[i] = el.name;
                    i++;

            }
            return projectArrayByStatus;

        }else {
            statusEnum projectStatus = statusEnum.valueOf(status);
            int counter = 0;
            for (Project el : projectList) {
                if (el.status.equals(projectStatus)) {
                    counter++;
                }
            }
            projectArrayByStatus = new String[counter];

            int i = 0;
            for (Project el : projectList) {
                if (el.status.equals(projectStatus)) {
                    projectArrayByStatus[i] = el.name;
                    i++;
                }
            }
            return projectArrayByStatus;
        }

    }

    public String[] getProjectByStatusAndUser(String login){
        String[] projectArrayByStatus = null;
        //statusEnum projectStatus = statusEnum.valueOf(status);
        //if(status.equals("AKTYWNY")){
            int counter = 0;
            for (Project el : projectList) {
                //if (el.status.equals(projectStatus)) {
                    for(int i = 0; i<el.usersArray.length;i++){
                        if(el.usersArray[i]!=null) {
                            if (el.usersArray[i].equals(login)) {
                                counter++;
                            }
                        }
                    }

                //}
            }
            projectArrayByStatus = new String[counter];

            int i = 0;
            for (Project el : projectList) {
               // if (el.status.equals(projectStatus)) {
                    for(int j = 0; j<el.usersArray.length;j++){
                        if(el.usersArray[j]!=null) {
                            if (el.usersArray[j].equals(login)) {
                                projectArrayByStatus[i] = el.name;
                                i++;
                            }
                        }
                    }

               // }
            }

        //}
        return projectArrayByStatus;
    }

    public String getUserArrayClear(UserInProject userInProject){
        String positionString="";
        for(int i =0; i<usersArray.length;i++){
            if(usersArray[i]!=null) {
                if(i==1){
                    positionString += "%"+usersArray[i]+"."+userInProject.getUserInProject(usersArray[1],this).getStatus();
                }else {
                    positionString += usersArray[i]+"."+userInProject.getUserInProject(usersArray[0],this).getStatus();
                }
            }
        }
        return positionString;
    }

    public boolean isUserInProject(String userLogin,Project project){
        boolean isIn = false;
        for(Project el : projectList){
            if(el==project) {
                for (int i = 0; i < el.usersArray.length; i++) {
                    if (el.usersArray[i] != null) {
                        if (el.usersArray[i].equals(userLogin)) {
                            isIn = true;
                        }
                    }
                }
            }

        }
        return isIn;
    }

    public void addProjectToList(Project project){
        boolean isNew = false;
        for(Project el:projectList){
            if(!el.name.equals(project.name)){
                isNew=true;
            }
        }
        if(isNew){
            projectList.add(project);
        }else {
            JOptionPane.showMessageDialog(null, "Istnieje już projekt o takiej nazwie!", "Ostrzeżenie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setStatus(String status){
        statusEnum statusE = statusEnum.valueOf(status);
        this.status=statusE;
    }
}
