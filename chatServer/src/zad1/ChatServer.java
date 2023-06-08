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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatServer implements Runnable{
    private final String host;
    private final int port;
    private Selector selector = null;
    private boolean serverIsRunning = true;
    private List<String> logs = new ArrayList<>();
    private ServerSocketChannel serverChannel = null;
    private Lock pendingRequestLock = new ReentrantLock();
    private Map<SocketChannel, String> logMap = new HashMap<>();
    private Map<SocketChannel, String> clientMap = new HashMap<>();
    private ExecutorService exec = Executors.newSingleThreadExecutor();

    public ChatServer(String host, int port){
        this.host=host;
        this.port=port;

        try {

            InetSocketAddress isa = new InetSocketAddress(host,port);
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(isa);
            serverChannel.configureBlocking(false);

            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(){
        exec.submit(this);
        System.out.println("Server started");
    }

    public void stopServer(){
        for(SocketChannel sc : clientMap.keySet()){
            if (sc.isOpen()){
                try {
                    sc.close();
                    sc.socket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        if (exec!=null){
            pendingRequestLock.lock();
            try {
                exec.shutdown();
            }finally {
                pendingRequestLock.unlock();
            }
        }
        try {
            selector.close();
            serverChannel.close();
            serverChannel.socket().close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        System.out.println("Server stopped");
    }

    public String getServerLog(){
        StringBuilder response = new StringBuilder();
        for(String el : logs){
            response.append(el).append("\n");
        }
        return response.toString();
    }

    @Override
    public void run() {
        while (serverIsRunning){
            try{
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();

                    if(key.isAcceptable()){
                        SocketChannel cc = serverChannel.accept();
                        cc.configureBlocking(false);
                        cc.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    }
                    if (key.isReadable()){
                        SocketChannel cc = (SocketChannel) key.channel();

                        try{
                            pendingRequestLock.lock();
                            serviceRequest(cc);
                        }catch (Exception exc){
                            exc.printStackTrace();
                        }finally {
                            pendingRequestLock.unlock();
                        }
                    }

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static Charset charset = StandardCharsets.UTF_8;
    private static final int BSIZE = 1024;
    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);
    private StringBuilder requestString = new StringBuilder();
    private CharBuffer charBuffer;

    private void serviceRequest(SocketChannel sc) throws IOException {
        if (!sc.isOpen()) return; // jeżeli kanał zamknięty - nie ma nic do roboty
        // Odczytanie zlecenia
        requestString.setLength(0);
        bbuf.clear();

        int inputSize= sc.read(bbuf);
        if (inputSize<=0) return;

        bbuf.flip();
        charBuffer=charset.decode(bbuf);
        String[] inputRequest = charBuffer.toString().split("#");

        for(String el : inputRequest){
            if (el.contains("login")){
                String[] nameSplitted = el.split(" ");
                clientMap.put(sc,nameSplitted[0]);
                logs.add(LocalTime.now() + " "+clientMap.get(sc) + " logged in");
                writeResp(nameSplitted[0]+" logged in");
            }
            else if(el.contains("logged out")){
                logs.add(LocalTime.now() + " "+el);
                writeResp(el);

                if (sc.isOpen()){
                    sc.close();
                    sc.socket().close();
                }
                clientMap.remove(sc);
            }else {
                logs.add(LocalTime.now() + " "+clientMap.get(sc)+": " + el);
                writeResp(clientMap.get(sc)+": "+el);
            }

        }

    }

    private StringBuffer remsg = new StringBuffer();
    private void writeResp(String message) throws IOException {
        remsg.setLength(0);
        remsg.append(message);
        remsg.append("\n");
        ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
        for (SocketChannel channel : clientMap.keySet()){
            if(channel.isOpen()){
                channel.write(buf);
                buf.rewind();
            }
        }
    }
}
