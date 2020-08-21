package javax.persistence;

import lombok.NoArgsConstructor;

/**
 * @author Launcel
 */
@NoArgsConstructor
public enum GenerationType
{
    TABLE,
    SEQUENCE,
    IDENTITY,
    AUTO,
    ;
}
