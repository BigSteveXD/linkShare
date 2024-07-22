import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class linkShareReceive{
    static int port = 7777;
    static myFrame frame;
    static Thread thread;
    //static ServerSocket ss;
    public static void main(String[] args){
        frame = new myFrame();
        frame.createFrame();
    }
    static Thread getThread(){
        return thread;
    }
    static int getPort(){
        return port;
    }
    static myFrame getFrame(){
        return frame;
    }
    void setPort(int myPort){
        port = myPort;
    }
    static class ServerTask implements Runnable{
        int port;
        myFrame frame;
        ServerTask(int port, myFrame frame){
            this.port = port;
            this.frame = frame;
        }
        @Override
        public void run(){
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try{//ServerSocket ss = new ServerSocket(port)
                while(true){
                    Socket soc = ss.accept();
                    InputStreamReader isr = new InputStreamReader(soc.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    new Thread(new ClientHandler(soc, br, frame)).start();
                }
            }catch (Exception e){
                System.out.println(e);
            }finally {
                try {
                    ss.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    static class ClientHandler implements Runnable{
        Socket soc;
        BufferedReader br;
        myFrame frame;
        public ClientHandler(Socket soc, BufferedReader br, myFrame frame){
            this.soc = soc;
            this.br = br;
            this.frame = frame;
        }
        @Override
        public void run(){
            try{
                String message;
                while((message = br.readLine()) != null){
                    frame.updateFrame(message);
                }
            }catch(Exception e){
                System.out.println(e);
            }finally{
                try{
                    soc.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }
    static class myFrame{
        JFrame frame;
        JPanel topPanel;
        JPanel botPanel;
        JScrollPane scrollPane;
        JTextArea textArea;
        public void createFrame(){
            frame = new JFrame();
            topPanel = new JPanel(new GridLayout(2,1,10,0));
            botPanel = new JPanel(new GridLayout(1,1));
            textArea = new JTextArea();//"Receive Text",8,50
            scrollPane = new JScrollPane(textArea);
            JButton setPortButton = new JButton("Set Port");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("LinkShareReceive");
            frame.setSize(500,350);

            topPanel.add(new JLabel("Port:"));
            JTextField portTextField = new JTextField("7777");
            topPanel.add(portTextField);
            setPortButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("ran");
                    port = Integer.parseInt(portTextField.getText());//setPort(stufff)
                    //if thread previously started, halt it, and start new one

                    if(getThread()==null){//thread.isAlive()==true
                        System.out.println("port started");
                        //port = Integer.parseInt(portTextField.getText());
                        thread = new Thread(new ServerTask(getPort(),getFrame()));
                        thread.start();
                    }else{
                        System.out.println("restarting...");
                        thread.interrupt();
                        //port = Integer.parseInt(portTextField.getText());
                        thread = new Thread(new ServerTask(getPort(),getFrame()));
                        thread.start();
                    }
                    System.out.println(getPort());
                }
            });
            topPanel.add(setPortButton);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setAutoscrolls(true);
            //textArea.setEditable(false);

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            botPanel.add(scrollPane);
            botPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            Container container = frame.getContentPane();
            container.add(topPanel, BorderLayout.NORTH);
            container.add(botPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        }
        public void updateFrame(String message){
            textArea.append("\n" + message);
        }
    }
}
