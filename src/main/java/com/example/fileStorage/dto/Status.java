package com.example.fileStorage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonProperty("ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    public Status() {

    }

    public Status(Boolean success) {
        this.success = success;
    }

    public Status(Boolean success, String error) {

        this.success = success;
        this.error = error;
    }

    public Status(String id) {
        this.id = id;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
