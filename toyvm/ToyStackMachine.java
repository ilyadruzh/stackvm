//   
package toystackmachine;

import java.io.*;
import java.util.*;

public class ToyStackMachine {
    //    
    private ArrayList <String> source = new ArrayList <> ();

    public ToyStackMachine () {
        //     
        loadSource (getFileName ());
        //    
        if (source.isEmpty ()) {
            System.err.printf ("Nothing to compile: empty source file%n");
            System.exit (0);
        }
        Assembler asm = new Assembler (source);
    }

    //       
    private String getFileName () {
        Scanner in = new Scanner (System.in);
        System.out.printf ("Source file name: ");
        String fn = in.nextLine ().trim ();
        //       
        if (fn.length () == 0) {
            System.err.printf ("Empty source file name%n");
            System.exit (0);
        }
        return fn;
    }

    //   
    private void loadSource (String fn) {
        try (BufferedReader br = new BufferedReader (new FileReader (fn))) {
            String str = br.readLine ();
            while (str != null) {
                //      
                source.add (str.toUpperCase ().trim ());
                //    
                str = br.readLine ();
            }
        } catch (IOException ioex) {
            System.err.printf ("Nothing to compile: source file not found%n");
            System.exit (0);
        }
    }

    //  
    public static void main (String[] args) {
        System.out.printf ("+++++++++++++++++++++++++++++++++%n");
        System.out.printf ("+ Assembler for ToyStackMachine +%n");
        System.out.printf ("+++++++++++++++++++++++++++++++++%n");
        ToyStackMachine tsm = new ToyStackMachine ();
    }
}
