/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.*;

/**
 *
 * @author Andy
 */
public class AngleLogger implements SerialAction {

    BufferedWriter out;

    public AngleLogger(String outputPath) {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "utf-8"));
            out.append("Angle Values\n");
            out.append("X\tY\tZ\t\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run(BufferedReader input) throws IOException {
        while (input.ready()) {
            String in = input.readLine();
            if (in.startsWith("!ANG:")) {
                in = in.substring(5);
                String[] angleStrings = in.split(",");
                for (String angle : angleStrings) {
                    out.append(angle);
                    double ang = Double.parseDouble(angle);
                out.append("\t");
                }
                out.append("\n");
            } else {
                System.out.println(in);
            }
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
