package com.bity.icp_kotlin_kit.file_parser.candid_parser.model

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal data class CandidTypeNat8(
    override val typeId: String? = null,
    override val variableName: String? = null,
    override val optionalType: OptionalType = OptionalType.None,
): CandidType() {

    override fun isKotlinTypealiasDefinition() = false
    override val kotlinType: String = "UByte"

    companion object : ParserNodeDeclaration<CandidTypeNat8> by reflective()
}