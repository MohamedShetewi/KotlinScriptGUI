package model;

import view.ScriptView;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.util.ArrayList;
import java.util.List;

public class ErrorNavigation {

    private final ScriptView scriptView;
    private final DefaultHighlighter.DefaultHighlightPainter painter;

    public ErrorNavigation(ScriptView scriptView, DefaultHighlighter.DefaultHighlightPainter painter) {
        this.scriptView = scriptView;
        this.painter = painter;
    }

    public void jumpToError(HyperlinkEvent e) {
        clearErrorHighlights();
        SwingUtilities.invokeLater(() -> {
            int lineNumber = Integer.parseInt(e.getDescription()) - 1;
            int startIndex = 0, endIndex = 0;
            try {
                int[]offsets = getOffsets(lineNumber);
                startIndex = offsets[0];
                endIndex = offsets[1];
                scriptView.getScriptArea().getHighlighter().addHighlight(startIndex, endIndex, painter);
                scriptView.getScriptArea().setCaretPosition(startIndex);
                scriptView.getScriptArea().requestFocus();
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void adjustScrollBar(int line)
    {

        int linesCount = (int) scriptView.getScriptArea().getText().lines().count();

        scriptView.getScriptAreaScroll().getVerticalScrollBar().setValue(line/linesCount);
    }

    private int[] getOffsets(int lineNumber)
    {
        int offset = 0;
        int count = 0;
        String scriptAreaText = scriptView.getScriptArea().getText();
        List<String> lines = new ArrayList<>();
        scriptAreaText.lines().forEach(lines::add);
        while (count < lineNumber)
        {
            offset+=lines.get(count).length()+1;
            count++;
        }
        return new int[]{offset, offset + lines.get(count).length()};
    }


    private void clearErrorHighlights() {
        SwingUtilities.invokeLater(() -> scriptView.getScriptArea().getHighlighter().removeAllHighlights());
    }
}
