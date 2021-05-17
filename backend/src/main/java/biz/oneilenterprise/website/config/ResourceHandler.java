package biz.oneilenterprise.website.config;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component(value = "handler")
public class ResourceHandler extends ResourceHttpRequestHandler {

    public static final String ATTR_FILE = ResourceHandler.class.getName() + ".file";

    @Override
    public Resource getResource(HttpServletRequest request) throws IOException {

        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}