package com.tech.pierandrei.Conversio.core.utils.EmailUtils;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoadTemplatesUtils {
    public String loadAlertTemplate() throws IOException {
        return new String(getClass().getResourceAsStream("/templates/alertTemplate.html").readAllBytes());
    }
    public String loadPromoTemplate() throws IOException {
        return new String(getClass().getResourceAsStream("/templates/promoTemplate.html").readAllBytes());
    }
    public String loadChargeTemplate() throws IOException {
        return new String(getClass().getResourceAsStream("/templates/chargeTemplate.html").readAllBytes());
    }
}
