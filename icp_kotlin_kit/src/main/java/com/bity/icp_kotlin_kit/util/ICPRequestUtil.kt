package com.bity.icp_kotlin_kit.util

import com.bity.icp_candid.domain.serializer.CandidSerializer
import com.bity.icp_kotlin_kit.domain.model.ICPDomainSeparator
import com.bity.icp_cryptography.util.DER
import com.bity.icp_cryptography.util.secureRandomOfLength
import com.bity.icp_kotlin_kit.data.datasource.api.enum.ContentRequestType
import com.bity.icp_kotlin_kit.data.datasource.api.model.CallApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.model.ContentApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.model.ICPRequestApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.model.ReadStateApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.request.ICPRequestEnvelope
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPSigningPrincipal
import java.time.Duration
import java.time.Instant

internal object ICPRequestUtil {

    private const val NONCE_BYTE_LENGTH = 32
    // 4 minutes
    private const val DEFAULT_INGRESS_EXPIRY_SECONDS: Long = 4 * 60

    fun buildContent(request: ICPRequestApiModel, sender: ICPPrincipal?): ContentApiModel {
        val nonce = secureRandomOfLength(NONCE_BYTE_LENGTH)
        val ingressExpiry = createIngressExpiry()
        val senderBytes = sender?.bytes ?: byteArrayOf(4)

        return when(request) {
            is ICPRequestApiModel.ReadState -> {
                val encodedPaths = request.paths.map { it.encodedComponents() }
                ReadStateApiModel(
                    requestType = ContentRequestType.ReadState,
                    sender = senderBytes,
                    nonce = nonce,
                    ingressExpiry = ingressExpiry,
                    paths = encodedPaths
                )
            }
            is ICPRequestApiModel.Call,
            is ICPRequestApiModel.Query -> {
                val method = request.method
                val serializedArgs = CandidSerializer.encode(method.args)
                CallApiModel(
                    requestType = ContentRequestType.fromICPRequestApiModel(request),
                    sender = senderBytes,
                    nonce = nonce,
                    ingressExpiry = ingressExpiry,
                    method_name = method.methodName,
                    canister_id = method.canister.bytes,
                    arg = serializedArgs
                )
            }
        }
    }

    suspend fun buildEnvelope(
        content: ContentApiModel,
        sender: ICPSigningPrincipal?
    ): ICPRequestEnvelope {

        requireNotNull(sender) {
            return ICPRequestEnvelope(content)
        }

        val requestId = content.calculateRequestId()
        val domainSeparatedData = ICPDomainSeparator("ic-request").domainSeparatedData(requestId)
        val senderSignature = sender.sign(domainSeparatedData)
        val senderPublicKey = DER.serialise(sender.rawPublicKey)
        return ICPRequestEnvelope(
            content,
            senderPublicKey,
            senderSignature
        )
    }

    private fun createIngressExpiry(seconds: Long = DEFAULT_INGRESS_EXPIRY_SECONDS): Long {
        val expiryDate = Instant.now().plusSeconds(seconds)
        return Duration.between(Instant.EPOCH, expiryDate).toNanos()
    }
}