import java.io.*;

public class SaveToFile {
    private CommentInProject commentInProject;
    private User user;
    private Project project;
    private UserInProject userInProject;

    SaveToFile(CommentInProject commentInProject, User user, Project project, UserInProject userInProject){
        this.commentInProject=commentInProject;
        this.user=user;
        this.project=project;
        this.userInProject=userInProject;
    }

    public void addToFile() {
        try {
            File fileNew = new File(".\\src\\data_n.txt");
            FileWriter fw = new FileWriter(fileNew);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            try{
                String allComents = "";
                String projectS = "";
                for (Project el : project.getProjectList()) {
                    projectS = "PROJEKT#" + el.getName() + "#" + el.getStatus() + "#" + el.getDate() + "#" + el.getOwner() + "#" + el.getUserArrayClear(userInProject)+"#";
                    pw.write(projectS);
                    for (int i = 0; i < commentInProject.getCommentInProjectArrayToFile(el.getName()).length; i++) {
                        for (int j = 0; j < commentInProject.getCommentInProjectArrayToFile(el.getName())[i].length; j++) {
                            allComents += commentInProject.getCommentInProjectArrayToFile(el.getName())[i][j];
                        }
                    }
                    pw.write(allComents);
                    allComents="";
                    pw.println();
                }


            }catch (Exception e){

            }

                try {

                    for (User el : user.getList()) {
                        String newData = "PRACOWNIK#" + el.getLogin() + "#" + el.getPass() + "#" + el.getName() + "#" + el.getLastname() + "#"+el.getPhoneNumber()+"#"+ el.getPesel() + "#" + el.getStatus() + el.getPositionClear();
                        pw.write(newData);
                        pw.println();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                pw.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            deleteFile();
            renameFile();
        }

    public void deleteFile() {
        File oldFile = new File(".\\src\\data.txt");
        try {
            FileWriter fw = new FileWriter(oldFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.flush();
            pw.close();
            oldFile.delete();
        }catch (IOException e){

        }
/*        if(oldFile.delete()){
            System.out.println("Usunieto poprawnie plik "+oldFile.getName());
        }else {
            System.out.println("Nie udalo sie usunac pliku");
        }*/
    }

    public void renameFile(){
        File oldFile = new File(".\\src\\data.txt");
        File newFile = new File(".\\src\\data_n.txt");
        boolean success = newFile.renameTo(oldFile);
/*        if(success){
            System.out.println("Udalo się zmienic nazwe");
        }else {
            System.out.println("Blad podczas zmiany nazwy");
        }*/
    }
}
