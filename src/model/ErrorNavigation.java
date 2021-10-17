package model;

import view.ScriptView;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ErrorNavigation {

    private final ScriptView scriptView;
    private DefaultHighlighter.DefaultHighlightPainter painter;

    public ErrorNavigation(ScriptView scriptView, DefaultHighlighter.DefaultHighlightPainter painter) {
        this.scriptView = scriptView;
        this.painter = painter;
    }

    public void jumpToError(HyperlinkEvent e) {

        SwingUtilities.invokeLater(() -> {
            clearErrorHighlights();
            int lineNumber = Integer.parseInt(e.getDescription()) - 1;
            scriptView.getScriptArea().setCaretPosition(lineNumber);
            scriptView.getScriptArea().requestFocus();
            painter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
            int startIndex = 0, endIndex = 0;
            try {
                int[]offsets = getOffsets(lineNumber, scriptView.getScriptArea().getText());
                startIndex = offsets[0];
                endIndex = offsets[1];
                scriptView.getScriptArea().getHighlighter().addHighlight(startIndex, endIndex, painter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }


    private int[] getOffsets(int lineNumber, String text)
    {
        int offset = 0;
        int count = 0;
        List<String> lines = new ArrayList<>();
        text.lines().forEach(lines::add);
        while (count < lineNumber)
        {
            offset+=lines.get(count).length()+1;
            count++;
        }
        return new int[]{offset, offset + lines.get(count).length()};
    }


    private void clearErrorHighlights() {
        scriptView.getScriptArea().getHighlighter().removeAllHighlights();
    }
}
