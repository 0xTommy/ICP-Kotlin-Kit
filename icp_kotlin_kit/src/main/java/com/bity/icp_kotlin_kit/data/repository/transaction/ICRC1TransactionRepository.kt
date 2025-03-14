package com.bity.icp_kotlin_kit.data.repository.transaction

import com.bity.icp_kotlin_kit.data.datasource.api.model.toDomainModel
import com.bity.icp_kotlin_kit.data.generated_file.ICRC1IndexCanister
import com.bity.icp_kotlin_kit.data.generated_file.ICRC1IndexCanister.GetAccountTransactionsArgs
import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.error.TransactionException
import com.bity.icp_kotlin_kit.domain.model.toDataModel
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransactionDestination
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransactionOperation
import com.bity.icp_kotlin_kit.domain.repository.TransactionRepository
import java.math.BigInteger

class ICRC1TransactionRepository(
    private val icpToken: ICPToken,
    private val indexCanister: ICPPrincipal
): TransactionRepository {

    // TODO, need to update
    override suspend fun fetchAllTransactions(account: ICPAccount): List<ICPTokenTransaction> {
        val getAccountTransactionsArgs = GetAccountTransactionsArgs(
            account = ICRC1IndexCanister.Account(
                owner = account.principal.toDataModel(),
                subaccount = account.subAccountId
            ),
            start = null,
            max_results = BigInteger("1000000")
        )
        val transactions = ICRC1IndexCanister.ICRC1IndexCanisterService(indexCanister)
            .get_account_transactions(getAccountTransactionsArgs)
        return when(transactions) {
            is ICRC1IndexCanister.GetTransactionsResult.Err ->
                throw TransactionException.ICRC1GetAllTransactionsException(transactions.getTransactionsErr.message)
            is ICRC1IndexCanister.GetTransactionsResult.Ok ->
                transactions.getTransactions.transactions.mapNotNull { it.toDataModel() }
        }
    }

    private fun ICRC1IndexCanister.TransactionWithId.toDataModel(): ICPTokenTransaction? {

        val operation: ICPTokenTransactionOperation
        val amount: BigInteger
        val fee: BigInteger
        val spender: ICPTokenTransactionDestination?
        val created: ULong?
        val icrc1Memo: ByteArray?

        when {
            transaction.burn != null -> {
                val burn = transaction.burn
                operation = ICPTokenTransactionOperation.Burn(
                    from = getDestinationAccount(burn.from)
                )
                amount = burn.amount
                fee = BigInteger.ZERO
                spender = burn.spender?.let { getDestinationAccount(it) }
                created = burn.created_at_time
                icrc1Memo = burn.memo?.map { it.toByte() }?.toByteArray()
            }

            transaction.approve != null -> {
                val approve = transaction.approve
                operation = ICPTokenTransactionOperation.Approve(
                    from = getDestinationAccount(approve.from),
                    expectedAllowance = approve.expected_allowance,
                    expires = approve.expires_at?.toLong()
                )
                amount = approve.amount
                fee = BigInteger.ZERO
                spender = getDestinationAccount(approve.spender)
                created = approve.created_at_time
                icrc1Memo = approve.memo?.map { it.toByte() }?.toByteArray()
            }

            transaction.transfer != null -> {
                val transfer = transaction.transfer
                operation = ICPTokenTransactionOperation.Transfer(
                    from = getDestinationAccount(transfer.from),
                    to = getDestinationAccount(transfer.to)
                )
                amount = transfer.amount
                fee = transfer.fee ?: BigInteger.ZERO
                spender = transfer.spender?.let { getDestinationAccount(it) }
                created = transfer.created_at_time
                icrc1Memo = transfer.memo?.map { it.toByte() }?.toByteArray()
            }

            transaction.mint != null -> {
                val mint = transaction.mint
                operation = ICPTokenTransactionOperation.Mint(
                    to = getDestinationAccount(mint.to)
                )
                amount = mint.amount
                fee = BigInteger.ZERO
                spender = null
                created = mint.created_at_time
                icrc1Memo = mint.memo?.map { it.toByte() }?.toByteArray()
            }

            else -> return null
        }

        return ICPTokenTransaction(
            blockIndex = id,
            operation = operation,
            icrc1Memo = icrc1Memo,
            memo = null,
            amount = amount,
            fee = fee,
            createdNanos = created,
            timeStampNanos = transaction.timestamp,
            spender = spender,
            token = icpToken
        )
    }

    private fun getDestinationAccount(account: ICRC1IndexCanister.Account): ICPTokenTransactionDestination.Account {
        return ICPTokenTransactionDestination.Account(
            icpAccount = ICPAccount(
                principal = account.owner.toDomainModel(),
                subAccountId = account.subaccount ?: ICPAccount.DEFAULT_SUB_ACCOUNT_ID
            )
        )
    }
}