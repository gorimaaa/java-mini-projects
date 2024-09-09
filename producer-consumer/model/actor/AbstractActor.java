package prodcons.model.actor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import prodcons.model.Actor;
import prodcons.model.Box;
import prodcons.util.Formatter;
import prodcons.util.event.SentenceListener;
import prodcons.util.event.SentenceSupport;
import util.Contract;

/**
 * Implantation partielle de la classe Actor.
 * Met en place le mécanisme de threads propre aux acteurs.
 */
abstract class AbstractActor implements Actor {

    // ATTRIBUTS
	
	 // verrou pour fireSentence
    public final static Object lockSentence = new Object();
    

    // nombre d'actions que cet acteur effectuera
    private final int maxIter;

    // formateur associé à cet acteur
    private final Formatter fmt;

    // support pour l'envoi de SentenceEvents
    private final SentenceSupport sentSup;

    // support pour l'envoi de PropertyChangeEvents
    private final PropertyChangeSupport changeSup;

    // boite associée à cet acteur
    private final Box box;

    // code cible de workerThread
    private final TaskCode task;

    // thread d'activation
    private volatile Thread thread;
    
   

    // CONSTRUCTEURS

    /**
     * Un acteur de nom n, agissant mi fois sur la boite b.
     * @pre <pre>
     *     n != null && !n.equals("")
     *     mi > 0
     *     b != null </pre>
     * @post <pre>
     *     getMaxIterNb() == mi
     *     getBox() == b
     *     !isAlive() </pre>
     */
    protected AbstractActor(String n, int mi, Box b) {
        Contract.checkCondition(n != null && !n.equals(""));
        Contract.checkCondition(mi > 0);
        Contract.checkCondition(b != null);

        box = b;
        maxIter = mi;
        fmt = new Formatter(n);
        sentSup = new SentenceSupport(this);
        changeSup = new PropertyChangeSupport(this);
        task = new TaskCode();
    }

    // REQUETES

    @Override
    public Box getBox() {
        return box;
    }

    @Override
    public int getMaxIterNb() {
        return maxIter;
    }

    @Override
    public boolean isActive() {
        return task.isActive();
    }

    @Override
    public boolean isAlive() {
        return thread != null && thread.isAlive();
    }

    // COMMANDES

    @Override
    public void addPropertyChangeListener(String pName,
            PropertyChangeListener lnr) {
        Contract.checkCondition(pName != null && lnr != null);

        changeSup.addPropertyChangeListener(pName, lnr);
    }

    @Override
    public void addSentenceListener(SentenceListener lnr) {
        Contract.checkCondition(lnr != null);
        
        sentSup.addSentenceListener(lnr);
    }

    @Override
    public void interruptAndWaitForTermination() {
        Contract.checkCondition(isAlive());
        thread.interrupt();
        while(thread.isAlive()) {
	        try {
	        	thread.join();
	        }catch(InterruptedException error) {
	        	error.printStackTrace();
	        }
        }
        
    }

    @Override
    public void start() {
        Contract.checkCondition(!isAlive());

        thread = new Thread(task);
        thread.start();
    }
    
    @Override
    public String toString() {
        return fmt.name();
    }

    // OUTILS
    
    /**
     * Indique si cet acteur peut utiliser sa boîte.
     */
    protected abstract boolean canUseBox();

    /**
     * L'acteur, selon sa spécification, utilise sa boite.
     * Une phrase est émise pour décrire l'action effectuée par l'acteur sur
     *  sa boite.
     * @pre <pre>
     *     canUseBox() </pre>
     * @post <pre>
     *     la boite a été utilisée selon la nature de l'acteur </pre>
     */
    protected abstract void useBox() throws InterruptedException;
    
    /**
     * Retarde un peu, et aléatoirement, l'exécution (à utiliser dans useBox()).
     */
    protected void spendTime() throws InterruptedException {
        final int minDelay = 20;
        final int maxDelay = 50;
        Thread.sleep(minDelay + (int) (Math.random() * maxDelay));
    }

    /**
     * Emet la phrase s correctement formatée.
     * La construction du message et sa notification constituent une
     *  seule opération atomique, gardée par le même verrou pour tous les
     *  acteurs.
     * Sinon les acteurs "se couperaient la parole" et des beugs comme ceux
     *  enregistrés par Logger.logIncoherentTime pourraient se produire.
     */
    protected void fireSentenceSaid(String s) {
        synchronized(lockSentence){
        	sentSup.fireSentenceSaid(fmt.format(s));
        }
    }

    // TYPES IMBRIQUES

    private final class TaskCode implements Runnable {

        // reflète l'état d'activité du thread de l'acteur,
        private boolean active;

        boolean isActive() {
            return active;
        }
        
        @Override
        public void run() {
            fireSentenceSaid("Naissance");
            setActive(true);
            int currentIter = 0;
            while (!thread.isInterrupted() && currentIter < maxIter) {
                currentIter += 1;
                fireSentenceSaid("Début de l'étape " + currentIter);
                oneStep();
                if (!thread.isInterrupted()) {
                    fireSentenceSaid("Fin de l'étape " + currentIter);
                }
            }
            fireSentenceSaid(thread.isInterrupted()
                    ? "Mort subite" : "Mort naturelle");
            setActive(false);
        }

        // Politique d'interruption :
        // positionner le flag d'interruption du thread interne.
        private void oneStep() {
            synchronized(lock) {
            	while(!thread.isInterrupted() && !canUseBox()) {
            		fireSentenceSaid("Rentre dans SA");
            		setActive(false);
            		try {
            			lock.wait();
            		}catch(InterruptedException error) {
            			thread.interrupt();
            		}
            		setActive(true);
            		fireSentenceSaid("Sort du SA");
            	}
            	
            	if(!thread.isInterrupted()) {
            		try {
            			useBox();
            			lock.notifyAll();
            		}
            		catch(InterruptedException error) {
            			thread.interrupt();
            		}
            	}
            }
        }
        
        private void setActive(boolean a) {
            boolean oldActive = isActive();
            active = a;
            changeSup.firePropertyChange(PROP_ACTIVE, oldActive, a);
        }
    }
}
