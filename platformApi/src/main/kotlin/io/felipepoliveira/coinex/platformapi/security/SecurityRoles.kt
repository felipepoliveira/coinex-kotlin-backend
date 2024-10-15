package io.felipepoliveira.coinex.platformapi.security

class SecurityRoles {
    companion object {
        const val STL_NONE = "ROLE_STL_0"
        const val STL_RECENT_AUTHENTICATION = "ROLE_STL_1"
        const val STL_LAST_HOUR = "ROLE_STL_2"
        const val STL_MOST_RECENT = "ROLE_STL_3"
    }
}

class StlRoles {
    companion object {
        val NONE = StlRole(SecurityRoles.STL_NONE, 0)
        val RECENT_AUTHENTICATION = StlRole(SecurityRoles.STL_RECENT_AUTHENTICATION, 1)
        val LAST_HOUR = StlRole(SecurityRoles.STL_LAST_HOUR, 2)
        val STL_MOST_RECENT = StlRole(SecurityRoles.STL_MOST_RECENT, 3)
    }
}

data class StlRole(
    val role: String,
    val level: Int,
)