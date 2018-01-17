package javax.persistence;

/**
 * @author Launcel
 */
public enum GenerationType
{
    TABLE,
    SEQUENCE,
    IDENTITY,
    AUTO;

    private GenerationType()
    {
    }
}
