package prodcons.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

import prodcons.model.actor.*;
import prodcons.model.actor.StdConsumer;
import prodcons.model.actor.StdProducer;
import prodcons.util.Formatter;
import prodcons.util.event.SentenceEvent;
import prodcons.util.event.SentenceListener;
import util.Contract;

public class StdProdConsModel implements ProdConsModel {

    // ATTRIBUTS STATIQUES

    private static final int MAX_VALUE = 100;

    // ATTRIBUTS

    private final Actor[] actors;
    private Box box;
    private volatile int prodNumber;
    private volatile int consNumber;
    private final PropertyChangeSupport support;

    private boolean running;
    private volatile boolean frozen;

    // CONSTRUCTEURS

    public StdProdConsModel(int prod, int cons, int iter) {
        Contract.checkCondition(prod > 0 && cons > 0 && iter > 0);

        box = new UnsafeBox();
        prodNumber = prod;
        consNumber = cons;
        actors = new Actor[prodNumber + consNumber];
        for (int i = 0; i < prodNumber; i++) {
            actors[i] = new StdProducer(iter, MAX_VALUE, box);
        }
        for (int i = prodNumber; i < prodNumber + consNumber; i++) {
            actors[i] = new StdConsumer(iter, box);
        }
        support = new SwingPropertyChangeSupport(this, true);
        
        // Relai des phrases émises par les acteurs
        
        FrozenDetector fr = new FrozenDetector();
        for(int i = 0; i < prodNumber + consNumber; i++) {
        	actors[i].addSentenceListener(new SentenceListener() {
				@Override
				public void sentenceSaid(SentenceEvent e) {
					support.firePropertyChange(PROP_SENTENCE, null, e.getSentence());
				}
        		
        	});
        	actors[i].addPropertyChangeListener(Actor.PROP_ACTIVE, fr);
        }
        frozen = false;
    }

    // REQUETES

    @Override
    public Box box() {
        return box;
    }

    @Override
    public Actor consumer(int i) {
        Contract.checkCondition(0 <= i && i < consNumber);

        return actors[prodNumber + i];
    }

    @Override
    public int consumersNb() {
        return consNumber;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Actor producer(int i) {
        Contract.checkCondition(0 <= i && i < prodNumber);

        return actors[i];
    }

    @Override
    public int producersNb() {
        return prodNumber;
    }
    
    // COMMANDES

    @Override
    public void addPropertyChangeListener(String pName,
                PropertyChangeListener lnr) {
        Contract.checkCondition(pName != null && lnr != null);

        support.addPropertyChangeListener(pName, lnr);
    }

    @Override
    public void start() {
        box.clear();
        Formatter.resetTime();
        frozen = false;
        setRunning(true);
        for(int i = 0; i < prodNumber + consNumber; i++) {
    		actors[i].start();
        }
    }

    @Override
    public void stop() {
    	Thread erase = new Thread(new EraserTask());
    	erase.start();
    	setRunning(false);
    }

    // OUTILS
    
    private void setRunning(boolean b) {
        boolean oldRunning = isRunning();
        running = b;
        support.firePropertyChange(PROP_RUNNING, oldRunning, b);
    }
    
    private void setFrozen() {
    	frozen = true;
    	support.firePropertyChange(PROP_FROZEN, false, frozen);
    }
    
    private class FrozenDetector implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			synchronized(Actor.lock){
		    	boolean isblockedprod = true;
		    	boolean isdeadprod = true;
		    	for (int i = 0; i < prodNumber; i++) {
		    		if(actors[i].isActive()) {
		    			isblockedprod = false;
		    		}
		    		if(actors[i].isAlive()) {
		    			isdeadprod = false;
		    		}
		        }
		    	isblockedprod = isdeadprod ? false : true;
		    	
		    	boolean isblockedcons = true;
		    	boolean isdeadcons = true;
		        for (int i = prodNumber; i < prodNumber + consNumber; i++) {
		        	if(actors[i].isActive()) {
		        		isblockedcons = false;
		    		}
		    		if(actors[i].isAlive()) {
		    			isdeadcons = false;
		    		}
		        }
		        isblockedcons = isdeadcons ? false : true;        
		        
		        if((isdeadprod && isblockedcons) || (isdeadcons && isblockedprod) ) {
		        	setFrozen();
		        	
		        }
			}

			
		}
    	
    }
    
    private class EraserTask implements Runnable{
    	public void run() {
    		for(int i = 0; i < prodNumber + consNumber; i++) {
        		if(actors[i].isAlive()) {
        			actors[i].interruptAndWaitForTermination();
        		}
            }
    	}
    	
    }
    
    
}
