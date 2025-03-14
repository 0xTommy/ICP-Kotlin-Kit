package com.bity.icp_kotlin_kit.domain.model.candid_type

import guru.zoroark.tegral.niwen.parser.ParserNodeDeclaration
import guru.zoroark.tegral.niwen.parser.reflective

internal class CandidTypeNat(
    override val typeId: String? = null,
    override val isTypeAlias: Boolean = false,
    override val variableName: String = "natValue",
    override val optionalType: OptionalType = OptionalType.None,
) : CandidType() {

    override fun getKotlinType(variableName: String?): String = "BigInteger"

    companion object : ParserNodeDeclaration<CandidTypeNat> by reflective()
}