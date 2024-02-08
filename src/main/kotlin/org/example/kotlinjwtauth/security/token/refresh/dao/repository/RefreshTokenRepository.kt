package org.example.kotlinjwtauth.security.token.refresh.dao.repository

import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    @Transactional
    @Modifying
    @Query("update RefreshToken r set r.hashedToken = :hashedToken where r.id = :id")
    fun updateHashedTokenById(@Param("hashedToken") hashedToken: String, @Param("id") id: Long)

}
