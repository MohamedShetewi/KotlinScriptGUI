package model;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class KeywordsHighLighter {

    final private String[] keywords = new String[]{
            "public", "if", "else", "var", "for", "break", "in", "while", "true", "false", "null", "print", "return", "String"
    };

    public void highlight(JTextArea scriptArea) {
        String regex = "([^a-zA-Z0-9]|^)(%s)([^a-zA-Z0-9])";

        Runnable r = () -> {
            List<String> lines = new ArrayList<>();
            scriptArea.getText().lines().forEach(lines::add);
            for(String line: lines)
            {
                for(String keyword: keywords)
                {
                    Matcher m =  Pattern.compile(String.format(regex,keyword)).matcher(line);
                    while (m.find())
                        System.out.println(line.substring(m.start(), m.end()));
                }
            }
        };
        r.run();



    }



}
