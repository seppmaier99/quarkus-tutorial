package com.example;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

public class FruityViceRecovery implements FallbackHandler<FruityVice> {

    @Override
    public FruityVice handle(ExecutionContext context) {
        return FruityVice.EMPTY_FRUIT;
    }

}