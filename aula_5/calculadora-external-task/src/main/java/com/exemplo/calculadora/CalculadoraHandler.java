package com.exemplo.calculadora;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
// create a subscription for this topic name
@ExternalTaskSubscription("calculadora")
public class CalculadoraHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        long resultado = 0L;

        long a = externalTask.getVariable("a");
        long b = externalTask.getVariable("b");

        String operacao = externalTask.getVariable("operacao");

        switch (operacao){
            case "soma":
                resultado = a + b;
                break;
            case "subtração":
                resultado = a - b;
                break;
            case "multiplicação":
                resultado = a * b;
                break;
            case "divisão":
                resultado = a / b;
                break;
        }

        VariableMap variables = Variables.createVariables();
        variables.put("resultado", resultado);

        // complete the external task
        externalTaskService.complete(externalTask, variables);

        Logger.getLogger("calculadora")
                .log(Level.INFO, "Calculo da operação de {0} com {1} e {2} = {3}", new Object[]{operacao, a, b, resultado});
    }

}