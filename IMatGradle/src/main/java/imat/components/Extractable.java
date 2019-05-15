package imat.components;

import java.util.Optional;

public interface Extractable<T> {

    Optional<T> extract();

}
