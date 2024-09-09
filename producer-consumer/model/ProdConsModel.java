package prodcons.model;

import java.beans.PropertyChangeListener;

/**
 * @inv <pre>
 *     consumersNb() > 0
 *     producersNb() > 0
 *     box() != null
 *     forall i, 0 <= i < consumersNb() : consumer(i) != null
 *     forall i, 0 <= i < producersNb() : producer(i) != null
 *     isRunning()
 *         <==> exists (i, j), 0 <= i < consumersNb(), 0 <= j < producersNb() :
 *                  consumer(i).isAlive() || producer(j).isAlive()
 *     !isRunning() ==> !isFrozen() </pre>
 *
 * @cons <pre>
 *     $ARGS$ int prod, int cons, int iter
 *     $PRE$
 *         prod > 0
 *         cons > 0
 *         iter > 0
 *     $POST$
 *         producersNb() == prod
 *         consumersNb() == cons
 *         iter est le nombre de fois que chaque acteur utilisera la boite
 *         !isRunning() </pre>
 */
public interface ProdConsModel {

    // CONSTANTES

    String PROP_SENTENCE = "sentence";
    String PROP_RUNNING = "running";
    String PROP_FROZEN = "frozen";

    // REQUETES

    /**
     * La boite partagée entre tous les acteurs.
     */
    Box box();

    /**
     * Le i-ème consommateur.
     * @pre <pre>
     *     0 <= i < consumersNb() </pre>
     */
    Actor consumer(int i);

    /**
     * Le nombre de consommateurs.
     */
    int consumersNb();

    /**
     * Indique si tous les producteurs sont morts et si tous les consommateurs
     *  non morts (au moins un) sont bloqués, ou le contraire.
     */
    boolean isFrozen();

    /**
     * Indique si au moins un acteur est vivant.
     */
    boolean isRunning();

    /**
     * Le i-ème producteur.
     * @pre <pre>
     *     0 <= i < producersNb() </pre>
     */
    Actor producer(int i);

    /**
     * Le nombre de producteurs.
     */
    int producersNb();

    // COMMANDES

    /**
     * Ajoute un écouteur pour les changements de valeur de la propriété pName.
     * @pre <pre>
     *     pName != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la liste des écouteurs </pre>
     */
    void addPropertyChangeListener(String pName, PropertyChangeListener lnr);

    /**
     * Termine tous les acteurs encore vivants.
     * @post <pre>
     *     !isRunning() </pre>
     */
    void stop();

    /**
     * Démarre (ou redémarre) tous les acteurs.
     * @pre <pre>
     *     !isRunning() </pre>
     * @post <pre>
     *     forall i, 0 <= i < producersNb() : producer(i).isAlive()
     *     forall i, 0 <= i < consumersNb() : consumer(i).isAlive() </pre>
     */
    void start();
}
