package io.felipepoliveira.coinex.mail

import io.felipepoliveira.coinex.models.UserModel
import org.springframework.stereotype.Component

@Component
class UserMail(
    val mailProvider: MailProvider
) {

    fun sendPasswordRecoveryMail(recipientUser: UserModel, passwordRecoveryToken: String) {

    }

}