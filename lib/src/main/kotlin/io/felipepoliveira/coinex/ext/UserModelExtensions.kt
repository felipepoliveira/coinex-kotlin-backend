package io.felipepoliveira.coinex.ext

import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.services.ServiceRequester

fun UserModel.asServiceRequester() = ServiceRequester(
    userUuid = this.uuid
)