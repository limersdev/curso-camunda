package br.com.plantacao;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Component
@ExternalTaskSubscription("ventilador")
public class Handler implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Map<String, Object> variaveis = new HashMap<>();
        Map<String, Object> resultadoTemperatura = externalTask.getVariable("resultado_temperatura");

        variaveis.put("processo", externalTask.getProcessInstanceId());
        variaveis.put("operacao_ventilador", resultadoTemperatura.get("operacao_ventilador"));

        logger.info("Operando ventilador {}", entries(variaveis));

        externalTaskService.complete(externalTask);
    }

}