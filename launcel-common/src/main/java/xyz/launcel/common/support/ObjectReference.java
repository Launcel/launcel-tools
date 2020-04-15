package xyz.launcel.common.support;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ObjectReference<T>
{
    private T reference;

    public T get()
    {
        return reference;
    }

    public void set(T reference)
    {
        this.reference = reference;
    }
}
