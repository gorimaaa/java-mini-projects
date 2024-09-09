package prodcons.model;

import util.Contract;

/**
 * Implantation non « thread safe » des boîtes.
 */
public class UnsafeBox implements Box {

    // ATTRIBUTS

    private Integer value;

    // CONSTRUCTEURS

    public UnsafeBox() {
        value = null;
    }

    // REQUETES

    @Override
    public int value() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    // COMMANDES

    @Override
    public void clear() {
        value = null;
    }

    @Override
    public void fill(int v) {
        Contract.checkCondition(isEmpty());

        value = v;
    }
}
