package com.bity.icp_kotlin_kit.domain.model.enum

import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal

enum class ICPSystemCanisters(val icpPrincipal: ICPPrincipal) {
    TokenRegistry(ICPPrincipal("b7hhy-tyaaa-aaaah-abbja-cai")),
    Root(ICPPrincipal("r7inp-6aaaa-aaaaa-aaabq-cai")),
    Management(ICPPrincipal("aaaaa-aa")),
    Ledger(ICPPrincipal("ryjl3-tyaaa-aaaaa-aaaba-cai")),
    Governance(ICPPrincipal("rrkah-fqaaa-aaaaa-aaaaq-cai")),
    CyclesMint(ICPPrincipal("rkp4c-7iaaa-aaaaa-aaaca-cai")),
    Ii(ICPPrincipal("rdmx6-jaaaa-aaaaa-aaadq-cai")),
}