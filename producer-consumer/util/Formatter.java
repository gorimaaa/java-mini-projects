package prodcons.util;

public class Formatter {

    // CONSTANTES STATIQUES
    
    // INDENT = 15 espaces + 1 barre verticale
    private static final String INDENT = "               |";
    // ratio pour convertir les nanosecondes en millisecondes
    private static final int NANO_TO_MILLI = 1000000;
    
    // ATTRIBUTS STATIQUES

    // numéro d'identification des formateurs, permettant de calculer
    // une indentation unique pour chaque formateur
    private static int idNb = 0;
    // mémorise l'instant initial d'une exécution
    private static long startTime;

    // ATTRIBUTS

    // indentation unique de ce formateur
    // un formateur possède un bloc d'indentation équivalent à INDENT ^ idNb
    private final String indent;
    // nom de l'acteur associé à ce formateur
    private final String name;

    // CONSTRUCTEURS

    public Formatter(String n) {
        name = n;
        StringBuffer idt = new StringBuffer();
        for (int i = 0; i < idNb; i++) {
            idt.append(INDENT);
        }
        indent = idt.toString();
        idNb += 1;
    }

    // REQUETES

    public String format(String m) {
        String prefix = getTime() + " ms> " + indent + name + " : ";
        return prefix + m + "\n";
    }
    
    public String name() {
        return name;
    }

    // OUTILS

    /**
     * Cette opération doit être atomique relativement à resetTime
     *  et à elle-même.
     */
    private static synchronized int getTime() {
        return (int) ((System.nanoTime() - startTime) / NANO_TO_MILLI);
    }

    /**
     * Cette opération doit être atomique relativement à getTime
     *  et à elle-même.
     */
    public static synchronized void resetTime() {
        startTime = System.nanoTime();
    }
}
