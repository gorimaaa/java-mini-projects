package prodcons.util.event;

import javax.swing.event.EventListenerList;

public class SentenceSupport {
    
    // ATTRIBUTS

    private final Object owner;
    private EventListenerList listenerList;

    // CONSTRUCTEURS
    
    public SentenceSupport(Object owner) {
        this.owner = owner;
    }

    // REQUETES
    
    public SentenceListener[] getSentenceListeners() {
        if (listenerList == null) {
            return new SentenceListener[0];
        }
        return listenerList.getListeners(SentenceListener.class);
    }

    // COMMANDES
    
    public void addSentenceListener(SentenceListener listener) {
        if (listener == null) {
            return;
        }
        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        listenerList.add(SentenceListener.class, listener);
    }

    public void removeSentenceListener(SentenceListener listener) {
        if (listener == null || listenerList == null) {
            return;
        }
        listenerList.remove(SentenceListener.class, listener);
    }

    public void fireSentenceSaid(String sentence) {
        if (listenerList == null) {
            return;
        }
        Object[] list = listenerList.getListenerList();
        SentenceEvent e = new SentenceEvent(owner, sentence);
        for (int i = list.length - 2; i >= 0; i--) {
            ((SentenceListener) list[i + 1]).sentenceSaid(e);
        }
    }
}
