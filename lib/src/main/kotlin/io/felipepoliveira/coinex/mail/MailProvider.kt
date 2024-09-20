package io.felipepoliveira.coinex.mail

interface MailProvider {
    /**
     * Send the mail to the given recipients. To the email to be sent the mailRecipient list should have at least
     * 1 recipient
     */
    fun sendMail(title: String, content: ByteArray, contentType: MailContentType, vararg mailRecipients: MailRecipient)
}

enum class MailContentType {
    /**
     * Define mime type of the mail as TEXT/HTML
     */
    TextHtml
}

data class MailRecipient(
    /**
     * The email address of the mail recipient
     */
    val emailAddress: String,
    /**
     * The mail recipient type
     */
    val type: MailRecipientType,
)

enum class MailRecipientType {
    To
}