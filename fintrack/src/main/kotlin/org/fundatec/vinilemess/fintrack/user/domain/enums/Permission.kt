package org.fundatec.vinilemess.fintrack.user.domain.enums

enum class Permission(private val permission: String) {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    fun getPermission(): String {
        return permission
    }
}