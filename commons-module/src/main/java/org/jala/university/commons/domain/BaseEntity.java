package org.jala.university.commons.domain;

import java.io.Serializable;

public interface BaseEntity<ID> extends Serializable {
    ID getId();
}
