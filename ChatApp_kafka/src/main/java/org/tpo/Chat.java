package org.tpo;

import org.apache.kafka.clients.producer.ProducerRecord;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executors;

public class Chat extends JFrame{
    private JPanel mainPanel;
    private JTextPane chatView;
    private JButton loginButton;
    private JTextField chatField;
    private JTextField userNameField;
    private JTextField message;
    private JButton sendButt;
    public JTextArea activeUsers;
    private JScrollPane scrollPane;

    private boolean isRunning = true;

    private String topic="";
    private String user ="";

    private boolean isCreatedEarlier = false;

    private int idUser;

    private MessageReceiver messageReceiver;
    public Chat(String id) {
        chatView.setEditable(false);
        activeUsers.setEditable(false);

        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(400,400));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setVisible(true);
        this.setTitle(id);
        this.pack();


        sendButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageSender.send(new ProducerRecord<>(topic,user+": "+message.getText()));
                message.setText("");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitle(userNameField.getText());
                if(loginButton.getText().equals("Login")){
                    if (!user.equals(userNameField.getText())){
                        isCreatedEarlier=false;
                    }
                    user=userNameField.getText();
                    topic=chatField.getText();
                    if (!isCreatedEarlier){
                        messageReceiver = new MessageReceiver(topic,user);
                        MessageSender.send(new ProducerRecord<>(topic,user+" dołączył do czatu"));
                        if(Main.activeUsers.keySet().isEmpty()){
                            idUser=1;
                            Main.activeUsers.put(idUser,user);
                        }else{
                            Set<Integer> keys = Main.activeUsers.keySet();
                            Object[] arr = Arrays.stream(keys.toArray()).sorted().toArray();
                            idUser=(int)arr[0]+1;
                            Main.activeUsers.put(idUser,user);
                        }
                    }
                    else{
                        if(Main.activeUsers.keySet().isEmpty()) {
                            idUser = 1;
                            Main.activeUsers.put(idUser, user);
                        }else {
                            Set<Integer> keys = Main.activeUsers.keySet();
                            Object[] arr = Arrays.stream(keys.toArray()).sorted().toArray();
                            idUser = (int) arr[0] + 1;
                            Main.activeUsers.put(idUser, user);
                        }
                    }
                    loginButton.setText("Logout");

                    activeUsers.setText("");
                    for(String el : Main.activeUsers.values()){
                        activeUsers.append(el+System.lineSeparator());
                    }
                    isCreatedEarlier=true;
                    isRunning=true;
                    runExec();
                    checkActive();
                }else {
                    loginButton.setText("Login");
                    chatView.setText("");
                    isRunning=false;
                    Main.activeUsers.remove(idUser);
                    activeUsers.setText("");
                }

            }
        });

        message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();
                MessageSender.send(new ProducerRecord<>(topic, now.getHour()+":"+now.getMinute()+" - "+ user+": "+message.getText()));
                message.setText("");
            }
        });
    }

    public void runExec(){
        StyledDocument doc = chatView.getStyledDocument();
        Style nick = chatView.addStyle("ColoredName",null);
        Style other = chatView.addStyle("ColoredOther",null);
        Style text = chatView.addStyle("ColoredText",null);
        StyleConstants.setForeground(nick, Color.blue);
        StyleConstants.setForeground(text, Color.black);
        StyleConstants.setForeground(other, Color.orange);
        Executors.newSingleThreadExecutor().submit(()->{
            while (isRunning){
                messageReceiver.kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach(m->{
                    //chatView.append(m.value()+System.lineSeparator());
                    if (m.value().contains("dołączył do czatu")){
                        try {
                            doc.insertString(doc.getLength(), m.value()+System.lineSeparator(), text);
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        String[] splitted = m.value().split("-");
                        String[] splitted2 = splitted[1].split(":");
                        try {
                            doc.insertString(doc.getLength(), splitted[0].trim() + " - ", text);
                            if(splitted2[0].trim().equals(user)){
                                doc.insertString(doc.getLength(), splitted2[0].trim(), nick);
                            }else {
                                doc.insertString(doc.getLength(), splitted2[0].trim(), other);
                            }
                            doc.insertString(doc.getLength(), " - " + splitted2[1].trim() + "\n", text);
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                        //chatView.setText(chatView.getText()+m.value()+"\n");
                    }
                });
            }
        });
    }

    public void checkActive(){
        Executors.newSingleThreadExecutor().submit(()->{
            int userCount=0;
            while (isRunning){
                if (Main.activeUsers.keySet().size()!=userCount){
                    userCount=Main.activeUsers.keySet().size();
                    activeUsers.setText("");
                    for(String el : Main.activeUsers.values()){
                        activeUsers.append(el+System.lineSeparator());
                    }
                }
            }
        });
    }
}
