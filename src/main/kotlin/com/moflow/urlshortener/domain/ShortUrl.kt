package com.moflow.urlshortener.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "shorten_url")
class ShortUrl(
    val originUrl: String,
    @Column(unique = true)
    val shortKey: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShortUrl

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ShortUrl(id='$id', originUrl=$originUrl, shortKey=$shortKey, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
