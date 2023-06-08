/**
 *
 *  @author Tomaszewski Igor S25077
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatClient {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel socketChannel;
    private List<String> chatHistory = new ArrayList<>();

    public ChatClient(String host, int port, String id){
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void login(int wait){
        chatHistory.add("=== "+id+" chat view\n");
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host,port));
            socketChannel.configureBlocking(false);

            int waitTime = 5;
            while(!socketChannel.finishConnect()){
                Thread.sleep(60);
                waitTime--;
                if (waitTime<=0){
                    throw new Exception(host+" timeout");
                }

            }
            send(id+" login#");
            readingThread(wait);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Thread readThread;
    private Lock readingLock = new ReentrantLock();
    private ByteBuffer readBuf = ByteBuffer.allocate(20000);
    Charset charset = Charset.forName("UTF-8");

    public void readingThread(int wait){
        readThread = new Thread(()->{
            while (true){
                readingLock.lock();
                try {
                    if(!socketChannel.isOpen()) break;
                    readBuf.clear();
                    if (Thread.interrupted() && !socketChannel.isOpen()) return;
                    int n = socketChannel.read(readBuf);
                    if (n<0) break;
                    int i = 0;
                    while (n==0){
                        Thread.sleep(wait);
                        if (Thread.interrupted() && !socketChannel.isOpen()) return;
                        n = socketChannel.read(readBuf);
                        i++;
                        if (i>=10) break;
                    }
                    readBuf.flip();
                    CharBuffer cb = charset.decode(readBuf);
                    String resp = cb.toString();
                    chatHistory.add(resp);
                }catch (InterruptedException exc){
                    return;
                }catch (Exception exc){
                    try {
                        if (socketChannel != null && socketChannel.isOpen()){
                            socketChannel.close();
                            socketChannel.socket().close();
                        }
                    }catch (IOException exc1){
                        exc1.printStackTrace();
                    }
                }finally {
                    readingLock.unlock();
                }
            }
        });
        readThread.start();
    }

    public void logout(){
        send(id+" logged out#");
        readingLock.lock();
        try {
            readThread.interrupt();
        } finally {
            readingLock.unlock();
        }
    }

    public void send(String req){
        ByteBuffer buffer = charset.encode(CharBuffer.wrap(req+"#"));
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getChatView(){
        String result = "";
        for (String el : chatHistory){
            result+=el;
        }
        return result;
    }
}
