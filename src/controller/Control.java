package controller;

import model.ErrorNavigation;
import model.KeywordsHighLighter;
import model.ScriptRunning;
import view.ScriptView;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control {

    private final ScriptRunning scriptRunning;
    private final KeywordsHighLighter keywordsHighLighter;
    private final ErrorNavigation errorNavigation;


    public Control() {
        DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
        ScriptView scriptView = new ScriptView();
        scriptRunning = new ScriptRunning(scriptView);
        errorNavigation = new ErrorNavigation(scriptView);
        keywordsHighLighter = new KeywordsHighLighter(scriptView.getScriptArea(), painter);

        scriptView.getOutputArea().addHyperlinkListener(errorClicked -> {
            if (errorClicked.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                errorNavigation.jumpToError(errorClicked);
        });

        scriptView.getRunButton().addActionListener(runningButtonClicked -> scriptRunning.runScript());

        scriptView.getScriptArea().addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                keywordsHighLighter.handleTextChanged();
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
    }



    public static void main(String[] args) {
        new Control();

    }
}
