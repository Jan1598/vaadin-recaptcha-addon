package org.vaadin.addons.googlerecaptcha.v2;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import lombok.Getter;
import org.vaadin.addons.googlerecaptcha.RecaptchaVerifier;

import java.util.Map;


@Tag( "recaptcha-component-v2" )
@Getter
@JavaScript( "https://www.google.com/recaptcha/api.js" )
public class RecaptchaComponentv2 extends Component
{
    private final RecaptchaVerifier recaptchaVerifier = new RecaptchaVerifier();
    private final String dataSiteKey;
    private final String secretKey;

    private boolean valid;

    public RecaptchaComponentv2( String dataSiteKey, String secretKey, String theme )
    {
        this.dataSiteKey = dataSiteKey;
        this.secretKey = secretKey;

        setId( "recaptcha-container-v2" );
        getElement().setAttribute( "class", "g-recaptcha" );
        getElement().setAttribute( "data-sitekey", dataSiteKey );
        getElement().setAttribute( "data-callback", "tokenCallback" );
        getElement().setAttribute( "data-theme", theme );

        loadRecaptcha();
    }

    public RecaptchaComponentv2( String dataSiteKey, String secretKey )
    {
        this( dataSiteKey, secretKey, "light" );
    }

    private void loadRecaptcha()
    {
        UI.getCurrent().getPage().executeJs( """
                    window.onReCaptchaLoad = function () {
                        if (typeof grecaptcha !== "undefined") {
                            grecaptcha.render($0, {
                                'sitekey': $1,
                                'callback': tokenCallback
                            });
                        }
                    };
                
                    function tokenCallback(token) {
                        $0.$server.callback(token);
                    }
                    window.tokenCallback = tokenCallback;
                """, this, dataSiteKey );

        reloadRecaptcha();
    }

    public void reloadRecaptcha()
    {
        getElement().executeJs( """
                    if (typeof grecaptcha !== "undefined") {
                        document.getElementById("recaptcha-container-v2").innerHTML = "";
                        grecaptcha.render($0, {
                            'sitekey': $1,
                            'callback': tokenCallback
                        });
                    }
                """, this, getElement().getAttribute( "data-sitekey" ) );
    }

    public void resetRecaptcha()
    {
        getElement().executeJs( """
                    if (typeof grecaptcha !== "undefined") {
                        grecaptcha.reset();
                    }
                """ );
        valid = false;
    }

    @ClientCallable
    public void callback( String response )
    {
        Map<String, Object> stringObjectMap = recaptchaVerifier.verifyRecaptcha( response, secretKey );
        valid = stringObjectMap != null && Boolean.TRUE.equals( stringObjectMap.get( "success" ) );
    }

}
