## Google ReCaptcha Addon for Vaadin Framework

This addon enables seamless integration of Google ReCaptcha into your Vaadin applications, enhancing the security of your website. To use this addon, simply create a ReCaptcha configuration at Google ReCaptcha (https://www.google.com/recaptcha), and provide your dataSiteKey and secretKey. Once configured, you can easily render Google ReCaptcha components to protect your website from bots and automated abuse.

If you have any questions or encounter any issues, feel free to reach out.
I’m happy to assist!


### Example
```java
@Route( value = "login", layout = LoginLayout.class )
@AnonymousAllowed
@RequiredArgsConstructor
public class LoginView extends Div
{
public LoginView()
{
String recaptchaDataSiteKey = "XXX";
String recaptchaSecretKey = "XXX";
RecaptchaComponent recaptcha = new RecaptchaComponent( recaptchaDataSiteKey, recaptchaSecretKey );

        Button confirmButton = new Button( "Confirm", event -> {
            if ( !recaptcha.isValid() )
            {
                Notification.show( "Invalid captcha!", 3000, Notification.Position.MIDDLE );
                return;
            }
            // Login flow
        } );

        Button resetButton = new Button( "Reset", event -> {
            recaptcha.resetRecaptcha();
        } );

        add( confirmButton, resetButton, recaptcha );
    }
}
```

### Dark theme
```java
RecaptchaComponent recaptcha = new RecaptchaComponent( recaptchaDataSiteKey, recaptchaSecretKey, "dark" );
```
