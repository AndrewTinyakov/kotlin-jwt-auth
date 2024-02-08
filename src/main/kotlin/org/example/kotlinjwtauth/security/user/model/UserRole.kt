package org.example.kotlinjwtauth.security.user.model

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy


@Entity
@Table(name = "roles")
class UserRole(@field:Column(name = "name") @field:Enumerated(EnumType.STRING) private var name: Role) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private var id: Long? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false
        val oEffectiveClass = if (o is HibernateProxy) o.hibernateLazyInitializer.persistentClass else o.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) (this as HibernateProxy).hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        val role = o as UserRole
        return getId() != null && getId() == role.getId()
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy) (this as HibernateProxy).hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
    }
}
