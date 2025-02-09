package com.bity.icp_kotlin_kit.data.service.candid

import com.bity.icp_kotlin_kit.file_parser.candid_parser.model.CandidTypeRecord
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CandidRecordParserTest {

    private val candidTypeParserService = CandidTypeParserServiceImpl()

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("candidRecord")
    fun variantDefinition(
        typeDefinition: String,
        expectedGeneratedClass: String
    ) {
        val candidTypeDefinition = candidTypeParserService
            .parseCandidType(typeDefinition) as? CandidTypeRecord
        assertNotNull(candidTypeDefinition)
        assertFalse(candidTypeDefinition.isTypeAlias)
        val kotlinDefinition = candidTypeDefinition.getClassDefinition()
        assertEquals(
            expected = expectedGeneratedClass
                .replace("""\s+|\t+""".toRegex(), " ")
                .trim(),
            actual = kotlinDefinition
                .replace("""\s+|\t+""".toRegex(), " ")
                .trim()
        )
    }

    companion object {

        @JvmStatic
        private fun candidRecord() = listOf(

            Arguments.of(
                """
                    type EscrowReceipt = record {
                      token : TokenSpec;
                      token_id : text;
                      seller : Account__1;
                      buyer : Account__1;
                      amount : nat;
                    };
                """.trimIndent(),
                """
                    class EscrowReceipt(
                        val token: TokenSpec,
                        val token_id: String,
                        val seller: Account__1,
                        val buyer: Account__1,
                        val amount: BigInteger
                    )
                """.trimIndent()
            ),

            Arguments.of(
                """
                    type DutchParams = record {
                      time_unit : variant { day : nat; hour : nat; minute : nat };
                      decay_type : variant { flat : nat; percent : float64 };
                    };
                """.trimIndent(),
                """
                    class DutchParams(
                        val time_unit: TimeUnit,
                        val decay_type: DecayType
                    ) {
                        sealed class TimeUnit {
                            class day(val day: BigInteger): TimeUnit()
                            class hour(val hour: BigInteger): TimeUnit()
                            class minute(val minute: BigInteger): TimeUnit()
                        }
                        
                        sealed class DecayType {
                            class flat(val flat: BigInteger): DecayType()
                            class percent(val percent: Double): DecayType()
                        }
                    }
                """.trimIndent()
            ),

            Arguments.of(
                """
                    type CanisterLogMessages = record {
                      data : vec LogMessagesData;
                      lastAnalyzedMessageTimeNanos : opt Nanos;
                    };
                """.trimIndent(),
                """
                    class CanisterLogMessages(
                        val data: kotlin.Array<LogMessagesData>,
                        val lastAnalyzedMessageTimeNanos: Nanos?
                    )
                """.trimIndent()
            ),

            Arguments.of(
                """
                    type add_token_input = record {
                        name        : text;
                        description : text;
                        thumbnail   : text;
                        frontend    : opt text;
                        principal_id : principal;
                        details     : vec record { text; detail_value }
                    };
                """.trimIndent(),
                """
                    class add_token_input(
                        val name: String,
                        val description: String,
                        val thumbnail: String,
                        val frontend: String?,
                        val principal_id: ICPPrincipalApiModel,
                        val details: Array<Details>
                    ) {
                        class Details(
                            val textValue: String,
                            val detail_value: detail_value
                        )
                    }
                """.trimIndent()
            ),

            Arguments.of(
                """
                    type token = record {
                        name        : text;
                        description : text;
                        thumbnail   : text;
                        frontend    : opt text;
                        principal_id : principal;
                        submitter: principal;
                        last_updated_by: principal;
                        last_updated_at: nat64;
                        details     : vec record { text; detail_value }
                    };
                """.trimIndent(),
                """
                    class token(
                        val name: String,
                        val description: String,
                        val thumbnail: String,
                        val frontend: String?,
                        val principal_id: ICPPrincipalApiModel,
                        val submitter: ICPPrincipalApiModel,
                        val last_updated_by: ICPPrincipalApiModel,
                        val last_updated_at: ULong,
                        val details: Array<Details>
                    ) {
                        class Details(
                            val textValue: String,
                            val detail_value: detail_value
                        )
                    }
                """.trimIndent()
            ),

            Arguments.of(
                "type Account = record { owner : principal; subaccount : opt Subaccount };",
                """
                    class Account(
                        val owner: ICPPrincipalApiModel,
                        val subaccount: Subaccount?
                    )
                """.trimIndent()
            ),

            Arguments.of(
                """
                    type AllocationRecordStable = record {
                      allocated_space : nat;
                      token_id : text;
                      available_space : nat;
                      canister : principal;
                      chunks : vec nat;
                      library_id : text;
                    };
                """.trimIndent(),
                """
                    class AllocationRecordStable(
                        val allocated_space: BigInteger,
                        val token_id: String,
                        val available_space: BigInteger,
                        val canister: ICPPrincipalApiModel,
                        val chunks: Array<BigInteger>,
                        val library_id: String
                    )
                """.trimIndent()
            )
        )

    }

}