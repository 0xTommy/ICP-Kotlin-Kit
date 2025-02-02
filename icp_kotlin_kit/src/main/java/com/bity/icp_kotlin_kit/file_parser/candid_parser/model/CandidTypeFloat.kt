package com.bity.icp_kotlin_kit.file_parser.candid_parser.model

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal data class CandidTypeFloat(
    override val typeId: String,
    override val variableName: String? = null,
    override val optionalType: OptionalType = OptionalType.None,
): CandidType() {

    override fun getKotlinType(variableName: String?): String = "Double"

    override fun getClassDefinitionForSealedClass(parentClassname: String): String {
        val variableDefinition = "val candidFloatValue: ${getKotlinVariableType()}"
        return "class $typeId($variableDefinition): $parentClassname()"
    }

    companion object : ParserNodeDeclaration<CandidTypeFloat> by reflective()
}