package io.felipepoliveira.coinex.mail

import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.ext.readAllToString
import io.felipepoliveira.coinex.i18n.I18n
import io.felipepoliveira.coinex.models.Language
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
    fun sendChangePrimaryEmailMail(recipientUser: UserModel, newEmail: String, emailChangeToken: String, emailChangeCode: String) {
        sendChangePrimaryEmailCurrentEmailCodeMail(recipientUser, emailChangeCode)
        sendChangePrimaryEmailNewEmailToken(newEmail, emailChangeToken, recipientUser.preferredLanguage)
    }

    /**
     * Send the primary email change to the current mail
     */
    private fun sendChangePrimaryEmailCurrentEmailCodeMail(recipientUser: UserModel, emailChangeCode: String) {
        val currentEmailCodeWildcardContent = WildcardString(Resources.getLocalizedResourceInputStream(
            "/mails/changePrimaryEmail/currentEmailCode/currentEmailCode.[language].html",
            recipientUser.preferredLanguage
        ).readAllToString())

        // add the wildcards value
        currentEmailCodeWildcardContent.addValue("userName", recipientUser.name)
        currentEmailCodeWildcardContent.addValue("confirmationCode", emailChangeCode)

        // Send the mail
        mailProvider.sendMailAsync(
            title = I18n.getMessage(recipientUser.preferredLanguage, I18n.MAIL_CHANGE_PRIMARY_EMAIL_CURRENT_EMAIL_CODE_TITLE),
            content = currentEmailCodeWildcardContent.toString().toByteArray(),
            contentType = MailContentType.TextHtml,
            MailRecipient(recipientUser.primaryEmail, MailRecipientType.To)
        )
    }

    /**
     * Send the primary email change mail to the new email
     */
    private fun sendChangePrimaryEmailNewEmailToken(recipientMail: String, emailChangeToken: String, language: Language) {
        val newEmailTokenWildcardContent = WildcardString(Resources.getLocalizedResourceInputStream(
            "/mails/changePrimaryEmail/newEmailToken/newEmailToken.[language].html",
            language
        ).readAllToString())

        // Encode the password token to be a valid URL token
        val encodedPrimaryEmailChangeToken = URLEncoder.encode(emailChangeToken, Charsets.UTF_8)

        // add the wildcards into the mail
        newEmailTokenWildcardContent.addValue("confirmationLink", "${contextualDependencies.accountsWebappUrl()}/change-primary-email?token=$encodedPrimaryEmailChangeToken")

        // Send the mail
        mailProvider.sendMailAsync(
            title = I18n.getMessage(language, I18n.MAIL_CHANGE_PRIMARY_EMAIL_NEW_EMAIL_TOKEN_TITLE),
            content = newEmailTokenWildcardContent.toString().toByteArray(),
            contentType = MailContentType.TextHtml,
            MailRecipient(recipientMail, MailRecipientType.To)
        )
    }

    /**
     * Send an email confirmation mail to the user
     */
    fun sendEmailConfirmationMail(recipientUser: UserModel, emailConfirmationToken: String) {
        // load the mail content into a wildcard string
        val mailWildcardContent = WildcardString(Resources.getLocalizedResourceInputStream(
            "/mails/passwordRecovery/passwordRecovery.[language].html",
            recipientUser.preferredLanguage
        ).readAllToString())

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
        )
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