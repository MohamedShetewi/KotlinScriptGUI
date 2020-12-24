package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScriptView extends JFrame {

    private JTextArea scriptArea;
    private JEditorPane outputArea;
    private JButton runButton;
    private JLabel runningLabel;


    public ScriptView()  {
        super();
        setResizable(false);
        setSize(900, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Inconsolata_Condensed-Regular.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        font = font.deriveFont(Font.BOLD , 20);

        JLabel scriptAreaIndicator = new JLabel("Script:");
        setComponentView(scriptAreaIndicator,10,10,0,0 , false);
        scriptAreaIndicator.setOpaque(true);

        scriptArea = new JTextArea();
        scriptArea.setFont(font);
        JScrollPane scriptAreaScroll = new JScrollPane(scriptArea);
        setComponentView(scriptAreaScroll,10 , 30 , 700 , 400 , false);

        runButton = new JButton("Run");
        runButton.setVisible(true);
        runButton.setBackground(new Color(250, 250 ,250));
        setComponentView(runButton,720,460,40,0,true);

        runningLabel = new JLabel("Running...");
        setComponentView(runningLabel,750,500,40,0 , false);
        runningLabel.setOpaque(false);
        runningLabel.setVisible(false);


        outputArea = new JEditorPane();
        outputArea.setForeground(Color.BLACK);
        outputArea.setFont(font.deriveFont(Font.BOLD,18));
        outputArea.setContentType("text/html");
        outputArea.setEditable(false);

        JScrollPane outputAreaScroll = new JScrollPane(outputArea);
        setComponentView(outputAreaScroll , 10,500,700,200,true);

        JLabel outputAreaIndicator = new JLabel("Output:");
        outputAreaIndicator.setOpaque(false);
        setComponentView(outputAreaIndicator,10,480,10 , 0 ,true);

        setVisible(true);
    }

    private void setComponentView(JComponent component, int x, int y, int width, int height, boolean right ){
        Dimension dim = component.getPreferredSize();
        Insets insets = component.getInsets();
        component.setBounds((right? insets.right : insets.left ) + x , insets.top + y, dim.width + width , dim.height + height);
        add(component);
    }


    public JLabel getRunningLabel() {
        return runningLabel;
    }


    public JTextArea getScriptArea() {
        return scriptArea;
    }

    public JEditorPane getOutputArea() {
        return outputArea;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public static void main(String[] args) {
       ScriptView s =  new ScriptView();
    }

}
