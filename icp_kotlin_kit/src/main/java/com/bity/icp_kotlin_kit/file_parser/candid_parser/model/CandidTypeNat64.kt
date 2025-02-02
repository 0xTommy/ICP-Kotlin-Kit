package com.bity.icp_kotlin_kit.file_parser.candid_parser.model

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal data class CandidTypeNat64(
    override val typeId: String? = null,
    override val variableName: String? = null,
    override val optionalType: OptionalType = OptionalType.None,
): CandidType() {

    override fun getKotlinType(variableName: String?): String = "ULong"

    override fun getClassDefinitionForSealedClass(parentClassname: String): String {
        val variableDefinition = "val nat64Value: ${getKotlinType()}"
        return "class $typeId($variableDefinition): $parentClassname()"
    }

    companion object : ParserNodeDeclaration<CandidTypeNat64> by reflective()
}