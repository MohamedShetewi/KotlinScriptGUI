package model;


import view.ScriptView;

import javax.swing.*;
import java.io.*;


public class ScriptRunning {

    private final ScriptView scriptView;

    public ScriptRunning(ScriptView scriptView) {
        this.scriptView = scriptView;
    }

    public void writeScript(String script) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("foo.kts");
        pw.print(script);
        pw.close();
    }

    public void runScript() {

        SwingUtilities.invokeLater(() -> {
            String typedScript = scriptView.getScriptArea().getText();
            scriptView.getOutputArea().setText("");
            scriptView.getScriptArea().getHighlighter().removeAllHighlights();
            scriptView.getRunningLabel().setVisible(true);

            try {

                writeScript(typedScript);
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("cmd", "/c", "kotlinc", "-script", "foo.kts");

                Process process = processBuilder.start();

                BufferedReader scriptOutputString = readScriptOutput(process);
                BufferedReader scriptErrorString = readScriptError(process);

                addScriptOutput(scriptOutputString, scriptErrorString, process);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        );
    }

    private void addScriptOutput(BufferedReader scriptOutputString, BufferedReader scriptErrorString, Process process) throws IOException {
//      SwingUtilities.invokeLater(() -> {
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                String line = "";
                String outputString = "";

                while (true) {
                    try {
                        if ((line = scriptOutputString.readLine()) == null) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outputString += (line + "<br>");
                    scriptView.getOutputArea().setText(outputString);
                }

                outputString += "<br>";
                try {
                    outputString += errorProcessing(scriptErrorString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scriptView.getRunningLabel().setVisible(false);

                int exitCode = 0;
                try {
                    exitCode = exitCode(process);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                outputString += ("<br>Process finished with exit code " + exitCode);
                scriptView.getOutputArea().setText(outputString);

                return null;
            }
        }.execute();

    }

    private String errorProcessing(BufferedReader errorBr) throws IOException {
        String line;
        StringBuilder output = new StringBuilder();
        output.append("<p style=\"color:#FF0000\";>");
        while ((line = errorBr.readLine()) != null) {
            int idxOfError = line.indexOf("foo.kts:");
            if (idxOfError == -1)
                output.append(line);
            else {
                int start = 0;
                while (start < idxOfError) {
                    if (Character.isWhitespace(line.charAt(start)))
                        output.append("&nbsp;&nbsp;");
                    else
                        output.append(line.charAt(start));
                    start++;
                }
                StringBuilder hyperLink = new StringBuilder("foo.kts:");
                int i = idxOfError + ("foo.kts:").length();
                StringBuilder lineNum = new StringBuilder();
                boolean isfirstColon = true;
                while (i < line.length()) {
                    char c = line.charAt(i);
                    if ((c < '0' || c > '9')) {
                        if (isfirstColon)
                            isfirstColon = false;
                        else
                            break;
                    }
                    hyperLink.append(line.charAt(i));
                    if (isfirstColon)
                        lineNum.append(line.charAt(i));
                    i++;
                }

                output.append("<a href=\"").append(lineNum).append("\">").append(hyperLink).append("</a>");
                while (i < line.length()) {
                    output.append(line.charAt(i));
                    i++;
                }
            }
            output.append("<br>");
        }
        return output.append("</p>").toString();
    }

    public BufferedReader readScriptOutput(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public BufferedReader readScriptError(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    public int exitCode(Process process) throws IOException, InterruptedException {
        return process.waitFor();
    }
}
