package org.vaadin.addons.googlerecaptcha.v3;

import java.util.Map;

public interface RecaptchaCallback
{
    void onResult( Map<String, Object> stringObjectMap );
}
