package org.vaadin.addons.googlerecaptcha;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import lombok.Getter;


@Tag( "recaptcha-component" )
@Getter
@JavaScript( "https://www.google.com/recaptcha/api.js" )
public class RecaptchaComponent extends Component
{
    private final RecaptchaVerifier recaptchaVerifier = new RecaptchaVerifier();
    private final String dataSiteKey;
    private final String secretKey;

    private boolean valid;

    public RecaptchaComponent( String dataSiteKey, String secretKey, String theme )
    {
        this.dataSiteKey = dataSiteKey;
        this.secretKey = secretKey;

        setId( "recaptcha-container" );
        getElement().setAttribute( "class", "g-recaptcha" );
        getElement().setAttribute( "data-sitekey", dataSiteKey );
        getElement().setAttribute( "data-callback", "myCallback" );
        getElement().setAttribute( "data-theme", theme );

        loadRecaptcha();
    }

    public RecaptchaComponent( String dataSiteKey, String secretKey )
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
                                'callback': myCallback
                            });
                        }
                    };
                
                    function myCallback(token) {
                        $0.$server.callback(token);
                    }
                    window.myCallback = myCallback;
                """, this, dataSiteKey );

        reloadRecaptcha();
    }

    public void reloadRecaptcha()
    {
        getElement().executeJs( """
                    if (typeof grecaptcha !== "undefined") {
                        document.getElementById("recaptcha-container").innerHTML = "";
                        grecaptcha.render($0, {
                            'sitekey': $1,
                            'callback': myCallback
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
        valid = recaptchaVerifier.verifyRecaptcha( response, secretKey );
    }

}
