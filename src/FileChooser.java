import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by edward_salcido on 3/17/15.
 */
public class FileChooser {

    public static void createAndShowGUI(){

        final JFrame frame = new JFrame("Centered");

        frame.setSize(400,400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new FlowLayout());

        JButton button = new JButton("Choose file/directory");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFileChooser(frame);
            }
        });

        frame.getContentPane().add(button);

    }

    private static void createFileChooser(final JFrame frame){
        String filename = File.separator+"tmp";
        JFileChooser fileChooser = new JFileChooser(new File(filename));

        fileChooser.showOpenDialog(frame);

        System.out.println("file to open: "+fileChooser.getSelectedFile());


    }

}
