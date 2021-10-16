package controller;

import model.ErrorNavigation;
import model.KeywordsHighLighter;
import model.ScriptRunning;
import view.ScriptView;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.DefaultHighlighter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control {

    private final ScriptRunning scriptRunning;
    private final KeywordsHighLighter keywordsHighLighter;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private final ErrorNavigation errorNavigation;


    public Control() {
        ScriptView scriptView = new ScriptView();
        scriptRunning = new ScriptRunning(scriptView);
        errorNavigation = new ErrorNavigation(scriptView, painter);
        keywordsHighLighter = new KeywordsHighLighter();

        scriptView.getOutputArea().addHyperlinkListener(errorClicked -> {
            if (errorClicked.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                errorNavigation.jumpToError(errorClicked);
        });

        scriptView.getRunButton().addActionListener(runningButtonClicked -> scriptRunning.runScript());

        scriptView.getScriptArea().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                keywordsHighLighter.highlight(scriptView.getScriptArea());
            }
        });
    }



    public static void main(String[] args) {
        new Control();

    }
}
