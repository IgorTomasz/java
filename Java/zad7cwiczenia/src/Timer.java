import javax.swing.*;

public class Timer{
    private static Thread NT;
    private static int counter = 0;

    public static synchronized void TimerThread(SaveToFile saveToFile, LogInWindow logInWindow, User user) {
        NT = new Thread(() -> {
            while (counter < 60) {
                try {
                    Thread.sleep(1000);
                } catch (Exception t) {

                }
                counter += 1;
                String time = String.valueOf(60 - counter);
                //System.out.println(counter);
                MainScreen.timerLabel.setText("Czas sesji: " + time);
                ProjectManager.timerLabel.setText("Czas sesji: " + time);
                MainScreenDev.timerLabel.setText("Czas sesji: "+time);
                ProjectDev.timerLabel.setText("Czas sesji: "+time);
            }
            saveToFile.addToFile();
            logInWindow.setVisible(true);
            logInWindow.loginText.setText("");
            logInWindow.passwordText.setText("");
/*            frame.setVisible(false);
            frame.dispose();*/
        });

        NT.start();
    }


    public static void resetCounter(){
        counter=0;
    }

    public static void stopThread(){
        NT.stop();
    }

}
