package imat.state;

public interface Observer {

    void subscribe();

    void unsubscribe();

    void update(Observable observable);

}
