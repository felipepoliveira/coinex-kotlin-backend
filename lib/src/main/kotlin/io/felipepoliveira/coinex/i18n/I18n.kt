package io.felipepoliveira.coinex.i18n

import io.felipepoliveira.coinex.models.Language
import io.felipepoliveira.coinex.utils.io.Resources
import java.util.Properties

sealed class I18n {
    companion object {
        // Store the code for the
        const val MAIL_CHANGE_PRIMARY_EMAIL_CURRENT_EMAIL_CODE_TITLE = "mails.changePrimaryEmail.currentEmailCode.title"
        const val MAIL_CHANGE_PRIMARY_EMAIL_NEW_EMAIL_TOKEN_TITLE = "mails.changePrimaryEmail.newEmailToken.title"
        const val MAIL_EMAIL_CONFIRMATION_TITLE = "mails.emailConfirmation.title"
        const val MAIL_PASSWORD_RECOVERY_TITLE = "mails.passwordRecovery.title"

        /**
         * Store the I18n messages
         */
        private val i18nMessages = mutableMapOf<Language, MutableMap<String, String>>()

        init {
            for (lang in Language.entries) {

                // load the language file content into a Properties object
                val langFileContent = Properties()
                langFileContent.load(Resources.getLocalizedResourceInputStream(
                    "/i18n/strings/strings.[lang].properties",
                    lang
                    )
                )

                // add the messages into the map
                val langMessages = mutableMapOf<String, String>()
                for ((code, msg) in langFileContent.entries) {
                    langMessages[code.toString()] = msg.toString()
                }

                // add the messages into the main container
                i18nMessages[lang] = langMessages
            }
        }

        /**
         * Return the internationalized message identified by the given language and message code
         */
        fun getMessage(language: Language, msgCode: String): String {
            val langMessages = i18nMessages[language] ?:
                throw Exception("The language $language was not loaded on I18n initialization")

            return langMessages[msgCode] ?:
                throw Exception("There is no code $msgCode for language $language")
        }
    }
}