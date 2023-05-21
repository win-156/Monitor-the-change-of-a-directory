import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JPanel{

    public  static DefaultListModel model, model2;
    public static JList jList;
    public  static JButton sendPath;
    public static JTextField textFPath;

    public static ArrayList<Socket> sock = new ArrayList<Socket>();

    public Server() throws IOException {

//       setlayout
        setLayout(new BorderLayout());
//       topPanel
        JPanel topPanel = new JPanel();
        JLabel jLabel = new JLabel("Server");

        topPanel.setLayout(new FlowLayout());
        topPanel.add(jLabel);
        add(topPanel, BorderLayout.PAGE_START);
// Jlist
        model = new DefaultListModel();
        jList = new JList(model);

        add(jList, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(jList);
        add(scrollPane, BorderLayout.CENTER);



//        create button folder
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        sendPath = new JButton("SendPath");
        textFPath = new JTextField();


        bottomPanel.add(sendPath);
        bottomPanel.add(textFPath);

        JPanel bottomPanel_parent = new JPanel();
        //trên xuống dưới
        bottomPanel_parent.setLayout(new BoxLayout(bottomPanel_parent, BoxLayout.PAGE_AXIS));
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(bottomPanel);

        //Label folder

        JPanel jpListNoti = new JPanel();
        jpListNoti.setLayout(new BorderLayout());
        JLabel notification = new JLabel("Notification of client.",SwingConstants.LEFT);
        jpListNoti.add(notification, BorderLayout.PAGE_START);

        model2 = new DefaultListModel();
        JList jList2 = new JList(model2);
        jpListNoti.add(jList2, BorderLayout.CENTER);
        JScrollPane scrollPane2 = new JScrollPane(jList2);
        jpListNoti.add(scrollPane2, BorderLayout.CENTER);

        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,20)));
        bottomPanel_parent.add(jpListNoti);

        add(bottomPanel_parent,BorderLayout.PAGE_END);
    }

    public static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jf = new JFrame("Server");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Server vidu = new Server();
        vidu.setOpaque(true);
        jf.setContentPane(vidu);

        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        try
        {
            ServerSocket s = new ServerSocket(3200);

            createAndShowGUI();

            Thread t1 = new Thread() {
                public void run() {
                    //synchronou
                    try {
                        while (true){
                            System.out.println("Waiting for a Client");
                            Socket ss = s.accept();
                            sock.add(ss);
                            model.add(model.size(),"Client: "+ (sock.size()-1) + "-Port:" + sock.get(sock.size()-1).getPort());
                            System.out.println("Talking to client");
                            System.out.println(ss.getPort());
                            InputStream is=ss.getInputStream();
                            BufferedReader br=new BufferedReader(new InputStreamReader(is));

                            Thread t2 = new Thread() {
                                public void run() {
                                    while (true){
                                        try {
                                            String Noti;
                                            Noti = br.readLine();
                                            model2.add(model2.size(), "Client Port: " + ss.getPort() + Noti);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                            };
                            t2.start();

                            Thread t3 = new Thread() {
                                public void run() {
                                    sendPath.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                                try {
                                                    int i =jList.getSelectedIndex();
                                                    Socket Ss = null;
                                                    if (i >=0)
                                                        Ss = sock.get(i);
                                                    else
                                                        Ss = sock.get(0);


                                                    OutputStream os= Ss.getOutputStream();
                                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                                                    String sendMessage;

                                                    sendMessage = textFPath.getText();
                                                    bw.write(sendMessage);
                                                    bw.newLine();
                                                    bw.flush();
                                                } catch (IOException a) {
                                                    throw new RuntimeException(a);

                                            }

                                        }
                                    });
                                }

                            };
                            t3.start();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            };

            t1.start();




            // bw.close();
            // br.close();

        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }

    }
}
