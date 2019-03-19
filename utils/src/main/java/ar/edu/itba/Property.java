package ar.edu.itba;

public class Property<T> {
    T property;
    public Property(T property){
        this.property = property;
    }
    public T getProperty() {
        return property;
    }
}
