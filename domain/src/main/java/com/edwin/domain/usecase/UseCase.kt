package com.edwin.domain.usecase

interface UseCase<out Type, in Params> {
    operator fun invoke(params: Params): Type
}