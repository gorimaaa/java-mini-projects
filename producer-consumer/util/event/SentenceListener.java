package prodcons.util.event;

import java.util.EventListener;

public interface SentenceListener extends EventListener {
    void sentenceSaid(SentenceEvent e);
}
