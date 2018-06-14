package start;

import java.io.*;

/**
 * Created by Raul on 13/06/2018.
 */
public class RunPythonFromJava {
    public static void main(String[] args) {
        try {
            Process p = null;
            p = Runtime.getRuntime().exec("python src/main/java/start/generateScientificProduction.py [2,3,4,5]");

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String input = "";

            while ((input = in.readLine()) != null) {
                System.out.println(input);
            }
            p.destroy();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        try{
//
//            String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
//            BufferedWriter out = new BufferedWriter(new FileWriter("test1.py"));
//            out.write(prg);
//            out.close();
//            int number1 = 10;
//            int number2 = 32;
//            Process p = Runtime.getRuntime().exec("python test1.py "+number1+" "+number2);
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            int ret = new Integer(in.readLine()).intValue();
//            System.out.println("value is : "+ret);
//        }catch(Exception e){}
// }

    }
}
