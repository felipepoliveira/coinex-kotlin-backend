package io.felipepoliveira.coinex.security

import org.springframework.security.crypto.bcrypt.BCrypt

fun calculatePasswordRank(password: String): PasswordRank {
    if (password.length < 8) {
        return PasswordRank.Unsafe
    }

    // Flag that marks each safe password criteria
    var hasLowerCaseCharacter = false
    var hasUpperCaseCharacter = false
    var hasDigit = false
    var hasSymbol = false

    for (c in password) {
        if (c.isUpperCase()) {
            hasUpperCaseCharacter = true
        }
        else if (c.isLowerCase()) {
            hasLowerCaseCharacter = true
        }
        else if (c.isDigit()) {
            hasDigit = true
        }
        else {
            hasSymbol = true
        }
    }

    // Return the rank depending on the safe flags
    val isSafe = (hasLowerCaseCharacter && hasUpperCaseCharacter && hasDigit && hasSymbol)
    return if (!isSafe) {
        PasswordRank.Unsafe
    }
    else {
        PasswordRank.Safe
    }
}

fun hashPasswordToString(password: String): String {
    val hash = BCrypt.gensalt(10)
    return BCrypt.hashpw(password, hash)
}

fun verifyPassword(hashedPassword: String, password: String): Boolean = BCrypt.checkpw(password, hashedPassword)

enum class PasswordRank(
    /**
     * The rank level
     */
    private val rankLevel: Int,
) {
    Unsafe(-1),
    Safe(0),
    VerySafe(1),
    ;

    fun isAtLeast(other: PasswordRank): Boolean {
        return this.rankLevel >= other.rankLevel
    }
}