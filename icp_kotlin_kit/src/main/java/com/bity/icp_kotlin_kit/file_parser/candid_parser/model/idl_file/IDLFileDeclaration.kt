package com.bity.icp_kotlin_kit.file_parser.candid_parser.model.idl_file

import com.bity.icp_kotlin_kit.file_parser.candid_parser.model.CandidService
import com.bity.icp_kotlin_kit.file_parser.candid_parser.model.CandidTypeDefinition

internal data class IDLFileDeclaration(
    val candidParsedTypes: List<CandidParsedType>,
    val service: CandidService?
)

internal data class CandidParsedType(
    val candidDefinition: String,
    val candidTypeDefinition: CandidTypeDefinition
)