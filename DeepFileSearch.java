import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DeepFileSearch{
    List <String> paths = new ArrayList<String>();
    boolean quickSeach = false;
    int resultCounter = 0;
    Thread t;

    DeepFileSearch(){
        JFrame f = new JFrame("Deep File Search");
        
        JLabel l1 = new JLabel("Enter the name of the file: ");
        l1.setBounds(10, 10, 200,20);
        
        JLabel l2 = new JLabel("Enter the path in which to search the file: ");
        l2.setBounds(10, 90, 300,20);
        
        JTextField t1 = new JTextField();
        t1.setBounds(10, 40, 470,30);

        JTextField t2 = new JTextField();
        t2.setBounds(10, 120, 470,30);

        JLabel l3 = new JLabel();
        l3.setBounds(10,170,100,30);
        
        JButton b = new JButton("Search");
        b.setBounds(185,170,100,30);

        JButton s = new JButton("Stop");
        s.setBounds(185,170,100,30);
        s.setVisible(false);

        JCheckBox c = new JCheckBox("Quick Search (no sub-folders)");
        c.setBounds(290,170,200,30);

        JTextArea t3 =  new JTextArea();
        JScrollPane scroll = new JScrollPane(t3,  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // t3.setBounds(10,220,470,230);
        t3.setEditable(false);
        scroll.setBounds(10,220,470,230);
        
        f.add(l1); f.add(l2); f.add(t1); f.add(t2); f.add(b); f.add(c); f.add(l3); f.add(s);
        f.add(scroll);
        f.setSize(500,500);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        b.addActionListener((ActionListener) new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // TODO Auto-generated method stub
               t = new Thread(){
                   
                   public void run(){
                        String filename = t1.getText().toString();
                        String path = t2.getText().toString();

                        File directory = new File(path);
                        if(filename.isEmpty() || path.isEmpty()){
                            t3.setText("Please enter the filename and path properly");
                        }
                        else{
                            if (directory.isDirectory()) {
                                    if(c.isSelected()){
                                        quickSeach = true;
                                    }
                                    else{
                                        quickSeach = false;
                                    }
                                    t1.setEditable(false);
                                    t2.setEditable(false);
                                    // b.setEnabled(false);
                                    b.setVisible(false);
                                    c.setEnabled(false);
                                    s.setVisible(true);
                                    l3.setText("");
                                    t3.setText("Searching... Please have patience, it may take a while  \nFeel free to continue with your work while we search for your file");
                                    findFile(filename, path);
                                    t3.setText("");

                                    if(!paths.isEmpty()){
                                        for(int i=0;i<paths.size();i++){
                                            t3.append(paths.get(i) + "\n");
                                        }
                                    }
                                    else{
                                        t3.setText(filename + " not found");
                                    }
                                    l3.setText(resultCounter+" file(s) found");
                            }
                            else{
                                    t3.setText("Enter a valid path");
                            }
                        }   
                        paths.clear();
                        resultCounter = 0;
                        t1.setEditable(true);
                        t2.setEditable(true);
                        b.setVisible(true);
                        // b.setEnabled(true);
                        c.setEnabled(true);
                    }
                };
                t.start();
            }
        });


        s.addActionListener((ActionListener) new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(t.isAlive()){
                    t.stop();
                }
                s.setVisible(false);
                paths.clear();
                t3.setText("");
                resultCounter = 0;
                t1.setEditable(true);
                t2.setEditable(true);
                // b.setEnabled(true);
                b.setVisible(true);
                c.setEnabled(true);
            }
        });
        
        
    }

    public void findFile(String fileName, String inputPath) {
        File directory = new File(inputPath);
        if (directory.isDirectory()) {
            File[] list = directory.listFiles();
            if(list!=null){
                for (File file : list) {
                    if (file.isDirectory()) {
                        if(quickSeach == false){
                            // System.out.println("Searching "+file.getAbsolutePath());
                            findFile(fileName, file.getAbsolutePath());
                        }
                    } else if (file.getName().toLowerCase().contains(fileName.toLowerCase())) {
                        // System.out.println(inputPath + "\\" + file.getName() + " found");
                        if(inputPath.length()==3){
                            paths.add(inputPath+file.getName());
                        }
                        else{
                            paths.add(inputPath+"\\"+file.getName());
                        }
                        resultCounter++;
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        new DeepFileSearch();
    }
}
