// 
package toystackmachine;

import java.util.*;

public class StackMachine {
    static final int MEMORYSIZE = 65536;    //   
    private int memory [] = new int [MEMORYSIZE],
                dstack [] = new int [256],  //  
                rstack [] = new int [256];  //  
    private int loaded = 0,                 //   
                dsp    = -1,                //    
                rsp    = -1,                //    
                pc     = 0,                 //  
                cycles = 0;                 //   

    private SymbolTable symb = new SymbolTable ();
    private ArrayList <Integer> obj;

    public StackMachine (ArrayList <Integer> obj) {
        this.obj = obj;
        if (obj.size () > MEMORYSIZE) {
            System.err.printf ("Too long object code%n");
            System.exit (0);
        }
        //     
        loaded = loadObject ();
        run ();
        printStatus ();
    }

    //     
    private int loadObject () {
        int counter = 0;                    //    

        for (counter = 0; counter < obj.size (); counter++) {
            memory [counter] = obj.get (counter);
        }
        return counter;
    }

    //    
    private void run () {
        int command = 0,                    // 
             buffer = 0;                    // 

        System.out.printf ("Run...%n");
        loop:
        try {
            for (;;) {
                command = memory [pc++];
                ++cycles;
                //    
                if (command >= 0) {
                    dstack [++dsp] = command;
                    continue;
                }
                switch (command) {
                    // 
                    case -1:
                        buffer = dstack [dsp];
                        dstack [++dsp] = buffer;
                        break;
                    // 
                    case -2:
                        dsp--;
                        break;
                    // 
                    case -3:
                        buffer = dstack [dsp -1];
                        dstack [dsp - 1] = dstack [dsp];
                        dstack [dsp] = buffer;
                        break;
                    // 
                    case -4:
                        buffer = dstack [dsp - 1];
                        dstack [++dsp] = buffer;
                        break;
                    //   
                    case -5:
                        buffer = dstack [dsp];
                        dsp--;
                        rstack [++rsp] = buffer;
                        break;
                    //   
                    case -6:
                        buffer = rstack [rsp];
                        rsp--;
                        dstack [++dsp] = buffer;
                        break;
                    // 
                    case -7:
                        dstack [dsp - 1] = dstack [dsp - 1] + dstack [dsp];
                        dsp--;
                        break;
                    // 
                    case -8:
                        dstack [dsp - 1] = dstack [dsp - 1] - dstack [dsp];
                        dsp--;
                        break;
                    // 
                    case -9:
                        dstack [dsp - 1] = dstack [dsp - 1] * dstack [dsp];
                        dsp--;
                        break;
                    //   
                    case -10:
                        int q = dstack [dsp - 1] / dstack [dsp],
                            r = dstack [dsp - 1] % dstack [dsp];
                        //  -   ,  -  
                        dstack [dsp - 1] = r;
                        dstack [dsp] = q;
                        break;
                    //  
                    case -11:
                        dstack [dsp] = -dstack [dsp];
                        break;
                    //  
                    case -12:
                        dstack [dsp] = Math.abs (dstack [dsp]);
                        break;
                    //  "AND"
                    case -13:
                        dstack [dsp - 1] = dstack [dsp - 1] & dstack [dsp];
                        dsp--;
                        break;
                    //  "OR"
                    case -14:
                        dstack [dsp - 1] = dstack [dsp - 1] | dstack [dsp];
                        dsp--;
                        break;
                    //  "XOR"
                    case -15:
                        dstack [dsp - 1] = dstack [dsp - 1] ^ dstack [dsp];
                        dsp--;
                        break;
                    //  
                    case -16:
                        dstack [dsp] = dstack [dsp] << 1;
                        break;
                    //  
                    case -17:
                        dstack [dsp] = dstack [dsp] >> 1;
                        break;
                    //  
                    case -18:
                        pc = dstack [dsp--];
                        break;
                    //   
                    case -19:
                        if (dstack [dsp - 1] < 0) {
                            pc = dstack [dsp--];
                            dsp--;
                        } else
                            dsp -= 2;
                        break;
                    //   
                    case -20:
                        if (dstack [dsp - 1] == 0) {
                            pc = dstack [dsp--];
                            dsp--;
                        } else
                            dsp -= 2;
                        break;
                    //   
                    case -21:
                        if (dstack [dsp - 1] > 0) {
                            pc = dstack [dsp--];
                            dsp--;
                        } else
                            dsp -= 2;
                        break;
                    //  
                    case -22:
                        rstack [++rsp] = pc;
                        pc = dstack [dsp];
                        dsp--;
                        break;
                    //   
                    case -23:
                        pc = rstack [rsp--];
                        break;
                    //   
                    case -24:
                        buffer = dstack [dsp];
                        dstack [dsp] = memory [buffer];
                        break;
                    //   
                    case -25:
                        buffer = dstack [dsp--];
                        memory [buffer] = dstack [dsp];
                        dsp--;
                        break;
                    //  
                    case -26:
                        dstack [++dsp] = readInt ();
                        break;
                    //   
                    case -27:
                        System.out.printf ("%d%n", dstack [dsp--]);
                        break;
                    //   
                    case -28:
                        System.out.printf ("%s", (char)dstack [dsp--]);
                        break;
                    //   PC
                    case -29:
                        dstack [++dsp] = pc - 1;
                        break;
                    //   
                    case -30:
                        if (dsp < 0) {
                            dstack [++dsp] = 0;
                        } else {
                            dstack [++dsp] = dsp;
                        }
                        break;
                    //  
                    case -31:
                        break;
                    // 
                    case -40:
                        System.out.printf ("Program completed%n");
                        break loop;
                    //  ,     
                    default:
                        System.err.printf ("Undefined operation code %d%n",
                                           command);
                        System.err.printf ("Program aborted%n");
                        break loop;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            System.err.println ("Memory empty or destroyed");
        }
    }

    //    
    private int readInt () {
        final Scanner in = new Scanner (System.in);
        int value = 0;

        System.out.printf ("Number: ");
        value = in.nextInt ();
        return value;
    }

    //  ,       
    private void printStatus () {
        //    
        if (dsp >= 0) {
            System.out.printf  ("%nDATA STACK:%n");
            System.out.printf  ("+---------+-----------+%n");
            System.out.printf  ("|   DSP   |   Value   |%n");
            System.out.printf  ("+---------+-----------+%n");
            for (int i = 0; i <= dsp; i++) {
                System.out.printf ("|%9d|%11d|%n", i, dstack [i]);
            }
            System.out.printf  ("+---------+-----------+%n");
        }
        if (rsp >= 0) {
            System.out.printf  ("%nRETURN STACK:%n");
            System.out.printf  ("+---------+-----------+%n");
            System.out.printf  ("|   RSP   |   Value   |%n");
            System.out.printf  ("+---------+-----------+%n");
            for (int i = 0; i <= rsp; i++) {
                System.out.printf ("|%9d|%11d|%n", i, rstack [i]);
            }
            System.out.printf  ("+---------+-----------+%n");
        }
        //    
        Set <Map.Entry <String, Integer>> entrys = symb.symtable.entrySet ();
        System.out.printf ("%nCONTENTS OF%n");
        //   
        for (Map.Entry<String, Integer> entry : entrys) {
            if (entry.getValue () >= 0)
                System.out.printf ("[%12s] = %-8d%n",
                                   entry.getKey (),
                                   memory [entry.getValue ()]);
        }
        // 
        System.out.printf ("   dsp = %d%n", dsp);
        System.out.printf ("   rsp = %d%n", rsp);
        System.out.printf ("    pc = %d%n", pc);
        System.out.printf ("cycles = %d%n", cycles);
    }
}
