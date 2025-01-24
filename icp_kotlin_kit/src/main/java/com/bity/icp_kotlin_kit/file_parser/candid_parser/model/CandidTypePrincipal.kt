package com.bity.icp_kotlin_kit.file_parser.candid_parser.model

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal data class CandidTypePrincipal(
    override val typeId: String,
    override val typeName: String? = null,
    override val optionalType: OptionalType = OptionalType.None,
): CandidType() {

    override fun getKotlinDefinition(candidTypeDefinitionId: String): String {
        val typealiasDefinition = "typealias $candidTypeDefinitionId = "
        return when(optionalType) {
            OptionalType.None -> typealiasDefinition + "ICPPrincipalApiModel"
            OptionalType.Optional -> typealiasDefinition + "ICPPrincipalApiModel?"
            OptionalType.DoubleOptional -> TODO()
        }
    }

    override fun getKotlinVariableType(): String = "ICPPrincipalApiModel"

    override fun getTypealiasDefinition(className: String): String {
        val typeasliasDefinition = "typealias $className = ICPPrincipal"
        return when(optionalType) {
            OptionalType.None -> typeasliasDefinition
            OptionalType.Optional -> "${typeasliasDefinition}?"
            OptionalType.DoubleOptional -> TODO()
        }
    }

    companion object : ParserNodeDeclaration<CandidTypePrincipal> by reflective()
}