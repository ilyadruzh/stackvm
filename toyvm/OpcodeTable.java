//  
package toystackmachine;

import java.util.*;

public class OpcodeTable {
    //  
    static final int UNDEFCODE = -255;
    private Map <String, Integer> optable = new HashMap <> ();

    public OpcodeTable () {
        //     
        optable.put ("DUP",    -1);         // 
        optable.put ("DROP",   -2);         // 
        optable.put ("SWAP",   -3);         // 
        optable.put ("OVER",   -4);         // 
        optable.put ("DTR",    -5);         //   
        optable.put ("RTD",    -6);         //   
        //  
        optable.put ("ADD",    -7);         // 
        optable.put ("SUB",    -8);         // 
        optable.put ("MUL",    -9);         // 
        optable.put ("DIV",   -10);         //     
        optable.put ("NEG",   -11);         //  
        optable.put ("ABS",   -12);         //  
        optable.put ("AND",   -13);         // 
        optable.put ("OR",    -14);         // 
        optable.put ("XOR",   -15);         //  
        optable.put ("SHL",   -16);         //  
        optable.put ("SHR",   -17);         //  
        //   
        optable.put ("BR",    -18);         //  
        optable.put ("BRN",   -19);         //   
        optable.put ("BRZ",   -20);         //   
        optable.put ("BRP",   -21);         //   
        optable.put ("CALL",  -22);         //  
        optable.put ("RET",   -23);         //   
        //    
        optable.put ("LOAD",  -24);         //   
        optable.put ("SAVE",  -25);         //   
        //    
        optable.put ("IN",    -26);         //  
        optable.put ("OUTN",  -27);         //   
        optable.put ("OUTC",  -28);         //   
        //  
        optable.put ("LPC",   -29);         //   PC
        optable.put ("DEPTH", -30);         //   
        optable.put ("NOP",   -31);         //  
        //  
        optable.put ("HALT",  -40);         // 
    }

    //   
    public boolean isMnemonic (String mnemo) {
        return optable.get (mnemo) != null;
    }

    //   
    public int getOpcode (String mnemo) {
        if (optable.get (mnemo) != null) {
            return optable.get (mnemo);
        }
        return UNDEFCODE;
    }
}
