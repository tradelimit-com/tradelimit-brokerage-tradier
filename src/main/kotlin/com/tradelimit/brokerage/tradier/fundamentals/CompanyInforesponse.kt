package com.tradelimit.brokerage.tradier.fundamentals

import kotlinx.serialization.Serializable


@Serializable
data class CompanyInfoResponse(val request: String, val type: String, val results: List<CompanyInfo>) {


    @Serializable
    data class CompanyInfo(val type: String, val id: String, )

}
