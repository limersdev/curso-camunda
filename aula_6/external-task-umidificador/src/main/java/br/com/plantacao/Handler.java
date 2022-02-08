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
@ExternalTaskSubscription("umidificador")
public class Handler implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Map<String, Object> variaveis = new HashMap<>();
        Map<String, Object> resultadoUmidade = externalTask.getVariable("resultado_umidade");

        variaveis.put("processo", externalTask.getProcessInstanceId());
        variaveis.put("operacao_umidificador", resultadoUmidade.get("operacao_umidificador"));

        logger.info("Operando umidificador {}", entries(variaveis));

        externalTaskService.complete(externalTask);
    }

}