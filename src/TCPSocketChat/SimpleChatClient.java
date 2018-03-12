/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPSocketChat;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author fauziachmadharuna
 */
public class SimpleChatClient {
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    
    public void go(){
        JFrame frame=new JFrame("Lucidicrously Simple Chat Client");
        JPanel mainPanel=new JPanel();
        incoming=new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(true);
        JScrollPane qScroller=new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing=new JTextField(20);
        JButton sendButton=new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        setUpNetworking();
        
        Thread readerThread=new Thread(new IncomingReader());
        readerThread.start();
        
        frame.setSize(650,500);
        frame.setVisible(true);
    }
    
    private void setUpNetworking(){
        try{
            sock=new Socket("127.0.0.1",5000);
            InputStreamReader streamReader=new InputStreamReader(sock.getInputStream());
            reader=new BufferedReader(streamReader);
            writer=new PrintWriter(sock.getOutputStream());
            System.out.println("Networking established");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                writer.println(outgoing.getText());
                writer.flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }outgoing.setText("");
            outgoing.requestFocus();
        }
    }
    public static void main(String[] args){
        new SimpleChatClient().go();
    }
    class IncomingReader implements Runnable{
        public void run(){
            String message;
            try{
                while((message=reader.readLine()) != null){
                    System.out.println("Client read "+message);
                    incoming.append(message + " \n");
                    
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
