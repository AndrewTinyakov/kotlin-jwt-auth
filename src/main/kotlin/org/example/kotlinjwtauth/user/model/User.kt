package org.example.kotlinjwtauth.user.model

import jakarta.persistence.*
import org.example.kotlinjwtauth.security.payload.request.SignUpRequest
import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.example.kotlinjwtauth.security.user.model.UserRole
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "app_users")
open class User(
    signUpRequest: SignUpRequest, @field:JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    ) @field:ManyToMany private val roles: Set<UserRole>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private var id: Long? = null

    @Column(name = "username")
    private var username = signUpRequest.username

    @Column(name = "password")
    private var password = signUpRequest.password

    @OneToMany(mappedBy = "user")
    private val refreshTokens: Set<RefreshToken>? = null

    @Column(name = "email")
    private var email = signUpRequest.email

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false
        val oEffectiveClass = if (o is HibernateProxy) o.hibernateLazyInitializer.persistentClass else o.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) (this as HibernateProxy).hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        val user = o as User
        return getId() != null && getId() == user.getId()
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy) (this as HibernateProxy).hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
    }
}