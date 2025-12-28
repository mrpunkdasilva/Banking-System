package org.jala.university.commons.presentation;

import lombok.Data;

@Data
public abstract class BaseController {
    private ViewContext context;

    protected final ViewContext getContext() {
        return context;
    }
}
