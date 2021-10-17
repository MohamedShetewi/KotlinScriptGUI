package model;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class KeywordsHighLighter {


    private final JTextPane scriptArea;
    private final StyledDocument styledDocument;
    private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private final AttributeSet blueAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
    private final AttributeSet blackAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    final private String[] keywords = new String[]{
            "public", "if", "else", "var", "for", "break", "in", "while", "true", "false", "null", "print", "return", "String"
    };

    public KeywordsHighLighter(JTextPane scriptArea, DefaultHighlighter.DefaultHighlightPainter painter) {
        this.scriptArea = scriptArea;
        this.styledDocument = this.scriptArea.getStyledDocument();
    }

    public void handleTextChanged() {
        SwingUtilities.invokeLater(this::highlight);
    }

    private void highlight() {
        String regex = "([^a-zA-Z0-9]|^)(%s)([^a-zA-Z0-9]|$)";

        styledDocument.setCharacterAttributes(0, scriptArea.getText().length(), blackAttributeSet, true);
        List<String> lines = new ArrayList<>();
        scriptArea.getText().lines().forEach(lines::add);

        int offset = 0;

        for (String line : lines) {
            for (String keyword : keywords) {
                Matcher m = Pattern.compile(String.format(regex, keyword)).matcher(line);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end() - 1;

                    while (line.charAt(start) < 'a' || line.charAt(start) > 'z') start++;
                    while (line.charAt(end) < 'a' || line.charAt(end) > 'z') end--;
                    styledDocument.setCharacterAttributes(offset + start, end - start + 1, blueAttributeSet, false);
                }
            }
            offset += line.length() + 1;
        }
    }
}
