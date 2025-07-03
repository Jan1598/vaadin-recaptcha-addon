## Google ReCaptcha Addon for Vaadin Framework

This addon enables seamless integration of Google ReCaptcha into your Vaadin applications, enhancing the security of your website. To use this addon, create a ReCaptcha configuration at Google ReCaptcha (https://www.google.com/recaptcha), and provide your dataSiteKey and secretKey. Once configured, you can render Google ReCaptcha components to protect your website from bots and automated abuse.

If you have any questions or encounter any issues, feel free to reach out.
I’m happy to assist!


### Example - ReCaptcha v2

```java
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import org.vaadin.addons.googlerecaptcha.v2.RecaptchaComponentv2;

@Route( value = "login", layout = LoginLayout.class )
@AnonymousAllowed
@RequiredArgsConstructor
public class LoginView extends Div
{
    public LoginView()
    {
        String recaptchaDataSiteKey = "XXX";
        String recaptchaSecretKey = "XXX";
        RecaptchaComponentv2 recaptcha = new RecaptchaComponentv2( recaptchaDataSiteKey, recaptchaSecretKey );

        Button confirmButton = new Button( "Confirm", event -> {
            if ( !recaptcha.isValid() )
            {
                Notification.show( "Invalid captcha!", 3000, Notification.Position.MIDDLE );
                return;
            }
            // Login flow
        } );

        Button resetButton = new Button( "Reset", event -> recaptcha.resetRecaptcha() );

        add( confirmButton, resetButton, recaptcha );
    }
}
```

#### Dark theme

```java
import org.vaadin.addons.googlerecaptcha.v2.RecaptchaComponentv2;

RecaptchaComponentv2 recaptcha = new RecaptchaComponentv2( recaptchaDataSiteKey, recaptchaSecretKey, "dark" );
```


### Example - ReCaptcha v3

```java
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import org.vaadin.addons.googlerecaptcha.v3.RecaptchaComponentv3;

@Route( value = "login", layout = LoginLayout.class )
@AnonymousAllowed
@RequiredArgsConstructor
public class LoginView extends Div
{

    public LoginView()
    {
        String recaptchaDataSiteKey = "XXX";
        String recaptchaSecretKey = "XXX";
        RecaptchaComponentv3 recaptcha = new RecaptchaComponentv3( recaptchaDataSiteKey, recaptchaSecretKey );

        Button confirmButton = new Button( "Confirm", event -> {
            recaptcha.setCallback( responseObj -> {
                double score = responseObj.get( "score" ) instanceof Number
                        ? ( ( Number ) responseObj.get( "score" ) ).doubleValue()
                        : 0.0;

                if ( score > 0.5 )
                {
                    // Login flow
                }
                else
                {
                    Notification.show( "Invalid interaction - ReCaptcha score too low" );
                }
            } );

            recaptcha.checkInteraction();
        } );

        add( confirmButton, recaptcha );
    }
}
```