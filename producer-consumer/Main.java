package prodcons;

import javax.swing.SwingUtilities;

import prodcons.gui.ProdCons;

/**
 * Les arguments attendus dans main() sont 3 entiers positifs qui indiquent
 *  le nombre de producteurs, de consommateurs et d'itérations, dans cet ordre.
 * Si ces nombres sont absents ou mal formés, ils sont remplacés par des valeurs
 *  par défaut.
 */
public final class Main {
    
    private static final int PROD_NB = 3;
    private static final int CONS_NB = 5;
    private static final int ITER_NB = 10;

    private Main() {
        // rien
    }
    
    public static void main(String[] args) {
        final int nP = (args.length >= 1)
                ? parseArg(args[0], PROD_NB) : PROD_NB;
        final int nC = (args.length >= 2)
                ? parseArg(args[1], CONS_NB) : CONS_NB;
        final int nIter = (args.length >= 3)
                ? parseArg(args[2], ITER_NB) : ITER_NB;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProdCons(nP, nC, nIter).display();
            }
        });
    }
    
    private static int parseArg(String arg, int def) {
        int res = def;
        try {
            int n = Integer.parseInt(arg);
            if (n >=  0) {
                res = n;
            }
        } catch (NumberFormatException e) {
            // rien : res vaut def
        }
        return res;
    }
}
