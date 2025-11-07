package com.technicalchallenge.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean valid = true;
    private List<String> errors = new ArrayList<>();

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }

    public void addAll(ValidationResult other) {
        if (!other.isValid()) {
            this.valid = false;
            this.errors.addAll(other.getErrors());
        }
    }

}
