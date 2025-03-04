package com.bity.icp_kotlin_kit.data.service.candid

import com.bity.icp_kotlin_kit.data.factory.CandidFileParserServiceFactory.provideKotlinFileGeneratorService
import org.junit.jupiter.api.Test
import java.io.File

class KotlinFileGeneratorServiceTest {

    private val kotlinFileGeneratorService = provideKotlinFileGeneratorService()

    @Test
    fun `parse file`() {
        val fileName = "ChainFusionToonis"
        val filePath = "src/test/resources/candid_file/$fileName.did"
        val candidFileText = File(filePath).readText()
        val generatedKotlinFile = kotlinFileGeneratorService.parseAndGetKotlinFile(
            candidFileText = candidFileText,
            fileName = fileName,
            packageName = "com.bity.icp_kotlin_kit.domain.generated_file"
        )
        println(generatedKotlinFile)
    }

    @Test
    fun `parse all files`() {
        val folder = File("src/test/resources/candid_file")
        folder.listFiles()?.forEach {
            val candidFileText = it.readText()
            val generatedKotlinFile = kotlinFileGeneratorService.parseAndGetKotlinFile(
                candidFileText = candidFileText,
                fileName = it.name.split(".").first(),
                packageName = "com.bity.icp_kotlin_kit.domain.generated_file"
            )
            println(generatedKotlinFile)
        }
    }

}