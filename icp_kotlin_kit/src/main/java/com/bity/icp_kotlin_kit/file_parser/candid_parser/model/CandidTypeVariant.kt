package com.bity.icp_kotlin_kit.file_parser.candid_parser.model

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal data class CandidTypeVariant(
    override val typeId: String? = null,
    override val typeName: String? = null,
    override val optionalType: OptionalType = OptionalType.None,
    val candidTypes: List<CandidType>
): CandidType() {

    override fun getKotlinDefinitionForSealedClass(className: String): String =
        throw RuntimeException("Unable to generate sealed class for CandidTypeVariant")

    override fun getKotlinVariableType(): String {
        requireNotNull(typeId)
        return typeId
    }

    override fun getVariableName(): String {
        requireNotNull(typeId)
        return typeId.replaceFirstChar { it.lowercase() }
    }

    companion object : ParserNodeDeclaration<CandidTypeVariant> by reflective()
}