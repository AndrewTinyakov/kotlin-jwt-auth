package org.example.kotlinjwtauth.user.model

import jakarta.persistence.*
import org.example.kotlinjwtauth.security.payload.request.SignUpRequest
import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.example.kotlinjwtauth.security.user.model.UserRole

@Entity
@Table(name = "app_users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long? = null,

    @Column(name = "username")
    var username: String,

    @Column(name = "password")
    var password: String,

    @OneToMany(mappedBy = "user")
    var refreshTokens: Set<RefreshToken> = HashSet(),

    @Column(name = "email")
    var email: String,

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<UserRole> = HashSet()

) {
    constructor(
        username: String,
        password: String,
        email: String,
        roles: Set<UserRole>
    ) : this(null, username, password, HashSet(), email, HashSet(roles))

    constructor(signUpRequest: SignUpRequest, roles: MutableSet<UserRole>) : this(
        username = signUpRequest.username,
        password = signUpRequest.password,
        email = signUpRequest.email,
        roles = roles
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(roles=$roles, id=$id, username='$username', email='$email')"
    }

}