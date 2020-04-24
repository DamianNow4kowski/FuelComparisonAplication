package com.example.fuelcomparison.source

class ResponseStatus {
    object Key {
        const val STATUS = "STATUS"
        const val ERROR_CODE = "ERROR_CODE"
        const val CONTENT = "CONTENT"
    }

    object General {
        const val SUCCESS = 1
        const val FAIL = 0
    }

    object Login {
        const val NULL_OR_EMPTY_INPUT = 1
        const val INCORRECT_DATA = 2
    }

    object Register {
        const val NULL_OR_EMPTY_INPUT = 1
        const val INCORRECT_DATA = 2
        const val USERNAME_OR_EMAIL_TAKEN = 3
    }
}