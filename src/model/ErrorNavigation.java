package model;

import view.ScriptView;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;

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
                startIndex = scriptView.getScriptArea().getLineStartOffset(lineNumber);
                endIndex = scriptView.getScriptArea().getLineEndOffset(lineNumber);
                scriptView.getScriptArea().getHighlighter().addHighlight(startIndex, endIndex, painter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void clearErrorHighlights() {
        scriptView.getScriptArea().getHighlighter().removeAllHighlights();
    }
}
