package com.tech.pierandrei.Conversio.core.utils.EmailUtils;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoadComponentsUtils {
    public String loadFooterOneTemplate() throws IOException {
        return new String(getClass().getResourceAsStream("/footers/footer-01.html").readAllBytes());
    }
}
