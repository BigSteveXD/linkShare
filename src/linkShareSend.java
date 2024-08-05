import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

public class linkShareSend{
    private static PrintWriter peerWriter;
    private static String peerIP = "";
    private static int myPort = 4444;
    public static void main(String[] args){
        createFrame();
    }
    static void setIP(String ip){
        peerIP = ip;
    }
    static void setPort(int port){
        myPort = port;
    }
    static void connectPeer(String ip, int port){
        //System.out.println("ran");
        try{
            Socket soc = new Socket(ip, port);
            PrintWriter pw = new PrintWriter(soc.getOutputStream(), true);
            peerWriter = pw;
        }catch(Exception e){
            System.out.println(e);
        }
    }
    static void sendMessage(String message){
        peerWriter.println(message);
    }
    static void createFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("LinkShareSend");

        JPanel topPanel = new JPanel(new GridLayout(3,2,10,0));
        JPanel midPanel = new JPanel(new GridLayout(1,1));
        JPanel botPanel = new JPanel(new GridLayout(1,1));
        JButton sendButton = new JButton("Send");
        JButton connectButton = new JButton("Connect");

        topPanel.add(new JLabel("Peer IPv4:"));
        JTextField ipTextField = new JTextField("xxx.xxx.xx.xx",10);
        topPanel.add(ipTextField);
        topPanel.add(new JLabel("Port:"));
        JTextField portTextField = new JTextField("7777",10);
        topPanel.add(portTextField);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextArea textArea = new JTextArea();//"Send Text"
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        midPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        connectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setIP(ipTextField.getText());
                setPort(Integer.parseInt(portTextField.getText()));
                if(!peerIP.isEmpty()){
                    //System.out.println("connecting...");
                    try{
                        connectPeer(peerIP, myPort);
                    }catch(Exception err){
                        //System.out.println("failed to connect");
                        System.out.println(err);
                    }
                }else{
                    //System.out.println("no ip entered");
                }
            }
        });
        topPanel.add(connectButton);
        sendButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                sendMessage(textArea.getText());
            }
        });
        midPanel.add(scrollPane);
        botPanel.add(sendButton);
        botPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        Container container = frame.getContentPane();
        container.add(topPanel, BorderLayout.NORTH);
        container.add(midPanel, BorderLayout.CENTER);
        container.add(botPanel, BorderLayout.SOUTH);

        frame.setSize(500,350);
        frame.setVisible(true);
    }
}
