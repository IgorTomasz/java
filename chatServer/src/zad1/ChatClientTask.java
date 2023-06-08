/**
 *
 *  @author Tomaszewski Igor S25077
 *
 */

package zad1;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<String> {

    private final ChatClient chatClient;

    public ChatClientTask(Callable<String> callable, ChatClient c) {
        super(callable);
        chatClient=c;
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait){
        return new ChatClientTask(()->{
                c.login(wait);
                if(wait!=0) Thread.sleep(wait);
                for (String el : msgs){
                    c.send(el);
                    if(wait!=0) Thread.sleep(wait);
                }
                c.logout();
            if(wait!=0) Thread.sleep(wait);
                return "log"; //do poprawy
        },c);
    }


    public ChatClient getClient() {
        return chatClient;
    }
}
