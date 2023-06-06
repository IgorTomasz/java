package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class CountryInfo extends JFrame{
    private JButton weatherButton;
    private JLabel nbp;
    private JLabel exchangeRate;
    private JPanel mainPanel;
    private JPanel webPanel;
    private JLabel temp;
    private JFrame mainFrame;
    private JFXPanel jfxPanel;
    private Service s;

    public CountryInfo(String weather, double rate1, double rate2,Service s){
        this.s=s;
        this.mainFrame=this;
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        jfxPanel=new JFXPanel();
        Platform.runLater(this::createJFXContent);
        webPanel.add(jfxPanel);
        this.pack();

        weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temp.setText(weather);
                exchangeRate.setText(String.valueOf(rate1));
                nbp.setText(String.valueOf(rate2));
                mainFrame.pack();
            }
        });
    }


    private void createJFXContent() {
        WebView webView = new WebView();
        webView.getEngine().load(String.format("https://en.wikipedia.org/wiki/%s",s.getCity()));
        Scene scene = new Scene(webView);
        jfxPanel.setScene(scene);
    }

}
