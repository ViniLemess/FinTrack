package org.fundatec.vinilemess.fintrack.user.domain

import org.fundatec.vinilemess.fintrack.user.domain.enums.Permission
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role(private val permissions: Set<Permission>) {

    USER(setOf(
            Permission.USER_READ,
            Permission.USER_CREATE,
            Permission.USER_UPDATE,
            Permission.USER_DELETE
    ));

    fun getAuthorities(): Set<SimpleGrantedAuthority> {
        return permissions.map {
            SimpleGrantedAuthority(it.getPermission())
        }.union(setOf(SimpleGrantedAuthority("ROLE_${this.name}")))
    }
}