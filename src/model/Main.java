package model;


import java.io.*;

public class Main {

    public void scriptWriting(String script) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("foo.kts");
        pw.print(script);
        pw.close();
    }

    public Process scriptRunning() throws IOException {
        return Runtime.getRuntime().exec("cmd /c kotlinc -script foo.kts ");
    }

    public BufferedReader scriptOutput(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public BufferedReader scriptError(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    public int exitCode(Process process) throws IOException, InterruptedException {
        return process.waitFor();
    }
}
