package prodcons.util;

public class Logger {
    
    private static final String TAB = "    ";
    
    private int lastTime;
    
    public void reset() {
        lastTime = 0;
    }
    
    public void logIncoherentTime(String sentence) {
        int newTime = Integer.parseInt(sentence.split(" ")[0]);
        if (newTime < lastTime) {
            String msg = "Phrase dite à " + newTime + " après " + lastTime
                    + "\n" + TAB + sentence;
            logMessage(msg);
        }
        lastTime = newTime;
    }
    
    /**
     * Cette méthode est synchronisée car elle doit s'exécuter de manière
     *  atomique alors qu'elle est appelée par plusieurs threads.
     */
    public static synchronized void logMessage(String m) {
        System.out.println(m);
    }
}
