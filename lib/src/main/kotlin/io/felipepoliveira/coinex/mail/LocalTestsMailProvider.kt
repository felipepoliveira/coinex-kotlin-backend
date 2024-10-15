package io.felipepoliveira.coinex.mail

import java.io.File
import java.io.FileWriter

class LocalTestsMailProvider(
    /**
     * Directory where the mails will be stored
     */
    private val mailsDirectory: File,
) : MailProvider {

    init {
        // assert that the directory exists
        if (!mailsDirectory.exists()) {
            mailsDirectory.mkdirs()
        }
        // if it exists, and it is not a directory throw a exception
        else if (!mailsDirectory.isDirectory()) {
            throw Exception("The provided mailDirectory should be a valid directory ${mailsDirectory.path}")
        }
    }

    private fun generateMailFilename(title: String, mailRecipient: MailRecipient): String {
        // Replace spaces and special characters in the title with underscores for file system compatibility
        val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9]"), "_")

        // Extract the recipient's email and replace special characters
        val recipientEmail = mailRecipient.emailAddress.replace(Regex("[^a-zA-Z0-9]"), "_")

        // Append the current timestamp to make the filename unique
        val timestamp = System.currentTimeMillis()

        // Generate the final filename with a .txt extension
        return "${mailsDirectory.path}${File.separator}$sanitizedTitle$recipientEmail$timestamp.txt"
    }

    override fun sendMail(
        title: String,
        content: ByteArray,
        contentType: MailContentType,
        vararg mailRecipients: MailRecipient
    ) {
        if (mailRecipients.isEmpty()) {
            throw Exception("Mail recipients can not be empty or null")
        }

        // generate the mail content file
        val mailContentFile = File(generateMailFilename(title, mailRecipients[0]))

        // write the content into the file
        FileWriter(mailContentFile).use { writer ->
            writer.write(String(content))
        }

        println("Email was successfully written in ${mailContentFile.path}")
    }
}