package org.example.kotlinjwtauth.security.user.model

import org.example.kotlinjwtauth.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial
import java.util.stream.Collectors


data class UserDetailsImpl(
    var id: Long?,
    private var username: String,
    private var email: String,
    private var password: String,
    private val authorities: Collection<GrantedAuthority>,
    val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImpl
        return id == user.id
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }

    companion object {
        @Serial
        private val serialVersionUID = 237455904059080669L

        fun build(user: User): UserDetailsImpl {
            val authorities = user.roles.stream()
                .map { role: UserRole -> SimpleGrantedAuthority(role.name.name) }
                .collect(Collectors.toList())

            return UserDetailsImpl(
                user.id,
                user.username,
                user.email,
                user.password,
                authorities,
                user
            )
        }
    }
}
