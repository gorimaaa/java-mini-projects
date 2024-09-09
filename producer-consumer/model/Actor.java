package prodcons.model;

import java.beans.PropertyChangeListener;

import prodcons.util.event.SentenceListener;

/**
 * Un acteur est un objet manipulant une boite.
 * Sa tâche consiste à faire au plus <code>getMaxIterNb()</code> fois
 *  quelquechose sur cette boîte.
 * On démarre l'acteur avec <code>start()</code>, et alors isAlive() devient
 *  vrai.
 * On peut stopper l'acteur à tout moment avec interruptAndWaitForTermination().
 * Une fois stoppé, isAlive() est faux.
 * L'acteur peut aussi s'arrêter de lui-même quand il a fini sa tâche.
 * Un acteur est un objet qui « fonctionne » tout seul, c'est-à-dire qu'il est
 *  animé par un thread interne, inaccessible de l'extérieur, seulement
 *  pilotable par le biais de certaines méthodes de cette classe.
 * Quand un acteur fait quelquechose avec une boite, il émet des SentenceEvent
 *  qui décrivent ce qu'il fait.
 * Pendant qu'un acteur exécute sa tâche, il est actif la plupart du temps et
 *  isActive() est vrai.
 * Parfois, il est bloqué dans l'attente de pouvoir utiliser sa boite, et dans
 *  ce cas isActive() est faux.
 *
 * @inv <pre>
 *     getMaxIterNb() > 0
 *     getBox() != null
 *     !isAlive() ==> !isActive() </pre>
 */
public interface Actor {
	// ATTRIBUTS 
	// verrou pour oneStep
    public final static Object lock = new Object();

    // CONSTANTES
	

    String PROP_ACTIVE = "active";

    // REQUETES

    /**
     * La boite associée à cet acteur.
     */
    Box getBox();

    /**
     * Le nombre maximal de fois que l'acteur peut faire quelque chose avant
     *  de s'arrêter.
     */
    int getMaxIterNb();

    /**
     * Indique si l'acteur est actif (ie. s'il n'est pas en attente de la boite
     *  au cours de son action).
     */
    boolean isActive();

    /**
     * Indique si l'acteur est encore vivant, c'est-à-dire si le thread
     *  qui l'anime a été démarré et n'a pas encore terminé son exécution.
     */
    boolean isAlive();

    // COMMANDES

    /**
     * Abonne un PropertyChangeListener auprès de cet acteur.
     * @pre <pre>
     *     pName != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la liste des écouteurs </pre>
     */
    void addPropertyChangeListener(String pName, PropertyChangeListener lnr);

    /**
     * Abonne un SentenceListener auprès de cet acteur.
     * @pre <pre>
     *     lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la liste des écouteurs </pre>
     */
    void addSentenceListener(SentenceListener lnr);

    /**
     * Interrompt l'acteur puis force le thread courant à attendre la mort du
     *  thread interne de l'acteur avant de continuer.
     * Dès que le thread interne va entrer dans une méthode bloquante, il va
     *  être interrompu et se terminer.
     * En théorie, si le thread interne n'était pas bloqué lors de l'appel de
     *  cette méthode, l'attente pourrait être longue ; en pratique ce ne sera
     *  pas le cas.
     * @pre <pre>
     *     isAlive() </pre>
     * @post <pre>
     *     !isAlive() </pre>
     */
    void interruptAndWaitForTermination();

    /**
     * Démarre un acteur, c'est-à-dire crée un nouveau thread interne et lance
     *  son exécution.
     * L'acteur commence à parler.
     * @pre <pre>
     *     !isAlive() </pre>
     * @post <pre>
     *     isAlive()
     *     l'action est démarrée </pre>
     */
    void start();
}
