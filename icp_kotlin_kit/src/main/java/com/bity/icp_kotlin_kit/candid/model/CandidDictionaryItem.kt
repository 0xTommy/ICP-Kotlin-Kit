package com.bity.icp_kotlin_kit.candid.model

internal class CandidDictionaryItem(
    val hashedKey: ULong,
    val value: CandidValue
) {
    constructor(key: String, value: CandidValue): this(
        hashedKey = CandidDictionary.hash(key),
        value = value
    )
}