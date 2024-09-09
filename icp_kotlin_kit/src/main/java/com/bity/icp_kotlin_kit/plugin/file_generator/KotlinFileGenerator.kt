package com.bity.icp_kotlin_kit.plugin.file_generator

import com.bity.icp_kotlin_kit.plugin.candid_parser.CandidFileParser
import com.bity.icp_kotlin_kit.plugin.candid_parser.model.file_generator.KotlinClassDefinitionType
import com.bity.icp_kotlin_kit.plugin.candid_parser.model.file_generator.KotlinTypeDefinition
import com.bity.icp_kotlin_kit.plugin.candid_parser.model.idl_file.IDLFileDeclaration
import com.bity.icp_kotlin_kit.plugin.candid_parser.model.idl_type.IDLType
import com.bity.icp_kotlin_kit.plugin.candid_parser.util.ext_fun.toKotlinFileString
import com.bity.icp_kotlin_kit.plugin.file_generator.helper.CandidDefinitionHelper
import java.io.File
import java.lang.IllegalStateException

internal class KotlinFileGenerator(
    private val didFilePath: String,
    private val showCandidDefinition: Boolean = true,
    outputFilePath: String,
) {

    private val idlFileDeclaration: IDLFileDeclaration
    private val fileName = didFilePath.split("/")
        .last()
        .removeSuffix(".did")
    private val outputFile = File(outputFilePath)

    private val fileText = StringBuilder(HEADER)

    init {
        val inputFile = File(didFilePath)
        assert(inputFile.exists()) {
            throw IllegalStateException("file $didFilePath not found")
        }

        idlFileDeclaration = CandidFileParser.parseFile(inputFile.readText())
    }

    fun generateKotlinFile() {

        val typeDeclarationConverter = IDLTypeDeclarationConverter(
            fileName = fileName,
            types = idlFileDeclaration.types
        )
        val kotlinGeneratedClasses = typeDeclarationConverter.convertTypes()

        val typeAliases = kotlinGeneratedClasses.filter {
            it.classDefinitionType is KotlinClassDefinitionType.TypeAlias
                    // || it.classDefinitionType is KotlinClassDefinitionType.Array
                    || it.classDefinitionType is KotlinClassDefinitionType.Function
        }
        val classes = kotlinGeneratedClasses.filter {
            it.classDefinitionType is KotlinClassDefinitionType.Class
                    || it.classDefinitionType is KotlinClassDefinitionType.SealedClass
        }

        // TypeAliases must be declare before object definition
        typeAliases.forEach {
            fileText.appendLine(it.kotlinDefinition(showCandidDefinition))
        }

        // Add file comment
        idlFileDeclaration.comment?.let {
            fileText.append(KotlinCommentGenerator.getKotlinComment(it))
        }

        fileText.appendLine("object $fileName{")

        // Additional classes declaration
        fileText.appendLine(
            classes.joinToString("") { it.kotlinDefinition(showCandidDefinition) }
        )

        // Service declaration
        idlFileDeclaration.service?.let {
            val idlFileServiceConverter = IDLFileServiceConverter(
                fileName = fileName,
                idlFileService = it,
                generatedClasses = typeDeclarationConverter.generatedClasses
            )
            fileText.appendLine(
                idlFileServiceConverter.getKotlinServiceDefinition(
                    showCandidDefinition = showCandidDefinition
                )
            )
        }

        fileText.appendLine("}")
        outputFile.writeText(fileText.toString().toKotlinFileString())
    }

    companion object {
        private const val HEADER = """// TODO, add package name
        
        import java.math.BigInteger
        import com.bity.icp_kotlin_kit.candid.CandidDecoder
        import com.bity.icp_kotlin_kit.candid.CandidEncoder
        import com.bity.icp_kotlin_kit.domain.model.ICPMethod
        import com.bity.icp_kotlin_kit.domain.usecase.ICPQuery
        import com.bity.icp_kotlin_kit.candid.model.CandidValue
        import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
        import com.bity.icp_kotlin_kit.domain.request.PollingValues
        import com.bity.icp_kotlin_kit.provideICPCanisterRepository
        import com.bity.icp_kotlin_kit.domain.model.ICPSigningPrincipal
        import com.bity.icp_kotlin_kit.plugin.candid_parser.util.shared.*
        import com.bity.icp_kotlin_kit.domain.repository.ICPCanisterRepository
        import com.bity.icp_kotlin_kit.domain.model.enum.ICPRequestCertification
       
        /**
         * File generated using ICP Kotlin Kit Plugin
         */
         
    """
    }
}