package sk.fri.uniza;

public interface ObservableInteractor {
    void registerObserver(ObserverInteractor observer);

    void notifyAllObservers(PostingSheet postingSheet);
}
