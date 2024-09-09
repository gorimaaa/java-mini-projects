package prodcons.model.actor;

import prodcons.model.Box;

public class StdConsumer extends AbstractActor {

    // ATTRIBUTS STATIQUES

    // nombre d'instances de consommateurs
    private static int instNb = 0;

    // CONSTRUCTEURS

    /**
     * Un consommateur devant travailler mi fois, sur une boite b.
     * @pre <pre>
     *     mi > 0
     *     b != null </pre>
     * @post <pre>
     *     getBox() == b </pre>
     */
    public StdConsumer(int mi, Box b) {
        super("C" + (++instNb), mi, b);
    }

    // OUTILS

    @Override
    protected boolean canUseBox() {
        return !getBox().isEmpty();
    }

    @Override
    protected void useBox() throws InterruptedException {
        int value = getBox().value();
        spendTime();
        getBox().clear();
        fireSentenceSaid("box --> " + value);
    }
}
