import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.*;


//create a main class and main method
public class main {
    public static void main(String[] args){
        //create a new frame
        JFrame frame = new JFrame("To Do List");

        new splashScreen();//creating object of splashScreen class

        //set the size of the frame
        frame.setSize(800, 800);

        //set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JButton helpButton = new JButton("Help"); //create a help button
        JButton aboutButton = new JButton("About");//create an about button
        mb.add(helpButton);//add the help button to the menu bar
        mb.add(aboutButton);//add the about button to the menu bar

        //when the user clicks the About button, a new frame will be created and the about message will be displayed
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //creates a variable to store the path and a file name for the icon
                Icon icon = new ImageIcon("icon.jpg");
                //scales the size of the image to fit the frame
                ((ImageIcon) icon).setImage(((ImageIcon) icon).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                //displays the icon in the frame, and shows the message in the frame
                JOptionPane.showMessageDialog(frame, 
                "Welcome to TODO List!\n" + 
                "In this applicaiton you can create various kinds of lists and save them on your computer!\n\n" 
                +"Version 1.0 by Orlov LLC.", "About", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });

        //when the user clicks the Help button, a new frame will be created and the help message will be displayed
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, 
                "This app creates a sorted table of things to do.\n" + 
                "On the bottom of the widnow you can type in a new task and click the SEND button or ENTER to create a new task.\n" +
                "You can also save the table to a file in the directory.\n" +
                "You can load a txt file that has already been created previously and modify it!\n"+
                "You can change/modify/delete data from the table by clicking on the row you would like to change.\n\n"+
                "Version 1.0 by Orlov LLC.", "Help", JOptionPane.QUESTION_MESSAGE);
            }
        });

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton delete = new JButton("Delete"); //create a delete button
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Send");//create a send button
        JButton save = new JButton("Save");//create a save button
        JButton load = new JButton("Load");//create a load button
        panel.add(delete);
        panel.add(label); //add componets to the panel
        panel.add(tf);
        panel.add(send);
        panel.add(save);
        panel.add(load);

        //table at the center
        JTable table = new JTable();
        
        //set the bounds of the table
        table.setBounds(100, 100, 500, 500);
        JScrollPane jScrollPane = new JScrollPane(table);
        frame.add(jScrollPane);
        frame.setSize(700, 700);
        frame.setVisible(true);

        //Adding Components to the frame
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.setVisible(true);

        //create 3 rows of the table
        String[] columnNames = {"Task", "Date", "Time"};

        //create a 2D array to store the data
        Object[][] data = {};



        //create a strring with today's date and time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().toString();

        //round time to hours and minutes
        //creates an array of strings to store the time
        String[] timeSplit = time.split(":");
        String hour = timeSplit[0];
        String minute = timeSplit[1];
        String timeRounded = hour + ":" + minute;

        //create a model and set it to the table
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);

        //create a listener for the send button
        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get the text from the text field
                String text = tf.getText();
                //add the text to the table
                model.addRow(new Object[]{text, date, timeRounded});
                //clear the text field
                tf.setText("");
            }
        });
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //get the text from the text field
                String text = tf.getText();
                //add the text to the table
                model.addRow(new Object[]{text, date, timeRounded});
                //clear the text field
                tf.setText("");
            }
        });

        //create a listener for the save button
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (BufferedWriter bfw = new BufferedWriter(new FileWriter(String.format("To Do List.txt", date, time)))) {
                    for(int i = 0 ; i < table.getColumnCount() ; i++){
                        bfw.write(table.getColumnName(i));
                        bfw.write("\t");
                    }
                    for (int i = 0 ; i < table.getRowCount(); i++){
                        bfw.newLine();
                        for(int j = 0 ; j < table.getColumnCount();j++){
                            bfw.write((String)(table.getValueAt(i,j)));
                            bfw.write("\t");;
                        }
                    }
                    bfw.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        //create a listener for the load button and give a user a choice to find a file from the directory
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //create a file chooser
                JFileChooser chooser = new JFileChooser();
                //create a dialougue window where user can choose a file
                chooser.showOpenDialog(null);
                //get the file
                File f = chooser.getSelectedFile();
                //create a string to store the file name and the path
                String filename = f.getAbsolutePath();
                //class that reads the file
                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    String line = br.readLine();
                    //while the file is not empty read the next line and split it by tabs, then add it to the table
                    while ((line = br.readLine()) != null) {
                        String[] row = line.split("\t");
                        model.addRow(row);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        //create a listener for the delete button and delete the selected row
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                model.removeRow(row);
            }
        });
    }
}