//  
package toystackmachine;

import java.util.*;

public class SymbolTable {
    //  
    static final int UNDEFSYMBOL = -1;
    public static Map <String, Integer> symtable;

    public SymbolTable () {
    }
    static {
        //      
        symtable = new TreeMap <> ();
    }

    //  
    public boolean lookupSymbol (String name) {
        return symtable.get (name) != null;
    }

    //  
    public void putSymbol (String name, int value) {
        if (!lookupSymbol (name)) {
            symtable.put (name, value);
        }
    }

    //  () 
    public int getValue (String name) {
        if (lookupSymbol (name)) {
            return symtable.get (name);
        }
        else {
            return UNDEFSYMBOL;
        }
    }

    //   
    public void printSymtable () {
        Set <Map.Entry <String, Integer>> entries = symtable.entrySet ();

        //   
        if (entries.isEmpty ()) return;
        //    
        System.out.printf ("%nSYMBOL TABLE%n");
        System.out.printf ("-----------------------%n");
        for (Map.Entry <String, Integer> entry: entries) {
            String key = entry.getKey ();
            System.out.printf ("%-16s %06d%n",
                               key,
                               getValue (key));
        }
        System.out.printf ("%n");
    }
}
