package biz.oneilindustries.website.dto;

import static biz.oneilindustries.AppConfig.BACK_END_URL;

import java.util.HashMap;

public class ShareXConfig {
    private String name = "Oneil Industries";
    private String destinationType = "ImageUploader, TextUploader, FileUploader";
    private String requestMethod = "POST";
    private String requestURL = BACK_END_URL + "/gallery/upload";
    private final HashMap<String, Object> parameters = new HashMap<>();
    private final HashMap<String, Object> headers = new HashMap<>();
    private String body = "MultipartFormData";
    private String FileFormName = "file";

    public ShareXConfig(String apiToken) {
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
