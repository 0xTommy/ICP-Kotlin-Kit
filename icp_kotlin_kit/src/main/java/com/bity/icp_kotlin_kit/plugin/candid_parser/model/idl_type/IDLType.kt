package com.bity.icp_kotlin_kit.plugin.candid_parser.model.idl_type

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.dsl.subtype

// TODO, remove = null and fix compile errors
internal sealed class IDLType(
    val isOptional: Boolean,
    val id: String? = null
) {
    companion object : ParserNodeDeclaration<IDLType> by subtype()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IDLType

        if (isOptional != other.isOptional) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isOptional.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


}