package biz.oneilenterprise.website.dto;

import java.util.HashMap;

public class ShareXConfig {
    private final String name = "Oneil Enterprise";
    private final String destinationType = "ImageUploader, TextUploader, FileUploader";
    private final String requestMethod = "POST";
    private final String requestURL;
    private final HashMap<String, Object> parameters = new HashMap<>();
    private final HashMap<String, Object> headers = new HashMap<>();
    private final String body = "MultipartFormData";
    private final String FileFormName = "file";

    public ShareXConfig(String apiToken, String backendUrl) {
        this.requestURL = backendUrl + "/gallery/upload";

        parameters.put("name", "%h.%mi.%s-%d.%mo.%yy");
        parameters.put("privacy", "unlisted");
        parameters.put("albumName", "none");

        headers.put("Authorization", "Bearer " + apiToken);
    }

    public String getName() {
        return name;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public HashMap<String, Object> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getFileFormName() {
        return FileFormName;
    }
}
