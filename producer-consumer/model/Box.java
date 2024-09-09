package prodcons.model;

/**
 * Une boite est une ressource partagée pouvant contenir un entier.
 * Les producteurs mettent des entiers dans une boite, et les consommateurs
 *  les y enlèvent.
 * @inv <pre>
 *     isEmpty() <==> value() == null </pre>
 * @cons <pre>
 *     $POST$ isEmpty() </pre>
 */
public interface Box {

    // REQUETES

    /**
     * Le contenu de la boite.
     */
    int value();

    /**
     * La boite est-elle vide ?
     */
    boolean isEmpty();

    // COMMANDES

    /**
     * Vide la boite.
     * @post <pre>
     *     isEmpty() </pre>
     */
    void clear();

    /**
     * Remplit la boite avec l'entier v.
     * @pre <pre>
     *     isEmpty() </pre>
     * @post <pre>
     *     value() == v </pre>
     */
    void fill(int v);
}
