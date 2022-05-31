/*
public class Timer extends Thread{
private int counter = 0;
    Timer(SaveToFile saveToFile, LogInWindow logInWindow, MainScreenDev mainScreenDev, User user){
        while(counter<60000){
            try{
                Thread.sleep(1000);
            }catch (Exception t){

            }
            counter+=1000;
            String time = String.valueOf(60-counter/1000);
            mainScreenDev.timerLabel.setText("Czas sesji: "+time);
            System.out.println(counter);
        }
        saveToFile.renameFile();
        logInWindow.setVisible(true);
        logInWindow.loginText.setText("");
        logInWindow.passwordText.setText("");
        mainScreenDev.setVisible(false);
        mainScreenDev.dispose();
        user.setVisible(false);
        user.dispose();
    }
    Timer(SaveToFile saveToFile, LogInWindow logInWindow, MainScreen mainScreen, User user){
        while(counter<60000){
            try{
                Thread.sleep(1000);
            }catch (Exception t){

            }
            counter+=1000;
            String time = String.valueOf(60-counter/1000);
            mainScreen.timerLabel.setText("Czas sesji: "+time);
            System.out.println(counter);
        }
        saveToFile.renameFile();
        logInWindow.setVisible(true);
        logInWindow.loginText.setText("");
        logInWindow.passwordText.setText("");
        mainScreen.setVisible(false);
        mainScreen.dispose();
        user.setVisible(false);
        user.dispose();
    }

    Timer(SaveToFile saveToFile, LogInWindow logInWindow, ProjectDev projectDev, User user){
        while(counter<60000){
            try{
                Thread.sleep(1000);
            }catch (Exception t){

            }
            counter+=1000;
            String time = String.valueOf(60-counter/1000);
            projectDev.timerLabel.setText("Czas sesji: "+time);
            System.out.println(counter);
        }
        saveToFile.renameFile();
        logInWindow.setVisible(true);
        logInWindow.loginText.setText("");
        logInWindow.passwordText.setText("");
        projectDev.setVisible(false);
        projectDev.dispose();
        user.setVisible(false);
        user.dispose();
    }

    Timer(SaveToFile saveToFile, LogInWindow logInWindow, ProjectManager projectManager, User user){
        while(counter<60000){
            try{
                Thread.sleep(1000);
            }catch (Exception t){

            }
            counter+=1000;
            String time = String.valueOf(60-counter/1000);
            projectManager.timerLabel.setText("Czas sesji: "+time);
            System.out.println(counter);
        }
        saveToFile.renameFile();
        logInWindow.setVisible(true);
        logInWindow.loginText.setText("");
        logInWindow.passwordText.setText("");
        projectManager.setVisible(false);
        projectManager.dispose();
        user.setVisible(false);
        user.dispose();
    }

    public void setCounter(int i){
        this.counter=i;
    }
}
*/
