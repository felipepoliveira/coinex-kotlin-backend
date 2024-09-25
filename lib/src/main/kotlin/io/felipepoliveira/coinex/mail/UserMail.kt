package io.felipepoliveira.coinex.mail

import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.ext.readAllToString
import io.felipepoliveira.coinex.i18n.I18n
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.utils.io.Resources
import io.felipepoliveira.coinex.utils.text.WildcardString
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class UserMail(
    val contextualDependencies: ContextualDependencies,
    val mailProvider: MailProvider
) {

    /**
     * Send an email confirmation mail to the user
     */
    fun sendEmailConfirmationMail(recipientUser: UserModel, emailConfirmationToken: String) {
        // load the mail content into a wildcard string
        val mailWildcardContent = WildcardString(Resources.getLocalizedResourceInputStream(
            "/mails/passwordRecovery/passwordRecovery.[language].html",
            recipientUser.preferredLanguage
        ).readAllToString()
        )

        // Encode the password token to be a valid URL token
        val encodedEmailConfirmationToken = URLEncoder.encode(emailConfirmationToken, Charsets.UTF_8)

        // add the wildcards into the mail
        mailWildcardContent.addValue("confirmationLink", "${contextualDependencies.accountsWebappUrl()}/email-confirmation?token=$encodedEmailConfirmationToken")
        mailWildcardContent.addValue("userName", recipientUser.name)

        // Send the mail
        mailProvider.sendMailAsync(
            title = I18n.getMessage(recipientUser.preferredLanguage, I18n.MAIL_EMAIL_CONFIRMATION_TITLE),
            content = mailWildcardContent.toString().toByteArray(),
            contentType = MailContentType.TextHtml,
            MailRecipient(recipientUser.primaryEmail, MailRecipientType.To)
        ).start()
    }

    /**
     * Send a password recovery mail to the user
     */
    fun sendPasswordRecoveryMail(recipientUser: UserModel, passwordRecoveryToken: String) {

        // load the mail content into a wildcard string
        val mailWildcardContent = WildcardString(Resources.getLocalizedResourceInputStream(
            "/mails/passwordRecovery/passwordRecovery.[language].html",
            recipientUser.preferredLanguage
            ).readAllToString()
        )

        // Encode the password token to be a valid URL token
        val encodedPasswordRecoveryToken = URLEncoder.encode(passwordRecoveryToken, Charsets.UTF_8)

        // add the wildcards into the mail
        mailWildcardContent.addValue("passwordRecoveryLink", "${contextualDependencies.accountsWebappUrl()}/password-recovery?token=$encodedPasswordRecoveryToken")

        // Send the mail
        mailProvider.sendMailAsync(
            title = I18n.getMessage(recipientUser.preferredLanguage, I18n.MAIL_PASSWORD_RECOVERY_TITLE),
            content = mailWildcardContent.toString().toByteArray(),
            contentType = MailContentType.TextHtml,
            MailRecipient(recipientUser.primaryEmail, MailRecipientType.To)
        )
    }

}