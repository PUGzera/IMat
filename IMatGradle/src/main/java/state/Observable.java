package state;

public interface Observable {

    void notifyObservers();

    void addObserver(Observer observer);

    void removeSubscriber(Observer observer);

}