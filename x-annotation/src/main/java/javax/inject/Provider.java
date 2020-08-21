package javax.inject;

/**
 * @author Launcel
 */
public interface Provider<T>
{
    T get();
}
