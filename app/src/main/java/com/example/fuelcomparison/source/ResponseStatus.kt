package com.example.fuelcomparison.source

class ResponseStatus {
    object Key {
        const val STATUS = "status"
        const val ERROR_CODE = "errorCode"
        const val REASON = "reason"
        const val CONTENT = "data"
    }

    object General {
        const val SUCCESS = 200
        const val FAIL = 500
    }

    object Login {
        const val NULL_OR_EMPTY_INPUT = "Null or empty input"
        const val INCORRECT_DATA = "Incorrect data"
    }

    object Register {
        const val NULL_OR_EMPTY_INPUT = "Null or empty input"
        const val INCORRECT_DATA = "Incorrect data"
        const val USERNAME_OR_EMAIL_TAKEN = "Username or email taken"
    }

    object AddGasStation {
        const val DB_INSERT_EXCEPTION = "Error during insert gas station"
    }
}