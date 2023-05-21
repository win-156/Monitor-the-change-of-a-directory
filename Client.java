import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client extends JPanel{
    public static BufferedReader br;
    public  static BufferedWriter bw;
    public static String path = "";
    public JTextField jTextFolder;
    public static String[] showFileAndFolder(String Lfile) {

        String a[]={""};
        ArrayList<String> ArrayFileFolder = new ArrayList<String>(Arrays.asList(a));
        //Creating a File object for director
        File directoryPath = new File(Lfile);
        //List of all files and directories
        String contents[] = directoryPath.list();
        System.out.println("List of files and directories in the specified directory:");
        for(int i=0; i<contents.length; i++) {
            ArrayFileFolder.add(contents[i]);
        }
        String arr[] = new String[ArrayFileFolder.size()];
        for (int i = 0; i < ArrayFileFolder.size(); i++)
            arr[i] = ArrayFileFolder.get(i);
        return arr;

    }
    public static void deleteFolder(File file){
        for (File subFile : file.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }
    public static void deleteFolderALL(String filePath)
    {
        File file = new File(filePath);
        deleteFolder(file);
    }
    public static void createFolder(String path)
    {
        File f1 = new File(path);
        //Creating a folder using mkdir() method
        boolean bool = f1.mkdir();
        if(bool){
            System.out.println("Folder is created successfully");
        }else{
            System.out.println("Error Found!");
        }
    }

    public static void deleteFile(String path){
        File myObj = new File(path);
        if (myObj.delete()) {
            System.out.println("Deleted the folder: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the folder.");
        }
    }

    public static void changeFile(String path, String textDelete, String textChange) throws IOException{
        try {
            BufferedReader bin = new BufferedReader(
                    new FileReader(path));
            String str = "", strNew = "";
            while ((str = bin.readLine()) != null) {
                String[] splitStr = str.split(" ");
                for(int i = 0; i < splitStr.length; i++){
                    String strTest = splitStr[i];
                    if(textDelete.equals(strTest))
                        strNew += textChange + " ";
                    else
                        strNew += strTest + " ";
                }
                strNew += "\n";
            }
            bin.close();
            System.out.println(strNew);
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(path));
            bw.write(strNew);
            bw.close();
        } catch (Exception E) {
            System.out.println("No read file + " + path);
        }
    }

    public static void deleteFilee(String path, String contentDelete){
        try {
            BufferedReader bin = new BufferedReader(
                    new FileReader(path));
            String str = "", strNew = "";
            while ((str = bin.readLine()) != null) {
                String[] splitStr = str.split(" ");
                for(int i = 0; i < splitStr.length; i++)
                {
                    String strTest = splitStr[i];
                    if(contentDelete.equals(strTest))
                    {
                        continue;
                    }
                    else
                        strNew+= strTest + " ";
                }
                strNew+="\n";
            }
            bin.close();

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(path));
            bw.write(strNew);
            bw.close();
        } catch (Exception E) {
            System.out.println("No read file + " + path);
        }

    }
    public static void addFile(String path, String textAdd){
        try {

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(path, true));
            bw.write(textAdd);
            bw.close();
        } catch (Exception E) {
            System.out.println("No read file + " + path);
        }
    }


    public Client() {

//        setlayout
        setLayout(new BorderLayout());
//       topPanel
        JPanel topPanel = new JPanel();
        JLabel jLabel = new JLabel("Client");

        topPanel.setLayout(new FlowLayout());
        topPanel.add(jLabel);
        add(topPanel, BorderLayout.PAGE_START);
// Jlist
        String war[] = showFileAndFolder(path);
        DefaultListModel model = new DefaultListModel();
        JList jList = new JList(model);
        for (int i = 0; i < war.length; i++)
        {
            model.add(i, war[i]);
        }
        add(jList, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(jList);
        add(scrollPane, BorderLayout.CENTER);

// botton

//        create element, input of folder
        JPanel JPinFolder = new JPanel();
        JPinFolder.setLayout((new BoxLayout(JPinFolder, BoxLayout.LINE_AXIS)));
        JLabel labelFolder = new JLabel("Input folder: ");
        jTextFolder = new JTextField();

        JPinFolder.add(labelFolder);
        JPinFolder.add(jTextFolder);

//        create button folder
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        JButton delete_folder = new JButton("Delete");
        JButton add_folder = new JButton("Add");

        delete_folder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                deleteFolderALL(path + "/" + jList.getSelectedValue());
                try {
                    bw.write("Delete folder: " + path + "/" + jList.getSelectedValue());
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                model.remove(jList.getSelectedIndex());
            }

        });
        add_folder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                createFolder(path+"/"+jTextFolder.getText());
                model.add(model.size(),jTextFolder.getText());
                try {
                    bw.write("Create folder: " + path+"/"+jTextFolder.getText());
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        bottomPanel.add(delete_folder);
        bottomPanel.add(Box.createRigidArea(new Dimension(20,0)));
        bottomPanel.add(add_folder);

//      create input file delete
        JPanel JPinFile = new JPanel();
        JPinFile.setLayout((new BoxLayout(JPinFile, BoxLayout.LINE_AXIS)));
        JLabel labelFile = new JLabel("Input string delete ");
        JTextField jTextFile = new JTextField();

        JPinFile.add(labelFile);
        JPinFile.add(jTextFile);

//       create input file change
        JPanel JPinFile1 = new JPanel();
        JPinFile1.setLayout((new BoxLayout(JPinFile1, BoxLayout.LINE_AXIS)));
        JLabel labelFile1 = new JLabel("Input string change: ");
        JTextField jTextFile1 = new JTextField();

        JPinFile1.add(labelFile1);
        JPinFile1.add(jTextFile1);

//       create input file change
        JPanel JPinFile2 = new JPanel();
        JPinFile2.setLayout((new BoxLayout(JPinFile2, BoxLayout.LINE_AXIS)));
        JLabel labelFile2 = new JLabel("Input string add: ");
        JTextField jTextFile2 = new JTextField();

        JPinFile2.add(labelFile2);
        JPinFile2.add(jTextFile2);

//      create button file
        JPanel bottomPanel_file = new JPanel();
        bottomPanel_file.setLayout(new BoxLayout(bottomPanel_file, BoxLayout.LINE_AXIS));
        JButton delete_content = new JButton("Delete");
        JButton change_content = new JButton("Change");
        JButton add_content = new JButton("Add");

        delete_content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFilee(path+"/"+jList.getSelectedValue(), jTextFile.getText());
                try {
                    bw.write("Delete: "+ jTextFile.getText()+" in file: "+ path +"/" + jList.getSelectedValue());
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        change_content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeFile(path+"/"+jList.getSelectedValue(), jTextFile.getText(), jTextFile1.getText());
                    bw.write("change: "+ jTextFile.getText()+ "to " + jTextFile1.getText() +" in file: "+ path +"/" + jList.getSelectedValue());
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add_content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFile(path+"/"+jList.getSelectedValue(), jTextFile2.getText());
                try {
                    bw.write("Add: "+ jTextFile2.getText() +" in file: "+ path +"/" + jList.getSelectedValue());
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        bottomPanel_file.add(delete_content);
        bottomPanel_file.add(change_content);
        bottomPanel_file.add(add_content);




        JPanel bottomPanel_parent = new JPanel();
        //trên xuống dưới
        bottomPanel_parent.setLayout(new BoxLayout(bottomPanel_parent, BoxLayout.PAGE_AXIS));
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(JPinFolder);
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(bottomPanel);
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(JPinFile);
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(JPinFile1);
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(JPinFile2);
        bottomPanel_parent.add(Box.createRigidArea(new Dimension(0,5)));
        bottomPanel_parent.add(bottomPanel_file);


        add(bottomPanel_parent,BorderLayout.PAGE_END);


    }

    public static void createAndShowGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jf = new JFrame("Client");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Client vidu = new Client();
        vidu.setOpaque(true);
        jf.setContentPane(vidu);


        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        try {
            Socket s = new Socket("localhost", 3200);
            System.out.println(s.getPort());

            InputStream is = s.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = s.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os));


            System.out.println("Talking to Server");


                path = br.readLine();

                System.out.println("Path: " + path);
                createAndShowGUI();
//             bw.close();
//             br.close();
        } catch (IOException e) {
            System.out.println("There're some error");
        }

    }
}
