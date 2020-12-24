package controller;

import model.Main;
import view.ScriptView;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.xml.stream.events.Characters;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;

public class Control {

    private ScriptView scriptView;
    private Main main;
    private Highlighter.HighlightPainter painter;

    public Control() {

        scriptView = new ScriptView();
        main = new Main();

        scriptView.getOutputArea().addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    startThread2(e);
            }
        });
        scriptView.getRunButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startThread();
            }
        });
    }

    private void startThread2(HyperlinkEvent e) {

         SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                int lineNumber = Integer.parseInt(e.getDescription()) - 1;
                scriptView.getScriptArea().setCaretPosition(lineNumber);
                painter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
                int startIndex = 0, endIndex = 0;
                try {
                    startIndex = scriptView.getScriptArea().getLineStartOffset(lineNumber);
                    endIndex = scriptView.getScriptArea().getLineEndOffset(lineNumber);
                    scriptView.getScriptArea().getHighlighter().addHighlight(startIndex, endIndex,painter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
         };
        sw.execute();
    }


    private void startThread() {

        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                String script = scriptView.getScriptArea().getText();
                scriptView.getOutputArea().setText("");
                scriptView.getRunningLabel().setVisible(true);
                try {

                    main.scriptWriting(script);
                    Process process = main.scriptRunning();

                    BufferedReader outputBR = main.scriptOutput(process);
                    BufferedReader errorBR = main.scriptError(process);

                    String line = "";
                    String output = "";
                    while ((line = outputBR.readLine()) != null) {
                        Thread.sleep(1);
                        output += (line + "<br>");
                        scriptView.getOutputArea().setText(output);
                    }
                    output += "<br>";
                    output += errorProcessing(errorBR);
                    scriptView.getRunningLabel().setVisible(false);

                    int exitCode = main.exitCode(process);
                    output += ("<br>Process finished with exit code " + exitCode);
                    scriptView.getOutputArea().setText(output);

                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        sw.execute();
    }


    private String errorProcessing(BufferedReader errorBr) throws IOException {
        String line = "";
        String output = "";

        while ((line = errorBr.readLine()) != null) {
            int idxOfError = line.indexOf("foo.kts:");
            if (idxOfError == -1)
                output += line;
            else {
                int start = 0;
                while (start < idxOfError) {
                    if (Character.isWhitespace(line.charAt(start)))
                        output += "&nbsp;&nbsp;";
                    else
                        output += line.charAt(start);
                    start++;
                }
                String hyperLink = "foo.kts:";
                int i = idxOfError + ("foo.kts:").length();
                String lineNum = "";
                while (i < line.length()) {
                    char c = line.charAt(i);
                    if (c < '0' || c > '9')
                        break;
                    hyperLink += line.charAt(i);
                    lineNum += line.charAt(i);
                    i++;
                }
                output += "<a href=\"" + lineNum + "\">" + hyperLink + "</a>";
                while (i < line.length()) {
                    output += line.charAt(i);
                    i++;
                }
            }
            output += "<br>";
        }
        return output;
    }

    public static void main(String[] args) {
        new Control();
    }
}
