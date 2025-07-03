package org.vaadin.addons.googlerecaptcha.v3;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.googlerecaptcha.RecaptchaVerifier;

import java.util.Map;


@Tag( "recaptcha-component-v3" )
@Getter
@Setter
public class RecaptchaComponentv3 extends Component
{
    private final RecaptchaVerifier recaptchaVerifier = new RecaptchaVerifier();
    private RecaptchaCallback callback;
    private final String dataSiteKey;
    private final String secretKey;

    public RecaptchaComponentv3( String dataSiteKey, String secretKey )
    {
        this.dataSiteKey = dataSiteKey;
        this.secretKey = secretKey;

        Page page = UI.getCurrent().getPage();
        page.addJavaScript( "https://www.google.com/recaptcha/api.js?render=" + dataSiteKey );
    }

    public void checkInteraction()
    {
        getElement().executeJs( """
                    if (typeof grecaptcha !== "undefined") {
                        grecaptcha.ready(function() {
                             grecaptcha.execute($1, {action: 'submit'}).then(function(token) {
                                 $0.$server.handleRecaptchaResponse(token);
                             });
                        });
                    }
                """, this, dataSiteKey );
    }

    @ClientCallable
    public void handleRecaptchaResponse( String token )
    {
        Map<String, Object> stringObjectMap = recaptchaVerifier.verifyRecaptcha( token, secretKey );
        if ( callback != null )
        {
            callback.onResult( stringObjectMap );
        }
    }
}
