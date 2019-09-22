package toystackmachine;

import java.util.*;

public class Assembler {
    static final int TT_EOL   = 0,          //    
                     TT_CODE  = 1,          // 
                     TT_LABEL = 2,          // 
                     TT_DATA  = 3,          //  (,   )
                     TT_ERROR = -1;         // 

    private ArrayList <String> source;
    private SymbolTable st = new SymbolTable ();
    private OpcodeTable ct = new OpcodeTable ();
    private ArrayList <Integer> obj = new ArrayList <> ();

    private String  currLine,               //  
                    token;                  // 
    private int     passNumber,             //  
                    loc,                    //  
                    lineNumber,             //    
                    tokenType,              //  
                    errorCount;             //  
    
    public Assembler (ArrayList <String> source) {
        this.source = source;
        parse ();
        //    -   
        printObjectCode ();
        st.printSymtable ();
        //   
        if (obj.isEmpty()) {
            System.out.printf ("Nothing to run%n");
            System.exit (0);
        } else {
            StackMachine stm = new StackMachine (obj);
        }
    }

    //  
    private void parse () {
        //  
        passNumber = 1;
        System.out.printf ("Parsing (pass %d): ", passNumber);
        passOne ();
        checkErrors ();
        //  
        passNumber = 2;
        System.out.printf ("Parsing (pass %d): ", passNumber);
        passTwo ();
        checkErrors ();
    }

    //    
    private void checkErrors () {
        if (errorCount != 0) {
            System.err.printf ("detected %d error(s)%n", errorCount);
            System.exit (0);
        } else {
            System.out.printf ("ok%n");
        }
    }

    //   ( )
    private void passOne () {
        lineNumber = 0;
        loc = 0;
        for (int i = 0; i < source.size (); i++) {
            lineNumber++;
            currLine = source.get (i);
            tokenType = getToken ();
            first ();
        }
    }

    //     
    private void first () {
        switch (tokenType) {
            case TT_EOL:
                break;
            case TT_LABEL:
                st.putSymbol (token, loc);
                tokenType = getToken ();
                first ();
                break;
            case TT_CODE: case TT_DATA:
                tokenType = getToken ();
                break;
            case TT_ERROR:
                errorCount++;
                break;
        }
    }

    //   (  )
    private void passTwo () {
        lineNumber = 0;
        loc = 0;
        for (int i = 0; i < source.size (); i++) {
            lineNumber++;
            currLine = source.get (i);
            tokenType = getToken ();
            second ();
        }
    }

    //     
    private void second () {
        switch (tokenType) {
            case TT_LABEL:
                tokenType = getToken ();
                second ();
                break;
            case TT_EOL: case TT_CODE: case TT_DATA:
                break;
            case TT_ERROR:
                errorCount++;
                break;
        }
    }

    //     
    private int getToken () {
        //  (  ";"  "#"    )
        if (currLine.contains (";")) {
            currLine = currLine.substring (0, currLine.indexOf (";")).trim ();
        }
        if (currLine.contains ("#")) {
            currLine = currLine.substring (0, currLine.indexOf ("#")).trim ();
        }
        //  -    
        if (currLine.isEmpty ()) {
            return TT_EOL;
        }

        //   (  ":")
        if (currLine.startsWith (":")) {
            //  -     
            if (currLine.contains (" ")) {
                token = currLine.substring (1, currLine.indexOf (" "));
            } else {
                //  -    
                token = currLine.substring (1);
            }
            //   
            if (isValidLabel (token)) {
                if (currLine.length () > token.length ()) {
                    currLine = currLine.substring (token.length () + 1).trim ();
                } else {
                    currLine = "";
                }
                return tokenType = TT_LABEL;
            } else {
                //   
                return tokenType = TT_ERROR;
            }
        }
        //    
        if (currLine.isEmpty ()) {
            return tokenType = TT_EOL;
        }

        //   
        token = currLine;
        //  ,     
        switch (passNumber) {
            //   -   
            case 1:
                loc++;
                return tokenType = TT_EOL;
            //   -  
            case 2:
                // 
                if (isNumber (token)) {
                    obj.add (parseWord (token));
                    loc++;
                    return tokenType = TT_DATA;
                }
                //   
                if (st.lookupSymbol (token)) {
                    obj.add (st.getValue (token));
                    loc++;
                    return tokenType = TT_DATA;
                }
                // 
                if (ct.isMnemonic (token)) {
                    obj.add (ct.getOpcode (token));
                    loc++;
                    return tokenType = TT_CODE;
                }
                error ("undefined symbol or code");
                return tokenType = TT_ERROR;
        }

        //  
        return TT_EOL;
    }

    //    
    private boolean isValidLabel (String lbl) {
        switch (passNumber) {
            //      
            case 1:
                //  
                if (lbl.isEmpty ()) {
                    error ("label expected");
                    return false;
                }
                //   
                if (st.lookupSymbol (lbl)) {
                    error ("duplicated label");
                    return false;
                }
                break;
            //      
            case 2:
                break;
        }
        return true;
    }

    //    
    private boolean isNumber (String operand) {
        try {
            parseWord (operand);
        }
        catch (Exception ex) {return false;}
        return true;
    }

    //       10
    private int parseWord (String lit) {
        return Integer.parseInt (lit, 10);
    }

    //    
    private void error (String msg) {
        System.err.printf ("ERROR: %s [line: %d, pass: %d]%n",
                           msg,
                           lineNumber,
                           passNumber);
    }

    //   
    private void printObjectCode () {
        int initAddr = 0;

        //   
        if (obj.isEmpty ()) return;
        //    
        System.out.printf ("%nOBJECT CODE%n");
        System.out.printf ("-----------------------%n");
        Iterator i = obj.iterator ();
        while (i.hasNext ()) {
            System.out.printf ("%06d         %8d%n",
                               initAddr++,
                               i.next ());
        }
        System.out.printf ("%n");
    }
}
