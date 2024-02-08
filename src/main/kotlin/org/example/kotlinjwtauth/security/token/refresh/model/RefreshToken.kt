package org.example.kotlinjwtauth.security.token.refresh.model

import jakarta.persistence.*
import org.example.kotlinjwtauth.user.model.User

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @field:JoinColumn(name = "user_id")
    @field:ManyToOne(fetch = FetchType.LAZY)
    val user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    var id: Long? = null

    @Column(name = "token")
    var hashedToken: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RefreshToken

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "RefreshToken(id=$id)"
    }


}
