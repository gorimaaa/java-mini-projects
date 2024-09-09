package prodcons.model.actor;

import prodcons.model.Box;
import util.Contract;

public class StdProducer extends AbstractActor {

    // ATTRIBUTS STATIQUES

    // nombre d'instances de producteurs
    private static int instNb = 0;

    // ATTRIBUTS

    // nombre maximal à produire
    private final int maxVal;

    // CONSTRUCTEURS

    /**
     * Un producteur devant travailler mi fois, sur une boite b.
     * La production ne devra pas dépasser mv.
     * @pre <pre>
     *     mi > 0
     *     b != null
     *     mv > 0 </pre>
     * @post <pre>
     *     getBox() == b </pre>
     */
    public StdProducer(int mi, int mv, Box b) {
        super("P" + (++instNb), mi, b);
        Contract.checkCondition(mv > 0);

        maxVal = mv + 1;
    }

    // OUTILS

    @Override
    protected boolean canUseBox() {
        return getBox().isEmpty();
    }

    @Override
    protected void useBox() throws InterruptedException {
        int value = (int) (Math.random() * maxVal);
        spendTime();
        getBox().fill(value);
        fireSentenceSaid("box <-- " + value);
    }
}
