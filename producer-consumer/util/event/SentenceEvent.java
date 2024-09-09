package prodcons.util.event;

import java.util.EventObject;

public class SentenceEvent extends EventObject {

    private final String sentence;

    public SentenceEvent(Object source, String s) {
        super(source);
        sentence = s;
    }

    public String getSentence() {
        return sentence;
    }
}
