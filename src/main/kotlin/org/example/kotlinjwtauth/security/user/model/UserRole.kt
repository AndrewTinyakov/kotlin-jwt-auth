package org.example.kotlinjwtauth.security.user.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class UserRole(
    @field:Column(name = "name")
    @field:Enumerated(EnumType.STRING)
    var name: Role
) {
    constructor(id: Long, name: Role) : this(name) {
        this.id = id
        this.name = name
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserRole

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "UserRole(name=$name, id=$id)"
    }

}
