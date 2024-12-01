package mx.edu.utez.integradora.utils;

public class ResponseObject {

    private Object response;
    private Type typeResponse;
    private String message;

    public ResponseObject(Object response, Type typeResponse, String message) {
        this.response = response;
        this.typeResponse = typeResponse;
        this.message = message;
    }

    public ResponseObject(Type typeResponse, String message) {
        this.typeResponse = typeResponse;
        this.message = message;
    }

    public ResponseObject(String message, Type typeResponse) {
        this.message = message;
        this.typeResponse = typeResponse;
    }

    public ResponseObject() {
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Type getTypeResponse() {
        return typeResponse;
    }

    public void setTypeResponse(Type typeResponse) {
        this.typeResponse = typeResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
