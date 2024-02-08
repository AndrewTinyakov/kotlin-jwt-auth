package org.example.kotlinjwtauth.security.token.refresh.model

import jakarta.persistence.*
import org.example.kotlinjwtauth.user.model.User

@Entity
@Table(name = "refresh_tokens")
class RefreshToken(@field:JoinColumn(name = "user_id") @field:ManyToOne(fetch = FetchType.LAZY) private val user: User) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private var id: Long? = null

    @Column(name = "token")
    private var hashedToken: String? = null
}
